package org.mslowko.turnbasedfighter.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CharacterPreviewDto {
    private String name;
    private int hp;
    private int level;
}
