package org.mslowko.turnbasedfighter.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CharacterDto {
    private String name;
    private int hp;
    private int damage;
    private int maxHP;
    private int maxHeal;
    private int level;
}
