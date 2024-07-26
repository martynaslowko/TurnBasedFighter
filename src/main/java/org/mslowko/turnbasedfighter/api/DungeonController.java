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

import java.security.Principal;

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
    public ResponseEntity<DungeonDto> joinDungeon(@PathVariable String id, @RequestBody CharacterJoinRequest joinRequest, Principal principal) {
        return ResponseEntity.ok(dungeonService.joinDungeon(id, principal.getName(), joinRequest));
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<DungeonLeaveResponse> leaveDungeon(@PathVariable String id, @RequestBody CharacterRequest request, Principal principal) {
        return ResponseEntity.ok(dungeonService.leaveDungeon(id, principal.getName(), request.getCharacterId()));
    }


    @PostMapping("/{id}/battle")
    public ResponseEntity<BattleResponse> handleAction(@PathVariable String id, @RequestBody CharacterActionRequest actionRequest, Principal principal) {
        return ResponseEntity.ok(dungeonService.handleAction(id, principal.getName(), actionRequest));
    }}
