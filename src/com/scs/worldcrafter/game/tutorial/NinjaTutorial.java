package com.scs.worldcrafter.game.tutorial;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.lib2d.gui.MultiLineLabel;

import com.scs.worldcrafter.Statics;
import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.game.GameModule;

public class NinjaTutorial extends AbstractTutorial {
	
	public NinjaTutorial(GameModule _game) {
		super(_game);
	}

	
	protected void setStage(int stage, MultiLineLabel label) {
		AbstractActivity act = Statics.act;
		
		switch (stage) {
		case 0:
			label.setText(act.getString(R.string.ninja_tutorial_0));
			/*Rectangle rect = new Rectangle("Box", 1f, Statics.SCREEN_HEIGHT * .5f, Statics.SCREEN_WIDTH * 0.25f, Statics.SCREEN_HEIGHT * .5f, paint_help_lines, null);
			this.attachChild(rect);
			rect = new Rectangle("Box", Statics.SCREEN_WIDTH * 0.75f, Statics.SCREEN_HEIGHT * .5f, Statics.SCREEN_WIDTH * 0.25f, Statics.SCREEN_HEIGHT*.5f, paint_help_lines, null);
			this.attachChild(rect);*/
			break;

		case 1:
			label.setText(act.getString(R.string.ninja_tutorial_1));
			/*Rectangle rect2 = new Rectangle("Box", 1f, 1f, Statics.SCREEN_WIDTH * 0.25f, Statics.SCREEN_HEIGHT * .5f, paint_help_lines, null);
			this.attachChild(rect2);
			rect2 = new Rectangle("Box", Statics.SCREEN_WIDTH * 0.75f, 0, Statics.SCREEN_WIDTH * 0.25f, Statics.SCREEN_HEIGHT*.5f, paint_help_lines, null);
			this.attachChild(rect2);*/
			break;

		case 2:
			label.setText(act.getString(R.string.ninja_tutorial_2));
			break;

		case 3:
			label.setText(act.getString(R.string.ninja_tutorial_3));
			break;

		case 4:
			label.setText(act.getString(R.string.ninja_tutorial_4));
			break;

		case 5:
			label.setText(act.getString(R.string.ninja_tutorial_5));
			break;

		case 6:
			label.setText(act.getString(R.string.ninja_tutorial_6));
			break;

		case 7:
			label.setText(act.getString(R.string.ninja_tutorial_7));
			break;

		case 8:
			label.setText(act.getString(R.string.ninja_tutorial_8));
			break;

		default:
			this.game.removeFromProcess(this);
			return;
		}

	}
	
}
