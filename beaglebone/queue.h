/*
	queue.h
	a implementation of a circular queue that can enqueue and dequeue
*/
#ifndef QUEUE
#define QUEUE

// struct that holds the queue and all relative information
struct circularQueue;
typedef struct circularQueue Queue;

// initializes a circularQueue's array, positions indexs, mutex and semaphore
Queue Queue_create();

// adds a message to the back of the queue if full message is dropped
void Queue_enqueue(Queue* jobQueue, const char* element);

// takes the first element of the queue if empty thread sleeps
void Queue_dequeue(Queue* jobQueue, char* element);

#endif
