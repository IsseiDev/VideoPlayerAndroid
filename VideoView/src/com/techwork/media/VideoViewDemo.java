/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.techwork.media;


import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoViewDemo extends Activity   {
	private static final String TAG = "VideoViewDemo";

	private VideoView mVideoView;
//	private EditText mPath;
	private Handler mHandle = new Handler();
	private TextView title;
	private ImageButton mPlay;
	private ImageButton mNext;
	private ImageButton mPrev;
	private ImageButton mStop;
	private ImageButton mBack;
	private ImageButton mForw;
	private ImageButton mLoop;
	private SeekBar seekBar;
	private ArrayList<String> titles;
	private String current;
	private int currentMedia = 0;
	@SuppressLint("SdCardPath")
	String pref = "/sdcard/Video/video";
	String post = ".mp4";
	String dir;
	private boolean isLoop=false;
	
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.play_music_layout);
		
		mVideoView = (VideoView) findViewById(R.id.videoView1);
		//mPath = (EditText) findViewById(R.id.path_video);
		
		
		currentMedia = (int) (Math.random()*5);
		title = (TextView) findViewById(R.id.songName);

		dir = new String (pref + currentMedia + post);
		//mPath.setText(pref+currentMedia+post);
		
		mPlay = (ImageButton) findViewById(R.id.btPlay);
		mStop = (ImageButton) findViewById(R.id.btStop);
		mNext = (ImageButton) findViewById(R.id.btNext);
		mPrev = (ImageButton) findViewById(R.id.btPrevious);
		mBack = (ImageButton) findViewById(R.id.btBackward);
		mForw = (ImageButton) findViewById(R.id.btForward);
		mLoop = (ImageButton) findViewById(R.id.btRepeat);
		//seekBar = (SeekBar) findViewById(R.id.songProgressBar);
		titles = new ArrayList<String>();
		titles.add(0, new String("Google musical"));
		titles.add(1, new String("Twitter musical"));
		titles.add(2, new String("Frozen musical"));
		titles.add(3, new String("Princess musical"));
		titles.add(4, new String("Facebook musical"));
		title.setText(titles.get(currentMedia));
		
		//seekBar.setOnSeekBarChangeListener((OnSeekBarChangeListener) this);
		mPlay.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				playVideo();
			}
		});
		mNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				playNextMedia();
				
			}
		});
		mPrev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				playPrevMedia();
			}
		});

		mStop.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				//seekBar.setProgress(0);
				if (mVideoView != null) {
					current = null;
					mVideoView.stopPlayback();
				}
			}
		});
		mForw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int curPost = mVideoView.getCurrentPosition();
				if(curPost + 5000 <= mVideoView.getDuration())
					mVideoView.seekTo(curPost+5000);
				else
					mVideoView.seekTo(mVideoView.getDuration());
			}
		});
		mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int curPost = mVideoView.getCurrentPosition();
				if(curPost - 5000 >= 0)
					mVideoView.seekTo(curPost - 5000);
				else
					mVideoView.seekTo(0);
			}
		});
		mLoop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isLoop)
				{
					isLoop = false;
					mLoop.setImageResource(R.drawable.icon_repeat);
					mLoop.setBackgroundResource(R.drawable.icon_repeat);
				}
				else{
					isLoop = true;
					mLoop.setImageResource(R.drawable.icon_repeat_all);
					mLoop.setBackgroundResource(R.drawable.icon_repeat_all);
				}
				
			}
		});
		
		runOnUiThread(new Runnable(){
			public void run() {
				playVideo();
			}
			
		});
	}

	private void playVideo() {
		try {
			//final String path = mPath.getText().toString();
			final String path = dir;
			Log.v(TAG, "path: " + path);
			if (path == null || path.length() == 0) {
				Toast.makeText(VideoViewDemo.this, "File URL/path is empty",
						Toast.LENGTH_LONG).show();

			} else {
				// If the path has not changed, just start the media player
				if (path.equals(current) && mVideoView != null) {
					mVideoView.start();
					mVideoView.requestFocus();
					return;
				}
				//current = path;
				mVideoView.setVideoPath(path);
				mVideoView.start();
				mVideoView.requestFocus();

			}
		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
			if (mVideoView != null) {
				mVideoView.stopPlayback();
			}
		}
	}
	
	private void playNextMedia(){
		if(currentMedia ==4){
			currentMedia = 0;
		}
		else{
			currentMedia += 1;
		}
		dir = pref + currentMedia + post;
		title.setText(titles.get(currentMedia));
		playVideo();
	}
	private void playPrevMedia(){
		if(currentMedia==0)
			currentMedia = 4;
		else
			currentMedia -=1;
		dir = pref + currentMedia + post;
		title.setText(titles.get(currentMedia));
		playVideo();
	}
/*
	public void updateSeekBar(){
		mHandle.postDelayed(upateTimeTask, 100);
	}
	public Runnable upateTimeTask = new Runnable() {
		
		@Override
		public void run() {
			seekBar.setProgress(100* (mVideoView.getCurrentPosition()/mVideoView.getDuration()));
			
			mHandle.postDelayed(this, 100);
		}
	};
*/	
	
	
	
}
