package com.haloproject.projectspartanv2;

import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Constants
{
    private static  String audioDeviceUuid;
    private static final String configFileName = "suitConfig";
    private static AtomicBoolean isInitialised = new AtomicBoolean();

    public static final void initializeConstants()
    {
        File sdcard = Environment.getExternalStorageDirectory();
        File settingsFile = new File(sdcard,"Pictures"+File.separator+configFileName);
        try
        {
            BufferedReader fileContentsReader = new BufferedReader(new FileReader(settingsFile));
            StringBuilder text = new StringBuilder();

            String line;
            while ((line = fileContentsReader.readLine()) != null) {
                text.append(line);
            }
            fileContentsReader.close();

            JSONObject jsonObject = new JSONObject(text.toString());
            audioDeviceUuid = jsonObject.getString("uuid");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (JSONException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static String getAudioDeviceUuid()
    {
        if(isInitialised.get())
        {
            return audioDeviceUuid;
        }
        else
        {
            initializeConstants();
            isInitialised.set(true);
            return audioDeviceUuid;
        }
    }
}