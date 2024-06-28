package org.mslowko.turnbasedfighter.engine.units;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mslowko.turnbasedfighter.model.Mob;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mslowko.turnbasedfighter.TestData.prepareCharacter;
import static org.mslowko.turnbasedfighter.TestData.prepareMob;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class MobEngineTest {

    @Test
    void portToParentTest(){
        Mob mob = prepareMob();
        MobEngine mobEngine = new MobEngine(mob);

        Mob mobPort = mobEngine.portToParent();

        assertThat(mobPort).isEqualTo(mob);
    }

    @Test
    void attackTest() {
        MobEngine mob = new MobEngine(prepareMob());
        CharacterEngine character = new CharacterEngine(prepareCharacter());

        int value = mob.attack(character);
        int diff = character.getMaxHP()-value;

        assertThat(diff).isEqualTo(character.getHp());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 10})
    void isAliveTest(int hp) {
        MobEngine mob = new MobEngine(prepareMob());
        mob.setHp(hp);
        assertThat(mob.isAlive())
                .isEqualTo(mob.getHp() > 0);
    }
}
