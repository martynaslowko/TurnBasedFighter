package org.mslowko.turnbasedfighter.engine.units;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mslowko.turnbasedfighter.model.Character;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mslowko.turnbasedfighter.TestData.prepareCharacter;
import static org.mslowko.turnbasedfighter.TestData.prepareMob;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CharacterEngineTest {

    @Test
    void portToParentTest(){
        Character character = prepareCharacter();
        CharacterEngine characterEngine = new CharacterEngine(character);

        Character characterPort = characterEngine.portToParent();

        assertThat(characterPort).isEqualTo(character);
    }

    @Test
    void attackTest() {
        MobEngine mob = new MobEngine(prepareMob());
        CharacterEngine character = new CharacterEngine(prepareCharacter());
        int mobHp = mob.getHp();

        int value = character.attack(mob);
        int diff = mobHp-value;

        assertThat(diff).isEqualTo(mob.getHp());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 10})
    void isAliveTest(int hp) {
        CharacterEngine character = new CharacterEngine(prepareCharacter());
        character.setHp(hp);
        assertThat(character.isAlive())
                .isEqualTo(character.getHp() > 0);
    }

    @Test
    void healTest() {
        CharacterEngine character = new CharacterEngine(prepareCharacter());
        character.setHp(1);
        character.heal();
        assertThat(character.getHp())
                .isBetween(2, character.getMaxHeal());
    }

    @Test
    void gainExpTest(){
        CharacterEngine character = new CharacterEngine(prepareCharacter());
        IntStream.range(0, 3)
                .forEach(i -> character.gainExp(10));
        assertThat(character.getExp()).isEqualTo(3*10);
    }

    @Test
    void levelUpTest() {
        CharacterEngine character = new CharacterEngine(prepareCharacter());
        int oldMaxHP = character.getMaxHP();
        int oldMaxHeal = character.getMaxHeal();
        int oldDamage = character.getDamage();

        character.setExp(524);
        character.levelUp();

        assertThat(character.getExp()).isLessThan(100);
        assertThat(character.getLevel()).isEqualTo(5);
        assertThat(character.getMaxHP()).isGreaterThan(oldMaxHP);
        assertThat(character.getMaxHeal()).isGreaterThan(oldMaxHeal);
        assertThat(character.getDamage()).isGreaterThan(oldDamage);
    }
}
