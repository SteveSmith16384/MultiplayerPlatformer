package ssmith.android.media;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnSeekCompleteListener;

public class MP3Player {

	private MediaPlayer mediaPlayer;
	private File curr_file;

	public void loadMP3(Context c, int r) throws IllegalArgumentException, IllegalStateException, IOException {
		mediaPlayer = MediaPlayer.create(c, r);
		mediaPlayer.setVolume(.5f, 0.5f);
        //mPlayer.start();
	}
	
	
	public void loadMP3(File tempMp3, OnErrorListener err, OnCompletionListener listener1, OnInfoListener listener2, OnSeekCompleteListener listener3) throws IllegalArgumentException, IllegalStateException, IOException {
		curr_file = tempMp3;

		// Tried reusing instance of media player
		// but that resulted in system crashes...  
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnErrorListener(err);
		mediaPlayer.setOnCompletionListener(listener1);
		mediaPlayer.setOnInfoListener(listener2);
		mediaPlayer.setOnSeekCompleteListener(listener3);

		// Tried passing path directly, but kept getting 
		// "Prepare failed.: status=0x1"
		// so using file descriptor instead
		FileInputStream fis = new FileInputStream(tempMp3);
		mediaPlayer.setDataSource(fis.getFD());
		mediaPlayer.prepare();

	}
	
	
	public void play() throws IllegalStateException, IOException {
		mediaPlayer.seekTo(0);
		mediaPlayer.start();
		
	}
	

	public void play(int pos_ms) throws IllegalStateException, IOException {
		//mediaPlayer.prepare();
		mediaPlayer.seekTo(pos_ms);
		mediaPlayer.start();
		
	}
	

	public void stop() {
		mediaPlayer.pause();
	}
	
	
	public int getPosMS() {
		return mediaPlayer.getCurrentPosition();
	}

	
	public void jumpTo(int ms) {
		mediaPlayer.seekTo(ms);
	}
	
	
	public void jumpBy(int ms) {
		mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + ms);
	}
	
	
	public void pause() {
		if (mediaPlayer != null) {
			mediaPlayer.pause();
		}
	}


	public void resume() {
		if (mediaPlayer != null) {
			mediaPlayer.start();
		}
	}


	public void restart() {
		mediaPlayer.seekTo(0);
	}


	public void toEnd() {
		mediaPlayer.seekTo(mediaPlayer.getDuration()-1);
	}
	
	
	public int getDuration() {
		return mediaPlayer.getDuration();
	}


	public void release() {
		mediaPlayer.release();
	}
	
	
	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	
	public File getCurrentFile() {
		return this.curr_file;
	}

}

