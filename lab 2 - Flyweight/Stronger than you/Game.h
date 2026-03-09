#pragma once
#include <iostream>
#include <vector>
#include <chrono>
#include "Player.h"
#include "Board.h"
#include "Artist.h"
#include "PlayerController.h"
#include "Direction.h"
#include "Screen.h"
#include "MazeHandler.h"

enum class Difficulty {
    EASY = 10,
    MEDIUM = 30,
    HARD = 50,
    HELL = 2000
};

class Game {
private:
    Player player;
    int boardSize;
    Board board;
    Artist artist;
    PlayerController controller;
    Screen screen;
    std::chrono::high_resolution_clock::time_point startTime;
    bool isGameRunning;
    bool hasWon;
    Difficulty difficulty;

public:
    Game()
        : difficulty(Difficulty::EASY), boardSize(static_cast<int>(Difficulty::EASY)),
        board(boardSize, boardSize), artist(board, player), controller(player, board),
        isGameRunning(false), hasWon(false) {
    }

    std::string getMazeFileForDifficulty(Difficulty diff) const {
        switch (diff) {
        case Difficulty::EASY:   return "mazes/maze_easy.txt";
        case Difficulty::MEDIUM: return "mazes/maze_medium.txt";
        case Difficulty::HARD:   return "mazes/maze_hard.txt";
        case Difficulty::HELL:   return "mazes/maze_hell.txt";
        default:                 return "mazes/maze_hard.txt";
        }
    }

    void initialize() {
        std::string mazeFile = getMazeFileForDifficulty(difficulty);
        const std::vector<TileInfo> tiles = readMazeFile(mazeFile);
        artist.drawFromData(tiles);
    }

    void run() {
        screen.clearScreen();
        startTime = std::chrono::high_resolution_clock::now();
        isGameRunning = true;
        hasWon = false;
    }

    void update() {
        screen.moveCursorToTop();
        screen.displayGameFrame(artist.displayVicinity(4));
    }

    void showVictoryScreen() {
        auto endTime = std::chrono::high_resolution_clock::now();
        auto duration = std::chrono::duration_cast<std::chrono::seconds>(endTime - startTime);
        screen.displayVictoryScreen(duration.count());
    }

    void cleanup() {
        screen.clearScreen();
        std::cout << Colors::GREEN << "Thanks for playing!" << Colors::RESET << std::endl;
    }

    void reset() {
        player.setPosition(0, 0);
        board = Board(boardSize, boardSize);
        initialize();
        hasWon = false;
        isGameRunning = true;
    }


    int getPlayerX() const { return player.getX(); }
    int getPlayerY() const { return player.getY(); }

    bool checkVictory() {
        Tile* endTile = board.getTile(player.getX(), player.getY());
        if (endTile && endTile->getSymbol() == tilesSettings[TilesType::EndTile]) {
            hasWon = true;
            return true;
        }
        return false;
    }

    bool isRunning() const { return isGameRunning; }
    bool won() const { return hasWon; }
    void setRunning(bool running) { isGameRunning = running; }

    void showDifficultyMenu() {
        screen.displayDifficultyMenu();
    }

    void showSettingsMenu() {
        screen.displaySettingsMenu();
    }

    void setDifficulty(Difficulty diff) {
        difficulty = diff;
        boardSize = static_cast<int>(diff);
        board = Board(boardSize, boardSize);
    }

    Difficulty getDifficulty() const { return difficulty; }
    int getBoardSize() const { return boardSize; }

    PlayerController& getController() {
        return controller;
    }
};