package org.mslowko.turnbasedfighter.pojo.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.mslowko.turnbasedfighter.pojo.dto.DungeonDto;

@Data
@AllArgsConstructor
public class DungeonCreateResponse {
    private DungeonDto dungeon;
    private String joinKey;
}
