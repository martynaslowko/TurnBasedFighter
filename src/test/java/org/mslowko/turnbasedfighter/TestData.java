package org.mslowko.turnbasedfighter;

import org.mslowko.turnbasedfighter.model.Character;
import org.mslowko.turnbasedfighter.model.Dungeon;
import org.mslowko.turnbasedfighter.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class TestData {
    public static Player prepareExistingPlayerCharacter(String playerName, String characterName) {
        Player player = new Player(playerName);
        Character character = new Character(characterName);
        player.getCharacters().add(character);
        return player;
    }

    public static Dungeon prepareJoinableDungeon() {
        Map<String, String> playerCharacterNames = Map.of("p1", "c1", "p2", "c2");
        List<Character> characters = playerCharacterNames.entrySet().stream()
                .map(e -> prepareExistingPlayerCharacter(e.getKey(), e.getValue()).getCharacters().get(0))
                .toList();
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.setLobby(new ArrayList<>(characters));
        return dungeon;
    }

    public static Dungeon prepareSingleDungeon() {
        Dungeon dungeon = new Dungeon(1, 1);
        Character character = prepareExistingPlayerCharacter("p1", "c1").getCharacters().get(0);
        dungeon.setLobby(List.of(character));
        return dungeon;
    }

    public static List<Character> prepareCharacterList(int length) {
        List<String> characterNames = IntStream.rangeClosed(1, length).mapToObj(i -> "c" + i).toList();
        return characterNames.stream().map(s -> {
            Character character = new Character();
            character.setName(s);
            character.setLevel(1);
            return character;
        }).toList();
    }
}
