package com.example.programming;

import com.example.programming.client.GameRestClient;
import com.example.programming.client.GameWebClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Scanner;

public class GameMenuApplication {

    private static GameWebClient gameWebClient = new GameWebClient();
    private static GameRestClient gameRestClient = new GameRestClient();

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        showMainScreen();
    }

    private static void showMainScreen() {
        int choice;
        do {
            System.out.println("*****************************************************");
            System.out.println("*****************************************************");
            System.out.println("WELCOME TO THE GAME");
            System.out.println("*****************************************************");
            System.out.println("*****************************************************");
            System.out.println("1. START THE GAME");
            System.out.println("2. MAKE A MOVE");
            System.out.println("3. RESET");
            System.out.println("4. SEE CURRENT GAME STATE");
            System.out.println("5. DISCONNECT");
            System.out.println("6. EXIT");
            choice = scanner.nextInt();
            switch (choice) {
                case 1 : startGame();
                    break;
                case 2 : addMove();
                    break;
                case 3 : resetGame();
                    break;
                case 4 : displayState();
                    break;
                case 5 : disconnect();
                    break;
                default: break;
            }
        } while (choice!=6) ;
    }

    private static void disconnect() {
        System.out.println("*****************************************************");
        System.out.println("ENTER YOUR NAME");
        String name = scanner.next();
        ResponseEntity<Object> responseEntity = gameRestClient.disconnect(name);
        displayResponse(responseEntity);
    }

    private static void displayState() {
        //Mono<GameState> gameState = gameWebClient.getGameState();
        System.out.println("*****************************************************");
        System.out.println("DISPLAYING GAME STATE");
        ResponseEntity<Object> responseEntity = gameRestClient.getBoard();
        displayResponse(responseEntity);
    }

    private static void resetGame() {
       // Mono<GameState> gameState = gameWebClient.resetGame();
        //displayGameState(gameState);
        System.out.println("*****************************************************");
        System.out.println("RESETTING GAME");
        ResponseEntity<Object> responseEntity = gameRestClient.resetGame();
        displayResponse(responseEntity);
    }

    private static void addMove() {
        System.out.println("*****************************************************");
        System.out.println("ENTER YOUR NAME");
        String name = scanner.next();
        System.out.println("ENTER YOUR COLUMN");
        int column = scanner.nextInt();
       //Mono<GameState> gameState = gameWebClient.movePlayer(name,column);
        //displayGameState(gameState);
        ResponseEntity<Object> responseEntity = gameRestClient.addMove(name, column);
        displayResponse(responseEntity);
    }

    private static void startGame() {
        System.out.println("*****************************************************");
        System.out.println("ENTER YOUR NAME");
        String name = scanner.next();
       // Object gameState = gameWebClient.initPlayer(name);
        ResponseEntity<Object> responseEntity = gameRestClient.initPlayer(name);
        displayResponse(responseEntity);
    }

    private static void displayResponse(ResponseEntity<Object> responseEntity) {
        if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("SUCCESSFULLY CREATED PLAYER");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("MESSAGE : " + responseEntity.getBody());
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");
        } else if(responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("OPERATION SUCCESSFUL");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("MESSAGE : " + responseEntity.getBody());
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");
        } else{
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("ERROR ENCOUNTERED");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("ERROR : " + responseEntity.getBody());
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");
        }
    }



}
