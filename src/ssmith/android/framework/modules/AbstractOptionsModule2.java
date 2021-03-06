package ssmith.android.framework.modules;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import ssmith.android.compatibility.MotionEvent;
import ssmith.android.compatibility.Paint;
import ssmith.android.compatibility.PointF;
import ssmith.android.framework.MyEvent;
import ssmith.android.lib2d.Spatial;
import ssmith.android.lib2d.gui.AbstractComponent;
import ssmith.android.lib2d.gui.Button;
import ssmith.android.lib2d.gui.GUIFunctions;
import ssmith.android.lib2d.gui.Label;
import ssmith.android.lib2d.layouts.GridLayout;
import ssmith.android.lib2d.shapes.Geometry;
import ssmith.lang.GeometryFuncs;

import com.scs.multiplayerplatformer.Statics;

public abstract class AbstractOptionsModule2 extends AbstractModule {

	private static final float MIN_DRAG_DIST = Statics.SCREEN_HEIGHT/25;// Was 30, SCS 2/4/13

	private static Paint paint_free_text = new Paint();

	static {
		paint_free_text.setARGB(255, 200, 200, 200);
		paint_free_text.setAntiAlias(true);
	}

	private ArrayList<String> al_button_texts, al_action_cmd_texts;
	private PointF lastDownDcreen = new PointF();
	private boolean isDragging = false;
	private int cols;
	private BufferedImage bmp;
	private Paint paint_ink;
	private boolean canDrag, autoSelect, truncNames;
	protected int show; // -1, 0 or 1
	protected Label lblTitle;

	public AbstractOptionsModule2(int _cols, Paint _paint_ink, BufferedImage _bmp, int _show, boolean _auto_select, String title, boolean _trunc_names) {
		super();

		cols =_cols;
		paint_ink = _paint_ink;
		bmp = _bmp;
		show = _show;
		autoSelect = _auto_select;
		truncNames = _trunc_names;
		
		lblTitle = new Label("title", "", null, paint_free_text, false);
		lblTitle.setLocation(Statics.SCREEN_WIDTH * 0.05f, Statics.SCREEN_HEIGHT * 0.05f);
		lblTitle.updateGeometricState();
		this.statNodeFront.attachChild(lblTitle);
		this.statNodeFront.updateGeometricState();
		
		this.setTitle(title);
		
		this.statCam.lookAt(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT/2, true);
	}


	protected void setTitle(String title) {
		paint_free_text.setTextSize(GUIFunctions.getTextSizeToFit(title, Statics.SCREEN_WIDTH * 0.75f));
		lblTitle.setText(title);
	}
	
	
	public abstract void getOptions();

	
	public abstract void optionSelected(int idx);


	protected void setOptions() {
		al_button_texts = new ArrayList<String>();
		al_action_cmd_texts = new ArrayList<String>();
		getOptions();
		this.rootNode.removeAllChildren();
		if (al_button_texts.size() > 0) {
			int curr_col = 1;
			int curr_row = 1;
			String longest = "";
			GridLayout menu_node = new GridLayout("Menu", bmp.getWidth(), bmp.getHeight(), 10);
			for (int i=0 ; i<al_button_texts.size() ; i++) {
				String action_cmd = this.al_action_cmd_texts.get(i);
				if (action_cmd.length() == 0) {
					action_cmd = i + "_" + al_button_texts.get(i);
					al_action_cmd_texts.remove(i);
					al_action_cmd_texts.add(i, action_cmd);
				}
				Button b = new Button(action_cmd, al_button_texts.get(i), null, paint_ink, bmp); // "i+.." to ensure we always have unique commands
				menu_node.attachChild(b, curr_col, curr_row);

				curr_col++;
				if (curr_col > cols) {
					curr_col = 1;
					curr_row++;
				}
				if (al_button_texts.get(i).length() > longest.length()) {
					longest = al_button_texts.get(i);
				}

			}
			if (longest.length() > 40) {
				longest = longest.substring(0, 39); // Max!
			}
			paint_ink.setTextSize(GUIFunctions.getTextSizeToFit(longest, bmp.getWidth() * 0.9f));

			// Loop through children and reposition text now that the text size has changed
			for (Spatial g : menu_node.getChildren()) {
				if (g instanceof Button) {
					Button b = (Button)g;
					b.calcTextOffset();
					if (this.truncNames) {
						String s = b.getText();
						while (paint_ink.measureText(s) >= b.getWidth() * 0.95f) {
							int middle = s.length() / 2;
							String s_new = s.substring(0,  middle-2).trim() + "..." + s.substring(middle + 2).trim();
							s = s_new;
						}
						b.setText(s);
					}
				}
			}

			this.rootNode.attachChild(menu_node);
			rootNode.updateGeometricState();

			canDrag = menu_node.getHeight() > Statics.SCREEN_HEIGHT;
			if (canDrag) {
				if (show == 1) {
					rootCam.lookAt(menu_node.getWorldCentreX(), menu_node.getHeight(), true);
				} else if (show == -1) {
					rootCam.lookAt(menu_node.getWorldCentreX(), menu_node.getWorldY() + (Statics.SCREEN_HEIGHT/2), true);
				} else if (show == 0) {
					rootCam.lookAt(menu_node, true);
				} else {
					// Do nothing - we might be returning and want to view where we were before
				}
			} else {
				rootCam.lookAt(menu_node, true);
			}

			show = 999; // stop us moving next time

		}
	}


