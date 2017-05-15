package com.scs.multiplayerplatformer.graphics.mobs;

import ssmith.android.compatibility.PointF;
import ssmith.android.framework.AbstractActivity;
import ssmith.android.lib2d.MyPointF;
import ssmith.lang.GeometryFuncs;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.BlockInventory;
import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.game.PhysicsEngine;
import com.scs.multiplayerplatformer.game.Player;
import com.scs.multiplayerplatformer.graphics.ThrownItem;
import com.scs.multiplayerplatformer.graphics.blocks.Block;
import com.scs.multiplayerplatformer.input.IInputDevice;

public class PlayersAvatar extends AbstractWalkingMob {

	public float move_x_offset = 0;
	public BlockInventory inv;
	public int playernumZB, spritenumZB;
	public Player player;

	private IInputDevice input;
	private long firePressedTime;
	private boolean prevThrowPressed = false;
	private long jumpPressedTime;


	public PlayersAvatar(GameModule _game, Player _player, float x, float y, IInputDevice _input) {
		super(_game, Statics.act.getString("player"), x, y, Statics.PLAYER_WIDTH, Statics.PLAYER_HEIGHT, 3, 100, false, false, Statics.SD_PLAYERS_SIDE, false);

		player = _player;
		playernumZB = player.num;
		spritenumZB = playernumZB;
		while (spritenumZB > Statics.MAX_PLAYER_SPRITES) {
			spritenumZB -= Statics.MAX_PLAYER_SPRITES;
		}
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
			} else {
				stoppedJumping();
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
				phys = new PhysicsEngine(new MyPointF(0, Statics.JUMP_Y), Statics.ROCK_SPEED, Statics.ROCK_GRAVITY);
				this.jumpPressedTime = System.currentTimeMillis();
			}
		} else {
			if (System.currentTimeMillis() - this.jumpPressedTime > 1) {
				this.stoppedJumping();
			}
		}
	}
	
	
	private void stoppedJumping() {
		/*if (jumping) {
			jumping = false;
			phys = new PhysicsEngine(new MyPointF(0, Statics.JUMP_Y), Statics.ROCK_SPEED, Statics.ROCK_GRAVITY);
		}*/
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
		if (inv.hasBlock(type) || Statics.DEBUG) {
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
		Statics.act.sound_manager.playerDied();
		game.restartPlayer(this);
	}


	@Override
	protected void generateBitmaps(int size, float scale) {
		a_bmp_left[size][0] = Statics.img_cache.getImage("ninja" + spritenumZB + "_l0", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_left[size][1] = Statics.img_cache.getImage("ninja" + spritenumZB + "_l1", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_left[size][2] = Statics.img_cache.getImage("ninja" + spritenumZB + "_l2", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_left[size][3] = Statics.img_cache.getImage("ninja" + spritenumZB + "_l3", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_left[size][4] = Statics.img_cache.getImage("ninja" + spritenumZB + "_l4", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_left[size][5] = Statics.img_cache.getImage("ninja" + spritenumZB + "_l5", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_left[size][6] = Statics.img_cache.getImage("ninja" + spritenumZB + "_l6", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_left[size][7] = Statics.img_cache.getImage("ninja" + spritenumZB + "_l7", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);

		a_bmp_right[size][0] = Statics.img_cache.getImage("ninja" + spritenumZB + "_r0", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_right[size][1] = Statics.img_cache.getImage("ninja" + spritenumZB + "_r1", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_right[size][2] = Statics.img_cache.getImage("ninja" + spritenumZB + "_r2", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_right[size][3] = Statics.img_cache.getImage("ninja" + spritenumZB + "_r3", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_right[size][4] = Statics.img_cache.getImage("ninja" + spritenumZB + "_r4", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_right[size][5] = Statics.img_cache.getImage("ninja" + spritenumZB + "_r5", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_right[size][6] = Statics.img_cache.getImage("ninja" + spritenumZB + "_r6", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);
		a_bmp_right[size][7] = Statics.img_cache.getImage("ninja" + spritenumZB + "_r7", Statics.PLAYER_WIDTH*scale, Statics.PLAYER_HEIGHT*scale);		
	}



}
