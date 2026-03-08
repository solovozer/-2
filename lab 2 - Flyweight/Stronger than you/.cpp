s#include "Game.h"
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

int main() {
    srand(static_cast<unsigned int>(time(0)));
    
    Game game("maze.txt");
    game.initialize();
    game.run();
    
    char user_input = _getch();
    
    while (true) {
        if (!game.won()) {
            // Game is running, not won yet
            if (game.isRunning()) {
                game.update();
            }
            user_input = _getch();
            
            if (user_input == 'q' || user_input == 'Q') {
                break;
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
            }
        }
        else {
            // Game is won, waiting for restart or quit
            user_input = _getch();
            
            if (user_input == 'q' || user_input == 'Q') {
                break;
            }
            else if (user_input == 'r' || user_input == 'R') {
                game.reset();
                game.run();
            }
        }
    }
    
    game.cleanup();
    
    return 0;
}
