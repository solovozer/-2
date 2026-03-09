#pragma once
#include <sstream>
#include <iomanip>
#include <vector>
#include "Board.h"
#include "Player.h"
#include "MazeHandler.h"

class Artist {
private:
    Board& board;
    Player& player;
public:
    Artist(Board& b, Player& p) : board(b), player(p) {}

    void drawFromData(std::vector<TileInfo> tiles) {
        for (const auto& d : tiles) {
            switch (d.type) {
            case TilesType::WallTile:
                board.setTile(d.x, d.y, new Tile(WallTile)); 
                break;
            case TilesType::StartTile:
                player.setPosition(d.x, d.y);
                board.setTile(d.x, d.y, new Tile(StartTile));
                break;
            case TilesType::EndTile:
                board.setTile(d.x, d.y, new Tile(EndTile));
                break;
            default:
                board.setTile(d.x, d.y, new Tile(UnknownTile));
                break;
            }
        }
    }

    std::string display()
    {
        std::stringstream buffer;
        for (int y = 0; y < board.getRows(); y++)
        {
            for (int x = 0; x < board.getCols(); x++)
            {
                buffer << std::setw(1);
                if (x == player.getX() && y == player.getY()) {
                    buffer << board.getTile(x, y)->getSymbol();
                } else {
                    Tile* t = board.getTile(x, y);
                    buffer << t->getColor() << t->getSymbol() << Colors::RESET;
                }
                buffer << std::endl;

            }
            buffer << '\n';
        }
        return buffer.str();
    }

    std::string displayVicinity(int radius) {
        std::stringstream buffer;
        int viewSize = (radius * 2) + 1;
        int startX = player.getX() - radius;
        int startY = player.getY() - radius;

        if (startX < 0) startX = 0;
        if (startY < 0) startY = 0;
        if (startX + viewSize > board.getCols()) startX = board.getCols()  - viewSize;
        if (startY + viewSize > board.getRows()) startY = board.getRows() - viewSize;

        for (int y = startY; y < startY + viewSize; y++) {
            for (int x = startX; x < startX + viewSize; x++) {
                if (x == player.getX() && y == player.getY()) buffer << Colors::WHITE << tilesSettings[TilesType::PlayerTile] << Colors::RESET;
                else if (x >= 0 && x < board.getCols() && y >= 0 && y < board.getRows()) {
                    Tile* t = board.getTile(x, y);
                    buffer << t->getColor() << t->getSymbol() << Colors::RESET;
                }
            }
            buffer << std::endl;
        }

        return buffer.str();
    }
};
