package com.example.programming.client;

import com.example.programming.model.GameState;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static com.example.programming.constants.GameConstants.*;

public class GameWebClient {

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(30000);

    private final WebClient webClient;

    public GameWebClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
                .responseTimeout(Duration.ofMillis(30000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(30000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(30000, TimeUnit.MILLISECONDS)));
        this.webClient = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }

    @Autowired
    public GameWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<GameState> initPlayer(String name) {
        return webClient.post().uri(builder -> builder.scheme("http")
                .host("localhost").port(8080)
                .path(READY_PLAYER_V1)
                .queryParam("name", name)
                .build()).retrieve().bodyToMono(GameState.class);
    }

    public Mono<GameState> movePlayer(String name, int column) {
        return webClient.post().uri(builder -> builder.scheme("http")
                .host("localhost").port(8080)
                .path(MOVE_PLAYER_V1)
                .queryParam("name", name)
                .queryParam("column", column)
                .build()).retrieve().bodyToMono(GameState.class);
    }

    public Mono<GameState> resetGame() {
        return webClient.get().uri(builder -> builder.scheme("http")
                .host("localhost").port(8080)
                .path(RESET_V1)
                .build()).retrieve().bodyToMono(GameState.class);
    }

    public Mono<GameState> getGameState() {
        return webClient.get().uri(builder -> builder.scheme("http")
                .host("localhost").port(8080)
                .path(RESET_V1)
                .build()).retrieve().bodyToMono(GameState.class);
    }
}
