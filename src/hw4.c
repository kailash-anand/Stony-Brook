#include "hw4.h"

void initialize_game(ChessGame *game) {
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

bool is_valid_pawn_move(char piece, int src_row, int src_col, int dest_row, int dest_col, ChessGame *game) {
    (void)piece;
    (void)src_row;
    (void)src_col;
    (void)dest_row;
    (void)dest_col;
    (void)game;
    return false;
}

bool is_valid_rook_move(int src_row, int src_col, int dest_row, int dest_col, ChessGame *game) {
    (void)src_row;
    (void)src_col;
    (void)dest_row;
    (void)dest_col;
    (void)game;
    return false;
}

bool is_valid_knight_move(int src_row, int src_col, int dest_row, int dest_col) {
    (void)src_row;
    (void)src_col;
    (void)dest_row;
    (void)dest_col;
    return false;
}

bool is_valid_bishop_move(int src_row, int src_col, int dest_row, int dest_col, ChessGame *game) {
    (void)src_row;
    (void)src_col;
    (void)dest_row;
    (void)dest_col;
    (void)game;
    return false;
}

bool is_valid_queen_move(int src_row, int src_col, int dest_row, int dest_col, ChessGame *game) {
    (void)src_row;
    (void)src_col;
    (void)dest_row;
    (void)dest_col;
    (void)game;
    return false;
}

bool is_valid_king_move(int src_row, int src_col, int dest_row, int dest_col) {
    (void)src_row;
    (void)src_col;
    (void)dest_row;
    (void)dest_col;
    return false;
}

bool is_valid_move(char piece, int src_row, int src_col, int dest_row, int dest_col, ChessGame *game) {
    (void)piece;
    (void)src_row;
    (void)src_col;
    (void)dest_row;
    (void)dest_col;
    (void)game;
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

void display_chessboard(ChessGame *game) {
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
