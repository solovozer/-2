#include <iostream>
#include <iomanip>
#include <conio.h>
#include <fstream>
#include <sstream>
#include <vector>
#include <map>
#include <string>
#include <cmath>
#include "Tiles.h"
#include "TilesSetting.h"
#include "MazeHandler.h"

#include <windows.h>

void MoveCursorToTop() {
    HANDLE hOut = GetStdHandle(STD_OUTPUT_HANDLE);
    COORD coord = { 0, 0 };
    SetConsoleCursorPosition(hOut, coord);
}

using namespace std;

static TilesSettings tilesSettings;

struct Direction {
    int dx;
    int dy;
private:
    Direction(int a, int b) : dx(a < 0 ? -1 : a > 0), dy(b < 0 ? -1 : b > 0) {}
public:
    static Direction Left() { return Direction(-1, 0); }
    static Direction Right() { return Direction(1, 0); }
    static Direction Up() { return Direction(0, -1); }
    static Direction Down() { return Direction(0, 1); }
};

class Player {
private:
    int x;
    int y;
public:
    Player() : x(0), y(0) {}

    int getX() const { return x; }
    int getY() const { return y; }
    void setPosition(int newX, int newY) { x = newX; y = newY; }
};

class Tile {
private:
    char symbol;
    string color;
public:
    Tile(TilesType tt, string tileColor = Colors::WHITE) : symbol(tilesSettings[tt]), color(tileColor) {
    }
    char getSymbol() const { return symbol; }
    string getColor() const { return color; }
    string getPaintedSymbol() const { return color + symbol + Colors::RESET; }
};

class TileFactory {
    static map<TilesType, Tile*> tiles;
public:
    static Tile* getTile(TilesType tt) {
        auto it = tiles.find(tt);
        if (it == tiles.end()) {
            Tile* t = new Tile(tt);
            tiles[tt] = t;
            return t;
        }
        return it->second;
    }
};

map<TilesType, Tile*> TileFactory::tiles;


class Board {
private:
    int rows, cols;
    vector<vector<Tile*>> grid;
public:
    Board(int m, int n) : rows(m), cols(n), grid(m, vector<Tile*>(n, TileFactory::getTile(TilesType::PathTile))) {}

    int getRows() const { return rows; }
    int getCols() const { return cols; }
    Tile* getTile(int x, int y) const {
        if (x >= 0 && x < cols && y >= 0 && y < rows) return grid[y][x];
        return nullptr;
    }
    void setTile(int x, int y, Tile* t) {
        if (x >= 0 && x < cols && y >= 0 && y < rows) grid[y][x] = t;
    }
};

class PlayerController {
private:
    Player& player;
    Board& board;
public:
    PlayerController(Player& p, Board& b) : player(p), board(b) {}

    void Move(Direction dir) {
        int currentX = player.getX();
        int currentY = player.getY();
        int x = currentX + dir.dx;
        int y = currentY + dir.dy;
        Tile* temp = TileFactory::getTile(TilesType::WallTile);
        if (x >= 0 && x < board.getCols() && board.getTile(x, currentY) != temp) currentX = x;
        if (y >= 0 && y < board.getRows() && board.getTile(currentX, y) != temp) currentY = y;
        player.setPosition(currentX, currentY);
    }
};


class Artist {
private:
    Board& board;
    Player& player;
public:
    Artist(Board& b, Player& p) : board(b), player(p) {}

    void drawFromData(vector<TileInfo> tiles) {
        for (const auto& d : tiles) {
            switch (d.type) {
            case TilesType::WallTile:
                board.setTile(d.x, d.y, TileFactory::getTile(TilesType::WallTile)); 
                break;
            case TilesType::StartTile:
                player.setPosition(d.x, d.y);
                board.setTile(d.x, d.y, TileFactory::getTile(TilesType::StartTile));
                break;
            case TilesType::EndTile:
                board.setTile(d.x, d.y, TileFactory::getTile(TilesType::EndTile));
                break;
            default:
                board.setTile(d.x, d.y, TileFactory::getTile(TilesType::UnknownTile));
                break;
            }
        }
    }

