package org.mslowko.turnbasedfighter.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.mslowko.turnbasedfighter.model.Character;

import java.util.List;

@Data
@NoArgsConstructor
public class PlayerDto {
    private String name;
    private List<Character> characters;
}
