package ssmith.android.framework.modules;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import ssmith.android.compatibility.Canvas;
import ssmith.android.compatibility.Paint;
import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.MyEvent;
import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.Node;
import ssmith.android.lib2d.gui.AbstractComponent;
import ssmith.android.lib2d.shapes.Geometry;
import ssmith.lang.Functions;

import com.scs.multiplayerplatformer.MainThread;
import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.Player;

public abstract class AbstractModule extends Thread {//implements NewControllerListener {
	
	private static Paint paint_button_text = new Paint();

	public Camera rootCam;
	public Node rootNode = new Node("root_node");
	public Node statNodeBack = new Node("stat_node");
	public Node statNodeFront = new Node("stat_node");
	protected Camera statCam; // For stat node so some graphics are always drawn at the same pos
	protected BufferedImage background;
	//protected AbstractModule mod_return_to;
	private PleaseWaitDialog pleaseWaitDialog;
	
	static {
		paint_button_text.setARGB(255, 255, 255, 255);
		paint_button_text.setAntiAlias(true);
		paint_button_text.setTextSize(15);
	}

	
	public AbstractModule() {
		rootCam = new Camera();
		statCam = new Camera();
		
		statCam.lookAt(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT/2, true); // Default, for toast
	}
	
	
	public MainThread getThread() {
		return AbstractActivity.thread;// this.view.thread;
	}

	
	/**
	 * If true is returned, all remaining events are removed
	 * 
	 * @param evt
	 * @return
	 * @throws Exception
	 */
	public abstract boolean processEvent(MyEvent evt) throws Exception;
	
	public void started() {
		// Override if reqd.
	}
	

	public void stopped() {
		// Override if reqd.
	}
	

	public void setBackground(String key) {
		background = Statics.img_cache.getImage(key, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT);
	}


	public boolean onKeyDown(int keyCode, KeyEvent msg) {
		// Override if req'd.
		return false;
	}

	
	public boolean onKeyUp(int keyCode, KeyEvent msg) {
		// Override if req'd.
		return false;
	}

	
	/**
	 * Override if required.
	 * @return
	 */
	public boolean onBackPressed() {
		return true;//returnTo();
	}

	/*
	protected boolean returnTo() {
	/*	if (this.mod_return_to != null) {
			this.getThread().setNextModule(this.mod_return_to);
		}
		//return true;

	}
	*/
	
	public void doDraw(Canvas c, long interpol) {
		if (background != null) {
			c.drawBitmap(this.background, 0, 0, null);
		}

		statCam.update(interpol);
		statNodeBack.doDraw(c, statCam, interpol);

		rootCam.update(interpol);
		rootNode.doDraw(c, rootCam, interpol);

		statNodeFront.doDraw(c, statCam, interpol);

		if (Statics.RELEASE_MODE == false || Statics.DEBUG) {
			c.drawText("Not in release mode", 5, 200, paint_button_text);
		}
		
	}


	public abstract void updateGame(long interpol);
	
	
	public AbstractComponent GetComponentAt(Node node, float x, float y) {
		ArrayList<Geometry> colls = node.getCollidersAt(x, y);
		if (colls.size() > 0) {
			for (Geometry g : colls) {
				if (g instanceof AbstractComponent) {
					AbstractComponent b = (AbstractComponent)g;
					return b;
				}
			}
		}
		return null;
	}
	

	public void showPleaseWait(String msg) {
		if (Statics.RELEASE_MODE == false) {
			if (statCam.getActualCentre().x == 0) {
				throw new RuntimeException("Stat cam is not set for toast!");
			}
		}
		this.dismissPleaseWait();
		if (pleaseWaitDialog == null) {
			pleaseWaitDialog = new PleaseWaitDialog(msg);
			this.statNodeFront.attachChild(statNodeFront.getNumChildren(), pleaseWaitDialog);
			this.statNodeFront.updateGeometricState();
			this.getThread().doDrawing();
		}
	}
	
	
	public void dismissPleaseWait() {
		if (pleaseWaitDialog != null) {
			pleaseWaitDialog.removeFromParent();
			pleaseWaitDialog = null;
			this.statNodeFront.updateGeometricState();
			this.getThread().doDrawing();
		}
		
	}
	
	
	public void showToast(String s) {
		this.showPleaseWait(s);
		Runnable r = new Runnable()  {
			public void run() {
				Functions.delay(1000*2);
				dismissPleaseWait();
			}
		};
		Thread t = new Thread(r);
		t.setName("ShowToastThread");
		t.start();
	}
	

	/*public void onActivityResult(int requestCode, int resultCode) {
		// Override if required
	}*/
	

	public void newPlayer(Player player) {
		// Override if reqd
	}


}
