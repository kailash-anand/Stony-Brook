#include <stdio.h>

void displayBoard(int board[], int length);

int playTurn(int board[], int length, int selection);

void displayBoard(int board[5], int length)
{
    for(int i=0 ; i<length ; i++)
    {
        if(i == length-1)
        {
            printf("| ");
        }
        printf("%d", board[i]);
        printf(" ");
    }
    printf("\n");
}

int playTurn(int board[], int length, int selection)
{
    int count = board[selection];
    int index = selection + 1;

    for(int i=0 ; i<count ; i++)
    {
        board[index % length] += 1;
         ++index;
    }
    --index;

    board[selection] = 0;

    return (index % length);
}

int main() {
    int selection;
    int board[5];
    int length = sizeof(board)/sizeof(board[0]);

    for(int i=0 ; i<length-1 ; i++)
    {
        board[i] = 2;
    }
    board[length-1] = 0;

    displayBoard(board,length);
    printf("Choose a section (1-4)-");
    scanf("%d", &selection);
    while(1)
    {
        int result = playTurn(board, length, selection-1);
        
        if(result != 4 && board[result] == 1)
        {
            displayBoard(board, length);
            printf("You lost beacuse the last counter fell into section ");
            printf("%d", result+1);
            break;
        }

        if(result == 4 && board[result] == 8)
        {
            printf("YOU WIN!");
            break;
        }

        if(result != 4)
        {
            displayBoard(board,length);
            printf("Last piece landed in section ");
            printf("%d", result+1); 
            printf(". Continue sowing seeds!\n");
            selection = result + 1;
        }
        else
        {
            displayBoard(board,length);
            printf("Choose a section (1-4)-");
            scanf("%d", &selection);
            while(selection >= length || board[selection-1] == 0)
            {
                printf("Invalid choice. Choose a section (1-4): ");
                scanf("%d", &selection);
            }
        }
    }

    return 0;
}
