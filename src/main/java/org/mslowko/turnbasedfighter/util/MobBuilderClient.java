package org.mslowko.turnbasedfighter.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.mslowko.turnbasedfighter.model.Mob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class MobBuilderClient {
    @Value("${server.address}")
    private String host;

    @Value("${mob-builder.port}")
    private String port;

    private WebClient webClient;

    @PostConstruct
    void init() {
        String url = String.format("http://%s:%s/mobs", host, port);
        webClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    public Mob fetchMob(int level, boolean isBoss) {
        String uri = isBoss ? "/fetch/boss" : "/fetch";
        return webClient.get()
                .uri(builder -> builder.path(uri).queryParam("level", level).build())
                .retrieve()
                .bodyToMono(Mob.class)
                .block();
    }
}
