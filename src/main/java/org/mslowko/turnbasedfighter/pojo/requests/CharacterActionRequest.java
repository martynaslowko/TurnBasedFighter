package org.mslowko.turnbasedfighter.pojo.requests;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.mslowko.turnbasedfighter.engine.Action;

@Data
@NoArgsConstructor
public class CharacterActionRequest {
    private Action action;
    private String characterId;
}
