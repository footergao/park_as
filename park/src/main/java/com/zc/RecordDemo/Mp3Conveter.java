package com.zc.RecordDemo;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.cordova.LOG;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.hdzx.tenement.ui.common.RecordActivity;
import com.hdzx.tenement.utils.Logger;


/**
 * 7/4/14  1:20 PM
 * Created by JustinZhang.
 */
public class Mp3Conveter {
    public static final int NUM_CHANNELS = 1;
    public static final int SAMPLE_RATE = 16000;
    public static final int BITRATE = 8;
    public static final int MODE = 1;
    public static final int QUALITY = 5;
    private static Context context;

    public native void initEncoder(int numChannels, int sampleRate, int bitRate, int mode, int quality);

    public native void destroyEncoder();

    public native void encodeFile(String sourcePath, String targetPath);

    static {
    	context=RecordActivity.context;
        File dir = context.getDir("lib", Context.MODE_PRIVATE);  
        File soFile = new File(dir, "libmp3lame.so");  
        assetToFile(context, "libmp3lame.so", soFile);  
        
        try {  
            System.load(soFile.getAbsolutePath());  
        } catch (Exception e) {  
        }  
        
        Log.v("gl", " System.load");
    }

    public Mp3Conveter() {

    	Log.v("gl", "Mp3Conveter");
        initEncoder(NUM_CHANNELS, SAMPLE_RATE, BITRATE, MODE, QUALITY);

    }
    
    public static void assetToFile(Context context, String name, File file) {  
        AssetManager assetManager = context.getAssets();  
  
        InputStream is;  
        try {  
            is = assetManager.open(name);  
            java.io.ByteArrayOutputStream bout = new java.io.ByteArrayOutputStream();  
  
            byte[] bufferByte = new byte[1024];  
            int l = -1;  
            while ((l = is.read(bufferByte)) > -1) {  
                bout.write(bufferByte, 0, l);  
            }  
            byte[] rBytes = bout.toByteArray();  
            bout.close();  
            is.close();  
  
            if (!file.exists()) {  
                file.createNewFile();  
            }  
  
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(  
                    file));  
            dos.write(rBytes);  
            dos.flush();  
            dos.close(); 
  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (OutOfMemoryError e) {  
            e.printStackTrace();  
        }  
    }  
}
