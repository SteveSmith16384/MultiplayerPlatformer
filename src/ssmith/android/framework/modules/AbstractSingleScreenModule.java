package ssmith.android.framework.modules;

import ssmith.android.compatibility.MotionEvent;
import ssmith.android.framework.MyEvent;

import com.scs.multiplayerplatformer.Statics;

public abstract class AbstractSingleScreenModule extends AbstractModule {

	private static final long PAUSE_DURATION = 500;

	private long startTime;

	public AbstractSingleScreenModule() {
		super();

		startTime = System.currentTimeMillis();
		
		statCam.lookAt(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT/2, true);
	}

	
	@Override
	public boolean processEvent(MyEvent evt) throws Exception {
		if (System.currentTimeMillis() > startTime + PAUSE_DURATION) {
			if (evt.getAction() == MotionEvent.ACTION_UP) {
				this.onBackPressed();
				return true;
			}
		}
		return false;
	}

	
	/*private void returnTo() {
		//view.sound_manager.playSound(R.raw.beep);
		super.getThread().setNextModule(this.return_to);
	}
*/
	
	@Override
	public void updateGame(long interpol) {
		// Do nothing

	}
	
	
	/*@Override
	public boolean onBackPressed() {
		//returnTo();
		return true;
	}
*/

}
