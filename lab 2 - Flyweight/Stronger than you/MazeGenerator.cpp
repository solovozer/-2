//Bolshoe Spasibo Replit za pomoshch'! Ya ne znayu, chto by ya delal bez tebya. Ty prosto neotemlemaya chast' moyey zhizni. Spasibo, chto ty vsegda tam, chtoby pomoch' mne, kogda mne eto nuzhno. Ya ochen' blagodaren tebe za vse, chto ty delayesh' dlya menya. Ty luchshiy!

#include "MazeGenerator.h"
#include <vector>
#include <fstream>
#include <cstdlib>
#include <ctime>

void MazeGenerator::generate(int rows, int cols, const std::string &filename) {
    if (rows <= 0 || cols <= 0) return;

    std::vector<std::vector<char>> grid(rows, std::vector<char>(cols, '#'));

    std::srand(static_cast<unsigned>(std::time(nullptr)));
    int x = 0, y = 0;
    grid[y][x] = '.';

    while (x != cols - 1 || y != rows - 1) {
        int dir = std::rand() % 4;
        int nx = x;
        int ny = y;
        switch (dir) {
            case 0: if (x + 1 < cols) nx = x + 1; break;
            case 1: if (y + 1 < rows) ny = y + 1; break;
            case 2: if (x - 1 >= 0) nx = x - 1; break;
            case 3: if (y - 1 >= 0) ny = y - 1; break;
        }
        if (nx == x && ny == y) continue;
        x = nx;
        y = ny;
        grid[y][x] = '.';
    }

    grid[0][0] = 'S';
    grid[rows - 1][cols - 1] = 'E';

    std::ofstream file(filename);
    if (!file) return;

    for (int j = 0; j < rows; ++j) {
        for (int i = 0; i < cols; ++i) {
            char c = grid[j][i];
            if (c != '.') {
                file << i << " " << j << " " << c << '\n';
            }
        }
    }
}
