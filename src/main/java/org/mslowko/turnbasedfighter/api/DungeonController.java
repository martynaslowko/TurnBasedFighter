package org.mslowko.turnbasedfighter.api;

import lombok.RequiredArgsConstructor;
import org.mslowko.turnbasedfighter.pojo.requests.CharacterJoinRequest;
import org.mslowko.turnbasedfighter.pojo.dto.DungeonDto;
import org.mslowko.turnbasedfighter.pojo.responses.DungeonCreateResponse;
import org.mslowko.turnbasedfighter.service.DungeonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("dungeon")
@RequiredArgsConstructor
public class DungeonController {
    private final DungeonService dungeonService;

    @PostMapping("/new")
    public ResponseEntity<DungeonCreateResponse> openNewDungeon(@RequestParam("waves") int waves, @RequestParam("slots") int slots) {
        return ResponseEntity.ok(dungeonService.createDungeon(waves, slots));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<DungeonDto> startDungeon(@PathVariable String id) {
        return ResponseEntity.ok(dungeonService.startDungeon(id));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<DungeonDto> joinDungeon(@PathVariable String id, @RequestBody CharacterJoinRequest joinDto) {
        return ResponseEntity.ok(dungeonService.joinDungeon(id, joinDto));
    }
}
