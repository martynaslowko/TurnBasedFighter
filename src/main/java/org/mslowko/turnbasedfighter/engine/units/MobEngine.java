package org.mslowko.turnbasedfighter.engine.units;

import org.mslowko.turnbasedfighter.model.Mob;
import org.mslowko.turnbasedfighter.model.Unit;

public class MobEngine extends Mob implements Engine<Mob> {
    public MobEngine(Mob mob) {
        super(mob.getName(), mob.getTier(), mob.getHp(), mob.getDamage(), mob.getExp());
        this.id = mob.getId();
    }

    @Override
    public Mob portToParent() {
        return new Mob(this.id, this.name, this.tier, this.hp, this.damage, this.exp);
    }

    public int attack(Unit target) {
        return Engine.super.attack(target, this.damage);
    }

    public boolean isAlive() {
        return Engine.super.isAlive(this.hp);
    }
}
