package com.scs.worldcrafter.game;

import java.util.Iterator;

import ssmith.android.framework.AbstractActivity;
import ssmith.android.framework.modules.AbstractComplexModule;
import ssmith.android.lib2d.gui.Button;
import ssmith.android.lib2d.gui.GUIFunctions;
import ssmith.android.lib2d.gui.Label;
import ssmith.android.lib2d.gui.ToggleButton;
import ssmith.android.lib2d.layouts.FlowGridLayout;
import ssmith.android.lib2d.shapes.Geometry;
import ssmith.android.util.ImageCache;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.scs.ninja.main.lite.R;
import com.scs.worldcrafter.Statics;
import com.scs.worldcrafter.graphics.blocks.Block;

public class InventoryModule extends AbstractComplexModule {

	private static float icon_size = Statics.SCREEN_WIDTH / 7;//.ICON_SIZE * 1.5f;

	private static String CANCEL = "EXIT";
	private static String EAT = "EAT";

	// Items
	public static final byte HAND = -1; // Note that the number represents how much damage it does!

	private static final int GAP = 5;

	private static Paint paint_icon_border = new Paint();
	private static Paint paint_icon_ink = new Paint();
	private static Paint paint_block_ink = new Paint();

	private GameModule game;
	private ToggleButton btn_eat;
	private Label sublabel;
	private ImageCache img_cache;

	static {
		paint_icon_border.setARGB(255, 0, 200, 0);
		paint_icon_border.setAntiAlias(true);
		paint_icon_border.setStyle(Style.FILL);

		paint_icon_ink.setARGB(255, 255, 255, 255);
		paint_icon_ink.setAntiAlias(true);
		//paint_icon_ink.setStyle(Style.STROKE);

		paint_block_ink.setARGB(255, 255, 255, 255);
		paint_block_ink.setAntiAlias(true);
		//paint_block_ink.setStyle(Style.STROKE);
		paint_block_ink.setTextSize(GUIFunctions.GetTextSizeToFit("iron ore:20", icon_size));
	}