    string display()
    {
        stringstream buffer;
        for (int y = 0; y < board.getRows(); y++)
        {
            for (int x = 0; x < board.getCols(); x++)
            {
                buffer << std::setw(1);
                if (x == player.getX() && y == player.getY()) buffer << TileFactory::getTile(TilesType::PlayerTile)->getSymbol();
                {
                    Tile* t = board.getTile(x, y);
                    buffer << t->getColor() << t->getSymbol() << Colors::RESET << endl;
                }

            }
            buffer << '\n';
        }
        return buffer.str();
    }

    string displayVicinity(int radius) {
        stringstream buffer;
        int viewSize = (radius * 2) + 1;
        int startX = player.getX() - radius;
        int startY = player.getY() - radius;

        if (startX < 0) startX = 0;
        if (startY < 0) startY = 0;
        if (startX + viewSize > board.getCols()) startX = board.getCols()  - viewSize;
        if (startY + viewSize > board.getRows()) startY = board.getRows() - viewSize;

        for (int y = startY; y < startY + viewSize; y++) {
            for (int x = startX; x < startX + viewSize; x++) {
                if (x == player.getX() && y == player.getY()) buffer << Colors::WHITE << TileFactory::getTile(TilesType::PlayerTile)->getSymbol() << Colors::RESET;
                else if (x >= 0 && x < board.getCols() && y >= 0 && y < board.getRows()) {
                    Tile* t = board.getTile(x, y);
                    buffer << t->getColor() << t->getSymbol() << Colors::RESET;
                }
                else buffer << TileFactory::getTile(TilesType::VoidTile);
            }
            buffer << endl;
        }

        return buffer.str();
    }
};

string GetRandomColor() {
    static const vector<string> colors = { "\033[31m", "\033[32m", "\033[33m", "\033[34m", "\033[35m" };
    return colors[rand() % colors.size()];
}


void ShowStartScreen() {
#ifdef _WIN32
    system("cls");
#else
    std::cout << "\033[2J\033[H";
#endif

    std::cout << "========================================" << std::endl;
    std::cout << "||                                    ||" << std::endl;
    std::cout << "||          MAZE RUNNER 1.0           ||" << std::endl;
    std::cout << "||                                    ||" << std::endl;
    std::cout << "========================================" << std::endl;
    std::cout << "||                                    ||" << std::endl;
    std::cout << "||      [W] Move Up                   ||" << std::endl;
    std::cout << "||      [S] Move Down                 ||" << std::endl;
    std::cout << "||      [A] Move Left                 ||" << std::endl;
    std::cout << "||      [D] Move Right                ||" << std::endl;
    std::cout << "||                                    ||" << std::endl;
    std::cout << "||      [Q] Quit Game                 ||" << std::endl;
    std::cout << "||                                    ||" << std::endl;
    std::cout << "========================================" << std::endl;
    std::cout << "\n Press any key to start... ";
}

int main() {
    const vector<TileInfo> tiles = readMazeFile("maze.txt");
    Player player;
    Board board(50, 50);
    Artist artist(board, player);
    artist.drawFromData(tiles);
    vector<string> palette = { Colors::GREEN, Colors::CYAN, Colors::MAGENTA, Colors::YELLOW };

    PlayerController pc(player, board);

    ShowStartScreen();
    char user_input = _getch();
    string error = "";
#ifdef _WIN32
    system("cls");
#else
    std::cout << "\033[2J\033[H";
#endif

    while (user_input != 'q') {
        error = "";
        MoveCursorToTop();
        cout << artist.displayVicinity(10) << endl;
        char user_input = _getch();
        if (user_input == 'a') pc.Move(Direction::Left());
        else if (user_input == 'd') pc.Move(Direction::Right());
        else if (user_input == 'w') pc.Move(Direction::Up());
        else if (user_input == 's') pc.Move(Direction::Down());
        else error = "Unknown input, try again";
        if (!error.empty()) {
            cout << error << endl;
        }
        else {
            cout << "                           " << endl;
        }
    }
    return 0;
}


/*    for (auto& i : tiles) {
        cout << i.x << " " << i.y << " " << i.type << endl;
    }*/