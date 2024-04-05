package org.mslowko.turnbasedfighter;

import org.mslowko.turnbasedfighter.model.Character;
import org.mslowko.turnbasedfighter.model.Player;

public class TestData {
    public static Player prepareExistingPlayerCharacter(String playerName, String characterName) {
        Player player = new Player(playerName);
        Character character = new Character(characterName);
        player.getCharacters().add(character);
        return player;
    }
}