	public InventoryModule(AbstractActivity act, GameModule _return_to) {
		super(act, _return_to);
		
		this.scroll_lr = false;

		CANCEL = act.getString(R.string.return_text);
		EAT = act.getString(R.string.eat);

		paint_icon_ink.setTextSize(GUIFunctions.GetTextSizeToFit(CANCEL, Statics.ICON_SIZE));

		img_cache = new ImageCache(act.getResources());

		game = _return_to;

		Label l = new Label("Title", act.getString(R.string.inventory), null, paint_icon_ink);
		//l.parent.updateGeometricState();
		l.setCentre(Statics.SCREEN_WIDTH/2, paint_icon_ink.getTextSize());
		this.stat_node_front.attachChild(l);

		// Show inventory
		BlockInventory inv = game.inv;
		int cols = 5;//(int)Math.ceil(Math.sqrt(inv.size()));
		FlowGridLayout grid = new FlowGridLayout("Grid", icon_size, icon_size, GAP, cols);

		Bitmap bmp = img_cache.getImage(R.drawable.hand, icon_size, icon_size);
		Button button = new Button(""+HAND, "0", null, paint_icon_ink, bmp);
		grid.attachChild(button);

		Iterator<Byte> it = inv.keySet().iterator();
		boolean has_items = false;
		while (it.hasNext()) {
			has_items = true;
			byte type = it.next();
			int amt = inv.get(type);
			bmp = Block.GetBitmap(img_cache, (byte)type, icon_size, icon_size);
			button = new Button(""+type, Block.GetDesc(type) + ":" + amt, null, paint_block_ink, bmp);
			grid.attachChild(button);
		}
		this.root_node.attachChild(grid);
		this.root_node.updateGeometricState();

		// Show icons
		/*btn_id = new ToggleButton(ID, ID, Statics.SCREEN_WIDTH - Statics.ICON_SIZE, 0, Statics.ICON_SIZE, Statics.ICON_SIZE, null, null, paint_icon_ink, this.img_cache.getImage(R.drawable.menu_frame_green, Statics.ICON_SIZE, Statics.ICON_SIZE), this.img_cache.getImage(R.drawable.menu_frame_blue, Statics.ICON_SIZE, Statics.ICON_SIZE));
		this.stat_node_front.attachChild(btn_id);*/
		btn_eat = new ToggleButton(EAT, EAT, Statics.SCREEN_WIDTH - Statics.ICON_SIZE, 0, null, null, paint_icon_ink, img_cache.getImage(R.drawable.button_green, Statics.ICON_SIZE, Statics.ICON_SIZE), img_cache.getImage(R.drawable.button_blue, Statics.ICON_SIZE, Statics.ICON_SIZE));
		this.stat_node_front.attachChild(btn_eat);
		Button cancel = new Button(CANCEL, CANCEL, Statics.SCREEN_WIDTH - Statics.ICON_SIZE, Statics.ICON_SIZE, paint_icon_border, paint_icon_ink, img_cache.getImage(R.drawable.button_blue, Statics.ICON_SIZE, Statics.ICON_SIZE));
		this.stat_node_front.attachChild(cancel);

		if (has_items) {
			sublabel = new Label("Instructions", act.getString(R.string.select_block), null, paint_icon_ink);
		} else {
			sublabel = new Label("Instructions", act.getString(R.string.no_items), null, paint_icon_ink);
		}
		sublabel.updateGeometricState();
		sublabel.setCentre(Statics.SCREEN_WIDTH/2, Statics.SCREEN_HEIGHT - paint_icon_ink.getTextSize());
		this.stat_node_front.attachChild(sublabel);

		this.stat_node_front.updateGeometricState();

		this.stat_cam.lookAt(stat_node_front, true);
		this.root_cam.lookAt(root_node, true);
	}


	@Override
	public void stopped() {
		img_cache.clear();
	}
	
	
	@Override
	public boolean componentClicked(Geometry c) {
		AbstractActivity act = Statics.act;
		
		if (c instanceof ToggleButton) {
			ToggleButton t = (ToggleButton)c;
			t.toggeSelected();
			
			if (t == btn_eat) {
				sublabel.setText(act.getString(R.string.select_item_to_eat));
			}
		} else if (c instanceof Button) {
			Button button = (Button)c;
			if (button.getActionCommand().equalsIgnoreCase(CANCEL)) {
				//returnToGame();
				super.returnTo();
			} else {
				byte type = Byte.parseByte(button.getActionCommand());
				/*if (this.btn_id.isSelected()) {
					if (type > 0) {
						this.sublabel.setText("That is a " + Block.GetDesc(type) + " block.");
					} else {
						if (type == HAND) {
							this.sublabel.setText("That is the Hand icon, to dig with.");
						}
					}
					this.btn_id.setSelected(false);
				} else {*/
				if (this.btn_eat.isSelected()) {
					if (type > 0) {
						if (Block.GetConsumeValue(type) > 0) { // Is it food?
							this.game.player.incHealth(Block.GetConsumeValue(type));
							game.showToast("Yum yum!");
							game.inv.addBlock(type, -1);
							game.player.setItemType((byte)-1);
							game.setCurrentItemIcon();
							//returnToGame();
							super.returnTo();
						} else {
							sublabel.setText(act.getString(R.string.cant_eat));
						}
					}
				} else {
					game.player.setItemType(type);
					game.setCurrentItemIcon();
					//returnToGame();
					super.returnTo();
				}

			}

		}
		return true;
	}


	/*public boolean onBackPressed() {
		returnToGame();
		return true;
	}


	private void returnToGame() {
		this.getThread().setNextModule(this.game);
	}
*/


	@Override
	public void updateGame(long interpol) {
		// Do nothing

	}

}
