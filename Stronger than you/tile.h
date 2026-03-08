#include <map>
using namespace std;

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