#pragma once
#include "Player.h"
#include "Board.h"
#include "Direction.h"

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
        if (x >= 0 && x < board.getCols() && board.getTile(x, currentY)->getSymbol() != tilesSettings[TilesType::WallTile]) currentX = x;
        if (y >= 0 && y < board.getRows() && board.getTile(currentX, y)->getSymbol() != tilesSettings[TilesType::WallTile]) currentY = y;
        player.setPosition(currentX, currentY);
    }
};