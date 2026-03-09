#pragma once

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
