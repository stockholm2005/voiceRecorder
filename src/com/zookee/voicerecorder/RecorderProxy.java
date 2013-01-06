/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package com.zookee.voicerecorder;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.util.Log;
import org.appcelerator.titanium.proxy.ActivityProxy;
import android.app.Activity;
import android.media.MediaRecorder;
import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

// This proxy can be created by calling Voicerecorder.createExample({message: "hello world"})
@Kroll.proxy(creatableInModule=VoicerecorderModule.class)
public class RecorderProxy extends ActivityProxy 
	implements MediaRecorder.OnInfoListener,MediaPlayer.OnCompletionListener
{
	// Standard Debugging variables
	private static final String LCAT = "ExampleProxy";
    int currertindex = 0;
    
    private MediaRecorder mediarecorder;
    private MediaPlayer mPlayer = null;
	private KrollFunction maxDurationReachedCB = null;
	private KrollFunction playCompleteCB = null;
    
    String filePath="";
    private int maxDuration = 10000; 

	// Constructor
	public RecorderProxy()
	{
		super();
	}

	// Handle creation options
	@Override
	public void handleCreationDict(KrollDict options)
	{
		super.handleCreationDict(options);
	}
	
	// Methods
	@Kroll.method
	public void startRecord(HashMap args){
		Object callback;
				
		// Save the callback functions, verifying that they are of the correct type
		if (args.containsKey("durationReached")) {
			callback = args.get("durationReached");
			if (callback instanceof KrollFunction) {
				maxDurationReachedCB = (KrollFunction)callback;
			}
		}
		 try {
             mediarecorder = new MediaRecorder();
             mediarecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
             mediarecorder
                     .setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
             mediarecorder
                     .setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
             mediarecorder.setMaxDuration(maxDuration);
             mediarecorder.setOnInfoListener(this);

             Log.d("voicerecorder",filePath);
             mediarecorder.setOutputFile(filePath);
              mediarecorder.prepare();
             mediarecorder.start();
         } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
	}
	
	@Kroll.method
	public void stopRecord(){
		try{
			if(mediarecorder!=null){
				mediarecorder.stop();
				mediarecorder.release();
				mediarecorder = null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Kroll.method
    public void startPlaying(HashMap args) {
		Object callback;
		
		// Save the callback functions, verifying that they are of the correct type
		if (args.containsKey("playCompleted")) {
			callback = args.get("playCompleted");
			if (callback instanceof KrollFunction) {
				playCompleteCB = (KrollFunction)callback;
			}
		}
		
        mPlayer.start();
    }
	
	@Kroll.method
	public void pause(){
		try{
			mPlayer.pause();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Kroll.method
    public void stopPlaying() {
		if(mPlayer!=null){
			try{
			mPlayer.stop();
			mPlayer.reset();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
    }
	
	@Kroll.setProperty
	public void setRecordFile(String _fileName)
	{
		if(mPlayer == null){
			mPlayer = new MediaPlayer();
		}
		filePath=_fileName;
        try {
            mPlayer.setDataSource(filePath);
            mPlayer.setOnCompletionListener(this);
            mPlayer.prepare();
        } catch (IOException e) {
        		e.printStackTrace();
        }
	}

	@Kroll.setProperty
	public void setMaxDuration(int _max){
		maxDuration = _max;
	}
	@Override
	public void onInfo(MediaRecorder mr, int arg1, int arg2) {
		// TODO Auto-generated method stub
		if (arg1 == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
	         mr.stop();
	         mr.release();
	         mr = null;
	         HashMap<String, String> event = new HashMap<String, String>();
			event.put("message", "message");
			event.put("title", "title");
			maxDurationReachedCB.call(getKrollObject(), event);
	      }
	}

	@Override
	public void onCompletion(MediaPlayer mr) {
		//don't reset() here, reset will set the mr to idel state, then you
		//have to reset the data source, and prepare() again.refer to:
		//http://developer.android.com/images/mediaplayer_state_diagram.gif
		//mr.reset();
		// TODO Auto-generated method stub
		HashMap<String, String> event = new HashMap<String, String>();
		event.put("message", "message");
		event.put("title", "title");
		playCompleteCB.call(getKrollObject(), event);
	}
}