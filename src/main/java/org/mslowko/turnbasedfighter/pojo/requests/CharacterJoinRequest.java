package org.mslowko.turnbasedfighter.pojo.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CharacterJoinRequest {
    private String key;
    private String characterId;
}
