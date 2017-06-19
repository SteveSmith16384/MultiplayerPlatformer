package com.scs.multiplayerplatformer.graphics.mobs;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import ssmith.android.compatibility.Canvas;
import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.MyPointF;
import ssmith.android.lib2d.shapes.AbstractRectangle;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.game.PhysicsEngine;
import com.scs.multiplayerplatformer.graphics.blocks.Block;

public abstract class AbstractWalkingMob extends AbstractMob {

	protected boolean canSwim;
	protected boolean isOnGroundOrLadder;
	protected float bounciness = 1;
	protected float stickiness = 1;
	protected boolean facingLeft = false;
	protected BufferedImage a_bmp_left[][]; // Size/FrameNum
	protected BufferedImage a_bmp_right[][]; // Size/FrameNum
	private int maxFrames, currFrame;
	private long frameTime=0, frameInterval;
	protected boolean jumping = false;
	protected PhysicsEngine phys;
	private float currFallSpeed = Statics.PLAYER_FALL_SPEED;
	public boolean movingUp = false;
	public boolean movingDown = false;
	protected boolean isOnLadder;
	protected float ladderX;

	public AbstractWalkingMob(GameModule _game, String name, float x, float y, float w, float h, int _max_frames, long _frame_interval, boolean destroy_blocks, byte side, boolean _can_swim) {
		super(_game, name, x, y, w, h, destroy_blocks, side);

		maxFrames = _max_frames;
		frameInterval = _frame_interval;
		canSwim = _can_swim;

		this.setNumFrames(maxFrames);

	}


	protected void setNumFrames(int f) {
		a_bmp_left = new BufferedImage[Statics.MAX_BMP_WIDTH][f];
		a_bmp_right = new BufferedImage[Statics.MAX_BMP_WIDTH][f];

	}


	// This just adjusts the animation before calling the super().
	// Returns true of move() was successful
	@Override
	public boolean move(float off_x, float off_y, boolean ladderBlocks) {
		if (off_x != 0) {
			if (frameTime > frameInterval && frameInterval > 0) {
				frameTime = 0;
				this.currFrame++;
				if (this.currFrame >= this.maxFrames) {
					this.currFrame = 0;
				}
			}
		}

		if (off_x < 0) {
			this.facingLeft = true;
		} else if (off_x > 0) {
			this.facingLeft = false;
		}
		boolean b = super.move(off_x, off_y, ladderBlocks);
		return b;
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		// Do nothing
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol, float scale) {
		if (this.visible) {
			frameTime += interpol;
			int width = (int)(this.getWidth() * scale);
			if (facingLeft) {
				if (a_bmp_left[width][this.currFrame] == null) {
					this.generateBitmaps(width, scale);
				}
				g.drawBitmap(a_bmp_left[width][this.currFrame], this.getWindowX(cam, scale), this.getWindowY(cam, scale), paint);
			} else {
				if (a_bmp_right[width][this.currFrame] == null) {
					this.generateBitmaps(width, scale);
				}
				g.drawBitmap(a_bmp_right[width][this.currFrame],this.getWindowX(cam, scale), getWindowY(cam, scale), paint);
			}
		}

	}


	protected abstract void generateBitmaps(int size, float scale);

	protected void startJumping() {
		if (isOnGroundOrLadder && jumping == false) {
			Statics.act.soundManager.playerJumped();
			jumping = true;
			phys = new PhysicsEngine(new MyPointF(0, Statics.JUMP_Y), Statics.ROCK_SPEED, Statics.ROCK_GRAVITY);
		}
	}


	protected boolean isJumping() {
		return this.jumping;
	}


	protected void performJumpingOrGravity() {
		isOnGroundOrLadder = false;
		isOnLadder = false;
		ladderX = 0;
		boolean in_water = false;
		bounciness = 1f;//.5f; // Min cos we can only go up
		stickiness = .5f; // Min cos we can only go up

		// Check for special blocks we might be touching - NOT by gravity!
		ArrayList<AbstractRectangle> colls = game.blockGrid.getColliders(this.getWorldBounds());
		for (AbstractRectangle g : colls) {
			if (g instanceof Block) {
				Block b = (Block)g;
				if (b.getType() == Block.WATER) {
					in_water = true;
				}
				if (Block.BlocksDownMovement(b.getType())) {
					isOnGroundOrLadder = true;
				}
				if (Block.IsLadder(b.getType())) {
					isOnLadder = true;
					ladderX = b.getWorldX();
				}
			}
		}

		if (jumping) {
			phys.process();
			if (isOnGroundOrLadder && this.getJumpingYOff() >= 0) {
				this.jumping = false;
			} else {
				if (this.move(0, this.getJumpingYOff(), false) == false) { // Hit a ceiling?
					this.jumping = false;
				} else {
					// Have we fallen off bottom of map?
					if (this.getWorldY() > game.blockGrid.getHeight() * 2) {
						this.died(); // To restart
						return;
					}
				}
			}
		} else {
			if (isOnGroundOrLadder == false) {
				// Gravity
				if (!in_water || !canSwim) {
					boolean moved = this.move(0, currFallSpeed, true);
					if (moved) {
						isOnGroundOrLadder = false;
					} else {
						isOnGroundOrLadder = true;
					}
					if (isOnGroundOrLadder) {
						currFallSpeed = Statics.PLAYER_FALL_SPEED; // Reset current fall speed
					} else {
						currFallSpeed = currFallSpeed * 2f;
						if (currFallSpeed > Statics.MAX_FALL_SPEED) {
							currFallSpeed = Statics.MAX_FALL_SPEED;
						}
					}
				}
			}
			if (isOnGroundOrLadder) {
				if (this.movingUp) {
					this.move(0, -Statics.PLAYER_SPEED, false);
				} else if (movingDown) {
					this.move(0, Statics.PLAYER_SPEED, false);
				}
			}
		}

	}


	@Override
	void hitBlockCheck(Block b, float off_x, float off_y) {
		if (off_y > 0) {
			bounciness = Math.max(this.bounciness, Block.GetBounciness(b.getType()));
			stickiness = Math.max(this.stickiness, Block.GetStickiness(b.getType()));
		}
	}


	protected void alignWithLadder() {
		// Align us with the ladder
		if (isOnLadder) {
			PlayersAvatar player = (PlayersAvatar)this;
			if (player.moveXOffset == 0) {
				float adj_dist = (Statics.SQ_SIZE - this.getWidth())/2;
				float target_pos = ladderX + adj_dist;
				float MOVE_DIST = Statics.PLAYER_SPEED/2;
				float diff = Math.abs(this.getWorldX() - target_pos);
				if (diff > MOVE_DIST) {
					if (this.getWorldX() > target_pos) {
						this.adjustLocation(-MOVE_DIST, 0);
						this.updateGeometricState();
					} else if (this.getWorldX() < target_pos) {
						this.adjustLocation(MOVE_DIST, 0);
						this.updateGeometricState();
					}
				}
			}
		}

	}


	protected float getJumpingYOff() {
		if (phys != null) {
			return phys.offset.y;
		} else {
			return 0f;
		}
	}

}
