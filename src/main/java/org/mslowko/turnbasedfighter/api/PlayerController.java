package org.mslowko.turnbasedfighter.api;

import lombok.RequiredArgsConstructor;
import org.mslowko.turnbasedfighter.pojo.dto.CharacterDto;
import org.mslowko.turnbasedfighter.pojo.dto.PlayerDto;
import org.mslowko.turnbasedfighter.pojo.requests.NameRequest;
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
    public ResponseEntity<PlayerDto> addNewCharacter(@PathVariable("player") String player, @RequestBody NameRequest request) {
        return ResponseEntity.ok(playerService.addCharacter(player, request.getName()));
    }

    @GetMapping("/{player}/characters/{character}")
    public ResponseEntity<CharacterDto> fetchPlayerCharacter(@PathVariable("player") String player, @PathVariable("character") String characterName) {
        return ResponseEntity.ok(playerService.fetchPlayerCharacter(player,characterName));
    }
}
