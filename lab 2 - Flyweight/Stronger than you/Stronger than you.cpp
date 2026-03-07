#include <iostream>
#include <fstream>
#include <vector>
#include <map>
#include <string>
#include <cmath>
#include "MazeGenerator.h"

using namespace std;

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
public:
    Tile(char s) : symbol(s) {}
    char getSymbol() const { return symbol; }
};

class TileFactory {
    static map<char, Tile*> tiles;
public:
    static Tile* getTile(char symbol) {
        auto it = tiles.find(symbol);
        if (it == tiles.end()) {
            Tile* t = new Tile(symbol);
            tiles[symbol] = t;
            return t;
        }
        return it->second;
    }
};

map<char, Tile*> TileFactory::tiles;

class Board {
private:
    int rows, cols;
    vector<vector<Tile*>> grid;
public:
    Board(int m, int n) : rows(m), cols(n), grid(m, vector<Tile*>(n, TileFactory::getTile('.'))) {}

    int getRows() const { return rows; }
    int getCols() const { return cols; }
    Tile* getTile(int x, int y) const {
        if (x >= 0 && x < cols && y >= 0 && y < rows)
            return grid[y][x];
        return nullptr;
    }
    void setTile(int x, int y, Tile* t) {
        if (x >= 0 && x < cols && y >= 0 && y < rows)
            grid[y][x] = t;
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
        if (x >= 0 && x < board.getCols())
            currentX = x;
        if (y >= 0 && y < board.getRows())
            currentY = y;
        player.setPosition(currentX, currentY);
    }
};


class Artist {
private:
    Board& board;
public:
    Artist(Board& b) : board(b) {}
    void drawWall(int x, int y) {
        board.setTile(x, y, TileFactory::getTile('#'));
    }
    string display(const Player& player)
    {
        string buffer;
        for (int y = 0; y < board.getRows(); y++)
        {
            for (int x = 0; x < board.getCols(); x++)
            {
                if (x == player.getX() && y == player.getY())
                    buffer += '@';
                else {
                    Tile* t = board.getTile(x, y);
                    buffer += (t ? t->getSymbol() : '?');
                }
            }
            buffer += '\n';
        }
        return buffer;
    }
};

struct TileInfo {
    int x;
    int y;
    char symbol;
};

const static vector<TileInfo> readMazeFile(const string &filename) {
    vector<TileInfo> tiles;
    ifstream file(filename);
    if (file) {
        int x, y;
        char sym;
        // file format: x y symbol  (symbol could be '#', 'S', 'E', etc.)
        while (file >> x >> y >> sym) {
            tiles.push_back({x, y, sym});
        }
    }
    return tiles;
}

int main() {
    // read a more generic maze file, allowing walls (#), start (S), end (E), etc.
    const vector<TileInfo> tiles = readMazeFile("maze.txt");
    Player player;
    Board board(100, 100);
    Artist artist(board);

    for (const auto &t : tiles) {
        switch (t.symbol) {
        case '#':
            artist.drawWall(t.x, t.y);
            break;
        case 'S':
            player.setPosition(t.x, t.y);
            board.setTile(t.x, t.y, TileFactory::getTile('S'));
            break;
        case 'E':
            board.setTile(t.x, t.y, TileFactory::getTile('E'));
            break;
        default:
            board.setTile(t.x, t.y, TileFactory::getTile(t.symbol));
            break;
        }
    }

    PlayerController pc(player, board);

    string user_input = "";
    while (user_input != "q") {
        cin >> user_input;
        if (user_input == "l") pc.Move(Direction::Left());
        else if (user_input == "r") pc.Move(Direction::Right());
        else if (user_input == "u") pc.Move(Direction::Up());
        else if (user_input == "d") pc.Move(Direction::Down());
        else
            cout << "Unknown key, try again" << endl;

        cout << artist.display(player) << endl;
    }
    return 0;
}