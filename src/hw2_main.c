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

void readAndWritePPM(char *input, char *output);

int countDigits(char *str);

int main(int argc, char **argv) 
{
    int errorCheck[9];
    const int ERR_LENGTH = 9;
    int countI = 0;
    int countO = 0;
    int countP = 0;
    int countC = 0;
    FILE *fp = NULL;
    FILE *fp2 = NULL;
    char *input = 0;
    char *output = 0;
    char *argument = 0;
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
                    input = argv[i+1];
                    break;
                case 'o':
                    countO++;
                    if((fp2 = fopen(argv[i+1], "w")) == NULL)
                    {
                        errorCheck[4] = 1;
                    }
                    output = argv[i+1];   
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

        if(errorCheck[i] == 1)
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
    }

    if(fp != NULL)
    {
        fclose(fp);
    }
    
    if(fp2 != NULL)
    {
        fclose(fp2);
    }
    
    readAndWritePPM(input, output);

    return 0;
}


bool argInvalid(char *arg, char argument)
{
    char *temp = arg;
    int count = 0;

    switch(argument)
    {
        case 'c':
            if(countDigits(arg) != 4)
            {
                return true;
            }
            break;
        case 'p':
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


void readAndWritePPM(char *input, char *output)
{
    FILE *file1 = fopen(input, "r");
    FILE *file2 = fopen(output, "w");

    char title[3];
    unsigned int width = 0;
    unsigned int height = 0;
    unsigned int maxSize = 0;

    fscanf(file1,"%s", title);
    fscanf(file1,"%u %u %u", &width, &height, &maxSize);

    unsigned int data[width*3*height];

    for(unsigned int i = 0; i < width*3*height; i++)
    {
        fscanf(file1,"%u",&data[i]);
    }

    fclose(file1);

    fprintf(file2,"%s\n",title);
    fprintf(file2,"%u %u\n",width,height);
    fprintf(file2,"%u\n",maxSize);

    for(unsigned int j = 0; j < height; j++)
    {
        for(unsigned int k = 0; k < width*3; k++)
        {
            fprintf(file2,"%u ", data[(j*width*3) + k]);
        }
        fprintf(file2,"\n");
    }

    fclose(file2);
}
