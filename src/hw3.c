#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <assert.h>
#include <stdbool.h>

#include "hw3.h" 

#define DEBUG(...) fprintf(stderr, "[          ] [ DEBUG ] "); fprintf(stderr, __VA_ARGS__); fprintf(stderr, " -- %s()\n", __func__)

bool isBoardEmpty = true;

bool validateInput(GameState *game, int row, int col, char direction, const char *tiles, int *num_tiles_placed);

GameState* resizeBoard(GameState *game, int horizontal, int vertical);

GameState* initialize_game_state(const char *filename) {
    FILE *game = fopen(filename, "r");

    GameState *newState = NULL;

    char currentValue = 0;
    int firstIndex = 0;
    int secondIndex = 0;
    int thirdIndex = 0;


    int numRows = 0;
    int numCols = 0;

    while(1)
    {
        while(currentValue != '\n')
        {
            fscanf(game, "%c", &currentValue);

            if(currentValue != '.' || currentValue != '\n')
            {
                isBoardEmpty = false;
            }

            if(currentValue != '\n')
            { numCols++; }
        }

        numRows++;

        fseek(game, ftell(game), SEEK_SET);

        fscanf(game, "%c", &currentValue);
        if(currentValue == '\n')
        {
            break;
        }

        fseek(game, -1, SEEK_CUR);
    }
    numCols = numCols/numRows;

    int memory = numRows*numCols*5 + numRows*numCols*5*4;
    newState = malloc(memory);


    (*newState).board = malloc(sizeof(char **) * numRows);
    (*newState).noOfTiles = malloc(sizeof(int *) * numRows);
    for(int i = 0; i < numRows; i++)
    {
        (*newState).board[i] = malloc(sizeof(char *) * numCols);
        (*newState).noOfTiles[i] = malloc(sizeof(int) * numCols);

        for(int j = 0; j < numCols; j++)
        {
            (*newState).board[i][j] = calloc(5, sizeof(char) * 5);
        }
    }

    (*newState).rows = numRows;
    (*newState).cols = numCols;

    fseek(game, 0, SEEK_SET);
    currentValue = 0;

    while(1)
    {
        if(currentValue == EOF)
        {
            break;
        }

        secondIndex = 0;
        while(currentValue != '\n')
        {
            fscanf(game, "%c", &currentValue);
            if(currentValue != '\n')
            { 
                (*newState).board[firstIndex][secondIndex][thirdIndex] = currentValue;

                if(currentValue == '.')
                {
                    (*newState).noOfTiles[firstIndex][secondIndex] = 0;
                } 
                else
                {
                    (*newState).noOfTiles[firstIndex][secondIndex] = 1;
                }
            }
            secondIndex++;
        }

        firstIndex++;

        fseek(game, ftell(game), SEEK_SET);

        fscanf(game, "%c", &currentValue);
        if(currentValue == '\n')
        {
            break;
        }

        fseek(game, -1, SEEK_CUR);
    }

    fclose(game);

    return newState;
}

GameState* place_tiles(GameState *game, int row, int col, char direction, const char *tiles, int *num_tiles_placed) {
    bool check = false;
    check = validateInput(game, row, col, direction, tiles, num_tiles_placed);

    if(check)
    {
        return game;
    }

    int countTiles = 0;
    const char *temp = tiles;
    int startRow = row;
    int startCol = col;
    int heightIndex = 0;

    if(direction == 'H')
    {
        while(*temp)
        {
            if((startCol + 1) > game->cols)
            {
                int extra = strlen(tiles) - (startCol - col);
                game = resizeBoard(game, extra, 0);
            }

            if(*temp != ' ')
            {
                heightIndex = 0;
                if(game->board[startRow][startCol][heightIndex] != '.')
                {
                    while(isalpha(game->board[startRow][startCol][heightIndex]))
                    {
                        heightIndex++;
                    }
                }

                game->board[startRow][startCol][heightIndex] = *temp;
                game->noOfTiles[startRow][startCol] = heightIndex + 1;
                countTiles++;
            }
            
            startCol++;
            temp++;
        }
    }
    else
    {
        while(*temp)
        {
            if((startRow + 1) > game->rows)
            {
                int extra = strlen(tiles) - (startRow - row);
                game = resizeBoard(game, 0, extra);
            }

            if(*temp != ' ')
            {
                heightIndex = 0;
                if(game->board[startRow][startCol][heightIndex] != '.')
                {
                    while(isalpha(game->board[startRow][startCol][heightIndex]))
                    {
                        heightIndex++;
                    }
                }

                game->board[startRow][startCol][heightIndex] = *temp;
                game->noOfTiles[startRow][startCol] = heightIndex + 1;
                countTiles++;
            }
            
            startRow++;
            temp++;
        }
    }

    *num_tiles_placed = countTiles;
    return game;
}

