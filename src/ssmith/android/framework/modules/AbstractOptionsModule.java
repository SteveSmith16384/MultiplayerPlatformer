package ssmith.android.framework.modules;

import java.util.ArrayList;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.MyEvent;
import ssmith.android.io.IOFunctions;
import ssmith.android.lib2d.gui.AbstractComponent;
import ssmith.android.lib2d.gui.Button;
import ssmith.android.lib2d.layouts.GridLayout;
import ssmith.android.lib2d.shapes.Geometry;
import ssmith.lang.GeometryFuncs;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.scs.worldcrafter.Statics;

public abstract class AbstractOptionsModule extends AbstractModule {

	private static final float MIN_DRAG_DIST = Statics.SCREEN_HEIGHT/25;// Was 30, SCS 2/4/13

	private String selected_cmd;
	private ArrayList<String> options;
	private PointF last_down_screen = new PointF();
	private boolean is_dragging = false;
	private int cols;
	private Bitmap bmp;
	private Paint paint_ink;
	private boolean can_drag;
	private boolean show_end;

	public AbstractOptionsModule(AbstractActivity _act, AbstractModule _return_to, int _cols, Paint _paint_ink, Bitmap _bmp, boolean _show_end) {
		super(_act, _return_to);

		cols =_cols;
		paint_ink = _paint_ink;
		bmp = _bmp;
		show_end = _show_end;
	}


	public abstract ArrayList<String> getOptions();

	public abstract void optionSelected(String cmd);


	private void setOptions() {
		options = getOptions();

		// Create initial menu
		this.stat_node_front.detachAllChildren();

		int curr_col = 1;
		int curr_row = 1;
		GridLayout menu_node = new GridLayout("Menu", bmp.getWidth(), bmp.getHeight(), 10);
		for (int i=0 ; i<options.size() ; i++) {
			/*if (options[i] == null) {
				throw new RuntimeException("Option is null");
			}*/
			Button b = new Button(options.get(i), options.get(i), null, paint_ink, bmp);
			menu_node.attachChild(b, curr_col, curr_row);

			curr_col++;
			if (curr_col > cols) {
				curr_col = 1;
				curr_row++;
			}

		}
		this.stat_node_front.attachChild(menu_node);

		stat_node_front.updateGeometricState();

		if (show_end) {
			stat_cam.lookAt(menu_node.getWorldCentreX(), menu_node.getHeight(), true);
		} else {
			stat_cam.lookAt(menu_node, true);
		}
		can_drag = menu_node.getHeight() > Statics.SCREEN_HEIGHT;
	}


	@Override
	public boolean processEvent(MyEvent ev) throws Exception {
		AbstractActivity act = Statics.act;
		
		if (ev.getAction() == MotionEvent.ACTION_UP) { // Note we only catch UP so we don't select two options at the same time!
			last_down_screen.y = ev.getY();

			if (is_dragging == false) { // Check for an icon pressed
				// Adjust for camera location
				float x = ev.getX() + stat_cam.left;
				float y = ev.getY() + this.stat_cam.top;

				ArrayList<Geometry> colls = this.stat_node_front.getCollidersAt(x, y);
				if (colls.size() > 0) {
					for (Geometry g : colls) {
						if (g instanceof AbstractComponent) {
							AbstractComponent b = (AbstractComponent)g;
							selected_cmd = b.getActionCommand();
							if (selected_cmd.length() > 0) { // In case it's a textbox or something
								//view.sound_manager.playSound(R.raw.beep);
								IOFunctions.Vibrate(act.getBaseContext(), Statics.VIBRATE_LEN);
								this.optionSelected(this.selected_cmd);
								return true;
							}
						}
					}
				}
			} 
			is_dragging = false;
		} else if (ev.getAction() == MotionEvent.ACTION_MOVE) { // Dragging!
			if (can_drag) {
				float offy = last_down_screen.y - ev.getY();
				double dist = GeometryFuncs.distance(0, 0, 0, offy);
				if (dist > MIN_DRAG_DIST || is_dragging) {
					this.stat_cam.moveCam(0, offy);
					if (this.root_node.getHeight() > Statics.SCREEN_HEIGHT) {
						if (this.root_cam.top < this.root_node.getWorldY()) {
							this.root_cam.moveCam(0, this.root_node.getWorldY() - this.root_cam.top);
						} else if (this.root_cam.bottom > this.root_node.getWorldBounds().bottom) {
							this.root_cam.moveCam(0, this.root_node.getWorldBounds().bottom - this.root_cam.bottom);
						}
					}
					is_dragging = true;
				}
				//last_down_screen.x = x2;
				last_down_screen.y = ev.getY();
			}
		}	
		return false;			
	}


	@Override
	public void updateGame(long interpol) {
		if (options == null) {
			this.setOptions();
		}
	}


	/*protected void returnToPrevModule() {
		//view.sound_manager.playSound(R.raw.beep);
		super.getThread().setNextModule(this.return_to);
	}

	
	@Override
	public boolean onBackPressed() {
		returnToPrevModule();
		return true;
	}*/

}
