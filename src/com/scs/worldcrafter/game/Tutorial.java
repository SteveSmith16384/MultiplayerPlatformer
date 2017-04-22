package com.scs.worldcrafter.game;

import ssmith.android.lib2d.Node;

import ssmith.android.lib2d.gui.MultiLineLabel;
import ssmith.android.lib2d.shapes.Rectangle;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.scs.worldcrafter.Statics;
import com.scs.ninja.main.lite.R;


public class Tutorial extends Node implements IProcessable {

	private static final int INTERVAL = 4000;

	private static Paint paint_text = new Paint();
	private static Paint paint_help_lines = new Paint();

	private int stage = -1;
	private long curr_timer = 0;
	private GameModule game;

	static {
		paint_text.setARGB(255, 255, 255, 255);
		paint_text.setAntiAlias(true);
		paint_text.setStyle(Style.STROKE);
		paint_text.setTextSize(Statics.GetHeightScaled(0.06f));

		paint_help_lines.setARGB(255, 255, 255, 0);
		paint_help_lines.setAntiAlias(true);
		paint_help_lines.setStyle(Style.STROKE);
		paint_help_lines.setStrokeWidth(6f);
	}

	
	public Tutorial(GameModule _game) {
		super("Tutorial");

		game = _game;
		
		this.game.stat_node_front.attachChild(this);
		//this.updateGeometricState(true);
		this.game.addToProcess_Instant(this);

	}


	@Override
	public void process(long interpol) {
		this.curr_timer -= interpol;
		if (this.curr_timer <= 0) {
			curr_timer = INTERVAL;
			stage++;

			//this.removeFromParent();
			this.detachAllChildren();

			MultiLineLabel label = new MultiLineLabel("Tutorial", "TO BE SET", null, paint_text, true, Statics.SCREEN_WIDTH * 0.6f);

			switch (stage) {
			case 0:
				label.setText(Statics.act.getString(R.string.wc_tutorial_0));
				Rectangle rect = new Rectangle("Box", 1f, Statics.SCREEN_HEIGHT * .5f, Statics.SCREEN_WIDTH * 0.25f, Statics.SCREEN_HEIGHT * .5f, paint_help_lines, null);
				this.attachChild(rect);
				rect = new Rectangle("Box", Statics.SCREEN_WIDTH * 0.75f, Statics.SCREEN_HEIGHT * .5f, Statics.SCREEN_WIDTH * 0.25f, Statics.SCREEN_HEIGHT*.5f, paint_help_lines, null);
				this.attachChild(rect);
				break;

			case 1:
				label.setText(Statics.act.getString(R.string.wc_tutorial_1));
				Rectangle rect2 = new Rectangle("Box", 1f, 1f, Statics.SCREEN_WIDTH * 0.25f, Statics.SCREEN_HEIGHT * .5f, paint_help_lines, null);
				this.attachChild(rect2);
				rect2 = new Rectangle("Box", Statics.SCREEN_WIDTH * 0.75f, 0, Statics.SCREEN_WIDTH * 0.25f, Statics.SCREEN_HEIGHT*.5f, paint_help_lines, null);
				this.attachChild(rect2);
				break;

			case 2:
				label.setText(Statics.act.getString(R.string.wc_tutorial_2));
				break;

			case 3:
				label.setText(Statics.act.getString(R.string.wc_tutorial_3));
				break;

			case 4:
				label.setText(Statics.act.getString(R.string.wc_tutorial_4));
				break;

			case 5:
				label.setText(Statics.act.getString(R.string.wc_tutorial_5));
				break;

			case 6:
				label.setText(Statics.act.getString(R.string.wc_tutorial_6));
				break;

			case 7:
				label.setText(Statics.act.getString(R.string.wc_tutorial_7));
				break;

			case 8:
				label.setText(Statics.act.getString(R.string.wc_tutorial_8));
				break;

			case 9:
				label.setText(Statics.act.getString(R.string.wc_tutorial_9));
				break;

			case 10:
				label.setText(Statics.act.getString(R.string.wc_tutorial_10));
				rect2 = new Rectangle("Box", 1, Statics.SCREEN_HEIGHT-Statics.HEALTH_BAR_HEIGHT, Statics.HEALTH_BAR_WIDTH, Statics.SCREEN_HEIGHT-1, paint_help_lines, null);
				this.attachChild(rect2);
				break;

			case 11:
				label.setText(Statics.act.getString(R.string.wc_tutorial_11));
				break;

			default:
				this.game.removeFromProcess(this);
				return;
			}

			label.setCentre(Statics.SCREEN_WIDTH/2, (Statics.SCREEN_HEIGHT*.75f) - (paint_text.getTextSize()*.5f));
			this.attachChild(label);
			this.updateGeometricState();

		}
	}
	

}
