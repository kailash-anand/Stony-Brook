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

void readAndWritePPM(char *input, char *output, char *copy, char *paste);

void readAndWriteSBU(char *input, char *output);

void readSBUwritePPM(char *input, char *output, char *copy, char *paste);

void readPPMwriteSBU(char *input, char *output);

bool fileEnds(char *file);

bool colorExists(unsigned int currentPixel[], unsigned int colorTable[], unsigned int size);

unsigned int findPixelIndex(unsigned int currentPixel[], unsigned int colorTable[], unsigned int colorCount);

int countDigits(char *str);

int main(int argc, char **argv)
{
    int errorCheck[9] = {0};
    const int ERR_LENGTH = 9;
    int countI = 0;
    int countO = 0;
    int countP = 0;
    int countC = 0;
    FILE *fp = NULL;
    FILE *fp2 = NULL;
    char *input = 0;
    char *output = 0;
    char *copy = 0;
    char *paste = 0;
    char *argument = 0;
    for (int i = 1; i < argc; i++)
    {
        argument = argv[i];
        if (*argument == '-')
        {
            if ((i + 1) == argc)
            {
                errorCheck[0] = 1;
                break;
            }

            char *temp = argv[i + 1];
            if (*temp == '-')
            {
                errorCheck[0] = 1;
                break;
            }

            argument++;

            switch (*argument)
            {
            case 'i':
                countI++;
                if ((fp = fopen(argv[i + 1], "r")) == NULL)
                {
                    errorCheck[3] = 1;
                }
                input = argv[i + 1];
                break;
            case 'o':
                countO++;
                if ((fp2 = fopen(argv[i + 1], "w")) == NULL)
                {
                    errorCheck[4] = 1;
                }
                output = argv[i + 1];
                break;
            case 'c':
                countC++;
                errorCheck[6] = argInvalid(argv[i + 1], 'c');
                copy = argv[i + 1];
                break;
            case 'p':
                countP++;
                errorCheck[7] = argInvalid(argv[i + 1], 'p');
                paste = argv[i + 1];
                break;
            case 'r':
                errorCheck[8] = argInvalid(argv[i + 1], 'r');
                break;
            default:
                errorCheck[1] = 1;
            }
        }
    }

    if (countO == 0 || countI == 0)
    {
        errorCheck[0] = 1;
    }

    if (countO > countI || countI > 1)
    {
        errorCheck[2] = 1;
    }

    if (countP > 0 && countC == 0)
    {
        errorCheck[5] = 1;
    }

    for (int i = 0; i < ERR_LENGTH; i++)
    {
        if (errorCheck[i] == 1)
        {
            switch ((i + 1))
            {
            case 1:
                return MISSING_ARGUMENT;
            case 2:
                return UNRECOGNIZED_ARGUMENT;
            case 3:
                return DUPLICATE_ARGUMENT;
            case 4:
                return INPUT_FILE_MISSING;
            case 5:
                return OUTPUT_FILE_UNWRITABLE;
            case 6:
                return C_ARGUMENT_MISSING;
            case 7:
                return C_ARGUMENT_INVALID;
            case 8:
                return P_ARGUMENT_INVALID;
            case 9:
                return R_ARGUMENT_INVALID;
            }
        }
    }

    if (fp != NULL)
    {
        fclose(fp);
    }

    if (fp2 != NULL)
    {
        fclose(fp2);
    }

    if (fileEnds(input) == 0 && fileEnds(output) == 0)
    {
        readAndWriteSBU(input, output);
    }
    else if (fileEnds(input) == 0 && fileEnds(output) == 1)
    {
        readSBUwritePPM(input, output, copy, paste);
    }
    else if (fileEnds(input) == 1 && fileEnds(output) == 0)
    {
        readPPMwriteSBU(input, output);
    }
    else
    {
        readAndWritePPM(input, output, copy, paste);
    }

    return 0;
}

