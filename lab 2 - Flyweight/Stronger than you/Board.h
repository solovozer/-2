 #pragma once
#include <vector>
#include "Tile.h"

class Board {
private:
    int rows, cols;
    std::vector<std::vector<TilesType>> grid;
public:
    Board(int m, int n) : rows(m), cols(n), grid(m, std::vector<TilesType>(n, TilesType::PathTile)) {}

    int getRows() const { return rows; }
    int getCols() const { return cols; }
    Tile* getTile(int x, int y) const {
        if (x >= 0 && x < cols && y >= 0 && y < rows) return TileFactory::getTile(grid[y][x]);
        return nullptr;
    }
    void setTile(int x, int y, TilesType tt) {
        if (x >= 0 && x < cols && y >= 0 && y < rows) grid[y][x] = tt;
    }
};
