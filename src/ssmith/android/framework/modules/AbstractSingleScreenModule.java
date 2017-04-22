package ssmith.android.framework.modules;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.MyEvent;
import android.view.MotionEvent;

import com.scs.worldcrafter.Statics;

public abstract class AbstractSingleScreenModule extends AbstractModule {

	private static final long PAUSE_DURATION = 500;

	private long start_time;

	public AbstractSingleScreenModule(AbstractActivity act, AbstractModule _return_to) {
		super(act, _return_to);

		start_time = System.currentTimeMillis();
		
		stat_cam.lookAt(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT/2, true);
	}

	
	@Override
	public boolean processEvent(MyEvent evt) throws Exception {
		if (System.currentTimeMillis() > start_time + PAUSE_DURATION) {
			if (evt.getAction() == MotionEvent.ACTION_UP) {
				returnTo();
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
	
	
	@Override
	public boolean onBackPressed() {
		returnTo();
		return true;
	}


}
