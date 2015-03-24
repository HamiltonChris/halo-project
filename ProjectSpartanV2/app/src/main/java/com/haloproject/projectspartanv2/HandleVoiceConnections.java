package com.haloproject.projectspartanv2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class HandleVoiceConnections extends Thread
{
    private volatile Thread currentThread;

    @Override
    public void run()
    {
        try
        {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothAdapter.enable();
            BluetoothServerSocket bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("SuitControllerAudio", UUID.fromString(Constants.getAudioDeviceUuid()));
            AtomicInteger currentNum = new AtomicInteger(0);
            while (true)
            {
                BluetoothSocket bluetoothSocket = bluetoothServerSocket.accept();
                currentNum.addAndGet(1);
                currentThread = new HandleNextConnection(bluetoothSocket, currentNum);
                currentThread.start();
            }
        } catch (Exception e)
        {
            int x = 1;
        }

    }
}

