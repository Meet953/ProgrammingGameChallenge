package com.example.programming;

import com.example.programming.client.GameRestClient;
import com.example.programming.model.GameState;
import com.example.programming.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.example.programming.constants.GameConstants.READY_PLAYER_V1;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class GameRestClientTest {

    @Autowired
    private GameRestClient gameRestClient;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private static final String HOST_URL = "http://localhost:8080/";

    private  ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        restTemplate = gameRestClient.getRestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testReadyPlayer() throws Exception {
        final String uri = HOST_URL + READY_PLAYER_V1 + "?name=Joe";

        GameState gameState = new GameState();
        List<Player> players = new ArrayList<>();
        players.add(new Player("Joe"));
        gameState.setPlayers(players);
        mockServer.expect(requestTo(new URI(uri)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(gameState))
                );

        ResponseEntity<Object> responseEntity = gameRestClient.initPlayer("Joe");
        assertNotNull(responseEntity);
      /*  Mockito.when(restTemplate.postForEntity(uri,new HttpEntity<String>(""), Object.class)).thenReturn(new ResponseEntity<>(gameState, HttpStatus.CREATED));
        ResponseEntity<Object> responseEntity =  gameRestClient.initPlayer("Joe");
        System.out.println( responseEntity.getBody());*/
    }

}
