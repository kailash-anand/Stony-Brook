#include "hw4.h"
#include "math.h"

bool isBlocked(int src_row, int src_col, int dest_row, int dest_col, ChessGame *game);

bool hasPiece(int row, int col, ChessGame *game);

bool isOutOfBounds(int row, int col);

int countSquares(int src_row, int src_col, int dest_row, int dest_col);

void initialize_game(ChessGame *game) 
{
    const int BOARD_DIMENTIONS = 8; // The board is and 8x8 board.

    game->moveCount = 0;
    game->capturedCount = 0;
    game->currentPlayer = WHITE_PLAYER;

    for(int i = 0; i < BOARD_DIMENTIONS; i++)
    {
        for(int j = 0; j < BOARD_DIMENTIONS; j++)
        {
            switch(i)
            {
                case 0:
                    switch(j)
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
                    switch(j)
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

void chessboard_to_fen(char fen[], ChessGame *game) {
    (void)fen;
    (void)game;
}

bool is_valid_pawn_move(char piece, int src_row, int src_col, int dest_row, int dest_col, ChessGame *game) 
{
    if(dest_row == src_row)
    {
        return false;
    }

    int count = 0;

    if(piece == 'p')
    {
        if(dest_row < src_row)
        {
            return false;
        }

        count = countSquares(src_row, src_col, dest_row, dest_col);
        if(((count == 1) || (count == 2)))
        {
            if((count == 1) && abs(dest_col - src_col) == 1)
            {
                if(hasPiece(dest_row, dest_col, game))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if((count == 1) && (dest_col == src_col))
            {
                if(hasPiece(dest_row, dest_col, game))
                {
                    return false;
                }
            }
            else if((count == 2) && (dest_col == src_col))
            {
                if(hasPiece(dest_row, dest_col, game) || hasPiece((dest_row - 1) ,dest_col, game))
                {
                    return false;
                }
            }

            return true;
        }
    }

    if(piece == 'P')
    {
        if(dest_row > src_row)
        {
            return false;
        }

        count = countSquares(src_row, src_col, dest_row, dest_col);
        if(((count == 1) || (count == 2)))
        {
            if((count == 1) && abs(dest_col - src_col) == 1)
            {
                if(hasPiece(dest_row, dest_col, game))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if((count == 1) && (dest_col == src_col))
            {
                if(hasPiece(dest_row, dest_col, game))
                {
                    return false;
                }
            }
            else if((count == 2) && (dest_col == src_col))
            {
                if(hasPiece(dest_row, dest_col, game) || hasPiece((dest_row + 1) ,dest_col, game))
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
    //Check if horizontal or vertical direction treversed
    if((src_row == dest_row) || (src_col == dest_col))
    {
        if(isBlocked(src_row, src_col, dest_row, dest_col, game))
        {
            return false;
        }

        return true;
    }

    return false;
}

bool is_valid_knight_move(int src_row, int src_col, int dest_row, int dest_col) 
{
    if(abs(dest_row - src_row) == 2)
    {
        if(abs(dest_col - src_col) == 1)
        {
            return true;
        }
    }

    if(abs(dest_row - src_row) == 1)
    {
        if(abs(dest_col - src_col) == 2)
        {
            return true;
        }
    }

    return false;
}

bool is_valid_bishop_move(int src_row, int src_col, int dest_row, int dest_col, ChessGame *game) 
{
    if(countSquares(src_row, src_col, dest_row, dest_col) == -1)
    {
        return false;
    }

    if(isBlocked(src_row, src_col, dest_row, dest_col, game))
    {
        return false;
    }

    return true;
}

bool is_valid_queen_move(int src_row, int src_col, int dest_row, int dest_col, ChessGame *game) 
{
    if(is_valid_rook_move(src_row, src_col, dest_row, dest_col, game))
    {
        return true;
    }

    if(is_valid_bishop_move(src_row, src_col, dest_row, dest_col, game))
    {
        return true;
    }

    return false;
}

bool is_valid_king_move(int src_row, int src_col, int dest_row, int dest_col) 
{
    if(countSquares(src_row, src_col, dest_row, dest_col) == 1)
    {
        return true;
    }

    return false;
}

bool is_valid_move(char piece, int src_row, int src_col, int dest_row, int dest_col, ChessGame *game) 
{
    if((isOutOfBounds(src_row, src_col) || isOutOfBounds(dest_row, dest_col)))
    {
        return false;
    }

    switch(toupper(piece))
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

void fen_to_chessboard(const char *fen, ChessGame *game) {
    (void)fen;
    (void)game;
}

int parse_move(const char *move, ChessMove *parsed_move) {
    (void)move;
    (void)parsed_move;
    return -999;
}

int make_move(ChessGame *game, ChessMove *move, bool is_client, bool validate_move) {
    (void)game;
    (void)move;
    (void)is_client;
    (void)validate_move;
    return -999;
}

int send_command(ChessGame *game, const char *message, int socketfd, bool is_client) {
    (void)game;
    (void)message;
    (void)socketfd;
    (void)is_client;
    return -999;
}

int receive_command(ChessGame *game, const char *message, int socketfd, bool is_client) {
    (void)game;
    (void)message;
    (void)socketfd;
    (void)is_client;
    return -999;
}

int save_game(ChessGame *game, const char *username, const char *db_filename) {
    (void)game;
    (void)username;
    (void)db_filename;
    return -999;
}

int load_game(ChessGame *game, const char *username, const char *db_filename, int save_number) {
    (void)game;
    (void)username;
    (void)db_filename;
    (void)save_number;
    return -999;
}

void display_chessboard(ChessGame *game) 
{
    printf("\nChessboard:\n");
    printf("  a b c d e f g h\n");
    for (int i = 0; i < 8; i++) {
        printf("%d ", 8 - i);
        for (int j = 0; j < 8; j++) {
            printf("%c ", game->chessboard[i][j]);
        }
        printf("%d\n", 8 - i);
    }
    printf("  a b c d e f g h\n");
}

/* Method checks if between two points, does any piece exist. Also consideres diagonal movement */
bool isBlocked(int src_row, int src_col, int dest_row, int dest_col, ChessGame *game)
{
    int differenceRow =  abs(dest_row - src_row);
    int differenceCol = abs(src_col - dest_col);

    if(src_row == dest_row)
    {
        if(dest_col < src_col)
        {
            for(int i = (src_col - 1); i > dest_col; i--)
            {
                if(hasPiece(src_row, i, game))
                {
                    return true;
                }
            }
        }
        else
        {
            for(int i = (src_col + 1); i < dest_col; i++)
            {
                if(hasPiece(src_row, i, game))
                {
                    return true;
                }
            }
        }
    }

    if(src_col == dest_col)
    {
        if(dest_row < src_row)
        {
            for(int i = (src_row - 1); i > dest_row; i--)
            {
                if(hasPiece(i, src_col, game))
                {
                    return true;
                }
            }
        }
        else
        {
            for(int i = (src_row + 1); i < dest_row; i++)
            {
                if(hasPiece(i, src_col, game))
                {
                    return true;
                }
            }
        }
    }

    if(differenceRow == differenceCol)
    {
        if(dest_col > src_col)
        {
            if(dest_row > src_row)
            {
                for(int i = (src_row + 1), j = (src_col + 1); i < dest_row && j < dest_col; i++, j++)
                {
                    if(hasPiece(i, j, game))
                    {
                        return true;
                    }
                }
            }
            else
            {
                for(int i = (src_row - 1), j = (src_col + 1); i > dest_row && j < dest_col; i--, j++)
                {
                    if(hasPiece(i, j, game))
                    {
                        return true;
                    }
                }
            }
        }
        else
        {
            if(dest_row > src_row)
            {
                for(int i = (src_row + 1), j = (src_col - 1); i < dest_row && j > dest_col; i++, j--)
                {
                    if(hasPiece(i, j, game))
                    {
                        return true;
                    }
                }
            }
            else
            {
                for(int i = (src_row - 1), j = (src_col - 1); i > dest_row && j > dest_col; i--, j--)
                {
                    if(hasPiece(i, j, game))
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
    if(game->chessboard[row][col] == '.')
    {
        return false;
    }

    return true;
}

bool isOutOfBounds(int row, int col)
{
    if(row < 0 || row > 7)
    {
        return true;
    }

    if(col < 0 || col > 7)
    {
        return true;
    }

    return false;
}

int countSquares(int src_row, int src_col, int dest_row, int dest_col)
{
    int differenceRow =  abs(dest_row - src_row);
    int differenceCol = abs(src_col - dest_col);
    int difference = 0;

    if(differenceRow == differenceCol)
    {
        difference = differenceRow;
    }
    else if(differenceRow == 0)
    {
        difference = differenceCol;
    }
    else if(differenceCol == 0)
    {
        difference = differenceRow;
    }
    else
    {
        return -1;
    }

    if(difference > 0)
    {
        return difference;
    }

    return -1;
}