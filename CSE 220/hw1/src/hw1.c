#include "hw1.h"
#include <math.h>
#include <string.h>

unsigned int printOrGetHeader(int switchId, const char headerName[], unsigned char packet[]);

unsigned int printOrGetPayload(unsigned char packet[], int index, char mode[]);

int reconstructData(unsigned char packet[], int array[], int array_len);

void fillHeaders(unsigned char *packet[], unsigned int packet_len, int headers[], int numberOfPayloads, int index);

void fillPacket(unsigned char packet[], int array[], int numberOfElements, int startIndex);

void print_packet_sf(unsigned char packet[])
{
    int index = 16;
    const int NO_OF_HEADERS = 10;
    const char *HEADER_NAMES[] = {"Source Address" , "Destination Address", "Source Port",
                                       "Destination Port", "Fragment Offset", "Packet Length",
                                       "Maximum Hop Count", "Checksum", "Compression Scheme",
                                       "Traffic Class"};
    for(int i = 0; i < NO_OF_HEADERS; i++)
    {
        printOrGetHeader((i + 1), HEADER_NAMES[i], &packet[0]);
    }

    printf("Payload:");
    unsigned int length = ((packet[9] & 0b00000011) << 12) | (packet[10] << 4) | ((packet[11] & 0b11110000) >> 4);
    for(unsigned int i = 0; i < (length - 16); i += 4)
    {
        printOrGetPayload(&packet[0], index, "PRINT");
        index += 4;
    }
    
    printf("\n");
}

unsigned int compute_checksum_sf(unsigned char packet[])
{
    const int PKT_LENGTH_ID = 6;
    unsigned int length = printOrGetHeader(PKT_LENGTH_ID, "GET", &packet[0]);
    unsigned int checkSum = 0;
    int index = 16;

    for(unsigned int i = 0; i < 10; i++)
    {
        if(i == 7)
        {
            continue;
        }
        checkSum += printOrGetHeader((i + 1), "GET", &packet[0]);
    }

    for(unsigned int i = 0; i < (length - 16); i += 4)
    {
        checkSum += abs(printOrGetPayload(&packet[0], index, "GET"));
        index += 4;
    }

    return (checkSum % ((1<<23) - 1));
}

unsigned int reconstruct_array_sf(unsigned char *packets[], unsigned int packets_len, int *array, unsigned int array_len) {
    unsigned int count = 0;
    const int PACKET_LENGTH_ID = 6;

    for(unsigned int i = 0; i < packets_len; i++)
    {
        count += reconstructData(packets[i], array, array_len);
    }
    return count;
}

unsigned int packetize_array_sf(int *array, unsigned int array_len, unsigned char *packets[], unsigned int packets_len,
                          unsigned int max_payload, unsigned int src_addr, unsigned int dest_addr,
                          unsigned int src_port, unsigned int dest_port, unsigned int maximum_hop_count,
                          unsigned int compression_scheme, unsigned int traffic_class)
{
    unsigned int maxPayloadsPerPacket = max_payload/4;
    unsigned int numberOfPayloads = array_len/maxPayloadsPerPacket;
    unsigned int leftoverPayload = array_len - (numberOfPayloads*maxPayloadsPerPacket);

    if(numberOfPayloads >= packets_len)
    {
        numberOfPayloads = packets_len;
        leftoverPayload = 0;
    }
    else
    {
        if(leftoverPayload != 0)
        {
            numberOfPayloads++;
        }
    }

    const int HEADER_SIZE = 16;
    for(int i = 0; i < numberOfPayloads; i++)
    {
        if(leftoverPayload != 0)
        {
            if(i == (numberOfPayloads - 1))
            {
                packets[i] = malloc(HEADER_SIZE + leftoverPayload*4);
                break;
            }
        }

        packets[i] = malloc(HEADER_SIZE + maxPayloadsPerPacket*4);
    }

    unsigned int packetIndex = 0;
    unsigned int count = 0;
    unsigned int headers[] = {src_addr, dest_addr, src_port, dest_port, 0,0, maximum_hop_count,
                              0, compression_scheme, traffic_class};

    for(int i = 0; i < numberOfPayloads; i++)
    {
        if((leftoverPayload != 0) && (i == (numberOfPayloads - 1)))
        {
            headers[4] = packetIndex*4;
            headers[5] = leftoverPayload*4 + 16;
            fillHeaders(packets, packets_len, &headers[0], numberOfPayloads, i);
            fillPacket(packets[i], &array[0], leftoverPayload, packetIndex);
            count ++;
            break;
        }

        headers[4] = packetIndex*4;
        headers[5] = maxPayloadsPerPacket*4 + 16;
        fillHeaders(packets, packets_len, &headers[0], numberOfPayloads, i);
        fillPacket(packets[i], &array[0], maxPayloadsPerPacket, packetIndex);
        packetIndex += maxPayloadsPerPacket;
        count++;;
    }

    return count;
}

