package com.example.programming;

import com.example.programming.client.GameRestClient;
import com.example.programming.client.GameWebClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(classes = GameRestClient.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameWebClientTest {

    @Autowired
    private GameRestClient gameRestClient;

    @Autowired
    private WebTestClient webTestClient;

    private static final String baseUrl = "http://localhost:8080/play/api/v1/start";


    @Test
    void readyPlayer() {


    }

}
