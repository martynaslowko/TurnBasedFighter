package org.mslowko.turnbasedfighter.service;

import lombok.RequiredArgsConstructor;
import org.mslowko.turnbasedfighter.model.Player;
import org.mslowko.turnbasedfighter.model.repository.PlayerRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;

    public Player newPlayer(String name) {
        Player player = new Player(name);
        playerRepository.save(player);
        return player;
    }
}