unsigned int printOrGetHeader(int switchId, const char headerName[], unsigned char packet[])
{
    unsigned int decoded = 0;
    switch(switchId)
    {
        case 1:
            decoded = (packet[0] << 20) | (packet[1] << 12) | (packet[2] << 4) | (packet[3] >> 4);
            break;

        case 2:
            decoded = ((packet[3] & 0b00001111) << 24) | (packet[4] << 16) | (packet[5] << 8) | packet[6];
            break;

        case 3:
            decoded = (packet[7] & 0b11110000) >> 4;
            break;

        case 4:
            decoded = (packet[7] & 0b00001111);
            break;

        case 5:
            decoded = (packet[8] << 6) | ((packet[9] & 0b11111100) >> 2);
            break;

        case 6:
            decoded = ((packet[9] & 0b00000011) << 12) | (packet[10] << 4) | ((packet[11] & 0b11110000) >> 4);
            break;

        case 7:
            decoded = ((packet[11] & 0b00001111) << 1) | ((packet[12] & 0b10000000) >> 7);
            break;

        case 8:
            decoded = ((packet[12] & 0b01111111) << 16) | (packet[13] << 8) | packet[14];
            break;

        case 9:
            decoded = ((packet[15] & 0b11000000) >> 6);
            break;

        case 10:
            decoded = (packet[15] & 0b00111111);
            break;    
    }
    
    if(strcmp(headerName, "GET") == 0)
    {
        return decoded;
    }

    printf("%s: %d\n", headerName, decoded);

    return 0;
}

unsigned int printOrGetPayload(unsigned char packet[], int index, char mode[])
{
    int shift = 32;
    const int ELEMENT_SIZE = 8;
    unsigned int payload = 0;
    
    while(shift > ELEMENT_SIZE)
    {
        shift = shift - ELEMENT_SIZE;
        payload = payload | (packet[index] << shift);
        index++;
    }
    payload = payload | packet[index];

    if(strcmp(mode, "GET") == 0)
    {
        return payload;
    }

    if(strcmp(mode, "PRINT") == 0)
    {
        printf(" %d", payload);
    }

    return 0;
}

int reconstructData(unsigned char packet[], int array[], int array_len)
{
    const int FRAGMENT_OFFSET_ID = 5, PACKET_LENGTH_ID = 6, CHECK_SUM_ID = 8;
    int countPayload = 0;
    unsigned int fragmentOffset = printOrGetHeader(FRAGMENT_OFFSET_ID, "GET", packet);
    unsigned int packetLength = printOrGetHeader(PACKET_LENGTH_ID, "GET", packet);
    unsigned int checkSum = printOrGetHeader(CHECK_SUM_ID, "GET", packet);

    if(compute_checksum_sf(packet) == checkSum)
    {
        int index = 16;
        unsigned int location = fragmentOffset/4;

        for(unsigned int i = 0; i < (packetLength - 16); i += 4)
        {
            if(location >= array_len)
            {
                break;
            }
            array[location] = printOrGetPayload(&packet[0], index, "GET");

            countPayload++;
            location++;
            index += 4;
        }
        return countPayload;
    }
    else
    {
        return 0;
    }
}

void fillHeaders(unsigned char *packet[], unsigned int packet_len, int headers[], int numberOfPayloads, int index)
{
    const int HEADER_SIZE = 16;
    int debug = 1;

    if(numberOfPayloads > packet_len)
    {
        numberOfPayloads = packet_len;
    }

    packet[index][0] = headers[0] >> 20;
    packet[index][1] = (headers[0] >> 12) & 0x00FF;
    packet[index][2] = (headers[0] >> 4) & 0x0000FF;
    packet[index][3] = ((headers[0] << 28) >> 24) | (headers[1] >> 24);
    packet[index][4] = (headers[1] << 8) >> 24;
    packet[index][5] = (headers[1] << 16) >> 24;
    packet[index][6] = (headers[1] << 24) >> 24;
    packet[index][7] = (headers[2] << 4) | (headers[3]);
    packet[index][8] = (headers[4] >> 6);
    packet[index][9] = ((headers[4] << 26) >> 24) | (headers[5] >> 12);
    packet[index][10] = (headers[5] << 20) >> 24;
    packet[index][11] = ((headers[5] << 28) >> 24) | (headers[6] >> 1);
    packet[index][12] = ((headers[6] << 31) >> 31);
    packet[index][15] = (headers[8] << 6) | headers[9];
    
}

void fillPacket(unsigned char packet[], int array[], int numberOfElements, int startIndex)
{
    int arrayIndex = 16;
    for(int i = startIndex; i < (startIndex + numberOfElements); i++)
    { 
       packet[arrayIndex] = array[i] >> 24;
       arrayIndex++;
       packet[arrayIndex] = (array[i] << 8) >> 24;
       arrayIndex++;
       packet[arrayIndex] = (array[i] << 16) >> 24;
       arrayIndex++;
       packet[arrayIndex] = (array[i] << 24) >> 24;
       arrayIndex++; 
    }
    
    unsigned int checkSum = compute_checksum_sf(packet);

    packet[12] = (packet[12] << 7);
    packet[12] = packet[12] | (checkSum >> 16);
    packet[13] = ((checkSum << 16) >> 24);
    packet[14] = ((checkSum) << 24) >> 24;
}




