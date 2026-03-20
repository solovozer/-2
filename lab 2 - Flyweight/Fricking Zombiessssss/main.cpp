#define FLYWEIGHT 1


#if FLYWEIGHT
#include "fw.h"
#else
#include "nofw.h"
#endif



int main() {
    Game myGame;
    myGame.Run();
    return 0;
}