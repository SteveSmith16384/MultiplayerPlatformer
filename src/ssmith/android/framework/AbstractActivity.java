package ssmith.android.framework;

import javax.swing.JOptionPane;

import ssmith.audio.MP3Player;

import com.scs.multiplayerplatformer.MainThread;
import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.XMLHelper;
import com.scs.multiplayerplatformer.sfx.ISfxPlayer;
import com.scs.multiplayerplatformer.sfx.StdSfxPlayer;


public abstract class AbstractActivity implements Thread.UncaughtExceptionHandler {

	public static MainThread thread; // thread must be here as this is the only constant class
	public static ISfxPlayer soundManager;
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

		if (soundManager == null) {
			soundManager = new StdSfxPlayer("assets/sfx/");
			soundManager.playSound("Venus.ogg");
		}

		//mp3Music = new MP3Player("assets/music/Venus.ogg", true);
		//mp3Music.start();

	}
	
	
	public static void handleError(AbstractActivity act, Throwable ex) {
		handleError(ex);
	}

	
	public static void handleError(Throwable ex) {
		try {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage() + ".", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex2) {
			ex2.printStackTrace();
		}
	}


	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		handleError(null, ex);
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
