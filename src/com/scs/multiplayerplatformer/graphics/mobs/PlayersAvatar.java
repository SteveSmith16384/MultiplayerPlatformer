package com.scs.multiplayerplatformer.graphics.mobs;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

import ssmith.android.compatibility.Canvas;
import ssmith.android.compatibility.PointF;
import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.MyPointF;
import ssmith.lang.GeometryFuncs;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.BlockInventory;
import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.game.PhysicsEngine;
import com.scs.multiplayerplatformer.graphics.ThrownItem;
import com.scs.multiplayerplatformer.graphics.blocks.Block;
import com.scs.multiplayerplatformer.input.IInputDevice;

public final class PlayersAvatar extends AbstractWalkingMob {

	public float move_x_offset = 0;
	public BlockInventory inv;
	public int playernumZB;

	public IInputDevice input;
	private long firePressedTime;
	private boolean prevThrowPressed = false;
	public Point checkpoint_map;


	public PlayersAvatar(GameModule _game, float x, float y, IInputDevice _input, int num) {
		super(_game, Statics.act.getString("player")  + num, x, y, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT, 3, 100, false, false, Statics.SD_PLAYERS_SIDE, true);

		playernumZB = num;
		input = _input;

		inv = new BlockInventory(this);

		this.setNumFrames(8);

	}


	@Override
	public void process(long interpol) {
		if (frozenUntil < System.currentTimeMillis()) {
			if (is_on_ice == false) {
				move_x_offset = 0;
			}
			if (input.isLeftPressed()) {
				move_x_offset = -1 * input.getStickDistance();
			} else if (input.isRightPressed()) {
				move_x_offset = 1 * input.getStickDistance();
			}

			moving_up = false;
			moving_down = false;
			if (input.isJumpPressed()) {
				startJumping();
			}

			if (input.isUpPressed()) {
				moving_up = true;
			} else if (input.isDownPressed()) {
				moving_down = true;
			}

			boolean throwPressed = input.isThrowPressed(); 
			if (throwPressed) {
				if (!prevThrowPressed) {
					this.firePressedTime = System.currentTimeMillis();
				}
			} else {
				if (prevThrowPressed) {
					throwItem(input.getAngle(), System.currentTimeMillis() - this.firePressedTime);
				}
			}
			this.prevThrowPressed = throwPressed;

			if (move_x_offset != 0) {
				this.move(move_x_offset * Statics.PLAYER_SPEED, 0, false);
			}

		}

		performJumpingOrGravity();
		this.alignWithLadder();
		checkForSuffocation();
	}


	protected void startJumping() {
		if (jumping == false) {
			if (is_on_ground_or_ladder) {
				Statics.act.sound_manager.playerJumped();
				jumping = true;
				phys = new PhysicsEngine(new MyPointF(0, Statics.JUMP_Y*this.bounciness), Statics.ROCK_SPEED, Statics.ROCK_GRAVITY);
			}
		}
	}


	private void throwItem(int angle, float power) {
		power = power / 1000;
		if (power > 1) {
			power = 1;
		} 
		/*if (Statics.DEBUG) {
			Statics.p("Duration: " + power);
			Statics.p("Ang: " + angle);
		}*/
		byte type = getCurrentItemType();
		byte fallback_type = Block.SHURIKEN; // Default

		if (Block.CanBeThrown(type) == false || inv.hasBlock(type) == false) {
			type = fallback_type;
		}

		boolean thrown = false;
		if (inv.hasBlock(type)) {
			thrown = true;
		} else {
			game.showToast("You have nothing to throw!  Try getting a shuriken");
			return;
		}

		if (thrown) {
			//act.sound_manager.playSound("throwitem");
			PointF p = GeometryFuncs.GetPointFromAngle(angle, power*2);
			MyPointF dir = new MyPointF(p.x, p.y);
			//Statics.p("Dir:" + dir);
			if (type == Block.SHURIKEN) {
				ThrownItem.ThrowShuriken(game, this, dir);
			} else {
				ThrownItem.ThrowRock(game, type, this, dir);
			}
			inv.addBlock(type, -1);
		}

	}


	@Override
	public void died() {
		game.playerDied(this);
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol, float scale) {
		super.doDraw(g, cam, interpol, scale);
		if (this.visible) {
			// Draw player number
			g.drawText(""+(playernumZB+1), this.getWindowX(cam, scale), this.getWindowY(cam, scale), paint);
		}
	}


	@Override
	protected void generateBitmaps(int size, float scale) {
		int id = 0;
		a_bmp_left[size][0] = Statics.img_cache.getImage("ninja" + id + "_l0", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_left[size][1] = Statics.img_cache.getImage("ninja" + id + "_l1", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_left[size][2] = Statics.img_cache.getImage("ninja" + id + "_l2", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_left[size][3] = Statics.img_cache.getImage("ninja" + id + "_l3", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_left[size][4] = Statics.img_cache.getImage("ninja" + id + "_l4", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_left[size][5] = Statics.img_cache.getImage("ninja" + id + "_l5", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_left[size][6] = Statics.img_cache.getImage("ninja" + id + "_l6", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_left[size][7] = Statics.img_cache.getImage("ninja" + id + "_l7", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);

		a_bmp_right[size][0] = Statics.img_cache.getImage("ninja" + id + "_r0", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_right[size][1] = Statics.img_cache.getImage("ninja" + id + "_r1", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_right[size][2] = Statics.img_cache.getImage("ninja" + id + "_r2", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_right[size][3] = Statics.img_cache.getImage("ninja" + id + "_r3", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_right[size][4] = Statics.img_cache.getImage("ninja" + id + "_r4", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_right[size][5] = Statics.img_cache.getImage("ninja" + id + "_r5", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_right[size][6] = Statics.img_cache.getImage("ninja" + id + "_r6", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_right[size][7] = Statics.img_cache.getImage("ninja" + id + "_r7", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);

		// Adjust colours
		for (int f=0 ; f<8 ; f++) {
			{
				BufferedImage img = a_bmp_left[size][f];
				adjustImage(img);
			}

			{
				// bmp_right
				BufferedImage img = a_bmp_right[size][f];
				adjustImage(img);
			}
		}
	}


	private void adjustImage(BufferedImage img) {
		int LOWER = 20;
		int UPPER = 240;

		for (int y=0 ; y<img.getHeight() ; y++) {
			for (int x=0 ; x<img.getWidth() ; x++) {
				int i = img.getRGB(x, y);
				Color c = new Color(i);
				if (c.getAlpha() == 255) {
					if (c.getRed() > LOWER && c.getGreen() > LOWER && c.getBlue() > LOWER && c.getRed() < UPPER && c.getGreen() < UPPER && c.getBlue() < UPPER) {
						Color newCol = null; 
						switch (this.playernumZB) {
						case 0:
							newCol = c;
							break;
						case 1:
							newCol = new Color(c.getRed()/2, c.getGreen(), c.getBlue());
							break;
						case 2:
							newCol = new Color(c.getRed(), c.getGreen()/2, c.getBlue());
							break;
						case 3:
							newCol = new Color(c.getRed(), c.getGreen(), c.getBlue()/2);
							break;
						case 4:
							newCol = new Color(c.getRed(), c.getGreen()/2, c.getBlue()/2);
							break;
						case 5:
							newCol = new Color(c.getRed()/2, c.getGreen()/2, c.getBlue());
							break;
						case 6:
							newCol = new Color(c.getRed()/2, c.getGreen()/2, c.getBlue()/2);
							break;
						}
						img.setRGB(x, y, newCol.getRGB());
					}
				}
			}
		}

	}


}
