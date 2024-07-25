package org.mslowko.turnbasedfighter.api;

import lombok.RequiredArgsConstructor;
import org.mslowko.turnbasedfighter.pojo.dto.DungeonDto;
import org.mslowko.turnbasedfighter.pojo.requests.CharacterActionRequest;
import org.mslowko.turnbasedfighter.pojo.requests.CharacterJoinRequest;
import org.mslowko.turnbasedfighter.pojo.requests.CharacterRequest;
import org.mslowko.turnbasedfighter.pojo.responses.BattleResponse;
import org.mslowko.turnbasedfighter.pojo.responses.DungeonCreateResponse;
import org.mslowko.turnbasedfighter.pojo.responses.DungeonLeaveResponse;
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
    public ResponseEntity<DungeonDto> joinDungeon(@PathVariable String id, @RequestBody CharacterJoinRequest joinRequest) {
        return ResponseEntity.ok(dungeonService.joinDungeon(id, joinRequest));
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<DungeonLeaveResponse> leaveDungeon(@PathVariable String id, @RequestBody CharacterRequest request) {
        return ResponseEntity.ok(dungeonService.leaveDungeon(id, request.getPlayer(), request.getCharacterName()));
    }


    @PostMapping("/{id}/battle")
    public ResponseEntity<BattleResponse> handleAction(@PathVariable String id, @RequestBody CharacterActionRequest actionRequest) {
        return ResponseEntity.ok(dungeonService.handleAction(id, actionRequest));
    }}
