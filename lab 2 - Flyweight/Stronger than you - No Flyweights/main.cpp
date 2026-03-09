#include "Game.h"
#include <cstdlib>
#include <ctime>
#include <conio.h>
#include <windows.h>
#include <psapi.h>

// Global tile settings instance
TilesSettings tilesSettings;

enum class GameState {
    START_MENU,
    DIFFICULTY_MENU,
    SETTINGS_MENU,
    PLAYING,
    VICTORY,
    QUIT
};

void print_total_memory() {
    PROCESS_MEMORY_COUNTERS_EX pmc;
    if (GetProcessMemoryInfo(GetCurrentProcess(), (PROCESS_MEMORY_COUNTERS*)&pmc, sizeof(pmc))) {
        size_t physMemUsedByMe = pmc.WorkingSetSize;
        size_t virtualMemUsedByMe = pmc.PrivateUsage;

        std::cout << "Physical RAM: " << physMemUsedByMe / (1024.0 * 1024.0) << " MB" << std::endl;
        std::cout << "Virtual Mem:  " << virtualMemUsedByMe / (1024.0 * 1024.0) << " MB" << std::endl;
    }
}

int main() {
    srand(static_cast<unsigned int>(time(0)));

    Game game;
    GameState state = GameState::START_MENU;

    while (state != GameState::QUIT) {
        if (state == GameState::START_MENU) {
            Screen screen;
            screen.showStartScreen();
            if (game.getDifficulty() == Difficulty::HELL) cout << Colors::RED << "Welcome to hell :)" << Colors::RESET << endl;
            char user_input = _getch();

            if (user_input == 'h' || user_input == 'H') {
                state = GameState::DIFFICULTY_MENU;
            }
            else if (user_input == 'k' || user_input == 'K') {
                state = GameState::SETTINGS_MENU;
            }
            else if (user_input == 'q' || user_input == 'Q') {
                state = GameState::QUIT;
            }
            else {
                // Start the game
                game.initialize();
                game.run();
                state = GameState::PLAYING;
            }
        }
        else if (state == GameState::DIFFICULTY_MENU) {
            game.showDifficultyMenu();
            char user_input = _getch();

            if (user_input == '1') {
                game.setDifficulty(Difficulty::EASY);
                state = GameState::START_MENU;
            }
            else if (user_input == '2') {
                game.setDifficulty(Difficulty::MEDIUM);
                state = GameState::START_MENU;
            }
            else if (user_input == '3') {
                game.setDifficulty(Difficulty::HARD);
                state = GameState::START_MENU;
            }
            else if (toupper(user_input) == 'I')
                    if (toupper(_getch()) == 'M')
                        if (toupper(_getch()) == 'M')
                            if (toupper(_getch()) == 'O')
                                if (toupper(_getch()) == 'R')
                                    if (toupper(_getch()) == 'T')
                                        if (toupper(_getch()) == 'A')
                                            if (toupper(_getch()) == 'L')
                                                if (toupper(_getch()) == 'I')
                                                    if (toupper(_getch()) == 'T')
                                                        if (toupper(_getch()) == 'Y') {
                                                            game.setDifficulty(Difficulty::HELL);
                                                            state = GameState::START_MENU;
                                                        }
            else if (user_input == 'b' || user_input == 'B') {
                state = GameState::START_MENU;
            }
        }
        else if (state == GameState::SETTINGS_MENU) {
            game.showSettingsMenu();
            char user_input = _getch();

            if (user_input == '1') {
                std::cout << Colors::CYAN << "\nEnter new symbol for Path: " << Colors::RESET;
                char symbol = _getch();
                tilesSettings.Setting(TilesType::PathTile, symbol);
            }
            else if (user_input == '2') {
                std::cout << Colors::CYAN << "\nEnter new symbol for Player: " << Colors::RESET;
                char symbol = _getch();
                tilesSettings.Setting(TilesType::PlayerTile, symbol);
            }
            else if (user_input == '3') {
                std::cout << Colors::CYAN << "\nEnter new symbol for Start: " << Colors::RESET;
                char symbol = _getch();
                tilesSettings.Setting(TilesType::StartTile, symbol);
            }
            else if (user_input == '4') {
                std::cout << Colors::CYAN << "\nEnter new symbol for End: " << Colors::RESET;
                char symbol = _getch();
                tilesSettings.Setting(TilesType::EndTile, symbol);
            }
            else if (user_input == '5') {
                std::cout << Colors::CYAN << "\nEnter new symbol for Wall: " << Colors::RESET;
                char symbol = _getch();
                tilesSettings.Setting(TilesType::WallTile, symbol);
            }
            else if (user_input == 'b' || user_input == 'B') {
                state = GameState::START_MENU;
            }
        }
        else if (state == GameState::PLAYING) {
            if (!game.won()) {
                // Game is running, not won yet
                if (game.isRunning()) {
                    game.update();
                }
                char user_input = _getch();

                if (user_input == 'q' || user_input == 'Q') {
                    state = GameState::QUIT;
                }
                else if (user_input == 'a' || user_input == 'A') {
                    game.getController().Move(Direction::Left());
                }
                else if (user_input == 'd' || user_input == 'D') {
                    game.getController().Move(Direction::Right());
                }
                else if (user_input == 'w' || user_input == 'W') {
                    game.getController().Move(Direction::Up());
                }
                else if (user_input == 's' || user_input == 'S') {
                    game.getController().Move(Direction::Down());
                }

                // Check if player reached the end tile
                if (game.checkVictory()) {
                    game.setRunning(false);
                    game.showVictoryScreen();
                    state = GameState::VICTORY;
                }
            }
        }
        else if (state == GameState::VICTORY) {
            // Game is won, waiting for restart or quit
            char user_input = _getch();

            if (user_input == 'q' || user_input == 'Q') {
                state = GameState::QUIT;
            }
            else if (user_input == 'b' || user_input == 'B') {
                game.reset();
                state = GameState::START_MENU;
            }
            else if (user_input == 'r' || user_input == 'R') {
                game.reset();
                game.run();
                state = GameState::PLAYING;
            }
        }
        print_total_memory();
    }

    game.cleanup();

    return 0;
}