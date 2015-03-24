package com.haloproject.projectspartanv2;

import android.bluetooth.BluetoothSocket;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class HandleNextConnection extends Thread
{
    private int sampleRate = 8000;
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
    AudioTrack player;
    AtomicInteger currentNum;
    BluetoothSocket bluetoothSocket;
    int currentNumLocal;

    public HandleNextConnection(BluetoothSocket bluetoothSocket,AtomicInteger currentNum)
    {
        this.bluetoothSocket = bluetoothSocket;
        this.currentNum = currentNum;
        currentNumLocal = currentNum.get();
    }

    @Override
    public void run()
    {
        try
        {
            byte[] buffer = new byte[minBufSize];
            player = new AudioTrack(AudioTrack.MODE_STREAM, sampleRate, AudioFormat.CHANNEL_OUT_MONO, audioFormat, minBufSize, AudioTrack.MODE_STREAM);
            player.setVolume(AudioTrack.getMaxVolume());
            player.play();

            while (currentNumLocal == currentNum.get())
            {
                int bytesRead = bluetoothSocket.getInputStream().read(buffer);
                player.write(buffer, 0, bytesRead);
            }
            player.stop();
        } catch (IOException e)
        {
            int x = 1;
        }
    }
}
