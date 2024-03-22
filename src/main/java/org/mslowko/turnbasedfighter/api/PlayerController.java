package org.mslowko.turnbasedfighter.api;

import lombok.RequiredArgsConstructor;
import org.mslowko.turnbasedfighter.model.Player;
import org.mslowko.turnbasedfighter.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("player")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @PostMapping("/new")
    public @ResponseBody ResponseEntity<Player> createNewPlayer(@RequestParam("name") String name) {
        try {
            return ResponseEntity.ok(playerService.newPlayer(name));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error", e);
        }
    }
}
