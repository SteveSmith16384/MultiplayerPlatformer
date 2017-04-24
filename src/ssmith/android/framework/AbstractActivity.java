package ssmith.android.framework;

import ssmith.android.compatibility.Activity;
import ssmith.android.compatibility.Bundle;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.audio.MP3Player;
import ssmith.audio.SoundCacheThread;
import sun.rmi.runtime.Log;

import com.scs.ninja.XMLHelper;
import com.scs.worldcrafter.MainThread;
import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.start.ErrorModule;


public abstract class AbstractActivity extends Activity implements Thread.UncaughtExceptionHandler {

	public static MainThread thread; // thread must be here as this is the only constant class
	public static SoundCacheThread sound_manager;
	public static MP3Player mp3_player;
	private XMLHelper xml;

	public AbstractActivity() {
		super();

	}


	public void onCreate(Bundle savedInstanceState) {
		Statics.init(this);
		Statics.act = this;

		Thread.setDefaultUncaughtExceptionHandler(this);

		/*try {
			Statics.VERSION_NAME = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
			Statics.NAME = getResources().getString(R.string.app_name);
		} catch (NameNotFoundException e) {
			HandleError(null, e);
		}
		 */
		// tell system to use the layout defined in our XML file
		//setContentView(R.layout.lunar_layout);

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
			sound_manager = new SoundCacheThread("sfx");
		}

	}
	
	
	public static void HandleError(AbstractActivity act, Throwable ex) {
		HandleError(ex);
	}

	
	public static void HandleError(Throwable ex) {
		try {
			ex.printStackTrace();
			ErrorReporter.getInstance().handleSilentException(ex);
			AbstractModule m = Statics.GetStartupModule(Statics.act);
			AbstractActivity.thread.setNextModule(new ErrorModule(Statics.act, m, ex));
		} catch (Exception ex2) {
			ex2.printStackTrace();
		}
	}


	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		HandleError(null, ex);
	}


	/*@Override
	public void onBackPressed() {
		if (thread != null) {
			boolean res = thread.onBackPressed();
			if (res == false) {
				super.onBackPressed();
			}
		}
	}*/


	public String getString(String id) {
		return xml.getString(id);
	}


	public String getString(String id, String s) {
		String newstr = xml.getString(id);
		newstr = newstr.replaceAll("%", s);
		return newstr;
	}


	public void startMusic() {
	}


	public void pauseMusic() {
	}


	public void resumeMusic() {
	}


}
