#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <assert.h>
#include <stdbool.h>

#include "hw3.h"

#define DEBUG(...)                              \
    fprintf(stderr, "[          ] [ DEBUG ] "); \
    fprintf(stderr, __VA_ARGS__);               \
    fprintf(stderr, " -- %s()\n", __func__)

char *uppercase(char *temp, int length);

void saveCurrentState(GameState *game, int row, int col, char direction, const char *tiles, int resizeDist);

bool checkDictionary(char *word, int wordLength);

bool validateInput(GameState *game, int row, int col, char direction, const char *tiles, int *num_tiles_placed);

bool checkWordLegality(GameState *game, int row, int col, char direction, const char *tiles, int *num_tiles_placed);

GameState *resizeBoard(GameState *game, int horizontal, int vertical);

GameState *initialize_game_state(const char *filename)
{
    FILE *game = fopen(filename, "r");

    GameState *newState = NULL;

    char currentValue = 0;
    int firstIndex = 0;
    int secondIndex = 0;
    int thirdIndex = 0;

    int numRows = 0;
    int numCols = 0;

    while (1)
    {
        while (currentValue != '\n')
        {
            fscanf(game, "%c", &currentValue);

            if (currentValue != '\n')
            {
                numCols++;
            }
        }

        numRows++;

        fseek(game, ftell(game), SEEK_SET);

        fscanf(game, "%c", &currentValue);
        if (currentValue == '\n')
        {
            break;
        }

        fseek(game, -1, SEEK_CUR);
    }
    numCols = numCols / numRows;

    int memory = numRows * numCols * 5 + numRows * numCols * 5 * 4;
    newState = malloc(memory);

    (*newState).board = malloc(sizeof(char **) * numRows);
    (*newState).noOfTiles = malloc(sizeof(int *) * numRows);
    for (int i = 0; i < numRows; i++)
    {
        (*newState).board[i] = malloc(sizeof(char *) * numCols);
        (*newState).noOfTiles[i] = malloc(sizeof(int) * numCols);

        for (int j = 0; j < numCols; j++)
        {
            (*newState).board[i][j] = calloc(5, sizeof(char) * 5);
        }
    }

    (*newState).rows = numRows;
    (*newState).cols = numCols;

    fseek(game, 0, SEEK_SET);
    currentValue = 0;
    newState->isBoardEmpty = 1;

    while (1)
    {
        if (currentValue == EOF)
        {
            break;
        }

        secondIndex = 0;
        while (currentValue != '\n')
        {
            fscanf(game, "%c", &currentValue);
            if (currentValue != '\n')
            {
                (*newState).board[firstIndex][secondIndex][thirdIndex] = currentValue;

                if (currentValue == '.')
                {
                    (*newState).noOfTiles[firstIndex][secondIndex] = 0;
                }
                else
                {
                    newState->isBoardEmpty = 0;
                    (*newState).noOfTiles[firstIndex][secondIndex] = 1;
                }
            }
            secondIndex++;
        }

        firstIndex++;

        fseek(game, ftell(game), SEEK_SET);

        fscanf(game, "%c", &currentValue);
        if (currentValue == '\n')
        {
            break;
        }

        fseek(game, -1, SEEK_CUR);
    }

    fclose(game);

    newState->allStatesIndex = -1;

    return newState;
}

