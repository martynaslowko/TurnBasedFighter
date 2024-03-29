package org.mslowko.turnbasedfighter.engine.units;

import org.mslowko.turnbasedfighter.model.Character;
import org.mslowko.turnbasedfighter.model.Unit;

import java.util.Random;

public class CharacterEngine extends Character implements Engine<Character> {
    private final Random random = new Random();

    public CharacterEngine(Character character) {
        super(character.getHp(), character.getDamage(), character.getExp(), character.getName(),
                character.getLevel(), character.getMaxHeal(), character.getMaxHP(), character.getVersion());
    }

    public void heal() {
        int minValue = this.maxHeal - 10 + 1;
        this.hp = Math.min(random.nextInt(minValue, this.maxHeal), this.maxHP);
    }

    @Override
    public Character portToParent() {
        return new Character(this.hp, this.damage, this.exp, this.name, this.level, this.maxHeal, this.maxHP, this.version);
    }

    public void attack(Unit target) {
        Engine.super.attack(target, this.damage);
    }

    public boolean isAlive() {
        return Engine.super.isAlive(this.hp);
    }
}
