#include "hw4.h"
#include "math.h"

bool isBlocked(int src_row, int src_col, int dest_row, int dest_col, ChessGame *game);

bool hasPiece(int row, int col, ChessGame *game);

bool isOutOfBounds(int row, int col);

int countSquares(int src_row, int src_col, int dest_row, int dest_col);

bool isInvalidLetterIndex(char letter);

bool isValidPromotionPiece(char piece);

int convertMoveToCoordinates(char row);

void initialize_game(ChessGame *game)
{
    const int BOARD_DIMENTIONS = 8; // The board is and 8x8 board.

    game->moveCount = 0;
    game->capturedCount = 0;
    game->currentPlayer = WHITE_PLAYER;

    for (int i = 0; i < BOARD_DIMENTIONS; i++)
    {
        for (int j = 0; j < BOARD_DIMENTIONS; j++)
        {
            switch (i)
            {
            case 0:
                switch (j)
                {
                case 0:
                    game->chessboard[i][j] = 'r';
                    break;
                case 1:
                    game->chessboard[i][j] = 'n';
                    break;
                case 2:
                    game->chessboard[i][j] = 'b';
                    break;
                case 3:
                    game->chessboard[i][j] = 'q';
                    break;
                case 4:
                    game->chessboard[i][j] = 'k';
                    break;
                case 5:
                    game->chessboard[i][j] = 'b';
                    break;
                case 6:
                    game->chessboard[i][j] = 'n';
                    break;
                case 7:
                    game->chessboard[i][j] = 'r';
                    break;
                }
                break;

            case 1:
                game->chessboard[i][j] = 'p';
                break;

            case 6:
                game->chessboard[i][j] = 'P';
                break;

            case 7:
                switch (j)
                {
                case 0:
                    game->chessboard[i][j] = 'R';
                    break;
                case 1:
                    game->chessboard[i][j] = 'N';
                    break;
                case 2:
                    game->chessboard[i][j] = 'B';
                    break;
                case 3:
                    game->chessboard[i][j] = 'Q';
                    break;
                case 4:
                    game->chessboard[i][j] = 'K';
                    break;
                case 5:
                    game->chessboard[i][j] = 'B';
                    break;
                case 6:
                    game->chessboard[i][j] = 'N';
                    break;
                case 7:
                    game->chessboard[i][j] = 'R';
                    break;
                }
                break;

            default:
                game->chessboard[i][j] = '.';
            }
        }
    }
}

void chessboard_to_fen(char fen[], ChessGame *game)
{
    const int BOARD_DIMENTIONS = 8;
    int count = 0;
    char *tempFen = fen;

    for (int i = 0; i < BOARD_DIMENTIONS; i++)
    {
        for (int j = 0; j < BOARD_DIMENTIONS; j++)
        {
            if (hasPiece(i, j, game))
            {
                *tempFen = game->chessboard[i][j];
                tempFen++;
            }
            else
            {
                while (!hasPiece(i, j, game) && (j < BOARD_DIMENTIONS))
                {
                    count++;
                    j++;
                }
                j--;

                *tempFen = count + '0';
                tempFen++;
                count = 0;
            }
        }

        *tempFen = '/';
        tempFen++;
    }

    tempFen--;
    *tempFen = ' ';
    tempFen++;

    if (game->currentPlayer == BLACK_PLAYER)
    {
        *tempFen = 'b';
    }
    else
    {
        *tempFen = 'w';
    }

    tempFen++;
    *tempFen = '\0';
}

