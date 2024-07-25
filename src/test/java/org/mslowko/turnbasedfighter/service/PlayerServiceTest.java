package org.mslowko.turnbasedfighter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.modelmapper.ModelMapper;
import org.mslowko.turnbasedfighter.TestData;
import org.mslowko.turnbasedfighter.model.Character;
import org.mslowko.turnbasedfighter.model.Player;
import org.mslowko.turnbasedfighter.model.repository.CharacterRepository;
import org.mslowko.turnbasedfighter.model.repository.PlayerRepository;
import org.mslowko.turnbasedfighter.pojo.dto.CharacterDto;
import org.mslowko.turnbasedfighter.pojo.exceptions.CharacterNotFoundException;
import org.mslowko.turnbasedfighter.pojo.exceptions.PlayerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mslowko.turnbasedfighter.Constants.neo4jImage;

@Testcontainers
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class PlayerServiceTest {
    @Container
    static Neo4jContainer<?> neo4jContainer = new Neo4jContainer<>(neo4jImage).withStartupTimeout(Duration.ofMinutes(5));

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", neo4jContainer::getBoltUrl);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", neo4jContainer::getAdminPassword);
    }

    @SpyBean
    private PlayerRepository playerRepository;

    @SpyBean
    private CharacterRepository characterRepository;

    @SpyBean
    private PlayerService playerService;

    @SpyBean
    private CharacterService characterService;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    void cleanUp() {
        playerRepository.deleteAll();
        characterRepository.deleteAll();
    }

    @Test
    void newPlayerTest() {
        String playerName = "player";
        String playerPassword = "pwd";
        playerService.newPlayer(playerName, playerPassword);
        boolean result = playerRepository.existsById(playerName);
        assertThat(result).isTrue();
    }

    @Test
    void newPlayerExceptionTest() {
        String playerName = "player";
        String playerPassword = "pwd";
        playerService.newPlayer(playerName, playerPassword);
        assertThrows(IllegalArgumentException.class,
                () -> playerService.newPlayer(playerName, playerPassword));
    }

    @Test
    void addCharacterTest() {
        String playerName = "player";
        Player player = new Player(playerName);
        playerRepository.save(player);

        String characterName = "character";
        playerService.addCharacter(playerName, characterName);

        Optional<Character> character = playerRepository.findById(playerName).get()
                .getCharacters().stream().filter(c-> c.getName().equals(characterName)).findAny();
        assertThat(character).isPresent();
    }

    @ParameterizedTest
    @CsvSource({"playerA,playerB,character", "player,player,character"})
    void addCharacterTest_Exceptions(String playerName1, String playerName2, String characterName) {
        Player player = TestData.prepareExistingPlayerCharacter(playerName1, characterName);
        playerRepository.save(player);

        Class<? extends RuntimeException> exceptionClass = playerName1.equals(playerName2)
                ? IllegalArgumentException.class : PlayerNotFoundException.class;
        assertThrows(exceptionClass,
                () -> playerService.addCharacter(playerName2, characterName));
    }

    @Test
    void fetchPlayerCharacterTest() {
        String playerName = "player";
        String characterName = "character";
        Player player = TestData.prepareExistingPlayerCharacter(playerName, characterName);
        playerRepository.save(player);

        CharacterDto fetchedCharacter = playerService.fetchPlayerCharacter(playerName, characterName);
        CharacterDto character = modelMapper.map(player.getCharacters().get(0), CharacterDto.class);

        assertThat(fetchedCharacter).isEqualTo(character);
    }

    @Test
    void fetchPlayerCharacterTest_wrongCharacter() {
        String playerName = "player";
        Player player = TestData.prepareExistingPlayerCharacter(playerName, "character");
        playerRepository.save(player);

        assertThrows(CharacterNotFoundException.class,
                () -> playerService.fetchPlayerCharacter(playerName, "blooper"));
    }
}