GameState* undo_place_tiles(GameState *game) {
    (void)game;
    return game;
}

void free_game_state(GameState *game) {
    for(int i = 0; i < game->rows; i++)
    {
        for(int j = 0; j < game->cols; j++)
        {
            free(game->board[i][j]);
        }

        free(game->board[i]);
        free(game->noOfTiles[i]);
    }
    free(game->board);
    free(game->noOfTiles);   
    free(game); 
}

void save_game_state(GameState *game, const char *filename) {
    FILE *save = fopen(filename, "w");
    int heightIndex = 0;

    for(int i = 0; i < game->rows; i++)
    {
        for(int j = 0; j < game->cols; j++)
        {
            heightIndex = 0;
            if(game->board[i][j][heightIndex] != '.')
            {
                while(isalpha(game->board[i][j][heightIndex]))
                {
                    heightIndex++;
                }
                heightIndex--;
            }
            
            fprintf(save, "%c", game->board[i][j][heightIndex]);
        }

        fprintf(save, "\n");
    }

    for(int i = 0; i < game->rows; i++)
    {
        for(int j = 0; j < game->cols; j++)
        {
            fprintf(save, "%d", game->noOfTiles[i][j]);
        }

        fprintf(save, "\n");
    }

    fclose(save);
}

bool validateInput(GameState *game, int row, int col, char direction, const char *tiles, int *num_tiles_placed)
{
    if(row < 0 || col < 0)
    {
        return true;
    }

    if(row >= game->rows || col >= game->cols)
    {
        return true;
    }

    if(!(direction == 'H' || direction == 'V'))
    {
        return true;
    }

    // if(!isBoardEmpty)
    // {
    //     int length = strlen(tiles);
    //     int count = 0;

    //     for(int i = 0; i < length; i++)
    //     {
    //         if(game->board[row][col][0] == '.')
    //         {
    //             count++;
    //         }
    //     }

    //     if(count == length || count == 0)
    //     {
    //         return true;
    //     }
    // }

    (void)tiles;
    (void)num_tiles_placed;
    return false;
}

GameState* resizeBoard(GameState *game, int horizontal, int vertical)
{
    if(horizontal != 0)
    {
        int memory = game->rows*(game->cols + horizontal)*5 + game->rows*(game->cols + horizontal)*5*4;
        game = realloc(game, memory);

        for(int i = 0; i < game->rows; i++)
        {
            game->board[i] = realloc(game->board[i], (game->cols + horizontal) * sizeof(char *));
            game->noOfTiles[i] = realloc(game->noOfTiles[i], (game->cols + horizontal) * sizeof(int *));

            for(int j = game->cols; j < game->cols + horizontal; j++)
            {
                game->board[i][j] = calloc(5, sizeof(char) * 5);
            }
        }

        for(int i = 0; i < game->rows; i++)
        {
            for(int j = game->cols; j < (game->cols + horizontal); j++)
            {
                game->board[i][j][0] = '.';
                game->noOfTiles[i][j] = 0;
            }
        }

        game->cols = game->cols + horizontal;

        return game;
    }

    if(vertical != 0)
    {
        int memory = (game->rows + vertical)*(game->cols)*5 + (game->rows + vertical)*(game->cols)*5*4;
        game = realloc(game, memory);

        game->board = realloc(game->board, (game->rows + vertical) * sizeof(char **));
        game->noOfTiles = realloc(game->noOfTiles, (game->rows + vertical) * sizeof(int *));
        for(int i = game->rows; i < (game->rows + vertical); i++)
        {
            game->board[i] = malloc((game->cols) * sizeof(char *));
            game->noOfTiles[i] = malloc((game->cols) * sizeof(int *));

            for(int j = 0; j < game->cols; j++)
            {
                game->board[i][j] = calloc(5, sizeof(char) * 5);
            }
        }

        for(int i = game->rows; i < game->rows + vertical; i++)
        {
            for(int j = 0; j < (game->cols); j++)
            {
                game->board[i][j][0] = '.';
                game->noOfTiles[i][j] = 0;
            }
        }

        game->rows = game->rows + vertical;

        return game;
    }

    return game;
}
