#include <iostream>
#include <fstream>
#include <vector>
#include <map>
#include <string>
#include <cmath>

using namespace std;

// simple value type for directions
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

const static vector<pair<int, int>> readWallFile(string filename) {
    vector<pair<int, int>> walls;
    ifstream file(filename);
    if (file) {
        int x, y;
        while (file >> x >> y) walls.emplace_back(make_pair(x, y));
    }
    return walls;
}

int main() {
    const vector<pair<int ,int>> walls = readWallFile("maze.txt");
    Player player;
    Board board(100, 100);
    Artist artist(board);
    for (const auto& w : walls) {
        artist.drawWall(w.first, w.second);
    }
    PlayerController pc(player, board);

    string user_input= "";
    while (user_input != "q") {
        cin >> user_input;
        if (user_input == "l") pc.Move(Direction::Left());
        else if (user_input == "r") pc.Move(Direction::Right());
        else if (user_input == "u") pc.Move(Direction::Up());
        else if (user_input == "d") pc.Move(Direction::Down());
        else cout << "Unknown key, try again" << endl;

        cout << artist.display(player) << endl;
    }
    return 0;
}