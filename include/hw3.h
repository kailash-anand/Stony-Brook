#include <stdlib.h>

typedef struct States
{
    int startRow;
    int startCol;
    int resizeDist;
    char resizeDirection;
    const char *text;
} States;

typedef struct GameState
{
    States *allStates;
    int allStatesIndex;
    char **dictionary;
    int isBoardEmpty;
    int rows, cols;
    int **noOfTiles;
    char ***board;
} GameState;

GameState* initialize_game_state(const char *filename);
GameState* place_tiles(GameState *game, int row, int col, char direction, const char *tiles, int *num_tiles_placed);
GameState* undo_place_tiles(GameState *game);
void free_game_state(GameState *game);
void save_game_state(GameState *game, const char *filename);
