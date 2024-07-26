package org.mslowko.turnbasedfighter.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.mslowko.turnbasedfighter.engine.BattleHandler;
import org.mslowko.turnbasedfighter.model.Character;
import org.mslowko.turnbasedfighter.model.Dungeon;
import org.mslowko.turnbasedfighter.model.repository.DungeonRepository;
import org.mslowko.turnbasedfighter.pojo.dto.DungeonDto;
import org.mslowko.turnbasedfighter.pojo.exceptions.*;
import org.mslowko.turnbasedfighter.pojo.requests.CharacterActionRequest;
import org.mslowko.turnbasedfighter.pojo.requests.CharacterJoinRequest;
import org.mslowko.turnbasedfighter.pojo.responses.BattleResponse;
import org.mslowko.turnbasedfighter.pojo.responses.DungeonCreateResponse;
import org.mslowko.turnbasedfighter.pojo.responses.DungeonLeaveResponse;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DungeonService {
    private final DungeonRepository dungeonRepository;
    private final CharacterService characterService;
    private final PlayerService playerService;
    private final ModelMapper modelMapper;
    private final BattleHandler battleHandler;

    public BattleResponse handleAction(String id, String playerId, CharacterActionRequest request) {
        playerService.fetchPlayerCharacter(playerId, request.getCharacterId());
        Dungeon dungeon = fetchDungeon(id);
        Character character = characterService.fetchCharacter(request.getCharacterId());
        if (!dungeon.isStarted())
            throw new DungeonNotStartedException(id);
        return battleHandler.nextTurn(dungeon, character, request.getAction());
    }

    public DungeonCreateResponse createDungeon(int waves, int slots) {
        Dungeon dungeon = new Dungeon(waves, slots);
        dungeonRepository.save(dungeon);
        DungeonDto dungeonDto = modelMapper.map(dungeon, DungeonDto.class);
        return new DungeonCreateResponse(dungeonDto, dungeon.getJoinKey());
    }

    public DungeonDto startDungeon(String id) {
        Dungeon dungeon = fetchDungeon(id);
        if (dungeon.isStarted())
            throw new DungeonAlreadyStartedException(dungeon.getId());

        dungeon.setStarted(true);
        battleHandler.handleCurrentCharacter(dungeon);
        battleHandler.nextWave(dungeon);
        dungeonRepository.save(dungeon);
        return modelMapper.map(dungeon, DungeonDto.class);
    }

    public DungeonDto joinDungeon(String id, String playerId, CharacterJoinRequest request) {
        playerService.fetchPlayerCharacter(playerId, request.getCharacterId());
        Dungeon dungeon = fetchDungeon(id);
        Character character = characterService.fetchCharacter(request.getCharacterId());
        validateJoinPrerequisites(dungeon, character, request.getKey());
        dungeon.getLobby().add(character);
        dungeonRepository.save(dungeon);
        return modelMapper.map(dungeon, DungeonDto.class);
    }

    public DungeonLeaveResponse leaveDungeon(String id, String player, String characterName) {
        Dungeon dungeon = fetchDungeon(id);
        playerService.fetchPlayerCharacter(player, characterName);
        Optional<Character> optionalCharacter = dungeon.getLobby().stream()
                .filter(c -> c.getName().equals(characterName))
                .findAny();
        if (optionalCharacter.isEmpty())
            throw new CharacterNotFoundException(characterName);
        if (dungeon.isStarted())
            throw new DungeonAlreadyStartedException(id);
        Character character = optionalCharacter.get();
        dungeon.getLobby().remove(character);
        dungeonRepository.save(dungeon);
        return new DungeonLeaveResponse(id);
    }

    private void validateJoinPrerequisites(Dungeon dungeon, Character character, String key) {
        if (dungeon.isStarted())
            throw new DungeonAlreadyStartedException(dungeon.getId());
        if (!Objects.equals(dungeon.getJoinKey(), key))
            throw new InvalidJoinKeyException();
        if (dungeon.getLobby().size() == dungeon.getSlots())
            throw new DungeonAlreadyFullException(dungeon.getId(), dungeon.getSlots());
        if (dungeonRepository.fetchDungeonByCharacterName(character.getName()).isPresent())
            throw new CharacterAlreadyDeployedException(character.getName(), dungeon.getId());
    }

    private Dungeon fetchDungeon(String id) {
        Optional<Dungeon> optionalDungeon = dungeonRepository.findById(id);
        if (optionalDungeon.isEmpty())
            throw new DungeonNotFoundException(id);
        return optionalDungeon.get();
    }
}
