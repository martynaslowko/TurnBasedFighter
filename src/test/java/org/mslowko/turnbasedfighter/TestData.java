package org.mslowko.turnbasedfighter;

import org.mslowko.turnbasedfighter.model.Character;
import org.mslowko.turnbasedfighter.model.Dungeon;
import org.mslowko.turnbasedfighter.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
}
