package com.scs.worldcrafter.start;

import java.io.IOException;
import java.util.ArrayList;

import ssmith.android.framework.ErrorReporter;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.MyEvent;
import ssmith.android.framework.modules.AbstractModule;
import ssmith.android.io.IOFunctions;
import ssmith.android.lib2d.gui.AbstractComponent;
import ssmith.android.lib2d.gui.Button;
import ssmith.android.lib2d.gui.GUIFunctions;
import ssmith.android.lib2d.gui.Label;
import ssmith.android.lib2d.layouts.GridLayout;
import ssmith.android.lib2d.shapes.BitmapRectangle;
import ssmith.android.lib2d.shapes.Geometry;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.view.MotionEvent;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.crafting.MiniCraftingGuide;
import com.scs.worldcrafter.miniwiki.BlockListModule;
import com.scs.worldcrafter.miniwiki.MobsMenuModule;
import com.scs.worldcrafter.miniwiki.WikiModule;
import com.scs.worldcrafter.sharemaps.ShareMapsModule;

public final class StartupModule extends AbstractModule {

	private static Paint paint_large_text = new Paint();
	private static Paint paint_normal_text = new Paint();
	private static Paint paint_button_text = new Paint();

	static {
		paint_large_text.setARGB(255, 255, 255, 255);
		paint_large_text.setAntiAlias(true);
		paint_large_text.setTextSize(Statics.GetHeightScaled(0.12f)); // Was 28

		paint_button_text.setARGB(255, 0, 0, 0);
		paint_button_text.setAntiAlias(true);
		paint_button_text.setTextSize(GUIFunctions.GetTextSizeToFit("More GamesX", Statics.SCREEN_WIDTH/3));

		paint_normal_text.setARGB(255, 255, 255, 255);
		paint_normal_text.setAntiAlias(true);
		paint_normal_text.setTextSize(paint_button_text.getTextSize()/2);

	}


	public StartupModule(AbstractActivity act) {
		super(act, null);

		background = Statics.img_cache.getImage(R.drawable.worldcrafter_logo, Statics.SCREEN_WIDTH, Statics.SCREEN_HEIGHT);

	}


	@Override
	public void started() {
		AbstractActivity act = Statics.act;
		
		if (BlockListModule.img_cache != null) {
			BlockListModule.img_cache.clear();
		}		
		if (MobsMenuModule.img_cache != null) {
			MobsMenuModule.img_cache.clear();
		}

		// Create initial menu
		this.stat_node_front.detachAllChildren();

		/*BitmapRectangle logo = new BitmapRectangle("Logo", Statics.img_cache.getImageByKey_WidthOnly(R.drawable.worldcrafter_logo, Statics.SCREEN_WIDTH * 0.8f), 0, 0); // logo
		logo.setCentre(Statics.SCREEN_WIDTH/2, paint_large_text.getTextSize());
		this.stat_node_front.attachChild(logo);
		 */
		Bitmap bmp_mf_blue = Statics.img_cache.getImage(R.drawable.button_blue, Statics.SCREEN_WIDTH/3, Statics.SCREEN_WIDTH/10);
		//Bitmap bmp_mf_green = Statics.img_cache.getImage(R.drawable.menu_frame_green, Statics.SCREEN_WIDTH/3, Statics.SCREEN_WIDTH/10);
		//Bitmap bmp_mf_yellow = Statics.img_cache.getImage(R.drawable.menu_frame_yellow, Statics.SCREEN_WIDTH/3, Statics.SCREEN_WIDTH/10);

		GridLayout menu_node = new GridLayout("Menu", Statics.SCREEN_WIDTH/3, Statics.SCREEN_WIDTH/10, 10);

		menu_node.attachChild(new Button(act.getString(R.string.start_game), null, paint_button_text, bmp_mf_blue), 0, 0);
		menu_node.attachChild(new Button(act.getString(R.string.tutorial), null, paint_button_text, bmp_mf_blue), 1, 0);

		menu_node.attachChild(new Button(act.getString(R.string.guide), null, paint_button_text, bmp_mf_blue), 0, 1);
		menu_node.attachChild(new Button(act.getString(R.string.crafting), null, paint_button_text, bmp_mf_blue), 1, 1);

		menu_node.attachChild(new Button(act.getString(R.string.settings), null, paint_button_text, bmp_mf_blue), 0, 2);
		//menu_node.attachChild(new Button(act.getString(R.string.share_maps), null, paint_button_text, bmp_mf_blue), 1, 2);
		menu_node.attachChild(new Button(act.getString(R.string.more_games), null, paint_button_text, bmp_mf_blue), 1, 2);

		menu_node.attachChild(new Button(act.getString(R.string.feedback), null, paint_button_text, bmp_mf_blue), 0, 3);
		if (Statics.ANDROID_MARKET) {
			menu_node.attachChild(new Button(act.getString(R.string.rate_this), null, paint_button_text, bmp_mf_blue), 1, 3);
		}
		//menu_node.attachChild(new Button(WEBSITE, null, paint_button_text, bmp_mf_yellow), 0, 1);
		//menu_node.attachChild(new Button(act.getString(R.string.about), null, paint_button_text, bmp_mf_blue), 1, 2);
		menu_node.updateGeometricState();

		menu_node.setCentre(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT/2);
		this.stat_node_front.attachChild(menu_node);

		BitmapRectangle logo2 = new BitmapRectangle("Logo2", Statics.img_cache.getImageByKey_WidthOnly(R.drawable.penultimate_logo, Statics.SCREEN_WIDTH * 0.2f), 0, 0);
		logo2.setCentre(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT * .9f);
		this.stat_node_front.attachChild(logo2); // logo

		Label label2 = new Label("Version", act.getString(R.string.version) + " " + Statics.VERSION_NAME, null, paint_normal_text);
		label2.setLocation(5, Statics.SCREEN_HEIGHT - (paint_normal_text.getTextSize()*2));
		stat_node_front.attachChild(label2);

		stat_node_front.updateGeometricState();

		this.stat_cam.lookAt(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT/2, true);
	}


