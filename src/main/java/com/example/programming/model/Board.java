package com.example.programming.model;

public class Board {

    private Sign[][] signBoard;
    private static final int MAX_ROW = 6;
    private static final int MAX_COL = 9;

    public Board() {
        signBoard = new Sign[MAX_ROW][MAX_COL];
        this.reset();
    }

    public void reset() {
        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j < MAX_COL; j++) {
                this.signBoard[i][j] = Sign.E;
            }
        }
    }

    private boolean isValidRow(int row) {
        return (row >= 0 && row < MAX_ROW);
    }

    public boolean isValidCol(int col) {
        return (col >= 0 && col < MAX_COL);
    }

    private boolean isEmpty(int row, int col) {
        if (isValidRow(row) && isValidCol(col)) {
            return this.signBoard[row][col] == Sign.E;
        }
        return false;
    }

    public boolean addMoveToBoard(int col, Sign sign) {
        boolean moved = true;
        for(int i = MAX_ROW - 1; i >= 0; i--) {
            if (isEmpty(i, col)) {
                this.signBoard[i][col] = sign;
                moved = true;
                break;
            } else {
                moved = false;
            }
        }
        printBoard();
        return moved;
    }


    private void printBoard() {
        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j < MAX_COL; j++) {
                System.out.print(this.signBoard[i][j].symbol + " ");
            }
            System.out.println();
        }
    }

    public boolean checkFull() {
        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j < MAX_COL; j++) {
                if (this.signBoard[i][j] == Sign.E) {
                    return false;
                }
            }
        }
        return true;
    }


    public boolean checkWinner(Sign sign) {
        return checkRows(sign) ||
                checkColumns(sign) ||
                checkMainDiagonal(sign) ||
                checkOppositeDiagonal(sign);

    }

    private boolean checkColumns(Sign p) {
        for (int i = 0; i <= MAX_ROW - 5; i++) {
            for (int j = 0; j < MAX_COL; j++) {
                if (signBoard[i][j]   == p &&
                        signBoard[i+1][j] == p &&
                        signBoard[i+2][j] == p &&
                        signBoard[i+3][j] == p &&
                        signBoard[i+4][j] == p) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkRows(Sign p) {
        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j <= MAX_COL - 5; j++) {
                if (signBoard[i][j]   == p &&
                        signBoard[i][j+1] == p &&
                        signBoard[i][j+2] == p &&
                        signBoard[i][j+3] == p &&
                        signBoard[i][j+4] == p) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkMainDiagonal(Sign p) {
        for (int i = 0; i <= MAX_ROW - 5; i++) {
            for (int j = 0; j <= MAX_COL - 5; j++) {
                if (signBoard[i][j] == p &&
                        signBoard[i + 1][j + 1] == p &&
                        signBoard[i + 2][j + 2] == p &&
                        signBoard[i + 3][j + 3] == p &&
                        signBoard[i + 4][j + 4] == p) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkOppositeDiagonal(Sign p) {
        for (int i = 0; i <= MAX_ROW - 5 ; i++) {
            for (int j = 4; j < MAX_COL; j++) {
                if (signBoard[i][j]   == p &&
                        signBoard[i+1][j-1] == p &&
                        signBoard[i+2][j-2] == p &&
                        signBoard[i+3][j-3] == p &&
                        signBoard[i+4][j-4] == p) {
                    return true;
                }
            }
        }
        return false;
    }

    public Sign[][] getSignBoard() {
        return signBoard;
    }
}