	@Override
	public boolean processEvent(MyEvent ev) throws Exception {
		if (ev.getAction() == MotionEvent.ACTION_UP) { // Note we only catch UP so we don't select two options at the same time!
			lastDownDcreen.y = ev.getY();

			if (isDragging == false) { // Check for an icon pressed
				// Adjust for camera location
				float x = ev.getX() + rootCam.left;
				float y = ev.getY() + this.rootCam.top;

				ArrayList<Geometry> colls = this.rootNode.getCollidersAt(x, y);
				if (colls.size() > 0) {
					for (Geometry g : colls) {
						if (g instanceof AbstractComponent) {
							AbstractComponent b = (AbstractComponent)g;
							String selected_cmd = b.getActionCommand();
							if (selected_cmd.length() > 0) { // In case it's a textbox or something
								this.selectOption(this.al_action_cmd_texts.indexOf(selected_cmd));
								return true;
							}
						}
					}
				}
			} 
			isDragging = false;
		} else if (ev.getAction() == MotionEvent.ACTION_MOVE) { // Dragging!
			float offy = lastDownDcreen.y - ev.getY();
			//AbstractActivity.Log("Dragging (" + offy + ")");
			double dist = GeometryFuncs.distance(0, 0, 0, offy);
			if (dist > MIN_DRAG_DIST || isDragging) {// && dist < MAX_DRAG_DIST) {
				if (canDrag) {
					this.rootCam.moveCam(0, offy);
					if (this.rootNode.getHeight() > Statics.SCREEN_HEIGHT) {
						if (this.rootCam.top < this.rootNode.getWorldY()) {
							this.rootCam.moveCam(0, this.rootNode.getWorldY() - this.rootCam.top);
						} else if (this.rootCam.bottom > this.rootNode.getWorldBounds().bottom) {
							this.rootCam.moveCam(0, this.rootNode.getWorldBounds().bottom - this.rootCam.bottom);
						}
					}
				}
				isDragging = true;
			}
			lastDownDcreen.y = ev.getY();
		} else if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			lastDownDcreen.y = ev.getY();
		}	
		return false;			
	}


	private void selectOption(int idx) {
		this.optionSelected(idx);

	}


	@Override
	public void updateGame(long interpol) {
		if (al_button_texts == null) {
			this.setOptions();
			// See if there's only one option.
			if (autoSelect) {
				if (al_button_texts != null) {
					if (al_button_texts.size() == 1) {
						this.selectOption(0);
					}
				}
			}
		}
	}


	/*@Override
	public boolean onBackPressed() {
		returnTo();
		return true;
	}*/
	
	
	protected void addOption(String s) {
		addOption(s, s);
	}

	
	protected void addOption(String text, String cmd) {
		this.al_button_texts.add(text);
		this.al_action_cmd_texts.add(cmd);
	}

	
	protected void addOption(String text, int cmd) {
		this.al_button_texts.add(text);
		this.al_action_cmd_texts.add(""+cmd);
	}

	
	public int getNumOfOptions() {
		return this.al_button_texts.size();
	}
	
	
	public boolean areThereAnyOptions() {
		return this.al_button_texts.size() > 0;
	}
	
	
	public String getButtonText(int idx) {
		return this.al_button_texts.get(idx);
	}


	public String getActionCommand(int idx) {
		return this.al_action_cmd_texts.get(idx);
	}


	public boolean onKeyUp(int keyCode, KeyEvent msg) {
		if (keyCode >= KeyEvent.VK_1 && keyCode <= KeyEvent.VK_9) {
			this.optionSelected(keyCode - KeyEvent.VK_1);
			return true;
		}
		return false;
	}
}
