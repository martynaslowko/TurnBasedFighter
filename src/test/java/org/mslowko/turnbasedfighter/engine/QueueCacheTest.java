package org.mslowko.turnbasedfighter.engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mslowko.turnbasedfighter.model.Character;
import org.mslowko.turnbasedfighter.model.Dungeon;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mslowko.turnbasedfighter.TestData.prepareCharacterList;

@SpringBootTest(classes = {QueueCache.class})
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class QueueCacheTest {

    @SpyBean
    QueueCache queueCache;

    @Test
    void getNextCharacter_createAndGet() {
        int slots = 3;
        Dungeon dungeon = new Dungeon(1, slots);
        List<Character> lobby = prepareCharacterList(slots);
        dungeon.setLobby(lobby);

        List<Character> queue = IntStream.range(0, slots)
                .mapToObj(i -> queueCache.getNextCharacter(dungeon))
                .toList();

        assertThat(queue).containsExactlyInAnyOrderElementsOf(lobby);
    }
}