GameState *place_tiles(GameState *game, int row, int col, char direction, const char *tiles, int *num_tiles_placed)
{
    bool check = false;
    int countTiles = 0;
    check = validateInput(game, row, col, direction, tiles, num_tiles_placed);

    if (check)
    {
        *num_tiles_placed = countTiles;
        return game;
    }

    const char *temp = tiles;
    int startRow = row;
    int startCol = col;
    int heightIndex = 0;
    int resizeDist = 0;

    if (direction == 'H')
    {
        while (*temp)
        {
            if ((startCol + 1) > game->cols)
            {
                int extra = strlen(tiles) - (startCol - col);
                resizeDist = extra;
                game = resizeBoard(game, extra, 0);
            }

            if (*temp != ' ')
            {
                heightIndex = 0;
                if (game->board[startRow][startCol][heightIndex] != '.')
                {
                    while (isalpha(game->board[startRow][startCol][heightIndex]))
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
        while (*temp)
        {
            if ((startRow + 1) > game->rows)
            {
                int extra = strlen(tiles) - (startRow - row);
                resizeDist = extra;
                game = resizeBoard(game, 0, extra);
            }

            if (*temp != ' ')
            {
                heightIndex = 0;
                if (game->board[startRow][startCol][heightIndex] != '.')
                {
                    while (isalpha(game->board[startRow][startCol][heightIndex]))
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

    if (countTiles > 0 && game->isBoardEmpty == 1)
    {
        game->isBoardEmpty = 0;
    }

    saveCurrentState(game, row, col, direction, tiles, resizeDist);

    *num_tiles_placed = countTiles;
    return game;
}

GameState *undo_place_tiles(GameState *game)
{
    if (game->allStatesIndex == -1)
    {
        return game;
    }

    const char *tiles = game->allStates[game->allStatesIndex - 1].text;
    int startRow = game->allStates[game->allStatesIndex - 1].startRow;
    int startCol = game->allStates[game->allStatesIndex - 1].startCol;
    int resize = game->allStates[game->allStatesIndex - 1].resizeDist;
    char direction = game->allStates[game->allStatesIndex - 1].resizeDirection;

    int heightIndex = 0;
    int length = strlen(tiles);

    if (direction == 'H')
    {
        for (int i = 0; i < length; i++)
        {
            if ((game->noOfTiles[startRow][startCol] - 1) > 0)
            {
                heightIndex = game->noOfTiles[startRow][startCol] - 1;
            }
            else
            {
                heightIndex = 0;
            }

            if (heightIndex == 0)
            {
                if (*tiles != ' ')
                {
                    game->board[startRow][startCol][heightIndex] = '.';
                    game->noOfTiles[startRow][startCol] = 0;
                }
            }
            else
            {
                if (*tiles != ' ')
                {
                    game->noOfTiles[startRow][startCol]--;
                }
            }
            tiles++;
            startCol++;
        }
    }
    else
    {
        for (int i = 0; i < length; i++)
        {
            if ((game->noOfTiles[startRow][startCol] - 1) > 0)
            {
                heightIndex = game->noOfTiles[startRow][startCol] - 1;
            }
            else
            {
                heightIndex = 0;
            }

            if (heightIndex == 0)
            {
                if (*tiles != ' ')
                {
                    game->board[startRow][startCol][heightIndex] = '.';
                    game->noOfTiles[startRow][startCol] = 0;
                }
            }
            else
            {
                if (*tiles != ' ')
                {
                    game->noOfTiles[startRow][startCol]--;
                }
            }
            tiles++;
            startRow++;
        }
    }

    game->allStates = realloc(game->allStates, (game->allStatesIndex - 1) * sizeof(States));
    game->allStatesIndex--;

    if (resize > 0)
    {
        if (direction == 'H')
        {
            for (int i = 0; i < game->rows; i++)
            {
                for (int j = game->cols - resize; j < game->cols; j++)
                {
                    free(game->board[i][j]);
                }

                game->board[i] = realloc(game->board[i], (game->cols - resize) * sizeof(char *));
                game->noOfTiles[i] = realloc(game->noOfTiles[i], (game->cols - resize) * sizeof(int *));
            }

            int memory = game->rows * (game->cols - resize) * 5 + game->rows * (game->cols - resize) * 5 * 4;
            game = realloc(game, memory);

            game->cols = game->cols - resize;
        }
        else
        {
            for (int i = game->rows - resize; i < (game->rows); i++)
            {
                for (int j = 0; j < game->cols; j++)
                {
                    free(game->board[i][j]);
                }

                free(game->board[i]);
                free(game->noOfTiles[i]);
            }

            int memory = (game->rows - resize) * (game->cols) * 5 + (game->rows - resize) * (game->cols) * 5 * 4;
            game = realloc(game, memory);

            game->board = realloc(game->board, (game->rows - resize) * sizeof(char **));
            game->noOfTiles = realloc(game->noOfTiles, (game->rows - resize) * sizeof(int *));

            game->rows = game->rows - resize;
        }
    }

    if (game->allStatesIndex == 0)
    {
        game->isBoardEmpty = 1;
    }

    return game;
}

void free_game_state(GameState *game)
{
    for (int i = 0; i < game->rows; i++)
    {
        for (int j = 0; j < game->cols; j++)
        {
            free(game->board[i][j]);
        }

        free(game->board[i]);
        free(game->noOfTiles[i]);
    }

    free(game->board);
    free(game->noOfTiles);

    if (game->allStatesIndex != -1)
    {
        free(game->allStates);
    }

    // for(int i = 0; i < 235885; i++)
    // {
    //     free(game->dictionary[i]);
    // }

    // free(game->dictionary);
    free(game);
}

void save_game_state(GameState *game, const char *filename)
{
    FILE *save = fopen(filename, "w");
    int heightIndex = 0;

    for (int i = 0; i < game->rows; i++)
    {
        for (int j = 0; j < game->cols; j++)
        {
            if ((game->noOfTiles[i][j] - 1) >= 0)
            {
                heightIndex = game->noOfTiles[i][j] - 1;
            }
            else
            {
                heightIndex = 0;
            }
            fprintf(save, "%c", game->board[i][j][heightIndex]);
        }

        fprintf(save, "\n");
    }

    for (int i = 0; i < game->rows; i++)
    {
        for (int j = 0; j < game->cols; j++)
        {
            fprintf(save, "%d", game->noOfTiles[i][j]);
        }

        fprintf(save, "\n");
    }

    fclose(save);
}

bool validateInput(GameState *game, int row, int col, char direction, const char *tiles, int *num_tiles_placed)
{
    if (row < 0 || col < 0)
    {
        return true;
    }

    if (row >= game->rows || col >= game->cols)
    {
        return true;
    }

    if (!(direction == 'H' || direction == 'V'))
    {
        return true;
    }

    if ((game->isBoardEmpty == 1) && (strlen(tiles) < 3))
    {
        return true;
    }

    int countTiles = 0;
    const char *temp = tiles;
    int startRow = row;
    int startCol = col;
    int heightIndex = 0;
    bool isWordAlone = true;
    int extra = 0;

    if (direction == 'H')
    {
        while (*temp)
        {
            /* Check if board size is not violated */
            if ((startCol + 1) > game->cols)
            {
                extra = strlen(tiles) - (startCol - col);
                break;
            }

            /* Checks if the current pos is a letter or an empty space */
            if (*temp != ' ')
            {
                heightIndex = 0;

                /* Checks if the height of the stack is not violated and also if two same letters are stacked */
                if (game->board[startRow][startCol][heightIndex] != '.')
                {
                    while (isalpha(game->board[startRow][startCol][heightIndex]))
                    {
                        heightIndex++;
                    }

                    if (heightIndex > 4)
                    {
                        return true;
                    }

                    if (game->board[startRow][startCol][heightIndex - 1] == *temp)
                    {
                        return true;
                    }
                }

                /* Checks if the word placed on the board (non-empty) is isolated or not */
                if (isWordAlone == true && game->isBoardEmpty == 0)
                {
                    /* Checks left for first letter */
                    if (startCol == col)
                    {
                        if ((startCol - 1) >= 0)
                        {
                            if (game->board[startRow][startCol - 1][0] != '.')
                            {
                                isWordAlone = false;
                            }
                        }

                        /* Case for a single letter */
                        if (strlen(tiles) == 1)
                        {
                            if ((startCol + 1) < game->cols)
                            {
                                if (game->board[startRow][startCol + 1][0] != '.')
                                {
                                    isWordAlone = false;
                                }
                            }
                        }
                    }

                    /* Checks right for last letter */
                    if ((startCol + 1) == (col + (int)strlen(tiles)))
                    {
                        if ((startCol + 1) < game->cols)
                        {
                            if (game->board[startRow][startCol + 1][0] != '.')
                            {
                                isWordAlone = false;
                            }
                        }
                    }

                    /* Checks bottom for the letter */
                    if ((startRow - 1) >= 0)
                    {
                        if (game->board[startRow - 1][startCol][0] != '.')
                        {
                            isWordAlone = false;
                        }
                    }

                    /* Checks top for the letter */
                    if ((startRow + 1) < game->rows)
                    {
                        if (game->board[startRow + 1][startCol][0] != '.')
                        {
                            isWordAlone = false;
                        }
                    }
                }

                countTiles++;
            }
            else
            {
                isWordAlone = false;                           // If empty space then word is not isolated
                if (game->board[startRow][startCol][0] == '.') // Checks if emptys space actually has a letter in the board
                {
                    return true;
                }
            }

            startCol++;
            temp++;
        }
    }
    else
    {
        while (*temp)
        {
            /* Check if board size is not violated */
            if ((startRow + 1) > game->rows)
            {
                extra = strlen(tiles) - (startRow - row);
                break;
            }

            /* Checks if the current pos is a letter or an empty space */
            if (*temp != ' ')
            {
                heightIndex = 0;

                /* Checks if the height of the stack is not violated and also if two same letters are stacked */
                if (game->board[startRow][startCol][heightIndex] != '.')
                {
                    while (isalpha(game->board[startRow][startCol][heightIndex]))
                    {
                        heightIndex++;
                    }

                    if (heightIndex > 4)
                    {
                        return true;
                    }

                    if (game->board[startRow][startCol][heightIndex - 1] == *temp)
                    {
                        return true;
                    }
                }

                /* Checks if the word placed on the board (non-empty) is isolated or not */
                if (isWordAlone == true && game->isBoardEmpty == 0)
                {
                    /* Checks left for first letter */
                    if (startRow == row)
                    {
                        if ((startRow - 1) >= 0)
                        {
                            if (game->board[startRow - 1][startCol][0] != '.')
                            {
                                isWordAlone = false;
                            }
                        }

                        /* Case for a single letter */
                        if (strlen(tiles) == 1)
                        {
                            if ((startRow + 1) < game->cols)
                            {
                                if (game->board[startRow + 1][startCol][0] != '.')
                                {
                                    isWordAlone = false;
                                }
                            }
                        }
                    }

                    /* Checks right for last letter */
                    if ((startRow + 1) == (row + (int)strlen(tiles)))
                    {
                        if ((startRow + 1) < game->rows)
                        {
                            if (game->board[startRow + 1][startCol][0] != '.')
                            {
                                isWordAlone = false;
                            }
                        }
                    }

                    /* Checks bottom for the letter */
                    if ((startCol - 1) >= 0)
                    {
                        if (game->board[startRow][startCol - 1][0] != '.')
                        {
                            isWordAlone = false;
                        }
                    }

                    /* Checks top for the letter */
                    if ((startCol + 1) < game->cols)
                    {
                        if (game->board[startRow][startCol + 1][0] != '.')
                        {
                            isWordAlone = false;
                        }
                    }
                }

                countTiles++;
            }
            else
            {
                isWordAlone = false;                           // If empty space then word is not isolated
                if (game->board[startRow][startCol][0] == '.') // Checks if emptys space actually has a letter in the board
                {
                    return true;
                }
            }

            startRow++;
            temp++;
        }
    }

    /* Finally checks if the the word placed is not surronded when the board is not empty */
    if (game->isBoardEmpty == 0 && isWordAlone == true)
    {
        return true;
    }

    /* Check if the words formed and the one added are legal */

    if (!checkWordLegality(game, row, col, direction, tiles, num_tiles_placed))
    {
        return true;
    }

    (void)num_tiles_placed;
    (void)extra;
    return false;
}

GameState *resizeBoard(GameState *game, int horizontal, int vertical)
{
    if (horizontal != 0)
    {
        int memory = game->rows * (game->cols + horizontal) * 5 + game->rows * (game->cols + horizontal) * 5 * 4;
        game = realloc(game, memory);

        for (int i = 0; i < game->rows; i++)
        {
            game->board[i] = realloc(game->board[i], (game->cols + horizontal) * sizeof(char *));
            game->noOfTiles[i] = realloc(game->noOfTiles[i], (game->cols + horizontal) * sizeof(int *));

            for (int j = game->cols; j < game->cols + horizontal; j++)
            {
                game->board[i][j] = calloc(5, sizeof(char) * 5);
            }
        }

        for (int i = 0; i < game->rows; i++)
        {
            for (int j = game->cols; j < (game->cols + horizontal); j++)
            {
                game->board[i][j][0] = '.';
                game->noOfTiles[i][j] = 0;
            }
        }

        game->cols = game->cols + horizontal;

        return game;
    }

    if (vertical != 0)
    {
        int memory = (game->rows + vertical) * (game->cols) * 5 + (game->rows + vertical) * (game->cols) * 5 * 4;
        game = realloc(game, memory);

        game->board = realloc(game->board, (game->rows + vertical) * sizeof(char **));
        game->noOfTiles = realloc(game->noOfTiles, (game->rows + vertical) * sizeof(int *));
        for (int i = game->rows; i < (game->rows + vertical); i++)
        {
            game->board[i] = malloc((game->cols) * sizeof(char *));
            game->noOfTiles[i] = malloc((game->cols) * sizeof(int *));

            for (int j = 0; j < game->cols; j++)
            {
                game->board[i][j] = calloc(5, sizeof(char) * 5);
            }
        }

        for (int i = game->rows; i < game->rows + vertical; i++)
        {
            for (int j = 0; j < (game->cols); j++)
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

bool checkDictionary(char *word, int wordLength)
{
    if (word == NULL)
    {
        return true;
    }

    FILE *words = fopen("./tests/words.txt", "r");
    const int SIZE = 235885;
    char currentWord[100] = {0};

    for (int i = 0; i < SIZE; i++)
    {
        fscanf(words, "%s", currentWord);

        if (strncmp(uppercase(word, wordLength), uppercase(currentWord, wordLength), wordLength) == 0)
        {
            fclose(words);
            return true;
        }
    }

    fclose(words);

    return false;
}

void saveCurrentState(GameState *game, int row, int col, char direction, const char *tiles, int resizeDist)
{
    if (game->allStatesIndex == -1)
    {
        game->allStatesIndex++;
        game->allStates = malloc(1 * sizeof(States));
    }
    else
    {
        game->allStates = realloc(game->allStates, (game->allStatesIndex + 1) * sizeof(States));
    }

    game->allStates[game->allStatesIndex].startRow = row;
    game->allStates[game->allStatesIndex].startCol = col;
    game->allStates[game->allStatesIndex].resizeDist = resizeDist;
    game->allStates[game->allStatesIndex].text = tiles;
    game->allStates[game->allStatesIndex].resizeDirection = direction;

    game->allStatesIndex++;
}

bool checkWordLegality(GameState *game, int row, int col, char direction, const char *tiles, int *num_tiles_placed)
{
    const char *temp = tiles;
    int startRow = row;
    int startCol = col;
    char wordAdded[100] = {0};
    int index = 0;
    int heightIndex = 0;

    if (direction == 'H')
    {
        /* If word added is an extentions of another word, create new word */
        if (startCol == col && startCol > 0)
        {
            int tempStartCol = startCol - 1;

            if ((game->noOfTiles[startRow][tempStartCol] - 1) >= 0)
            {
                heightIndex = game->noOfTiles[startRow][tempStartCol] - 1;
            }
            else
            {
                heightIndex = 0;
            }

            while (game->board[startRow][tempStartCol][heightIndex] != '.')
            {
                if ((game->noOfTiles[startRow][tempStartCol] - 1) >= 0)
                {
                    heightIndex = game->noOfTiles[startRow][tempStartCol] - 1;
                }
                else
                {
                    heightIndex = 0;
                }

                tempStartCol--;
                if (tempStartCol < 0)
                {
                    break;
                }
            }
            tempStartCol++;

            while (tempStartCol != startCol)
            {
                if ((game->noOfTiles[startRow][tempStartCol] - 1) >= 0)
                {
                    heightIndex = game->noOfTiles[startRow][tempStartCol] - 1;
                }
                else
                {
                    heightIndex = 0;
                }

                wordAdded[index] = game->board[startRow][tempStartCol][heightIndex];
                tempStartCol++;
                index++;
            }
        }

        while (*temp)
        {
            if ((startCol + 1) > game->cols)
            {
                while(*temp)
                {
                    wordAdded[index] = *temp;
                    index++;
                    temp++;
                }
                break;
            }

            if ((game->noOfTiles[startRow][startCol] - 1) >= 0)
            {
                heightIndex = game->noOfTiles[startRow][startCol] - 1;
            }
            else
            {
                heightIndex = 0;
            }

            if (*temp != ' ')
            {
                wordAdded[index] = *temp;
            }
            else
            {
                wordAdded[index] = game->board[startRow][startCol][heightIndex];
            }

            index++;
            startCol++;
            temp++;
        }
    }
    else
    {
        while (*temp)
        {
            if (startRow == row && startRow > 0)
            {
                int tempStartRow = startRow - 1;

                if ((game->noOfTiles[tempStartRow][startCol] - 1) >= 0)
                {
                    heightIndex = game->noOfTiles[tempStartRow][startCol] - 1;
                }
                else
                {
                    heightIndex = 0;
                }

                while (game->board[tempStartRow][startCol][heightIndex] != '.')
                {
                    if ((game->noOfTiles[tempStartRow][startCol] - 1) >= 0)
                    {
                        heightIndex = game->noOfTiles[tempStartRow][startCol] - 1;
                    }
                    else
                    {
                        heightIndex = 0;
                    }

                    tempStartRow--;
                    if (tempStartRow < 0)
                    {
                        break;
                    }
                }
                tempStartRow++;

                while (tempStartRow != startRow)
                {
                    if ((game->noOfTiles[tempStartRow][startCol] - 1) >= 0)
                    {
                        heightIndex = game->noOfTiles[tempStartRow][startCol] - 1;
                    }
                    else
                    {
                        heightIndex = 0;
                    }

                    wordAdded[index] = game->board[tempStartRow][startCol][heightIndex];
                    tempStartRow++;
                    index++;
                }
            }

            if (*temp != ' ')
            {
                wordAdded[index] = *temp;
            }
            else
            {
                if ((startRow + 1) > game->rows)
                {
                    break;
                }

                if ((game->noOfTiles[startRow][startCol] - 1) >= 0)
                {
                    heightIndex = game->noOfTiles[startRow][startCol] - 1;
                }
                else
                {
                    heightIndex = 0;
                }

                wordAdded[index] = game->board[startRow][startCol][heightIndex];
            }

            index++;
            startRow++;
            temp++;
        }
    }
    wordAdded[index] = '\0';

    (void)num_tiles_placed;
    // (void)wordAdded;
    // return false;
    return checkDictionary(wordAdded, strlen(tiles));
}

char *uppercase(char *temp, int length)
{
    char *s = temp;

    for (int i = 0; i < length; i++)
    {
        *s = toupper((unsigned char)*s);
        s++;
    }

    return temp;
}
