package org.mslowko.turnbasedfighter.api;

import lombok.RequiredArgsConstructor;
import org.mslowko.turnbasedfighter.dto.PlayerDto;
import org.mslowko.turnbasedfighter.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("player")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @PostMapping("/new")
    public ResponseEntity<PlayerDto> createNewPlayer(@RequestParam("name") String name) {
        return ResponseEntity.ok(playerService.newPlayer(name));
    }

    @PostMapping("/{player}/characters/new")
    public ResponseEntity<PlayerDto> addNewCharacter(@PathVariable("player") String player, @RequestBody String characterName) {
        return ResponseEntity.ok(playerService.addCharacter(player, characterName));
    }
}
