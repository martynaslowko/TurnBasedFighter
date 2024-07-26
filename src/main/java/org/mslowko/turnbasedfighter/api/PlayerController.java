package org.mslowko.turnbasedfighter.api;

import lombok.RequiredArgsConstructor;
import org.mslowko.turnbasedfighter.pojo.dto.CharacterDto;
import org.mslowko.turnbasedfighter.pojo.dto.PlayerDto;
import org.mslowko.turnbasedfighter.pojo.requests.CharacterRequest;
import org.mslowko.turnbasedfighter.pojo.requests.PlayerCreateRequest;
import org.mslowko.turnbasedfighter.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("player")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @PostMapping("/new")
    public ResponseEntity<PlayerDto> createNewPlayer(@RequestBody PlayerCreateRequest playerCreateRequest) {
        return ResponseEntity.ok(playerService.newPlayer(playerCreateRequest.getName(), playerCreateRequest.getPassword()));
    }

    @PostMapping("/characters/new")
    public ResponseEntity<PlayerDto> addNewCharacter(@RequestBody CharacterRequest request, Principal principal) {
        return ResponseEntity.ok(playerService.addCharacter(principal.getName(), request.getCharacterId()));
    }

    @GetMapping("/profile/{player}/{character}")
    public ResponseEntity<CharacterDto> fetchPlayerCharacter(@PathVariable("player") String player, @PathVariable("character") String characterName) {
        return ResponseEntity.ok(playerService.fetchPlayerCharacter(player, characterName));
    }

    @GetMapping("/profile/{player}")
    public ResponseEntity<PlayerDto> fetchPlayerCharacter(@PathVariable("player") String player) {
        return ResponseEntity.ok(playerService.getPlayer(player));
    }
}
