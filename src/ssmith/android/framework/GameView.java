package ssmith.android.framework;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.scs.worldcrafter.Statics;

/**
 * This gets recreated when the game is re-opened!
 *
 */
public final class GameView extends SurfaceView implements SurfaceHolder.Callback {//, ICanvas {

	//public MainThread thread; // thread
	public SurfaceHolder holder;
	//public SoundManager sound_manager;

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// register our interest in hearing about changes to our surface
		holder = super.getHolder();
		holder.addCallback(this);

		setFocusable(true); // make sure we get key events

		//sound_manager = new SoundManager(context);

	}


	public SurfaceHolder getHolder() {
		return this.holder;
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent msg) {
	    /*if (keyCode == KeyEvent.KEYCODE_BACK) {
	        AbstractActivity.Log("Back button pressed");
	    }*/
		if (AbstractActivity.thread != null) {
			return AbstractActivity.thread.onKeyDown(keyCode, msg);
		}
		return false;
	}


	@Override
	public boolean onKeyUp(int keyCode, KeyEvent msg) {
		if (AbstractActivity.thread != null) {
			return AbstractActivity.thread.onKeyUp(keyCode, msg);
		}
		return false;
	}


	/**
	 * Standard window-focus override. Notice focus lost so we can pause on
	 * focus lost. e.g. user switches to take a call.
	 */
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		if (AbstractActivity.thread != null) {
			AbstractActivity.thread.setPause(!hasWindowFocus);
		}
	}


	/* Callback invoked when the surface dimensions change. */
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// synchronized to make sure these all change atomically
		synchronized (holder) {
			Statics.init(width, height, this.getContext());
		}
	}


	/*
	 * Callback invoked when the Surface has been created and is ready to be
	 * used.
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		// Attach ourselves to the thread
		AbstractActivity.thread.setSurfaceHolder(holder);
		//AbstractActivity.thread.setPause(false);
	}


	/*
	 * Callback invoked when the Surface has been destroyed and must no longer
	 * be touched. WARNING: after this method returns, the Surface/Canvas must
	 * never be touched again!
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		// we have to tell thread to shut down & wait for it to finish, or else
		// it might touch the Surface after we return and explode
		/*boolean retry = true;
		thread.setRunning(false);
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// Do nothing
			}
		}*/
		AbstractActivity.thread.setPause(true);
	}


	/**
	 * 0, 2, 2, 2, 2, 2, 2, 1
	 * 0 = DOWN
	 * 2 = MOVE
	 * 1 = UP
	 */
	public boolean onTouchEvent(MotionEvent event) {
		if (AbstractActivity.thread != null) {
			final int pointerCount = event.getPointerCount();
			/*if (pointerCount > 1) {
				Log.d(Statics.NAME, "Action: " + event.getAction() + " - Coords: " + event.getX() + ", " + event.getY());
			}*/
			for (int p = 0; p < pointerCount; p++) {
				int action = event.getAction() & MotionEvent.ACTION_MASK;
				//int id = event.getPointerId(p);

				if (event.getActionIndex() == p) {
					Log.d(Statics.NAME, "Action: " + (ActionToString(action)) + " - Coords: " + event.getX() + ", " + event.getY());
					AbstractActivity.thread.addMotionEvent(new MyEvent(action, event.getX(p), event.getY(p)));
				}
			}
			//AbstractActivity.thread.addMotionEvent(new MyEvent(event.getAction(), event.getX(), event.getY()));
			return true;
		}
		return false;
	}


	public static String ActionToString(int action) {
	    switch (action) {
	                
	        case MotionEvent.ACTION_DOWN: return "Down";
	        case MotionEvent.ACTION_MOVE: return "Move";
	        case MotionEvent.ACTION_POINTER_DOWN: return "Pointer Down";
	        case MotionEvent.ACTION_UP: return "Up";
	        case MotionEvent.ACTION_POINTER_UP: return "Pointer Up";
	        case MotionEvent.ACTION_OUTSIDE: return "Outside";
	        case MotionEvent.ACTION_CANCEL: return "Cancel";
	    }
	    return "Unknown";
	}
}
