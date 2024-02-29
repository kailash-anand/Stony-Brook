#include "hw2.h"

#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <math.h>
#include <ctype.h>
#include <stdbool.h>
#include <string.h>
#include <unistd.h>

bool argInvalid(char *arg, char argument);

int countDigits(char *str);

int main(int argc, char **argv) 
{
    int errorCheck[9];
    const int ERR_LENGTH = 9;
    int countI = 0;
    int countO = 0;
    int countP = 0;
    int countC = 0;
    FILE *fp;
    FILE *fp2;
    char *argument;
    for(int i = 1; i < argc; i++)
    {
        argument = argv[i];
        if(*argument == '-')
        {
            if((i + 1) == argc)
            {
                errorCheck[0] = 1;
                break;
            }

            char *temp = argv[i+1];
            if(*temp == '-')
            {
                errorCheck[0] = 1;
                break;
            }

            argument++;

            switch(*argument)
            {
                case 'i':
                    countI++;
                    if((fp = fopen(argv[i+1], "r")) == NULL)
                    {
                        errorCheck[3] = 1;
                    }
                    break;
                case 'o':
                    countO++;
                    if((fp2 = fopen(argv[i+1], "w")) == NULL)
                    {
                        errorCheck[4] = 1;
                    }   
                    break;
                case 'c':
                    countC++;
                    errorCheck[6] = argInvalid(argv[i+1],'c');
                    break;
                case 'p':
                    countP++;
                    errorCheck[7] = argInvalid(argv[i+1],'p');
                    break;
                case 'r':
                    errorCheck[8] = argInvalid(argv[i+1],'r');
                    break;
                default:
                    errorCheck[1] = 1;
            }
        }
    }

    if(countO == 0 || countI == 0)
    {
        errorCheck[0] = 1;
    }

    if(countO > countI || countI > 1)
    {
        errorCheck[2] = 1;
    } 

    if(countP > 0 && countC == 0)
    {
        errorCheck[5] = 1;
    }

    for(int i = 0; i < ERR_LENGTH; i++)
    {
        if(errorCheck[i])
        {
            switch((i+1))
            {
                case 1: return MISSING_ARGUMENT;
                case 2: return UNRECOGNIZED_ARGUMENT;
                case 3: return DUPLICATE_ARGUMENT;
                case 4: return INPUT_FILE_MISSING;
                case 5: return OUTPUT_FILE_UNWRITABLE;
                case 6: return C_ARGUMENT_MISSING;
                case 7: return C_ARGUMENT_INVALID;
                case 8: return P_ARGUMENT_INVALID;
                case 9: return R_ARGUMENT_INVALID;
            }
        }

        //printf("%d ", errorCheck[i]);
    }

    printf("\n");
    return 0;
}

bool argInvalid(char *arg, char argument)
{
    char *temp = arg;
    int count = 0;

    switch(argument)
    {
        case 'c':
            printf("C: %d\n", countDigits(arg));
            if(countDigits(arg) != 4)
            {
                return true;
            }
            break;
        case 'p':
            printf("P: %d\n", countDigits(arg));
            if(countDigits(arg) != 2)
            {
                return true;
            }
            break;
        case 'r':
            while(!isdigit(*temp))
            {
                temp++;
                count++;
            }

            printf("R: %d\n", (countDigits(arg) + count));
            if((countDigits(arg) + count) != 5)
            {
                return true;
            }
            break;
    }

    return false;
}

int countDigits(char *str)
{
    int count = 0;
    char *temp = str;

    while(*temp)
    {
        if(*temp == ',')
        {
            count++;
        }

        temp++;
    }

    temp--;

    if(isdigit(*temp))
    {
        count++;
    }

    return count;
}
