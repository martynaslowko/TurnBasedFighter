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

    public int heal() {
        int minValue = this.maxHeal - 10 + 1;
        int sum = this.hp + random.nextInt(minValue, this.maxHeal);
        this.hp = Math.min(sum, this.maxHP);
        return this.hp;
    }

    public void gainExp(int exp) {
        this.exp += exp;
    }

    public void levelUp() {
        int currentExp = this.exp;
        if (currentExp < 100)
            return;
        int leftoverExp = currentExp % 100;
        int fullLevel = currentExp / 100;
        this.level += fullLevel;
        this.exp = leftoverExp;
        this.maxHP += 3;
        this.maxHeal += 2;
        this.damage += 2;
    }

    @Override
    public Character portToParent() {
        return new Character(this.hp, this.damage, this.exp, this.name, this.level, this.maxHeal, this.maxHP, this.version);
    }

    public int attack(Unit target) {
        return Engine.super.attack(target, this.damage);
    }

    public boolean isAlive() {
        return Engine.super.isAlive(this.hp);
    }
}