	@Override
	public void updateGame(long interpol) {
		// Do nothing
	}


	@Override
	public boolean processEvent(MyEvent ev) throws IOException {
		AbstractActivity act = Statics.act;
		
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			// Adjust for camera location
			float x = ev.getX() + stat_cam.left;
			float y = ev.getY() + this.stat_cam.top;
			ArrayList<Geometry> colls = this.stat_node_front.getCollidersAt(x, y);
			if (colls.size() > 0) {
				for (Geometry g : colls) {
					if (g instanceof AbstractComponent) {
						AbstractComponent b = (AbstractComponent)g;
						String cmd = b.getActionCommand();
						if (cmd.length() > 0) { // In case it's a textbox or something
							IOFunctions.Vibrate(act.getBaseContext(), Statics.VIBRATE_LEN);
							//view.sound_manager.playSound(R.raw.beep);
							if (cmd.equalsIgnoreCase(act.getString(R.string.start_game))) {
								Statics.show_tutorial = false;
								startNewGame();
								return true;
							} else if (cmd.equalsIgnoreCase(act.getString(R.string.tutorial))) {
								Statics.show_tutorial = true;
								startNewGame();
								return true;
								/*} else if (cmd.equalsIgnoreCase(CREDITS)) {
								this.getThread().setNextModule(new CreditsModule(this.act, this.getThread(), this.view, this));
								return true;*/
							} else if (cmd.equalsIgnoreCase(act.getString(R.string.about))) {
								this.getThread().setNextModule(new AboutModule(act, this));
								return true;
							} else if (cmd.equalsIgnoreCase(act.getString(R.string.guide))) {
								this.getThread().setNextModule(new WikiModule(act, this));
								return true;
							} else if (cmd.equalsIgnoreCase(act.getString(R.string.crafting))) {
								this.getThread().setNextModule(new MiniCraftingGuide(act, this));
								return true;
							} else if (cmd.equalsIgnoreCase(act.getString(R.string.settings))) {
								this.getThread().setNextModule(new SettingsModule(act, this));
								return true;
							} else if (cmd.equalsIgnoreCase(act.getString(R.string.share_maps))) {
								this.getThread().setNextModule(new ShareMapsModule(act, this));
								return true;
							} else if (cmd.equalsIgnoreCase(act.getString(R.string.more_games))) {
								try {
									Context mContext = act.getBaseContext();
									Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Penultimate Apps"));
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									mContext.startActivity(intent);
								} catch (Exception ex) {
									ErrorReporter.getInstance().handleSilentException(ex);
									this.getThread().setNextModule(new ErrorModule(act, this, "Unable to View Google Play", "Sorry, your phone does not seem to be able to handle going to Google Play via this app."));
								}
								return true;
							} else if (cmd.equalsIgnoreCase(act.getString(R.string.rate_this))) {
								try {
									Context mContext = act.getBaseContext();
									Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Statics.PACKAGE));
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									mContext.startActivity(intent);
								} catch (Exception ex) {
									ErrorReporter.getInstance().handleSilentException(ex);
									this.getThread().setNextModule(new ErrorModule(act, this, "Unable to View Google Play", "Sorry, your phone does not seem to be able to handle going to Google Play via this app."));
								}
								return true;
							} else if (cmd.equalsIgnoreCase(act.getString(R.string.feedback))) {
								SendFeedback(act);
								return true;
							} else {
								throw new RuntimeException("Unknown command: '" + cmd + "'");
							}
						}
					}
				}
			}
		}	
		return false;		
	}


	private void startNewGame() {
		AbstractActivity act = Statics.act;
		
		this.getThread().setNextModule(new SelectGameModeModule(act, this));

	}


	@Override
	public boolean onBackPressed() {
		AbstractActivity act = Statics.act;
		
		act.finish();
		return true;
	}


	public static void SendFeedback(AbstractActivity act) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		String subject = Statics.NAME + " Feedback".replaceAll(" ", "%20");
		Uri data = Uri.parse("mailto:" + Statics.EMAIL + "?subject=" + subject);
		intent.setData(data);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		act.getBaseContext().startActivity(intent);
	}


}
