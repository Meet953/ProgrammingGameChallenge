package com.example.programming.client;

import com.example.programming.model.GameError;
import com.example.programming.model.GameState;
import com.fasterxml.jackson.annotation.JacksonInject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;

import static com.example.programming.constants.GameConstants.*;

@Component
public class GameRestClient {
    private static final String HOST_URL = "http://localhost:8080/";

    @Bean
    public RestTemplate getRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(30000);
        factory.setReadTimeout(30000);
        return new RestTemplate(factory);
    }

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<Object> initPlayer(String name) {
        try {
            final String uri = HOST_URL + READY_PLAYER_V1 + "?name="+name;
            RestTemplate restTemplate = getRestTemplate();
            return restTemplate.postForEntity(uri,new HttpEntity<String>(""), Object.class);
        } catch (Exception e) {
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> addMove(String name, int position) {
        try {
            final String uri = HOST_URL + MOVE_PLAYER_V1 + "?name="+name + "&column="+position;
            RestTemplate restTemplate = getRestTemplate();
            return restTemplate.postForEntity(uri,new HttpEntity<String>(""), Object.class);
        } catch (Exception e) {
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    public  ResponseEntity<Object> getBoard() {
        try {
            final String uri = HOST_URL + GET_GAME_STATE_V1;
            RestTemplate restTemplate = getRestTemplate();
            return restTemplate.getForEntity(uri, Object.class);
        } catch (Exception e) {
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> disconnect(String name) {
        try {
            final String uri = HOST_URL + DISCONNECT_PLAYER_V1 + "?name="+name;
            RestTemplate restTemplate = getRestTemplate();
            return restTemplate.postForEntity(uri,new HttpEntity<String>(""), Object.class);
        } catch (Exception e) {
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> resetGame() {
        try {
            final String uri = HOST_URL + RESET_V1;
            RestTemplate restTemplate = getRestTemplate();
            return restTemplate.getForEntity(uri, Object.class);
        } catch (Exception e) {
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
