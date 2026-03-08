#pragma once
#include <vector>
#include "Tile.h"

class Board {
private:
    int rows, cols;
    std::vector<std::vector<Tile*>> grid;
public:
    Board(int m, int n) : rows(m), cols(n), grid(m, std::vector<Tile*>(n, TileFactory::getTile(TilesType::PathTile))) {}

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
