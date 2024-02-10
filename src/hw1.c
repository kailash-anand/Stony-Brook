#include "hw1.h"
#include <math.h>

void printHeader(int switchId, const char headerName[], unsigned char packet[]);

void printPayload(unsigned char packet[], int index);

void print_packet_sf(unsigned char packet[])
{
    int index = 16;
    const int NO_OF_HEADERS = 10;
    const int HEADER_SIZES[] = {28,28,4,4,14,14,5,23,2,6};
    const char *HEADER_NAMES[] = {"Source Address" , "Destination Address", "Source Port",
                                       "Destination Port", "Fragment Offset", "Packet Length",
                                       "Maximum Hop Count", "Checksum", "Compression Scheme",
                                       "Traffic Class"};
    for(int i = 0; i < NO_OF_HEADERS; i++)
    {
        printHeader((i + 1), HEADER_NAMES[i], &packet[0]);
    }

    printf("Payload:");
    for(int i = 0; i < 3; i++)
    {
        printPayload(&packet[0], index);
        index += 4;
    }
}

unsigned int compute_checksum_sf(unsigned char packet[])
{
    (void)packet;
    return -1;
}

unsigned int reconstruct_array_sf(unsigned char *packets[], unsigned int packets_len, int *array, unsigned int array_len) {
    (void)packets;
    (void)packets_len;
    (void)array;
    (void)array_len;
    return -1;
}

unsigned int packetize_array_sf(int *array, unsigned int array_len, unsigned char *packets[], unsigned int packets_len,
                          unsigned int max_payload, unsigned int src_addr, unsigned int dest_addr,
                          unsigned int src_port, unsigned int dest_port, unsigned int maximum_hop_count,
                          unsigned int compression_scheme, unsigned int traffic_class)
{
    (void)array;
    (void)array_len;
    (void)packets;
    (void)packets_len;
    (void)max_payload;
    (void)src_addr;
    (void)dest_addr;
    (void)src_port;
    (void)dest_port;
    (void)maximum_hop_count;
    (void)compression_scheme;
    (void)traffic_class;
    return -1;
}

void printHeader(int switchId, const char headerName[], unsigned char packet[])
{
    unsigned int decoded = 0;
    switch(switchId)
    {
        case 1:
            decoded = (packet[0] << 20) | (packet[1] << 12) | (packet[2] << 4) | (packet[3] >> 4);
            break;

        case 2:
            decoded = (packet[3] & 0b00001111) | (packet[4] << 16) | (packet[5] << 8) | packet[6];
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
    printf("%s: %d\n",headerName , decoded);
}

void printPayload(unsigned char packet[], int index)
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
    printf(" %d", payload);
}




