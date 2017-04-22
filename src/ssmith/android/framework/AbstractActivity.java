package ssmith.android.framework;

import ssmith.android.framework.ErrorReporter;

import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.media.MP3Player;
import ssmith.android.media.SoundManager;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.MainThread;
import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.start.AppRater;
import com.scs.worldcrafter.start.ErrorModule;


public abstract class AbstractActivity extends Activity implements Thread.UncaughtExceptionHandler {

	public static MainThread thread; // thread must be here as this is the only constant class
	public static SoundManager sound_manager;
	public static MP3Player mp3_player;

	public AbstractActivity() {
		super();

	}


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Statics.act = this;

		// remove title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// remove status bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Thread.setDefaultUncaughtExceptionHandler(this);

		try {
			Statics.VERSION_NAME = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
			Statics.NAME = getResources().getString(R.string.app_name);
		} catch (NameNotFoundException e) {
			HandleError(null, e);
		}

		// tell system to use the layout defined in our XML file
		setContentView(R.layout.lunar_layout);

		if (thread != null) {
			if (thread.isAlive() == false) {
				thread = null;
			}
		}
		if (thread == null) {
			thread = new MainThread();//getHolder(), super.getContext(), this);
			thread.setRunning(true);
			thread.start();
		}

		if (sound_manager == null) {
			sound_manager = new SoundManager(this.getBaseContext());
		}

		if (savedInstanceState == null) {
			// we were just launched: set up a new game
			//Log.w(this.getClass().getName(), "SIS is null");
		} else {
			// we are being restored: resume a previous game
			//Log.w(this.getClass().getName(), "SIS is nonnull");
		}

		try {
			if (Statics.ANDROID_MARKET) {
				AppRater.app_launched(this);
			}
		} catch (Exception ex) {
			ErrorReporter.getInstance().handleSilentException(ex);
		}

		//new SendEventThread(Statics.EV_OPENED);

	}


	/**
	 * Invoked when the Activity loses user focus.
	 */
	@Override
	protected void onPause() {
		AbstractActivity.Log("Activity.onPause...");
		super.onPause();
		thread.setPause(true);
		pauseMusic();
	}


	/**
	 * 
	 */
	@Override
	protected void onStart() {
		AbstractActivity.Log("Activity.onStart...");
		super.onStart();
	}


	/**
	 * Invoked when the Activity loses user focus.
	 */
	@Override
	protected void onResume() {
		AbstractActivity.Log("Activity.onResume...");
		super.onResume();
		thread.setPause(false);
		if (thread.current_module != null) {
			//if (thread.current_module.playMusic()) {
				resumeMusic();
			//}
		}
	}


	@Override
	protected void onStop() {
		AbstractActivity.Log("Activity.onStop...");
		super.onStop();
		thread.setPause(true);
		pauseMusic();
	}


	@Override
	protected void onDestroy() {
		AbstractActivity.Log("Activity.onDestroy...");
		super.onDestroy();
	}


	public static void Log(String text) {
		if (Statics.RELEASE_MODE == false) {
			Log.d(Statics.NAME, text);
		}
	}


	public static void HandleError(AbstractActivity act, Throwable ex) {
		try {
			ex.printStackTrace();
			if (Statics.RELEASE_MODE == false) {
				Log.e(Statics.NAME, ex.toString());
			}
			ErrorReporter.getInstance().handleSilentException(ex);
			if (act != null) {
				AbstractModule m = Statics.GetStartupModule(act);
				AbstractActivity.thread.setNextModule(new ErrorModule(act, m, ex));
			}
		} catch (Exception ex2) {
			ex2.printStackTrace();
		}
	}


	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		HandleError(null, ex);
	}


	@Override
	public void onBackPressed() {
		if (thread != null) {
			boolean res = thread.onBackPressed();
			if (res == false) {
				super.onBackPressed();
			}
		}
	}


	public String getString(int R, String... params) {
		String orig = getResources().getString(R);
		for (int i=0 ; i<params.length ; i++) {
			orig = orig.replaceAll("%", params[i]);
		}
		return orig;
	}


	public void startMusic() {
		try {
			if (Statics.cfg.mute == false) {
				mp3_player = new MP3Player();
				mp3_player.loadMP3(Statics.act, Statics.MUSIC_R);
				mp3_player.play();
			}
		} catch (Exception ex) {
			ErrorReporter.getInstance().handleSilentException(ex);
		}
	}


	public void pauseMusic() {
		try {
			if (mp3_player != null) {
				mp3_player.pause();
			}
		} catch (Exception ex) {
			ErrorReporter.getInstance().handleSilentException(ex);
		}
	}


	public void resumeMusic() {
		try {
			if (Statics.cfg.mute == false) {
				if (mp3_player != null) {
					mp3_player.resume();
				} else {
					startMusic();
				}
			}
		} catch (Exception ex) {
			ErrorReporter.getInstance().handleSilentException(ex);
		}
	}


	public boolean isPlaying() {
		if (mp3_player != null) {
			return mp3_player.isPlaying();
		}
		return false;
	}
	
	
}
