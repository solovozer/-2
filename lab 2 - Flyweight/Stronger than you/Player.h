#pragma once

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