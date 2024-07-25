package org.mslowko.turnbasedfighter.pojo.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerCreateRequest {
    private String name;
    private String password;
}
