package com.scs.worldcrafter.game.tutorial;

import ssmith.android.lib2d.Node;
import ssmith.android.lib2d.gui.MultiLineLabel;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.game.GameModule;
import com.scs.worldcrafter.game.IProcessable;

public abstract class AbstractTutorial extends Node implements IProcessable {

	private static final int INTERVAL = 6000;

	private static Paint paint_text = new Paint();
	protected static Paint paint_help_lines = new Paint();

	private int stage = -1;
	private long curr_timer = 0;
	protected GameModule game;

	static {
		paint_text.setARGB(255, 255, 255, 255);
		paint_text.setAntiAlias(true);
		//paint_text.setStyle(Style.STROKE);
		paint_text.setTextSize(Statics.GetHeightScaled(0.06f));

		paint_help_lines.setARGB(255, 255, 255, 0);
		paint_help_lines.setAntiAlias(true);
		paint_help_lines.setStyle(Style.STROKE);
		paint_help_lines.setStrokeWidth(6f);
	}

	
	public AbstractTutorial(GameModule _game) {
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

			this.detachAllChildren();

			MultiLineLabel label = new MultiLineLabel("Tutorial", "", null, paint_text, true, Statics.SCREEN_WIDTH * 0.7f);

			this.setStage(stage, label);

			label.setCentre(Statics.SCREEN_WIDTH/2, (Statics.SCREEN_HEIGHT*.75f) - (paint_text.getTextSize()*.5f));
			this.attachChild(label);
			this.updateGeometricState();

		}
	}
	
	
	protected abstract void setStage(int stage, MultiLineLabel label);

}
