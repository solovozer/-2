#pragma once
#include <fstream>
#include <string>

using namespace std;

struct TileInfo {
    int x;
    int y;
    TilesType type;
};

const static vector<TileInfo> readMazeFile(const string& filename) {
    vector<TileInfo> tiles;
    ifstream file(filename);
    if (file) {
        int x, y, sym;
        while (file >> x >> y >> sym) tiles.push_back({ x, y, (TilesType)sym });
    }
    return tiles;
}