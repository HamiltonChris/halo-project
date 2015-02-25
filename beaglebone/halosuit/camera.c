/*
    camera.c
*/
#include <unistd.h>
#include <sys/types.h>

#include <halosuit/camera.h>

static pid_t child_pid;

static is_streaming = false;

void camera_startStream()
{
    if (!is_streaming) {
        is_streaming = true;
        child_pid = fork();

        if (child_pid == 0) {
            exec(WEBCAM_PROGRAM_PATH);
            printf("error occured in exec\n");
            abort();
        }
    }
}

void camera_endStream()
{
    if (is_streaming) {
        is_streaming = false;
        
        if(kill(child_pid, SIGTERM)) {
            printf("error occured in kill\n");
        }
    }
}
