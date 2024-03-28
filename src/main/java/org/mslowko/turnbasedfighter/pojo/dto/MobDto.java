package org.mslowko.turnbasedfighter.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MobDto {
    private String name;
    private int hp;
    private int damage;
    private String tier;
}
