package suit.halo.suitcontrolleraudiotospeaker;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;


public class MainActivity extends Activity
{
    private int sampleRate = 16000;
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
    private volatile Thread  handleVoiceConnectionsThread,handleNextConnectionThread;
    private BluetoothSocket bluetoothSocket;
    private volatile int choice;
    private TextView tv;
    private static int count = 1;

    AudioTrack player;

    public void sendLow2AhTempWarning(View view)
    {
        choice = 1;
    }

    public void sendHighTempWarning(View view)
    {
        choice = 2;
    }

    private class HandleVoiceConnections extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                while(true)
                {
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    bluetoothAdapter.enable();
                    BluetoothServerSocket bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("SuitControllerAudio", UUID.fromString("393b63ac-9a0d-4331-8670-c8b53b298af7"));
                    synchronized (this)
                    {
                        bluetoothSocket = bluetoothServerSocket.accept();
                    }


                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("head temperature", "42.0");
                    jsonObject.put("water temperature", "33.0");
                    jsonObject.put("crotch temperature", "22.0");
                    jsonObject.put("armpits temperature", "36.0");
                    jsonObject.put("phone battery", "88");
                    jsonObject.put("hud battery", "77");
                    jsonObject.put("2 AH battery", "66");
                    jsonObject.put("8 AH battery", "99");
                    bluetoothSocket.getOutputStream().write(jsonObject.toString().getBytes());

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            tv.setText("Connected: "+count);
                            count++;
                            tv.invalidate();
                        }
                    });
                    new HandleNextConnection().run();
                }



            }
            catch (Exception e)
            {
                int x = 1;
            }

        }
    }

    private class HandleNextConnection extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                while(true)
                {
                    switch (choice)
                    {
                        case 1:
                        {
                            JSONObject jsonObject = new JSONObject();
                            JSONObject warningsObject = new JSONObject();
                            warningsObject.put("low 2AH battery warning", "");
                            jsonObject.putOpt("warnings",warningsObject);
                            choice = 0;
                            bluetoothSocket.getOutputStream().write(jsonObject.toString().getBytes());
                            break;
                        }
                        case 2:
                        {
                            JSONObject jsonObject = new JSONObject();
                            JSONObject warningsObject = new JSONObject();
                            warningsObject.put("high water temperature", "");
                            jsonObject.putOpt("warnings",warningsObject);
                            choice = 0;
                            bluetoothSocket.getOutputStream().write(jsonObject.toString().getBytes());
                            break;
                        }
                        default:
                            break;
                    }
                }
            }
            catch (IOException e)
            {
                int x = 1;
            }
            catch (JSONException j)
            {
                int x = 1;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Constants.initializeConstants();
        tv  = (TextView)findViewById(R.id.mainTextView);
        handleNextConnectionThread = new HandleVoiceConnections();
        handleNextConnectionThread.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
