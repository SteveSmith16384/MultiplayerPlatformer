package ssmith.android.framework;

import ssmith.audio.MP3Player;

import com.scs.multiplayerplatformer.MainThread;
import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.XMLHelper;
import com.scs.multiplayerplatformer.sfx.ISfxPlayer;
import com.scs.multiplayerplatformer.sfx.StdSfxPlayer;


public abstract class AbstractActivity implements Thread.UncaughtExceptionHandler {

	public static MainThread thread; // thread must be here as this is the only constant class
	public static ISfxPlayer sound_manager;
	private MP3Player mp3Music;
	private XMLHelper xml;

	public AbstractActivity() {
		super();

	}


	public void onCreate() {
		Statics.init(this);
		Statics.act = this;

		Thread.setDefaultUncaughtExceptionHandler(this);

		xml = new XMLHelper();
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
			sound_manager = new StdSfxPlayer("sfx");
		}

		mp3Music = new MP3Player("music/Venus.mp3", true);
		mp3Music.start();

	}
	
	
	public static void HandleError(AbstractActivity act, Throwable ex) {
		HandleError(ex);
	}

	
	public static void HandleError(Throwable ex) {
		try {
			ex.printStackTrace();
		} catch (Exception ex2) {
			ex2.printStackTrace();
		}
	}


	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		HandleError(null, ex);
	}


	public String getString(String id) {
		return xml.getString(id);
	}


	public String getString(String id, String s) {
		String newstr = xml.getString(id);
		newstr = newstr.replaceAll("%", s);
		return newstr;
	}
	
/*

	public void startMusic() {
	}


	public void pauseMusic() {
	}


	public void resumeMusic() {
	}
*/

}
