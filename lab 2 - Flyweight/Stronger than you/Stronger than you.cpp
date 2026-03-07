#include <iostream>
#include <fstream>
#include <vector>
#include <map>
#include <string>
#include <math.h>

using namespace std;

static struct Direction {
private:
    Direction(int a, int b) {
        dx = a < 0 ? -1 : a > 0;
        dy = b < 0 ? -1 : b > 0;
    }
public:
    int dx, dy;
    static Direction Left() { return Direction(-1, 0); }
    static Direction Right() { return Direction(1, 0); }
    static Direction Up() { return Direction(0, -1); }
    static Direction Down() { return Direction(0, 1); }
};

class Player {
    int x;
    int y;

    friend class PlayerController;
    friend class Artist;
public:
    Player() : x(0), y(0) {}
};

class PlayerController {
    Player player;
    Board board;
public:
    PlayerController(Player& p, Board& b) : player(p), board(b) {}
    void Move(Direction dir) const{
        int x = player.x + dir.dx, y = player.y + dir.dy;
        if (x >= 0 && x < board.cols) player.x = x;
        if (y >= 0 && y < board.rows) player.y = y;
    }
};

class Tile {
    char symbol;
public:
    Tile(char s) : symbol(s) {}
    char getSymbol() const { return symbol; }
};

class TileFactory {
    static map<char, Tile*> tiles;
public:
    static Tile* getTile(char symbol) {
        if (!tiles[symbol]) {
            tiles[symbol] = new Tile(symbol);
        }
        return tiles[symbol];
    }
};

map<char, Tile*> TileFactory::tiles;

class Board {
    int rows, cols;
    vector<vector<Tile*>> grid;

    friend class PlayerController;
    friend class Artist;
public:
    Board(int m, int n) : rows(m), cols(n), grid(m, vector<Tile*>(n, TileFactory::getTile('.'))) {}
};

class Artist {
    Board& board;
    
public:
    Artist(Board& b) : board(b) {}
    void drawWall(int x, int y) {
        if (x >= 0 && x < board.cols && y >= 0 && y < board.rows) {
            board.grid[y][x] = TileFactory::getTile('#');
        }
    }
    string display(const Player& player)
    {
        string buffer;

        for (int y = 0; y < board.rows; y++)
        {
            for (int x = 0; x < board.cols; x++)
            {
                if (x == player.x && y == player.y)
                    buffer += '@';
                else
                    buffer += board.grid[y][x]->getSymbol();
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