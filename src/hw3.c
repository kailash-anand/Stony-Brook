#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <assert.h>

#include "hw3.h" 

#define DEBUG(...) fprintf(stderr, "[          ] [ DEBUG ] "); fprintf(stderr, __VA_ARGS__); fprintf(stderr, " -- %s()\n", __func__)

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
            (*newState).board[i][j] = malloc(sizeof(char) * 5);
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
    (void)game;
    (void)row;
    (void)col;
    (void)direction;
    (void)tiles;
    (void)num_tiles_placed;
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

    for(int i = 0; i < game->rows; i++)
    {
        for(int j = 0; j < game->cols; j++)
        {
            fprintf(save, "%c ", game->board[i][j][0]);
        }

        fprintf(save, "\n");
    }

    for(int i = 0; i < game->rows; i++)
    {
        for(int j = 0; j < game->cols; j++)
        {
            fprintf(save, "%d ", game->noOfTiles[i][j]);
        }

        fprintf(save, "\n");
    }

    fclose(save);
}
