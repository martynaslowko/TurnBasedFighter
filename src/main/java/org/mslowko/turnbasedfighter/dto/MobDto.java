package org.mslowko.turnbasedfighter.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MobDto {
    private String name;
    private int hp;
    private int damage;
    private int exp;
    private String tier;
}
