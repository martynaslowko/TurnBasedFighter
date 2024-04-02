package org.mslowko.turnbasedfighter.engine;

import lombok.Getter;
import org.mslowko.turnbasedfighter.model.Character;
import org.mslowko.turnbasedfighter.model.Dungeon;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Getter
public class QueueCache {
    private final Map<String, Deque<String>> queueMap = new HashMap<>();

    public Character fetchCharacterFromQueueCache(Dungeon dungeon) {
        Deque<String> queue = queueMap.get(dungeon.getId());
        try {
            String name = queue.getFirst();
            queue.pop();
            queue.addLast(name);
            Optional<Character> character = dungeon.getLobby().stream()
                    .filter(c -> c.getName().equals(name))
                    .findFirst();
            if (character.isPresent())
                return character.get();
        } catch (NullPointerException e) {
            initializeQueue(dungeon);
            return fetchCharacterFromQueueCache(dungeon);
        }
        throw new IllegalStateException(); //Shouldn't happen
    }

    public void cleanUpQueue(Dungeon dungeon) {
        queueMap.remove(dungeon.getId());
    }

    private void initializeQueue(Dungeon dungeon) {
        List<String> charNames = dungeon.getLobby().stream().map(Character::getName).toList();
        queueMap.put(dungeon.getId(), new ArrayDeque<>(charNames));
    }
}
