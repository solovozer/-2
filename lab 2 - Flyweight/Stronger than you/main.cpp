#include "Game.h"
#include <cstdlib>
#include <ctime>

#ifdef _WIN32
    #include <conio.h>
#else
    #include <termios.h>
    #include <unistd.h>
    char _getch() {
        struct termios oldt, newt;
        char ch;
        tcgetattr(STDIN_FILENO, &oldt);
        newt = oldt;
        newt.c_lflag &= ~(ICANON | ECHO);
        tcsetattr(STDIN_FILENO, TCSANOW, &newt);
        ch = getchar();
        tcsetattr(STDIN_FILENO, TCSANOW, &oldt);
        return ch;
    }
#endif

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

int main() {
    srand(static_cast<unsigned int>(time(0)));
    
    Game game;
    GameState state = GameState::START_MENU;

    while (state != GameState::QUIT) {
        if (state == GameState::START_MENU) {
            Screen screen;
            screen.showStartScreen();
            char user_input = _getch();

            if (user_input == 'h' || user_input == 'H') {
                state = GameState::DIFFICULTY_MENU;
            } else if (user_input == 'k' || user_input == 'K') {
                state = GameState::SETTINGS_MENU;
            } else if (user_input == 'q' || user_input == 'Q') {
                state = GameState::QUIT;
            } else {
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
            } else if (user_input == '2') {
                game.setDifficulty(Difficulty::MEDIUM);
                state = GameState::START_MENU;
            } else if (user_input == '3') {
                game.setDifficulty(Difficulty::HARD);
                state = GameState::START_MENU;
            } else if (user_input == 'b' || user_input == 'B') {
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
                TileFactory::clearCache();
            } else if (user_input == '2') {
                std::cout << Colors::CYAN << "\nEnter new symbol for Player: " << Colors::RESET;
                char symbol = _getch();
                tilesSettings.Setting(TilesType::PlayerTile, symbol);
                TileFactory::clearCache();
            } else if (user_input == '3') {
                std::cout << Colors::CYAN << "\nEnter new symbol for Start: " << Colors::RESET;
                char symbol = _getch();
                tilesSettings.Setting(TilesType::StartTile, symbol);
                TileFactory::clearCache();
            } else if (user_input == '4') {
                std::cout << Colors::CYAN << "\nEnter new symbol for End: " << Colors::RESET;
                char symbol = _getch();
                tilesSettings.Setting(TilesType::EndTile, symbol);
                TileFactory::clearCache();
            } else if (user_input == '5') {
                std::cout << Colors::CYAN << "\nEnter new symbol for Wall: " << Colors::RESET;
                char symbol = _getch();
                tilesSettings.Setting(TilesType::WallTile, symbol);
                TileFactory::clearCache();
            } else if (user_input == 'b' || user_input == 'B') {
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
            else if (user_input == 'r' || user_input == 'R') {
                game.reset();
                game.run();
                state = GameState::PLAYING;
            }
        }
    }
    
    game.cleanup();
    
    return 0;
}
