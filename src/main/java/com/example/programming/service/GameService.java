package com.example.programming.service;

import com.example.programming.Exception.*;
import com.example.programming.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Scope("singleton")
public class GameService {

   private ArrayList<Sign> signsAvailable;
   private Map<Player, Sign> players;
   private Board playingBoard;
   private Status gameState;

    private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);

    private GameService(){
        resetGame();
    }

    public void resetGame() {
        signsAvailable = new ArrayList<>(2);
        signsAvailable.add(Sign.X);
        signsAvailable.add(Sign.O);
        players = new HashMap<>(2);
        playingBoard = new Board();
        playingBoard.reset();
        gameState = Status.NOT_STARTED;
    }

    public Player assignSignToPlayer(String playerName) throws NoSignsAvailableException {
        if (playerName.isEmpty()) {
            throw new InvalidPlayerNameException();
        }
        if (signsAvailable.size() > 0 ) {
            boolean isUnique = checkUniqueName(playerName);
            if (!isUnique) {
                throw new InvalidPlayerNameException();
            }
            Sign sign =  signsAvailable.remove(0);
            Player player = new Player();
            player.setName(playerName);
            player.setSign(sign);
            player.setChance(true);
            players.put(player, sign);
            updateGameStatus();
            LOGGER.info("Successfully Created Player : " +  playerName);
            return player;
        } else {
            throw new NoSignsAvailableException();
        }
    }

    private void updateGameStatus() {
        if (players.size() == 1 ) {
            LOGGER.info("Game status updated to " + Status.WAITING_FOR_PLAYER);
            gameState = Status.WAITING_FOR_PLAYER;
        } else if (players.size() == 2 ) {
            LOGGER.info("Game status updated to " + Status.STARTED);
            gameState = Status.STARTED;
        }
    }

    public Player movePlayerInBoard(String playerName, int position) {
        LOGGER.info("Adding move for Player : " + playerName);
        handleExceptionsForValidMove(playerName, position);
        Player currentPlayer = getPlayerFromName(playerName);
        if (!currentPlayer.isChance()) {
            LOGGER.debug("Player has already added move : " + playerName);
            throw new PlayerChanceException();
        }
        Sign currentSign = players.get(currentPlayer);
        LOGGER.debug("Current Player : " + currentPlayer.getName() + " | " + currentSign );

        Sign otherSign = (currentSign == Sign.X ) ? Sign.O : Sign.X;
        Player otherPlayer = getPlayerFromSign(otherSign);
        LOGGER.debug("Other Player : " + otherPlayer.getName() + " | " + otherSign );

        boolean moved = playingBoard.addMoveToBoard(position, currentSign);
        if (moved) {
            checkWinner(currentSign, currentPlayer);
            currentPlayer.setChance(false);
            otherPlayer.setChance(true);
            players.put(currentPlayer, currentSign);
            players.put(otherPlayer, otherSign);
            return currentPlayer;
        }
        else {
            throw new InvalidMoveException();
        }
    }

    private void checkWinner(Sign sign, Player player) {
        boolean hasWon = playingBoard.checkWinner(sign);
        if(hasWon) {
            player.setWinner(true);
            gameState  = Status.FINISHED;
        }
    }

    private void handleExceptionsForValidMove(String playerName, int position) {
        if(!(gameState == Status.STARTED)){
            LOGGER.info("Game status is not Started");
            throw new GameStatusException();
        }
        if (isInValidPlayer(playerName)) {
            LOGGER.debug("Player not available in game" + playerName);
            throw new InvalidPlayerNameException();
        }
        if (!isValidMove(position)) {
            LOGGER.debug("Invalid move");
            throw new InvalidMoveException();
        }
        boolean isFull = playingBoard.checkFull();
        if (isFull) {
            LOGGER.debug("Board is full. Game is Finished");
            gameState  = Status.FINISHED;
            throw new BoardFullException();
        }
    }

    public Player disconnectPlayer(String playerName) {
        if(!(gameState == Status.STARTED)){
            LOGGER.debug("Game status is not Started");
            throw new GameStatusException();
        }
        if (isInValidPlayer(playerName)) {
            LOGGER.debug("Player not available in game" + playerName);
            throw new InvalidPlayerNameException();
        }
        Player currentPlayer = getPlayerFromName(playerName);
        Sign currentSign = players.get(currentPlayer);
        Sign otherSign = (currentSign == Sign.X ) ? Sign.O : Sign.X;
        Player otherPlayer = getPlayerFromSign(otherSign);
        otherPlayer.setWinner(true);
        currentPlayer.setWinner(false);
        LOGGER.debug("Game disconnected. Loser : " + playerName + " Winner : " + otherPlayer.getName());
        gameState = Status.FINISHED;
        return otherPlayer;
    }

    private boolean checkUniqueName(String playerName) {
        return isInValidPlayer(playerName);
    }

    private boolean isInValidPlayer(String playerName) {
       return players.keySet().stream().noneMatch(p -> p.getName().equals(playerName));
   }

   private Player getPlayerFromName(String playerName) {
        Optional<Player> player =  players.keySet().stream().filter(p -> p.getName().equals(playerName)).findFirst();
        return player.orElse(null);
   }

   private Player getPlayerFromSign(Sign sign) {
       Stream<Player> player = keys(players, sign);
       return player.findFirst().get();
   }

    private boolean isValidMove(int position) {
       return playingBoard.isValidCol(position);
   }

    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(players.keySet());
    }

    private  <K, V> Stream<K> keys(Map<K, V> map, V value) {
        return map
                .entrySet()
                .stream()
                .filter(entry -> value.equals(entry.getValue()))
                .map(Map.Entry::getKey);
    }

    public Board getPlayingBoard() {
        return playingBoard;
    }

    public Status getGameState() {
        return gameState;
    }
}

