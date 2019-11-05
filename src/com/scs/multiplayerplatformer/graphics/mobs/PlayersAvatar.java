package com.scs.multiplayerplatformer.graphics.mobs;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.Statics.GameMode;
import com.scs.multiplayerplatformer.game.BlockInventory;
import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.game.PhysicsEngine;
import com.scs.multiplayerplatformer.game.Player;
import com.scs.multiplayerplatformer.graphics.ThrownItem;
import com.scs.multiplayerplatformer.graphics.blocks.Block;

import ssmith.android.compatibility.Canvas;
import ssmith.android.compatibility.PointF;
import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.MyPointF;
import ssmith.lang.GeometryFuncs;

public final class PlayersAvatar extends AbstractWalkingMob {

	public float moveXOffset = 0;
	public BlockInventory inv;
	public int playernumZB;

	public Player player;
	private long firePressedTime;
	private boolean prevThrowPressed = false;
	public Point checkpoint_map;


	public PlayersAvatar(GameModule _game, float x, float y, Player _player, int num) {
		super(_game, Statics.act.getString("player")  + num, x, y, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT, 3, 100, false, Statics.SD_PLAYERS_SIDE, false);

		playernumZB = num;
		player = _player;

		inv = new BlockInventory(this);

		this.setNumFrames(8);

	}


	@Override
	public void process(long interpol) {
		if (isOnIce == false) {
			moveXOffset = 0;
		}
		movingUp = false;
		movingDown = false;
		if (frozenUntil < System.currentTimeMillis()) {
			if (player.input.isLeftPressed()) {
				moveXOffset = -1 * player.input.getStickDistance();
			} else if (player.input.isRightPressed()) {
				moveXOffset = 1 * player.input.getStickDistance();
			}

			if (player.input.isJumpPressed()) {
				startJumping();
			}

			if (player.input.isUpPressed()) {
				movingUp = true;
			} else if (player.input.isDownPressed()) {
				movingDown = true;
			}

			boolean throwPressed = player.input.isThrowPressed(); 
			if (throwPressed) {
				if (!prevThrowPressed) {
					this.firePressedTime = System.currentTimeMillis();
				}
			} else {
				if (prevThrowPressed) {
					throwItem(player.input.getAngle(), System.currentTimeMillis() - this.firePressedTime);
				}
			}
			this.prevThrowPressed = throwPressed;

			if (moveXOffset != 0) {
				this.move(moveXOffset * Statics.PLAYER_SPEED, 0, false);
			}

		}

		performJumpingOrGravity();
		this.alignWithLadder();
		checkForSuffocation();
	}


	protected void startJumping() {
		if (jumping == false) {
			if (isOnGroundOrLadder) {
				Statics.act.soundManager.playerJumped();
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
				ThrownItem.throwShuriken(game, this, dir);
			} else {
				ThrownItem.throwRock(game, type, this, dir);
			}
			inv.addBlock(type, -1);
		}

	}


	@Override
	public void died() {
		if (Statics.GAME_MODE == GameMode.RaceToTheDeath) {
			this.frozenUntil = System.currentTimeMillis() + 1000;
			this.isOnGroundOrLadder = true;
			this.startJumping(); // In case we landed on fire
		} else {
			game.playerDied(this);
		}
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol, float scale) {
		super.doDraw(g, cam, interpol, scale);
		if (this.visible) {
			// Draw player number
			g.getGraphics().setColor(Color.white);
			g.drawText(""+(playernumZB+1), this.getWindowX(cam, scale), this.getWindowY(cam, scale), paint);
		}
	}


	@Override
	protected void generateBitmaps(int size, float scale) {
		/*
		 * 						BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
						scaled.getGraphics().drawImage(img, 0, 0, w, h, c);

		 */
		int width = (int)(Statics.PLAYER_WIDTH*scale);
		int height = (int)(Statics.PLAYER_HEIGHT*scale);
		int id = 0;
		for (int i=0 ; i<8 ; i++) {
			a_bmp_left[size][i] = Statics.img_cache.getImage("ninja" + id + "_l"+i, width, height);
			a_bmp_right[size][i] = Statics.img_cache.getImage("ninja" + id + "_r"+i, width, height);
		}
		/*a_bmp_left[size][0] = Statics.img_cache.getImage("ninja" + id + "_l0", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
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
		a_bmp_right[size][7] = Statics.img_cache.getImage("ninja" + id + "_r7", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);*/

		// Adjust colours
		for (int f=0 ; f<8 ; f++) {
			{
				// Left images
				BufferedImage copy = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				copy.getGraphics().drawImage(a_bmp_left[size][f], 0, 0, width, height, null);
				//BufferedImage img = a_bmp_left[size][f];
				adjustImage(copy);
				a_bmp_left[size][f] = copy;
			}

			{
				BufferedImage copy = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				copy.getGraphics().drawImage(a_bmp_right[size][f], 0, 0, width, height, null);
				adjustImage(copy);
				a_bmp_right[size][f] = copy;
				
				// bmp_right
				//BufferedImage img = a_bmp_right[size][f];
				//adjustImage(img);
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