bool is_valid_pawn_move(char piece, int src_row, int src_col, int dest_row, int dest_col, ChessGame *game)
{
    if (dest_row == src_row)
    {
        return false;
    }

    int count = 0;

    if (piece == 'p')
    {
        if (dest_row < src_row)
        {
            return false;
        }

        count = countSquares(src_row, src_col, dest_row, dest_col);
        if (((count == 1) || (count == 2)))
        {
            if ((count == 1) && abs(dest_col - src_col) == 1)
            {
                if (hasPiece(dest_row, dest_col, game))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if ((count == 1) && (dest_col == src_col))
            {
                if (hasPiece(dest_row, dest_col, game))
                {
                    return false;
                }
            }
            else if ((count == 2) && (dest_col == src_col))
            {
                if (hasPiece(dest_row, dest_col, game) || hasPiece((dest_row - 1), dest_col, game))
                {
                    return false;
                }
            }

            return true;
        }
    }

    if (piece == 'P')
    {
        if (dest_row > src_row)
        {
            return false;
        }

        count = countSquares(src_row, src_col, dest_row, dest_col);
        if (((count == 1) || (count == 2)))
        {
            if ((count == 1) && abs(dest_col - src_col) == 1)
            {
                if (hasPiece(dest_row, dest_col, game))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if ((count == 1) && (dest_col == src_col))
            {
                if (hasPiece(dest_row, dest_col, game))
                {
                    return false;
                }
            }
            else if ((count == 2) && (dest_col == src_col))
            {
                if (hasPiece(dest_row, dest_col, game) || hasPiece((dest_row + 1), dest_col, game))
                {
                    return false;
                }
            }

            return true;
        }
    }

    return false;
}

bool is_valid_rook_move(int src_row, int src_col, int dest_row, int dest_col, ChessGame *game)
{
    // Check if horizontal or vertical direction treversed
    if ((src_row == dest_row) || (src_col == dest_col))
    {
        if (isBlocked(src_row, src_col, dest_row, dest_col, game))
        {
            return false;
        }

        return true;
    }

    return false;
}

bool is_valid_knight_move(int src_row, int src_col, int dest_row, int dest_col)
{
    if (abs(dest_row - src_row) == 2)
    {
        if (abs(dest_col - src_col) == 1)
        {
            return true;
        }
    }

    if (abs(dest_row - src_row) == 1)
    {
        if (abs(dest_col - src_col) == 2)
        {
            return true;
        }
    }

    return false;
}

bool is_valid_bishop_move(int src_row, int src_col, int dest_row, int dest_col, ChessGame *game)
{
    if (abs(dest_row - src_row) != abs(dest_col - src_col))
    {
        return false;
    }

    if (isBlocked(src_row, src_col, dest_row, dest_col, game))
    {
        return false;
    }

    return true;
}

bool is_valid_queen_move(int src_row, int src_col, int dest_row, int dest_col, ChessGame *game)
{
    if (is_valid_rook_move(src_row, src_col, dest_row, dest_col, game))
    {
        return true;
    }

    if (is_valid_bishop_move(src_row, src_col, dest_row, dest_col, game))
    {
        return true;
    }

    return false;
}

bool is_valid_king_move(int src_row, int src_col, int dest_row, int dest_col)
{
    if (countSquares(src_row, src_col, dest_row, dest_col) == 1)
    {
        return true;
    }

    return false;
}

bool is_valid_move(char piece, int src_row, int src_col, int dest_row, int dest_col, ChessGame *game)
{
    if ((isOutOfBounds(src_row, src_col) || isOutOfBounds(dest_row, dest_col)))
    {
        return false;
    }

    switch (toupper(piece))
    {
    case 'P':
        return is_valid_pawn_move(piece, src_row, src_col, dest_row, dest_col, game);
        break;

    case 'N':
        return is_valid_knight_move(src_row, src_col, dest_row, dest_col);
        break;

    case 'B':
        return is_valid_bishop_move(src_row, src_col, dest_row, dest_col, game);
        break;

    case 'R':
        return is_valid_rook_move(src_row, src_col, dest_row, dest_col, game);
        break;

    case 'Q':
        return is_valid_queen_move(src_row, src_col, dest_row, dest_col, game);
        break;

    case 'K':
        return is_valid_king_move(src_row, src_col, dest_row, dest_col);
        break;
    }

    return false;
}

void fen_to_chessboard(const char *fen, ChessGame *game)
{
    const int BOARD_DIMENTIONS = 8;
    const char *tempFen = fen;
    int count = 0;

    for (int i = 0; i < BOARD_DIMENTIONS; i++)
    {
        for (int j = 0; j < BOARD_DIMENTIONS; j++)
        {
            if (isdigit(*tempFen))
            {
                count = atoi(tempFen);

                while (count > 0 && j < BOARD_DIMENTIONS)
                {
                    game->chessboard[i][j] = '.';
                    count--;
                    j++;
                }
                j--;
                tempFen++;
            }
            else
            {
                game->chessboard[i][j] = *tempFen;
                tempFen++;
            }
        }
        tempFen++;
    }

    if (*tempFen == 'b')
    {
        game->currentPlayer = BLACK_PLAYER;
    }
    else
    {
        game->currentPlayer = WHITE_PLAYER;
    }
}

int parse_move(const char *move, ChessMove *parsed_move)
{
    int length = strlen(move);
    if (!(length == 4 || length == 5))
    {
        return PARSE_MOVE_INVALID_FORMAT;
    }

    if ((isInvalidLetterIndex(move[0]) || isInvalidLetterIndex(move[2])))
    {
        return PARSE_MOVE_INVALID_FORMAT;
    }

    if (isOutOfBounds(((move[1] - 1) - '0'), ((move[3] - 1) - '0')))
    {
        return PARSE_MOVE_OUT_OF_BOUNDS;
    }

    if (length == 5)
    {
        int destination = move[3] - '0';
        if (!((destination == 8) || (destination == 1)))
        {
            return PARSE_MOVE_INVALID_DESTINATION;
        }

        if (!isValidPromotionPiece(move[4]))
        {
            return PARSE_MOVE_INVALID_PROMOTION;
        }
    }

    parsed_move->startSquare[0] = move[0];
    parsed_move->startSquare[1] = move[1];
    parsed_move->startSquare[2] = '\0';

    parsed_move->endSquare[0] = move[2];
    parsed_move->endSquare[1] = move[3];
    parsed_move->endSquare[2] = '\0';

    if (length == 5)
    {
        parsed_move->endSquare[2] = move[4];
        parsed_move->endSquare[3] = '\0';
    }

    return 0;
}

int make_move(ChessGame *game, ChessMove *move, bool is_client, bool validate_move)
{
    int src_col = convertMoveToCoordinates(move->startSquare[0]);
    int src_row = 8 - (move->startSquare[1] - '0');
    int dest_col = convertMoveToCoordinates(move->endSquare[0]);
    int dest_row = 8 - (move->endSquare[1] - '0');
    char promotion = '-';
    char capturedPiece = '-';
    int length = strlen(move->endSquare);

    if (length == 3)
    {
        promotion = move->endSquare[2];
    }

    if (validate_move)
    {
        if (is_client && (game->currentPlayer == BLACK_PLAYER))
        {
            return MOVE_OUT_OF_TURN;
        }

        if (!is_client && (game->currentPlayer == WHITE_PLAYER))
        {
            return MOVE_OUT_OF_TURN;
        }

        if (!hasPiece(src_row, src_col, game))
        {
            return MOVE_NOTHING;
        }

        if (is_client)
        {
            if (islower(game->chessboard[src_row][src_col]))
            {
                return MOVE_WRONG_COLOR;
            }
        }
        else
        {
            if (isupper(game->chessboard[src_row][src_col]))
            {
                return MOVE_WRONG_COLOR;
            }
        }

        if (is_client)
        {
            if (hasPiece(dest_row, dest_col, game) && isupper(game->chessboard[dest_row][dest_col]))
            {
                return MOVE_SUS;
            }
        }
        else
        {
            if (hasPiece(dest_row, dest_col, game) && islower(game->chessboard[dest_row][dest_col]))
            {
                return MOVE_SUS;
            }
        }

        if ((length == 3) && (toupper(game->chessboard[src_row][src_col]) != 'P'))
        {
            return MOVE_NOT_A_PAWN;
        }

        if ((length == 2) && (toupper(game->chessboard[src_row][src_col] == 'P')))
        {
            if ((dest_row == 7) || (dest_row == 0))
            {
                return MOVE_MISSING_PROMOTION;
            }
        }

        if (!is_valid_move(game->chessboard[src_row][src_col], src_row, src_col, dest_row, dest_col, game))
        {
            return MOVE_WRONG;
        }
    }

    if (hasPiece(dest_row, dest_col, game))
    {
        capturedPiece = game->chessboard[dest_row][dest_col];
    }

    game->chessboard[dest_row][dest_col] = game->chessboard[src_row][src_col];
    game->chessboard[src_row][src_col] = '.';

    if (promotion != '-')
    {
        if (game->chessboard[dest_row][dest_col] == 'p')
        {
            game->chessboard[dest_row][dest_col] = tolower(promotion);
        }

        if (game->chessboard[dest_row][dest_col] == 'P')
        {
            game->chessboard[dest_row][dest_col] = toupper(promotion);
        }
    }

    game->moves[game->moveCount] = *move;
    game->moveCount++;

    if (capturedPiece != '-')
    {
        game->capturedPieces[game->capturedCount] = capturedPiece;
        game->capturedCount++;
        game->capturedPieces[game->capturedCount] = '\0';
    }

    if (game->currentPlayer == BLACK_PLAYER)
    {
        game->currentPlayer = WHITE_PLAYER;
    }
    else
    {
        game->currentPlayer = BLACK_PLAYER;
    }

    return 0;
}

int send_command(ChessGame *game, const char *message, int socketfd, bool is_client)
{
    const char *tempMessagePointer = message;
    const char *file = "game_database.txt";

    while (*tempMessagePointer != ' ')
    {
        tempMessagePointer++;
    }
    tempMessagePointer++;

    int length = strlen(tempMessagePointer);

    if (strncmp(message, "/move", 5) == 0)
    {
        ChessMove move;
        if(parse_move(tempMessagePointer, &move) == 0)
        {
            if(make_move(game, &move, is_client, true) == 0)
            {
                return COMMAND_MOVE;
            }
        }
    
        return COMMAND_ERROR;
    }

    if (strncmp(message, "/forfeit", 8) == 0)
    {
        send(socketfd, message, sizeof(message), 0);
        return COMMAND_FORFEIT;
    }

    if (strncmp(message, "/chessboard", 11) == 0)
    {
        display_chessboard(game);
        return COMMAND_DISPLAY;
    }

    if (strncmp(message, "/import", 7) == 0)
    {
        if(!is_client)
        {
            fen_to_chessboard(tempMessagePointer, game);
            return COMMAND_IMPORT;
        }
    }

    if (strncmp(message, "/load", 5) == 0)
    {
        char username[length - 1];

        for (int i = 0; i < (length - 1); i++)
        {
            username[i] = *tempMessagePointer;
            tempMessagePointer++;
        }
        username[length - 2] = '\0';

        int saveCount = *tempMessagePointer - '0';

        if(load_game(game, username, file, saveCount) == 0)
        {
            send(socketfd, message, sizeof(message), 0);
            return COMMAND_LOAD;
        }
        else
        {
            return COMMAND_ERROR;
        }
    }

    if (strncmp(message, "/save", 5) == 0)
    {
        char username[length + 1];

        for (int i = 0; i < length; i++)
        {
            username[i] = *tempMessagePointer;
            tempMessagePointer++;
        }
        username[length] = '\0';

        if (save_game(game, username, file) == 0)
        {
            return COMMAND_SAVE;
        }
        else
        {
            return COMMAND_ERROR;
        }
    }

    return COMMAND_UNKNOWN;
}

int receive_command(ChessGame *game, const char *message, int socketfd, bool is_client)
{
    const char *tempMessagePointer = message;
    const char *file = "game_database.txt";

    while (*tempMessagePointer != ' ')
    {
        tempMessagePointer++;
    }
    tempMessagePointer++;

    int length = strlen(tempMessagePointer);

    if(strncmp(message, "/move", 5) == 0)
    {
        ChessMove move;
        if(parse_move(tempMessagePointer, &move) == 0)
        {
            make_move(game, &move, is_client, false);
            return COMMAND_MOVE;
        }

        return COMMAND_ERROR;
    }

    if(strncmp(message, "/forfeit", 8) == 0)
    {
        close(socketfd);
        return COMMAND_FORFEIT;
    }

    if(strncmp(message, "/import", 7) == 0)
    {
        if(is_client)
        {
            fen_to_chessboard(tempMessagePointer, game);
            return COMMAND_IMPORT;
        }
    }

    if(strncmp(message, "/load", 5) == 0)
    {
        char username[length - 1];

        for (int i = 0; i < (length - 1); i++)
        {
            username[i] = *tempMessagePointer;
            tempMessagePointer++;
        }
        username[length - 2] = '\0';

        int saveCount = *tempMessagePointer - '0';

        if(load_game(game, username, file, saveCount) == 0)
        {
            return COMMAND_LOAD;
        }

        return COMMAND_ERROR;
    }
    
    return -1;
}

int save_game(ChessGame *game, const char *username, const char *db_filename)
{
    FILE *gameFile = NULL;
    if ((gameFile = fopen(db_filename, "a")) == NULL)
    {
        return -1;
    }

    const char *temp = username;

    while (*temp)
    {
        if (*temp == ' ')
        {
            return -1;
        }
        temp++;
    }

    char fen[BUFFER_SIZE];
    chessboard_to_fen(fen, game);

    fprintf(gameFile, "%s:%s\n", username, fen);
    fclose(gameFile);

    return 0;
}

int load_game(ChessGame *game, const char *username, const char *db_filename, int save_number)
{
    FILE *gameFile = NULL;
    int countSaveNumber = 1;

    if ((gameFile = fopen(db_filename, "r")) == NULL)
    {
        return -1;
    }

    char name[100];
    int nameIndex = 0;
    char fen[BUFFER_SIZE];
    int fenIndex = 0;
    char temp = '-';
    bool breaker = false;
    bool endOfFile = false;

    while (countSaveNumber <= save_number)
    {
        nameIndex = 0;
        while(1)
        {
            if(fscanf(gameFile, "%c", &name[nameIndex]) == EOF)
            {
                endOfFile = true;
                break;
            }

            if(name[nameIndex] == ':')
            {
                name[nameIndex] = '\0';
                break;
            }

            nameIndex++;
        }

        if (strncmp(name, username, (nameIndex + 1)) == 0)
        {
            if (countSaveNumber == save_number)
            {
                while (1)
                {
                    fscanf(gameFile, "%c", &fen[fenIndex]);

                    if (fen[fenIndex] == '\0')
                    {
                        breaker = true;
                        break;
                    }

                    fenIndex++;
                }

                if (breaker)
                {
                    break;
                }
            }

            countSaveNumber++;
        }

        while (temp != '\n')
        {
            if ((fscanf(gameFile, "%c", &temp)) == EOF)
            {
                endOfFile = true;
                break;
            }
        }

        if (endOfFile)
        {
            break;
        }
    }

    fclose(gameFile);

    if (!breaker || endOfFile)
    {
        return -1;
    }

    fen_to_chessboard(fen, game);

    return 0;
}

void display_chessboard(ChessGame *game)
{
    printf("\nChessboard:\n");
    printf("  a b c d e f g h\n");
    for (int i = 0; i < 8; i++)
    {
        printf("%d ", 8 - i);
        for (int j = 0; j < 8; j++)
        {
            printf("%c ", game->chessboard[i][j]);
        }
        printf("%d\n", 8 - i);
    }
    printf("  a b c d e f g h\n");
}

bool isBlocked(int src_row, int src_col, int dest_row, int dest_col, ChessGame *game)
{
    int differenceRow = abs(dest_row - src_row);
    int differenceCol = abs(src_col - dest_col);

    if (src_row == dest_row)
    {
        if (dest_col < src_col)
        {
            for (int i = (src_col - 1); i > dest_col; i--)
            {
                if (hasPiece(src_row, i, game))
                {
                    return true;
                }
            }
        }
        else
        {
            for (int i = (src_col + 1); i < dest_col; i++)
            {
                if (hasPiece(src_row, i, game))
                {
                    return true;
                }
            }
        }
    }

    if (src_col == dest_col)
    {
        if (dest_row < src_row)
        {
            for (int i = (src_row - 1); i > dest_row; i--)
            {
                if (hasPiece(i, src_col, game))
                {
                    return true;
                }
            }
        }
        else
        {
            for (int i = (src_row + 1); i < dest_row; i++)
            {
                if (hasPiece(i, src_col, game))
                {
                    return true;
                }
            }
        }
    }

    if (differenceRow == differenceCol)
    {
        if (dest_col > src_col)
        {
            if (dest_row > src_row)
            {
                for (int i = (src_row + 1), j = (src_col + 1); i < dest_row && j < dest_col; i++, j++)
                {
                    if (hasPiece(i, j, game))
                    {
                        return true;
                    }
                }
            }
            else
            {
                for (int i = (src_row - 1), j = (src_col + 1); i > dest_row && j < dest_col; i--, j++)
                {
                    if (hasPiece(i, j, game))
                    {
                        return true;
                    }
                }
            }
        }
        else
        {
            if (dest_row > src_row)
            {
                for (int i = (src_row + 1), j = (src_col - 1); i < dest_row && j > dest_col; i++, j--)
                {
                    if (hasPiece(i, j, game))
                    {
                        return true;
                    }
                }
            }
            else
            {
                for (int i = (src_row - 1), j = (src_col - 1); i > dest_row && j > dest_col; i--, j--)
                {
                    if (hasPiece(i, j, game))
                    {
                        return true;
                    }
                }
            }
        }
    }

    return false;
}

bool hasPiece(int row, int col, ChessGame *game)
{
    if (game->chessboard[row][col] == '.')
    {
        return false;
    }

    return true;
}

bool isOutOfBounds(int row, int col)
{
    if (row < 0 || row > 7)
    {
        return true;
    }

    if (col < 0 || col > 7)
    {
        return true;
    }

    return false;
}

int countSquares(int src_row, int src_col, int dest_row, int dest_col)
{
    int differenceRow = abs(dest_row - src_row);
    int differenceCol = abs(src_col - dest_col);
    int difference = 0;

    if (differenceRow == differenceCol)
    {
        difference = differenceRow;
    }
    else if (differenceRow == 0)
    {
        difference = differenceCol;
    }
    else if (differenceCol == 0)
    {
        difference = differenceRow;
    }
    else
    {
        return -1;
    }

    if (difference > 0)
    {
        return difference;
    }

    return -1;
}

bool isInvalidLetterIndex(char letter)
{
    switch (letter)
    {
    case 'a':
        return false;
    case 'b':
        return false;
    case 'c':
        return false;
    case 'd':
        return false;
    case 'e':
        return false;
    case 'f':
        return false;
    case 'g':
        return false;
    case 'h':
        return false;
    }

    return true;
}

bool isValidPromotionPiece(char piece)
{
    switch (piece)
    {
    case 'n':
        return true;
    case 'q':
        return true;
    case 'b':
        return true;
    case 'r':
        return true;
    }

    return false;
}

int convertMoveToCoordinates(char row)
{
    switch (row)
    {
    case 'a':
        return 0;
    case 'b':
        return 1;
    case 'c':
        return 2;
    case 'd':
        return 3;
    case 'e':
        return 4;
    case 'f':
        return 5;
    case 'g':
        return 6;
    case 'h':
        return 7;
    }

    return -1;
}