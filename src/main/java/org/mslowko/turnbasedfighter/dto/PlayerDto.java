package org.mslowko.turnbasedfighter.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.mslowko.turnbasedfighter.pojo.Character;

import java.util.List;

@Data
@NoArgsConstructor
public class PlayerDto {
    private String name;
    private List<Character> characters;
}
