package org.mslowko.turnbasedfighter.pojo.responses;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DungeonLeaveResponse {
    public static final String MESSAGE = "Successfully left the dungeon.";
    public final String dungeonId;
}
