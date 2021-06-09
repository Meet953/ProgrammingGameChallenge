package com.example.programming.controller;

import com.example.programming.Exception.*;
import com.example.programming.model.GameError;
import com.example.programming.model.GameState;
import com.example.programming.model.Player;
import com.example.programming.model.Status;
import com.example.programming.service.GameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "GameController", description = "REST APIs related to Game")
@RequestMapping(value = "play/api/v1")
public class GameController {

    @Autowired
    private GameService gameService;

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    @ApiOperation(value = "Starts the game by initializing the Player. Creates new player in the game having name passed as param.", response = ResponseEntity.class, tags = "start")
    @PostMapping("/start")
    public ResponseEntity<Object> readyPlayer(@RequestParam(value = "name") String playerName) {
        try {
            LOGGER.info("Received request to start game for " + playerName);
            Player player = gameService.assignSignToPlayer(playerName);
            StringBuilder message = new StringBuilder();
            message.append("Player successfully created. Your Name : ' ").append(player.getName()).append(" '. ").append("Your sign : ' ").append(player.getSign()).append(" '. ");
            GameState gameState = getGameStateForResponse(message);
            return new ResponseEntity<>(gameState, HttpStatus.CREATED);
        } catch (InvalidPlayerNameException e) {
            LOGGER.info("Invalid Player Name found");
            GameError error = new GameError(InvalidPlayerNameException.getError(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
        } catch (NoSignsAvailableException e) {
            LOGGER.info("No signs available for new player");
            GameError error = new GameError(NoSignsAvailableException.getError(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
        }
    }

    @ApiOperation(value = "Adds the move to the board. Player with param name adds a move to the param column.", response = ResponseEntity.class, tags = "move")
    @PostMapping("/move")
    public ResponseEntity<Object> movePlayer(@RequestParam(value = "name") String playerName, @RequestParam(value = "column") int position) {
        try {
            if (playerName.isEmpty()) {
                throw new InvalidPlayerNameException();
            }
            LOGGER.info("Received request to add a move on board by " + playerName + " for column " + position);
            Player player = gameService.movePlayerInBoard(playerName, position - 1);
            GameState gameState = getGameStateForResponse(player);
            return new ResponseEntity<>(gameState, HttpStatus.OK);
        } catch (InvalidPlayerNameException e) {
            LOGGER.info("Player Name not found in the service");
            GameError error = new GameError(InvalidPlayerNameException.getError(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
        } catch (InvalidMoveException e) {
            LOGGER.info("Invalid move found");
            GameError error = new GameError(InvalidMoveException.getError(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
        } catch (BoardFullException e) {
            LOGGER.info("Board is full");
            GameError error = new GameError(BoardFullException.getError(), HttpStatus.PRECONDITION_FAILED);
            return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
        } catch (GameStatusException e) {
            LOGGER.info("Game not started");
            GameError error = new GameError(GameStatusException.getError(), HttpStatus.PRECONDITION_FAILED);
            return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
        } catch (PlayerChanceException e) {
            LOGGER.info("Its not player's turn");
            GameError error = new GameError(PlayerChanceException.getError(), HttpStatus.PRECONDITION_FAILED);
            return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
        }
    }

    @ApiOperation(value = "Resets the whole Game. Resets board, players and status.", response = ResponseEntity.class, tags = "reset")
    @GetMapping("/reset")
    public ResponseEntity<Object> resetBoard() {
        LOGGER.info("Received request to reset the Board");
        gameService.resetGame();
        GameState gameState = new GameState();
        settingGameStateFromService(gameState);
        return new ResponseEntity<>(gameState, HttpStatus.OK);
    }

    @ApiOperation(value = "Gets the current playing Game State. Returns status, board and players available at current stage.", response = ResponseEntity.class, tags = "state")
    @GetMapping("/get/state")
    public ResponseEntity<Object> getGameState() {
        LOGGER.info("Received request to get the Game State");
        GameState gameState = new GameState();
        settingGameStateFromService(gameState);
        return new ResponseEntity<>(gameState, HttpStatus.OK);
    }

    @ApiOperation(value = "Disconnects the game for the player with param name.", response = ResponseEntity.class, tags = "disconnect")
    @PostMapping("/disconnect")
    public ResponseEntity<Object> disconnectPlayer(@RequestParam(value = "name") String playerName) {
        try {
            LOGGER.info("Received request to disconnect game by player : " + playerName);
            Player player = gameService.disconnectPlayer(playerName);
            GameState gameState = getGameStateForResponse(player);
            return new ResponseEntity<>(gameState, HttpStatus.OK);
        } catch (InvalidPlayerNameException e) {
            LOGGER.info("Player Name not found in the service");
            GameError error = new GameError(InvalidPlayerNameException.getError(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
        } catch (GameStatusException e) {
            LOGGER.info("Game not started");
            GameError error = new GameError(GameStatusException.getError(), HttpStatus.PRECONDITION_FAILED);
            return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
        }
    }

    private GameState getGameStateForResponse(Player player) {
        GameState gameState = new GameState();
        settingGameStateFromService(gameState);
        if(player.isWinner()) {
            gameState.setMessage("CONGRATULATIONS. Player : ' " + player.getName() + " ' has won the game. Game Ended. Hit ('/reset') and then start new game");
        } else {
            gameState.setMessage("NICE MOVE. Player : ' " + player.getName() + " ' added move on board. Now waiting for other player.");
        }
        return gameState;
    }

    private void settingGameStateFromService(GameState gameState) {
        gameState.setPlayers(gameService.getPlayers());
        gameState.setBoard(gameService.getPlayingBoard());
        gameState.setStatus(gameService.getGameState());
    }

    private GameState getGameStateForResponse(StringBuilder message) {
        GameState gameState = new GameState();
        settingGameStateFromService(gameState);
        if ( gameState.getStatus() == Status.WAITING_FOR_PLAYER) {
            message.append("Now Waiting for Another Player to join");
        } else if (gameState.getStatus() == Status.STARTED){
            message.append("Game has started. Hit URL ('/move') to play your turn");
        }
        gameState.setMessage(message.toString());
        return gameState;
    }
}
