#pragma once
#include <iostream>
#include <string>
#include "TilesSetting.h"

#ifdef _WIN32
    #include <windows.h>
#endif

class Screen {
private:
    static const int BORDER_WIDTH = 80;
    static const int BORDER_HEIGHT = 25;

    void drawHorizontalBorder() const {
        std::cout << Colors::CYAN;
        for (int i = 0; i < BORDER_WIDTH; i++) {
            std::cout << "=";
        }
        std::cout << Colors::RESET << std::endl;
    }

    // Count visible characters (excluding ANSI color codes)
    int countVisibleChars(const std::string& str) const {
        int count = 0;
        bool inAnsiCode = false;
        for (char c : str) {
            if (c == '\033') {  // Start of ANSI code
                inAnsiCode = true;
            } else if (inAnsiCode && c == 'm') {  // End of ANSI code
                inAnsiCode = false;
            } else if (!inAnsiCode) {
                count++;
            }
        }
        return count;
    }

    // Helper function to print padded lines correctly (accounting for ANSI codes)
    void printPaddedLine(const std::string& content) const {
        std::cout << Colors::CYAN << "||" << Colors::RESET;
        std::cout << content;
        int visibleLength = countVisibleChars(content);
        int padding = BORDER_WIDTH - visibleLength - 4;  // -4 for "|| ||"
        if (padding > 0) {
            std::cout << std::string(padding, ' ');
        }
        std::cout << Colors::CYAN << "||" << Colors::RESET << std::endl;
    }

public:
    void clearScreen() const {
#ifdef _WIN32
        system("cls");
#else
        std::cout << "\033[2J\033[H";
#endif
    }

    void moveCursorToTop() const {
#ifdef _WIN32
        HANDLE hOut = GetStdHandle(STD_OUTPUT_HANDLE);
        COORD coord = { 0, 0 };
        SetConsoleCursorPosition(hOut, coord);
#else
        std::cout << "\033[H";
#endif
    }

    void showStartScreen() const {
        clearScreen();
        std::cout << std::endl;
        drawHorizontalBorder();
        printPaddedLine("");
        printPaddedLine(Colors::GREEN + std::string("          MAZE RUNNER 1.0") + Colors::RESET);
        printPaddedLine("");
        drawHorizontalBorder();
        printPaddedLine("");
        printPaddedLine(Colors::YELLOW + std::string("[W]") + Colors::RESET + " Move Up");
        printPaddedLine(Colors::YELLOW + std::string("[S]") + Colors::RESET + " Move Down");
        printPaddedLine(Colors::YELLOW + std::string("[A]") + Colors::RESET + " Move Left");
        printPaddedLine(Colors::YELLOW + std::string("[D]") + Colors::RESET + " Move Right");
        printPaddedLine("");
        printPaddedLine(Colors::RED + std::string("[Q]") + Colors::RESET + " Quit Game");
        printPaddedLine("");
        printPaddedLine(Colors::BLUE + std::string("[H]") + Colors::RESET + " Set Difficulty   " + Colors::MAGENTA + std::string("[K]") + Colors::RESET + " Symbol Settings");
        printPaddedLine("");
        drawHorizontalBorder();

        std::cout << Colors::GREEN << "\n Press any key to start... " << Colors::RESET;
    }

    void displayGameFrame(const std::string& gameContent) const {
        drawHorizontalBorder();
        printPaddedLine(Colors::YELLOW + std::string("Position") + Colors::RESET);
        drawHorizontalBorder();
        
        std::cout << gameContent;
        
        drawHorizontalBorder();
    }

    void displayVictoryScreen(long seconds) const {
        clearScreen();
        std::cout << std::endl;
        drawHorizontalBorder();
        printPaddedLine("");
        printPaddedLine(Colors::MAGENTA + std::string("            VICTORY!") + Colors::RESET);
        printPaddedLine("");
        drawHorizontalBorder();
        printPaddedLine("");
        printPaddedLine(Colors::GREEN + std::string("Time Spent: ") + Colors::RESET + Colors::YELLOW + std::to_string(seconds) + " seconds" + Colors::RESET);
        printPaddedLine("");
        printPaddedLine(Colors::YELLOW + std::string("[R]") + Colors::RESET + " Play Again");
        printPaddedLine(Colors::RED + std::string("[Q]") + Colors::RESET + " Quit Game");
        printPaddedLine(Colors::MAGENTA + std::string("[B]") + Colors::RESET + " GO BACK TO MAIN MENU");
        printPaddedLine("");
        drawHorizontalBorder();
    }

    void displayDifficultyMenu() const {
        clearScreen();
        std::cout << std::endl;
        drawHorizontalBorder();
        printPaddedLine("");
        printPaddedLine(Colors::BLUE + std::string("         SELECT DIFFICULTY") + Colors::RESET);
        printPaddedLine("");
        drawHorizontalBorder();
        printPaddedLine("");
        printPaddedLine(Colors::YELLOW + std::string("[1]") + Colors::RESET + " Easy   (10x10)");
        printPaddedLine(Colors::YELLOW + std::string("[2]") + Colors::RESET + " Medium (30x30)");
        printPaddedLine(Colors::YELLOW + std::string("[3]") + Colors::RESET + " Hard   (50x50)");
        printPaddedLine("");
        printPaddedLine(Colors::RED + std::string("[B]") + Colors::RESET + " Back");
        printPaddedLine("");
        drawHorizontalBorder();
    }

    void displaySettingsMenu() const {
        clearScreen();
        std::cout << std::endl;
        drawHorizontalBorder();
        printPaddedLine("");
        printPaddedLine(Colors::MAGENTA + std::string("         SYMBOL SETTINGS") + Colors::RESET);
        printPaddedLine("");
        drawHorizontalBorder();
        printPaddedLine("");
        printPaddedLine(std::string("Current Symbols:"));
        printPaddedLine(Colors::WHITE + std::string("Path: ") + Colors::RESET + tilesSettings[TilesType::PathTile]);
        printPaddedLine(Colors::CYAN + std::string("Player: ") + Colors::RESET + tilesSettings[TilesType::PlayerTile]);
        printPaddedLine(Colors::GREEN + std::string("Start: ") + Colors::RESET + tilesSettings[TilesType::StartTile]);
        printPaddedLine(Colors::MAGENTA + std::string("End: ") + Colors::RESET + tilesSettings[TilesType::EndTile]);
        printPaddedLine(Colors::RED + std::string("Wall: ") + Colors::RESET + tilesSettings[TilesType::WallTile]);
        printPaddedLine("");
        printPaddedLine(Colors::YELLOW + std::string("[1-5]") + Colors::RESET + " Change Symbol   " + Colors::RED + std::string("[B]") + Colors::RESET + " Back");
        printPaddedLine("");
        drawHorizontalBorder();
    }
};