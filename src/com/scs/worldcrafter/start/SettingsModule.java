package com.scs.worldcrafter.start;

import java.io.IOException;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.framework.modules.SimpleScrollingAbstractModule;
import ssmith.android.io.IOFunctions;
import ssmith.android.lib2d.gui.AbstractComponent;
import ssmith.android.lib2d.gui.Button;
import ssmith.android.lib2d.gui.CheckBox;
import ssmith.android.lib2d.gui.GUIFunctions;
import ssmith.android.lib2d.layouts.VerticalFlowLayout;
import android.graphics.Bitmap;
import android.graphics.Paint;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;

public final class SettingsModule extends SimpleScrollingAbstractModule {

	private CheckBox cb_use_buttons, cb_mute, cb_show_squares;
	private Button save, cancel;

	//private static Paint paint_large_text_ = new Paint();
	private static Paint paint_normal_text = new Paint();

	static {
		/*paint_large_text.setARGB(255, 255, 255, 255);
		paint_large_text.setAntiAlias(true);
		paint_large_text.setStyle(Style.STROKE);
		paint_large_text.setTextSize(Statics.GetHeightScaled(0.1f));
*/
		paint_normal_text.setARGB(255, 255, 255, 255);
		paint_normal_text.setAntiAlias(true);
		paint_normal_text.setTextSize(GUIFunctions.GetTextSizeToFit("Use buttons for movemement", Statics.SCREEN_WIDTH * 0.75f));//Statics.GetHeightScaled(0.07f));

	}


	public SettingsModule(AbstractActivity _act, AbstractModule _return_to) {
		super(_act, _return_to);
	}


	@Override
	public void started() {
		if (Statics.GAME_MODE == Statics.GM_NINJA) {
			this.setBackground(R.drawable.ninja_background2);
		} else if (Statics.GAME_MODE == Statics.GM_POLICECOP) {
			this.setBackground(R.drawable.policecop_background);
		} else {
			this.setBackground(R.drawable.menu_background);
		}

		Bitmap bmp_mf_blue = Statics.img_cache.getImage(R.drawable.button_blue, Statics.SCREEN_WIDTH * 0.75f, Statics.SCREEN_WIDTH/10);

		VerticalFlowLayout vfl = new VerticalFlowLayout("vfl", 5);

		boolean use_buttons = Statics.cfg.getUsingButtons();
		cb_use_buttons = new CheckBox("Use Buttons for Movement", null, paint_normal_text, bmp_mf_blue, use_buttons);
		vfl.attachChild(cb_use_buttons);

		boolean mute = Statics.cfg.isMute();
		cb_mute = new CheckBox("Mute", null, paint_normal_text, bmp_mf_blue, mute);
		vfl.attachChild(cb_mute);

		boolean show_squares = Statics.cfg.getShowControlSquares();
		cb_show_squares = new CheckBox("Show Control Squares", null, paint_normal_text, bmp_mf_blue, show_squares);
		vfl.attachChild(cb_show_squares);

		save = new Button("Save", null, paint_normal_text, bmp_mf_blue);
		vfl.attachChild(save);
		cancel = new Button("Cancel", null, paint_normal_text, bmp_mf_blue);
		vfl.attachChild(cancel);

		this.stat_node_front.attachChild(vfl);

		this.stat_node_front.updateGeometricState();

		this.stat_cam.lookAt(this.stat_node_front, true);
	}


	@Override
	public void handleClick(AbstractComponent c) throws Exception {
		AbstractActivity act = Statics.act;
		
		if (c instanceof CheckBox) {
			IOFunctions.Vibrate(act.getBaseContext(), Statics.VIBRATE_LEN);
			CheckBox cb = (CheckBox)c;
			cb.toggle();
		} else if (c == cancel) {
			IOFunctions.Vibrate(act.getBaseContext(), Statics.VIBRATE_LEN);
			this.returnTo();
		} else if (c == save) {
			IOFunctions.Vibrate(act.getBaseContext(), Statics.VIBRATE_LEN);
			saveSettings();
			this.returnTo();
		}

	}


	private void saveSettings() {
		try {
			Statics.cfg.setUseButtons(this.cb_use_buttons.isChecked());
			Statics.cfg.setIsMute(this.cb_mute.isChecked());
			Statics.cfg.setShowControlSquares(this.cb_show_squares.isChecked());
			if (this.cb_mute.isChecked()) {
				Statics.act.pauseMusic();
			} else {
				Statics.act.startMusic();
			}
		} catch (IOException e) {
			throw new RuntimeException("Unable to save settings: " + e.getMessage());
		}
	}

}
