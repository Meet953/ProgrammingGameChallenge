package com.example.programming.service;

import com.example.programming.Exception.*;
import com.example.programming.model.Player;
import com.example.programming.model.Sign;
import com.example.programming.model.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Optional;


@SpringBootTest
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    private static final int MAX_ROW = 6;
    private static final int MAX_COL = 9;

    @BeforeEach
    void init(){
        gameService.resetGame();
    }

    @Test
    public void testAssignSignToPlayerWithCorrectName() {
        Player player = gameService.assignSignToPlayer("Joe");
        assert player.getName().equals("Joe");
        assert player.getSign() == Sign.X;
        assert player.isChance();
        //assert Game state after 1 player is created
        assert gameService.getGameState() == Status.WAITING_FOR_PLAYER;
        assert gameService.getPlayers().get(0).getName().equals("Joe");
    }

    @Test
    public void testExceptionWhenAssignSignToPlayerWithEmptyName() {
        Assertions.assertThrows(InvalidPlayerNameException.class, () -> {
            gameService.assignSignToPlayer("");
        });
    }

    @Test
    public void testExceptionWhenAssignSignToPlayerWithSameName() {
        Assertions.assertThrows(InvalidPlayerNameException.class, () -> {
            gameService.assignSignToPlayer("Joe");
            gameService.assignSignToPlayer("Joe");
        });
    }

    @Test
    public void testAssignSignToTwoPlayersAndStartGame() {
        Player player1 = gameService.assignSignToPlayer("Ava");
        assert player1.getName().equals("Ava");
        Player player2 = gameService.assignSignToPlayer("Bob");
        assert player2.getName().equals("Bob");
        //assert Game state after 2 player are created
        assert gameService.getGameState() == Status.STARTED;
        ArrayList<Player> players = gameService.getPlayers();
        Player playerAva = players.stream().filter(p -> p.getName().equals("Ava")).findFirst().get();
        Player playerBob = players.stream().filter(p -> p.getName().equals("Bob")).findFirst().get();
        assert playerAva.getName().equals("Ava");
        assert playerAva.getSign() == Sign.X;
        assert playerBob.getName().equals("Bob");
        assert playerBob.getSign() == Sign.O;
    }

    @Test
    public void testExceptionWhenThreePlayersJoinTheGame() {
        gameService.assignSignToPlayer("Ava");
        //assert Game state after 1 player is created
        assert gameService.getGameState() == Status.WAITING_FOR_PLAYER;
        gameService.assignSignToPlayer("Bob");
        //assert Game state after 2 player are created
        assert gameService.getGameState() == Status.STARTED;

        Assertions.assertThrows(NoSignsAvailableException.class, () -> {
            gameService.assignSignToPlayer("Car");
        });

    }

    @Test
    public void testMovePlayerInBoardWhenNoPlayersHaveJoined() {
        Assertions.assertThrows(GameStatusException.class, () -> {
            gameService.movePlayerInBoard("Joe",8);
        });
    }

    @Test
    public void testMovePlayerInBoardWhenOnePlayersHaveJoined() {
        gameService.assignSignToPlayer("Ava");
        //assert Game state after 1 player is created
        assert gameService.getGameState() == Status.WAITING_FOR_PLAYER;
        Assertions.assertThrows(GameStatusException.class, () -> {
            gameService.movePlayerInBoard("Ava",8);
        });
    }

    @Test
    public void testMovePlayerInBoardWhenPlayersHaveJoined() {
        gameService.assignSignToPlayer("Ava");
        gameService.assignSignToPlayer("Bob");
        assert gameService.getGameState() == Status.STARTED;
        int column = 9;
        Player player = gameService.movePlayerInBoard("Ava",column - 1);
        assert player.getName().equals("Ava");
        assert player.getSign() == Sign.X;
        assert !player.isChance();

        //check the move added to board
        Sign[][] playBoard = gameService.getPlayingBoard().getSignBoard();
        assert playBoard[MAX_ROW-1][column-1] == Sign.X;

        //assert player status
        ArrayList<Player> players = gameService.getPlayers();
        Player playerAva = players.stream().filter(p -> p.getName().equals("Ava")).findFirst().get();
        Player playerBob = players.stream().filter(p -> p.getName().equals("Bob")).findFirst().get();
        assert playerAva.getName().equals("Ava");
        assert playerAva.getSign() == Sign.X;
        assert !playerAva.isChance();
        assert playerBob.getName().equals("Bob");
        assert playerBob.getSign() == Sign.O;
        assert playerBob.isChance();
    }

    @Test
    public void testMovePlayerInBoardWithInvalidName() {
        gameService.assignSignToPlayer("Ava");
        gameService.assignSignToPlayer("Bob");
        Assertions.assertThrows(InvalidPlayerNameException.class, () -> {
            gameService.movePlayerInBoard("Car",5);
        });
    }

    @Test
    public void testMovePlayerInBoardWithInvalidMove() {
        gameService.assignSignToPlayer("Ava");
        gameService.assignSignToPlayer("Bob");
        Assertions.assertThrows(InvalidMoveException.class, () -> {
            gameService.movePlayerInBoard("Ava",10);
        });
    }

    @Test
    public void testExceptionWhenPlayerMakeTwoTurnsInBoard() {
        gameService.assignSignToPlayer("Ava");
        gameService.assignSignToPlayer("Bob");
        assert gameService.getGameState() == Status.STARTED;
        int column = 9;
        Player player = gameService.movePlayerInBoard("Ava",column - 1);
        assert player.getName().equals("Ava");
        assert player.getSign() == Sign.X;
        assert !player.isChance();

        Assertions.assertThrows(PlayerChanceException.class, () -> {
            gameService.movePlayerInBoard("Ava",column - 1);
        });
    }

    @Test
    public void testMovePlayerInBoardWhenBothPlayersPlaying() {
        gameService.assignSignToPlayer("Ava");
        gameService.assignSignToPlayer("Bob");
        assert gameService.getGameState() == Status.STARTED;
        int column = 9;
        Player player = gameService.movePlayerInBoard("Ava",column - 1);
        assert player.getName().equals("Ava");
        assert player.getSign() == Sign.X;
        assert !player.isChance();

        //check the move added to board
        Sign[][] playBoard = gameService.getPlayingBoard().getSignBoard();
        assert playBoard[MAX_ROW-1][column-1] == Sign.X;

        Player player2 = gameService.movePlayerInBoard("Bob",column - 1);
        assert player2.getName().equals("Bob");
        assert player2.getSign() == Sign.O;
        assert !player2.isChance();

        //check the move added to board
        Sign[][] playBoard2 = gameService.getPlayingBoard().getSignBoard();
        assert playBoard2[MAX_ROW-1][column-1] == Sign.X;
        assert playBoard2[MAX_ROW-2][column-1] == Sign.O;

        //assert player status
        ArrayList<Player> players = gameService.getPlayers();
        Player playerAva = players.stream().filter(p -> p.getName().equals("Ava")).findFirst().get();
        Player playerBob = players.stream().filter(p -> p.getName().equals("Bob")).findFirst().get();
        assert playerAva.getName().equals("Ava");
        assert playerAva.getSign() == Sign.X;
        assert playerAva.isChance();
        assert playerBob.getName().equals("Bob");
        assert playerBob.getSign() == Sign.O;
        assert !playerBob.isChance();
    }


    @Test
    public void testMovePlayerInBoardWhenOnePlayerWinsWithFiveColumns() {
        gameService.assignSignToPlayer("Ava");
        gameService.assignSignToPlayer("Bob");
        assert gameService.getGameState() == Status.STARTED;

        //making moves until player wins
        int column1 = 9;
        int column2 = 8;
        for (int i=0;i <4; i++) {
            gameService.movePlayerInBoard("Ava",column1 - 1);
            gameService.movePlayerInBoard("Bob",column2 - 1);
        }
        //winning move
        Player player = gameService.movePlayerInBoard("Ava",column1 - 1);
        assert player.getName().equals("Ava");
        assert player.getSign() == Sign.X;
        assert player.isWinner();

        //check the move added to board
        Sign[][] playBoard2 = gameService.getPlayingBoard().getSignBoard();
        for (int i=1; i <=4; i++) {
            assert playBoard2[MAX_ROW-i][column1-1] == Sign.X;
            assert playBoard2[MAX_ROW-i][column2-1] == Sign.O;
        }
    }

    @Test
    public void testMovePlayerInBoardWhenOnePlayerWinsWithFiveRows() {
        gameService.assignSignToPlayer("Ava");
        gameService.assignSignToPlayer("Bob");
        assert gameService.getGameState() == Status.STARTED;

        //making moves until player wins
        for (int i=1;i <5; i++) {
            gameService.movePlayerInBoard("Ava",i);
            gameService.movePlayerInBoard("Bob",i);
        }
        //winning move
        Player player = gameService.movePlayerInBoard("Ava",5);
        assert player.getName().equals("Ava");
        assert player.getSign() == Sign.X;
        assert player.isWinner();

        //check the move added to board
        Sign[][] playBoard2 = gameService.getPlayingBoard().getSignBoard();
        for (int i=1; i <5; i++) {
            assert playBoard2[MAX_ROW-1][i] == Sign.X;
            assert playBoard2[MAX_ROW-2][i] == Sign.O;
        }
    }

    @Test
    public void testExceptionWhenPlayerPlaysIllegalColumn() {
        gameService.assignSignToPlayer("Ava");
        gameService.assignSignToPlayer("Bob");
        assert gameService.getGameState() == Status.STARTED;

        //making moves until player fills column
        Assertions.assertThrows(InvalidMoveException.class, () -> {
            for (int i=0;i <MAX_COL; i++) {
                gameService.movePlayerInBoard("Ava",8);
                gameService.movePlayerInBoard("Bob",8 );
            }
        });
    }

    @Test
    public void testExceptionWhenBoardIsFull() {
        gameService.assignSignToPlayer("Ava");
        gameService.assignSignToPlayer("Bob");
        assert gameService.getGameState() == Status.STARTED;

        //making random moves until both player fills board such that no player wins
        for(int j =0 ; j < 4; j++) {
            int col = MAX_COL;
            for(int i=0;i<4;i++) {
                gameService.movePlayerInBoard("Ava",col-1-i);
                gameService.movePlayerInBoard("Bob",col-2-i);
                col--;
            }
        }
        for(int j=0 ; j<3; j++) {
            gameService.movePlayerInBoard("Ava",0);
            gameService.movePlayerInBoard("Bob",0);
        }
        for(int i=0;i<2;i++){
            int col = MAX_COL;
            for(int j=1 ; j<4; j++) {
                gameService.movePlayerInBoard("Ava",col-1-j);
                gameService.movePlayerInBoard("Bob",col-2-j);
                col--;
            }
            gameService.movePlayerInBoard("Ava",1);
            gameService.movePlayerInBoard("Bob",8);
        }

        //Board is filled at this point
        Assertions.assertThrows(BoardFullException.class, () -> {
            gameService.movePlayerInBoard("Ava",4);
        });
    }


    @Test
    public void testResetGameWorksCorrectly() {
        gameService.assignSignToPlayer("Ava");
        gameService.assignSignToPlayer("Bob");
        assert gameService.getGameState() == Status.STARTED;
        int column = 9;
        gameService.movePlayerInBoard("Ava",column - 1);
        gameService.movePlayerInBoard("Bob",column - 1);

        //check the move added to board
        Sign[][] playBoard = gameService.getPlayingBoard().getSignBoard();
        assert playBoard[MAX_ROW-1][column-1] == Sign.X;
        assert playBoard[MAX_ROW-2][column-1] == Sign.O;

        //assert player status
        ArrayList<Player> players = gameService.getPlayers();
        assert players.size() == 2;

        //resetting game
        gameService.resetGame();
        assert gameService.getGameState() == Status.NOT_STARTED;
        //check the move removed from board
        Sign[][] playBoard2 = gameService.getPlayingBoard().getSignBoard();
        assert playBoard2[MAX_ROW-1][column-1] == Sign.E;
        assert playBoard2[MAX_ROW-2][column-1] == Sign.E;

        //assert player status
        ArrayList<Player> players2 = gameService.getPlayers();
        assert players2.size() == 0;
    }

    @Test
    public void testDisconnectPlayerWhenNoPlayerJoined() {
        gameService.assignSignToPlayer("Ava");
        Assertions.assertThrows(GameStatusException.class, () -> {
            gameService.disconnectPlayer("Ava");
        });
    }

    @Test
    public void testDisconnectPlayerWhenInvalidPlayerDisconnects() {
        gameService.assignSignToPlayer("Ava");
        gameService.assignSignToPlayer("Bob");
        Assertions.assertThrows(InvalidPlayerNameException.class, () -> {
            gameService.disconnectPlayer("Car");
        });
    }

    @Test
    public void testDisconnectPlayerWhenValidPlayerDisconnects() {
        gameService.assignSignToPlayer("Ava");
        gameService.assignSignToPlayer("Bob");

        Player player = gameService.disconnectPlayer("Ava");
        assert gameService.getGameState() == Status.FINISHED;
        assert player.getName().equals("Bob");
        assert player.isWinner();

        //assert player status
        ArrayList<Player> players = gameService.getPlayers();
        Player playerAva = players.stream().filter(p -> p.getName().equals("Ava")).findFirst().get();
        Player playerBob = players.stream().filter(p -> p.getName().equals("Bob")).findFirst().get();
        assert playerAva.getName().equals("Ava");
        assert !playerAva.isWinner();
        assert playerBob.getName().equals("Bob");
        assert playerBob.isWinner();
    }
}