bool argInvalid(char *arg, char argument)
{
    char *temp = arg;
    int count = 0;

    switch (argument)
    {
    case 'c':
        if (countDigits(arg) != 4)
        {
            return true;
        }
        break;
    case 'p':
        if (countDigits(arg) != 2)
        {
            return true;
        }
        break;
    case 'r':
        while (!isdigit(*temp))
        {
            temp++;
            count++;
        }

        if ((countDigits(arg) + count) != 5)
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

    while (*temp)
    {
        if (*temp == ',')
        {
            count++;
        }

        temp++;
    }

    temp--;

    if (isdigit(*temp))
    {
        count++;
    }

    return count;
}

bool fileEnds(char *file)
{
    char *temp = file;

    while (*temp)
    {
        temp++;
    }

    temp--;
    temp--;
    temp--;

    if (strcmp(temp, "sbu"))
    {
        return true;
    }
    else
    {
        return false;
    }
}

void readAndWritePPM(char *input, char *output, char *copy, char *paste)
{
    FILE *file1 = fopen(input, "r");

    char title[3];
    unsigned int width = 0;
    unsigned int height = 0;
    unsigned int maxSize = 0;

    fscanf(file1, "%s", title);
    fscanf(file1, "%u %u %u", &width, &height, &maxSize);

    unsigned int data[width * 3 * height];

    for (unsigned int i = 0; i < width * 3 * height; i++)
    {
        fscanf(file1, "%u", &data[i]);
    }

    fclose(file1);

    if (copy != 0)
    {
        int copyData[4];

        char *temp = strtok(copy, ",");
        copyData[0] = atoi(temp);

        for (int i = 1; i < 4; i++)
        {
            temp = strtok(NULL, ",");
            copyData[i] = atoi(temp);
        }

        int pasteRow, pasteColoumn;

        temp = strtok(paste, ",");
        pasteRow = atoi(temp);

        temp = strtok(NULL, ",");
        pasteColoumn = atoi(temp);

        if (((unsigned)copyData[2] * 3) > (width * 3 - copyData[1] * 3))
        {
            copyData[2] = width - copyData[1];
        }

        if (((unsigned)copyData[3] * 3) > (height * 3 - copyData[0] * 3))
        {
            copyData[3] = height - copyData[0];
        }

        if (((unsigned)copyData[2] * 3) > (width * 3 - pasteColoumn * 3))
        {
            copyData[2] = width - pasteColoumn;
        }

        unsigned int startIndex = (copyData[0]) * width * 3 + copyData[1] * 3;
        int skip = width * 3 - copyData[2] * 3;
        int length = copyData[2] * copyData[3] * 3;
        unsigned int copiedData[length];
        int index = 0;

        for (int i = 0; i < copyData[3]; i++)
        {
            for (int j = 0; j < copyData[2] * 3; j++)
            {
                copiedData[index] = data[startIndex];
                startIndex++;
                index++;
            }

            startIndex += skip;
        }

        if (((unsigned)copyData[3] * 3) > (height * 3 - pasteRow * 3))
        {
            copyData[3] = height - pasteRow;
        }

        startIndex = (pasteRow)*width * 3 + pasteColoumn * 3;
        skip = width * 3 - copyData[2] * 3;
        index = 0;

        for (int i = 0; i < copyData[3]; i++)
        {
            for (int j = 0; j < copyData[2] * 3; j++)
            {
                data[startIndex] = copiedData[index];
                startIndex++;
                index++;
            }

            startIndex += skip;
        }
    }

    FILE *file2 = fopen(output, "w");

    fprintf(file2, "%s\n", title);
    fprintf(file2, "%u %u\n", width, height);
    fprintf(file2, "%u\n", maxSize);

    for (unsigned int j = 0; j < height; j++)
    {
        for (unsigned int k = 0; k < width * 3; k++)
        {
            fprintf(file2, "%u ", data[(j * width * 3) + k]);
        }
        fprintf(file2, "\n");
    }

    fclose(file2);
}

void readAndWriteSBU(char *input, char *output)
{
    FILE *file1 = fopen(input, "r");
    FILE *file2 = fopen(output, "w");

    char title[4];
    unsigned int width = 0;
    unsigned int height = 0;
    unsigned int colorCount = 0;

    fscanf(file1, "%s", title);
    fscanf(file1, "%u %u %u", &width, &height, &colorCount);

    int length = colorCount * 3;
    unsigned int colors[length];

    for (int i = 0; i < length; i++)
    {
        fscanf(file1, "%u", &colors[i]);
    }

    length = width * height;
    unsigned int data[width * height];
    unsigned int count = 0;
    unsigned int value = 0;

    for (int i = 0; i < length; i++)
    {
        if (!fscanf(file1, "%u", &count))
        {
            fseek(file1, 1, SEEK_CUR);
            fscanf(file1, "%u", &count);
            fscanf(file1, "%u", &value);

            for (unsigned int j = 0; j < count; j++)
            {
                data[i] = value;
                i++;
            }
            i--;
        }
        else
        {
            data[i] = count;
        }
    }

    fclose(file1);

    fprintf(file2, "%s\n", title);
    fprintf(file2, "%u %u\n", width, height);
    fprintf(file2, "%u\n", colorCount);

    length = colorCount * 3;

    for (int i = 0; i < length; i++)
    {
        fprintf(file2, "%u ", colors[i]);
    }
    fprintf(file2, "\n");

    length = width * height;
    count = 0;
    value = 0;
    bool here = false;

    for (int i = 0; i < length; i++)
    {
        value = data[i];
        here = false;

        while (i < length - 1 && data[i + 1] == data[i])
        {
            count++;
            i++;
            here = true;
        }

        if (here)
        {
            count++;
            fprintf(file2, "%s", "*");
            fprintf(file2, "%u ", count);
            fprintf(file2, "%u ", value);
        }
        else
        {
            fprintf(file2, "%u ", value);
        }

        value = 0;
        count = 0;
    }

    fclose(file2);
}

void readSBUwritePPM(char *input, char *output, char *copy, char *paste)
{
    FILE *file1 = fopen(input, "r");
    FILE *file2 = fopen(output, "w");

    char title[4];
    unsigned int width = 0;
    unsigned int height = 0;
    unsigned int colorCount = 0;

    fscanf(file1, "%s", title);
    fscanf(file1, "%u %u %u", &width, &height, &colorCount);

    int length = colorCount * 3;
    unsigned int colors[length];

    for (int i = 0; i < length; i++)
    {
        fscanf(file1, "%u", &colors[i]);
    }

    length = width * height;
    unsigned int data[width * height];
    unsigned int count = 0;
    unsigned int value = 0;

    for (int i = 0; i < length; i++)
    {
        if (!fscanf(file1, "%u", &count))
        {
            fseek(file1, 1, SEEK_CUR);
            fscanf(file1, "%u", &count);
            fscanf(file1, "%u", &value);

            for (unsigned int j = 0; j < count; j++)
            {
                data[i] = value;
                i++;
            }
            i--;
        }
        else
        {
            data[i] = count;
        }
    }

    fclose(file1);

    int index = 0;
    value = 0;

    fprintf(file2, "P3\n");
    fprintf(file2, "%u %u\n", width, height);
    fprintf(file2, "%u\n", 255);

    for (int i = 0; i < length; i++)
    {
        value = data[i];
        index = value * 3;

        for (int j = index; j < index + 3; j++)
        {
            fprintf(file2, "%u ", colors[j]);
        }
    }
    fprintf(file2, "\n");
    fclose(file2);

    FILE *file3 = fopen(output, "r");

    char title1[3];
    unsigned int width1 = 0;
    unsigned int height1 = 0;
    unsigned int maxSize1 = 0;

    fscanf(file3, "%s", title1);
    fscanf(file3, "%u %u %u", &width1, &height1, &maxSize1);

    unsigned int data1[width1 * 3 * height1];

    for (unsigned int i = 0; i < width1 * 3 * height1; i++)
    {
        fscanf(file3, "%u", &data1[i]);
    }

    fclose(file3);

    if (copy != 0)
    {
        int copyData[4];

        char *temp = strtok(copy, ",");
        copyData[0] = atoi(temp);

        for (int i = 1; i < 4; i++)
        {
            temp = strtok(NULL, ",");
            copyData[i] = atoi(temp);
        }

        int pasteRow, pasteColoumn;

        temp = strtok(paste, ",");
        pasteRow = atoi(temp);

        temp = strtok(NULL, ",");
        pasteColoumn = atoi(temp);

        if (((unsigned)copyData[2] * 3) > (width * 3 - copyData[1] * 3))
        {
            copyData[2] = width - copyData[1];
        }

        if (((unsigned)copyData[3] * 3) > (height * 3 - copyData[0] * 3))
        {
            copyData[3] = height - copyData[0];
        }

        if (((unsigned)copyData[2] * 3) > (width * 3 - pasteColoumn * 3))
        {
            copyData[2] = width - pasteColoumn;
        }

        unsigned int startIndex = (copyData[0]) * width * 3 + copyData[1] * 3;
        int skip = width * 3 - copyData[2] * 3;
        int length2 = copyData[2] * copyData[3] * 3;
        unsigned int copiedData[length2];
        int index2 = 0;

        for (int i = 0; i < copyData[3]; i++)
        {
            for (int j = 0; j < copyData[2] * 3; j++)
            {
                copiedData[index2] = data1[startIndex];
                startIndex++;
                index2++;
            }

            startIndex += skip;
        }

        if (((unsigned)copyData[3] * 3) > (height * 3 - pasteRow * 3))
        {
            copyData[3] = height - pasteRow;
        }

        startIndex = (pasteRow)*width * 3 + pasteColoumn * 3;
        skip = width * 3 - copyData[2] * 3;
        index2 = 0;

        for (int i = 0; i < copyData[3]; i++)
        {
            for (int j = 0; j < copyData[2] * 3; j++)
            {
                data1[startIndex] = copiedData[index2];
                startIndex++;
                index2++;
            }

            startIndex += skip;
        }
    }

    FILE *file4 = fopen(output, "w");

    fprintf(file4, "%s\n", title1);
    fprintf(file4, "%u %u\n", width1, height1);
    fprintf(file4, "%u\n", maxSize1);

    for (unsigned int j = 0; j < height1; j++)
    {
        for (unsigned int k = 0; k < width1 * 3; k++)
        {
            fprintf(file4, "%u ", data1[(j * width1 * 3) + k]);
        }
        fprintf(file4, "\n");
    }

    fclose(file4);
}

void readPPMwriteSBU(char *input, char *output)
{
    FILE *file1 = fopen(input, "r");
    FILE *file2 = fopen(output, "w");

    char title[3];
    unsigned int width = 0;
    unsigned int height = 0;
    unsigned int maxSize = 0;

    fscanf(file1, "%s", title);
    fscanf(file1, "%u %u %u", &width, &height, &maxSize);

    unsigned int data[width * 3 * height];

    for (unsigned int i = 0; i < width * 3 * height; i++)
    {
        fscanf(file1, "%u", &data[i]);
    }

    fclose(file1);

    unsigned int colors[width * 3 * height];
    unsigned int colorCount = 0;
    unsigned int currentPixel[3];
    unsigned int index = 0;

    for (unsigned int i = 0; i < (width * 3 * height - 2); i += 3)
    {
        currentPixel[0] = data[i];
        currentPixel[1] = data[i + 1];
        currentPixel[2] = data[i + 2];

        if (!colorExists(currentPixel, colors, colorCount))
        {
            colors[index] = currentPixel[0];
            index++;
            colors[index] = currentPixel[1];
            index++;
            colors[index] = currentPixel[2];
            index++;
            colorCount++;
        }
    }

    unsigned int pixels[width*height];

    for (unsigned int i = 0; i < (width * height); i++)
    {
        currentPixel[0] = data[i * 3];     
        currentPixel[1] = data[i * 3 + 1]; 
        currentPixel[2] = data[i * 3 + 2]; 

        pixels[i] = findPixelIndex(currentPixel, colors, colorCount); 
    }

    fprintf(file2, "%s\n", "SBU");
    fprintf(file2, "%u %u\n", width, height);
    fprintf(file2, "%u\n", colorCount);

    int length = colorCount * 3;

    for (int i = 0; i < length; i++)
    {
        fprintf(file2, "%u ", colors[i]);
    }
    fprintf(file2, "\n");

    length = width * height;
    int count = 0;
    int value = 0;
    bool here = false;

    for (int i = 0; i < length; i++)
    {
        value = pixels[i];
        here = false;

        while (i < length - 1 && pixels[i + 1] == pixels[i])
        {
            count++;
            i++;
            here = true;
        }

        if (here)
        {
            count++;
            fprintf(file2, "%s", "*");
            fprintf(file2, "%u ", count);
            fprintf(file2, "%u ", value);
        }
        else
        {
            fprintf(file2, "%u ", value);
        }

        value = 0;
        count = 0;
    }

    fclose(file2);    
}

bool colorExists(unsigned int currentPixel[], unsigned int colorTable[], unsigned int colorCount)
{
    for (unsigned int i = 0; i < colorCount; i++)
    {
        if (currentPixel[0] == colorTable[i * 3] &&
            currentPixel[1] == colorTable[i * 3 + 1] &&
            currentPixel[2] == colorTable[i * 3 + 2])
        {
            return true;
        }
    }
    return false;
}

unsigned int findPixelIndex(unsigned int currentPixel[], unsigned int colorTable[], unsigned int colorCount)
{
    for (unsigned int i = 0; i < colorCount; i++)
    {
        if (currentPixel[0] == colorTable[i * 3] &&
            currentPixel[1] == colorTable[i * 3 + 1] &&
            currentPixel[2] == colorTable[i * 3 + 2])
        {
            return i;
        }
    }
    return 1000;
}
