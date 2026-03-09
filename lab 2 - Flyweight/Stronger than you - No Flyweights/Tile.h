#pragma once
#include <string>
#include <map>
#include "TilesSetting.h"

extern TilesSettings tilesSettings;

class Tile {
private:
    TilesType tilesType;
    std::string color;

    static std::string getColorForType(TilesType tt) {
        switch (tt) {
        case PathTile:      return Colors::WHITE;
        case PlayerTile:    return Colors::CYAN;
        case StartTile:     return Colors::GREEN;
        case EndTile:       return Colors::MAGENTA;
        case WallTile:      return Colors::RED;
        case DarknessTile:  return Colors::BLUE;
        case VoidTile:      return Colors::YELLOW;
        case UnknownTile:   return Colors::WHITE;
        default:            return Colors::WHITE;
        }
    }

public:
    Tile(TilesType tt) : tilesType(tt), color(getColorForType(tt)) {
    }
    char getSymbol() const { return tilesSettings[tilesType]; }
    std::string getColor() const { return color; }
    std::string getPaintedSymbol() const { return color + tilesSettings[tilesType] + Colors::RESET; }
};
