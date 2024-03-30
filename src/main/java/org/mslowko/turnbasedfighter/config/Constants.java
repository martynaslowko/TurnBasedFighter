package org.mslowko.turnbasedfighter.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    public enum BattleCode {
        TURN, WAVE, DOWN, VICTORY, DEFEAT
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class BattleMessages {
        public static final String ATTACK = "%s attacked %s, they took %d damage!";
        public static final String MISS = "%s missed the attack!";
        public static final String HEAL = "%s healed themselves, they're now %d HP!";
        public static final String UNIT_DEAD = "%s has been defeated.";
        public static final String GAME_OVER = "Everyone is defeated. Game over.";
        public static final String NEXT_WAVE = UNIT_DEAD + " Next wave incoming...";
        public static final String VICTORY = "Defeated all opponents. Victory!";
    }
}
