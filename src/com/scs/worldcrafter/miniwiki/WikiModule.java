package com.scs.worldcrafter.miniwiki;

import java.util.ArrayList;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.framework.modules.AbstractOptionsModule;
import ssmith.android.lib2d.gui.Label;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;

public final class WikiModule extends AbstractOptionsModule {
	
	//private String options[] = {"Gameplay", "Block Types", "Monsters", "Return"};

	private static final float ICON_WIDTH = Statics.SCREEN_WIDTH/4;

	private static Paint paint_large_text = new Paint();
	private static Paint paint_normal_text = new Paint();

	static {
		paint_large_text.setARGB(255, 0, 0, 0);
		paint_large_text.setAntiAlias(true);
		//paint_large_text.setStyle(Style.STROKE);
		paint_large_text.setTextSize(Statics.GetHeightScaled(0.09f)); // Was 28

		paint_normal_text.setARGB(255, 0, 0, 0);
		paint_normal_text.setAntiAlias(true);
	}


	public WikiModule(AbstractActivity _act, AbstractModule _return_to) {
		super(_act, _return_to, 3, paint_normal_text, Statics.img_cache.getImage(R.drawable.button_blue, ICON_WIDTH, Statics.SCREEN_WIDTH/10), false);

		background = Statics.img_cache.getImage(R.drawable.parchment, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT);

		Label l = new Label("Credits_title", "Mini-Wiki", null, paint_large_text);
		l.setCentre(Statics.SCREEN_WIDTH/2, paint_large_text.getTextSize());
		this.stat_node_front.attachChild(l);

		/*StringBuffer str = new StringBuffer();
		str.append(Statics.NAME + " is a game all about exploring, building and surviving.  It is still being developed, and new features are being added all the time.  ");
		str.append("If you have any suggestions or questions, please visit the website at http://worldcrafter-android.blogspot.com/");
		 */
		paint_normal_text.setTextSize(Statics.GetHeightScaled(0.05f));

		/*MultiLineLabel label2 = new MultiLineLabel("credits", str.toString(), null, paint_normal_text, true, Statics.SCREEN_WIDTH * 0.9f);
		label2.setCentre(this.view.canvas_width/2,this.view.canvas_height/2);
		stat_node_front.attachChild(label2);
		 */
		this.stat_node_front.updateGeometricState();


	}


	@Override
	public void doDraw(Canvas c, long interpol) {
		super.doDraw(c, interpol);

	}


	@Override
	public ArrayList<String> getOptions() {
		AbstractActivity act = Statics.act;
		
		// {"Gameplay", "Block Types", "Monsters", "Return"};
		ArrayList<String> s = new ArrayList<String>();//String s[] = new String[4];
		s.add(act.getString(R.string.gameplay));
		s.add(act.getString(R.string.block_types));
		s.add(act.getString(R.string.mobs));
		s.add(act.getString(R.string.return_text));
		return s;
	}


	@Override
	public void optionSelected(String cmd) {
		AbstractActivity act = Statics.act;
		
		if (cmd.equalsIgnoreCase(act.getString(R.string.block_types))) {
			this.getThread().setNextModule(new BlockListModule(act, this));
		} else if (cmd.equalsIgnoreCase(act.getString(R.string.gameplay))) {
			this.getThread().setNextModule(new GameplayMenuModule(act, this));
		} else if (cmd.equalsIgnoreCase(act.getString(R.string.mobs))) {
			this.getThread().setNextModule(new MobsMenuModule(act, this));
		} else {
			super.returnTo();// super.returnToPrevModule();
		}
	}


}

