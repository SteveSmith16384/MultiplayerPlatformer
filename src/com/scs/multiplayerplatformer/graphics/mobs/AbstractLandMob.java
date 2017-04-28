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

public abstract class AbstractLandMob extends AbstractMob {

	private BufferedImage bmp_left, bmp_right;
	protected boolean is_on_ground_or_ladder, can_swim;
	protected boolean facing_left = false; // Default to facing right
	protected BufferedImage a_bmp_left[];
	protected BufferedImage a_bmp_right[];
	private int max_frames, curr_frame;
	private long frame_time=0, frame_interval;
	protected boolean jumping = false;
	private PhysicsEngine phys;
	private float curr_fall_speed = Statics.PLAYER_FALL_SPEED;
	public boolean moving_down = false;

	public AbstractLandMob(GameModule _game, String name, float x, float y, float w, float h, byte health, int _max_frames, long _frame_interval, boolean remove_if_far_away, boolean destroy_blocks, byte side, boolean _can_swim) {
		super(_game, name, x, y, w, h, health, remove_if_far_away, destroy_blocks, side);

		max_frames = _max_frames;
		frame_interval = _frame_interval;
		can_swim = _can_swim;

		this.setNumFrames(max_frames);

	}


	protected void setNumFrames(int f) {
		a_bmp_left = new BufferedImage[f];
		a_bmp_right = new BufferedImage[f];

	}


	// This just adjusts the animation before calling the super().
	public boolean move(float off_x, float off_y, boolean ladderBlocks) {
		if (off_x != 0) {
			if (frame_time > frame_interval && frame_interval > 0) {
				frame_time = 0;
				this.curr_frame++;
				if (this.curr_frame >= this.max_frames) {
					this.curr_frame = 0;
				}
			}

			this.bmp_left = this.a_bmp_left[this.curr_frame];
			this.bmp_right = this.a_bmp_right[this.curr_frame];

			if (bmp_left == null || bmp_right == null) {
				throw new RuntimeException("No bitmaps to draw for " + name) ;
			}
		}

		if (off_x < 0) {
			this.facing_left = true;
		} else if (off_x > 0) {
			this.facing_left = false;
		}
		boolean b = super.move(off_x, off_y, ladderBlocks);
		return b;
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		if (this.visible) {
			frame_time += interpol;
			if (facing_left) {
				if (bmp_left == null) {
					bmp_left = a_bmp_left[0];
				}
				g.drawBitmap(bmp_left, this.getWorldX() - cam.left, this.getWorldY() - cam.top, paint);
			} else {
				if (bmp_right == null) {
					bmp_right = a_bmp_right[0];
				}
				g.drawBitmap(bmp_right, this.getWorldX() - cam.left, this.getWorldY() - cam.top, paint);
			}
		}
	}


	public void startJumping() {
		if (is_on_ground_or_ladder && jumping == false) {
			jumping = true;
			//this.jumping_y_off = jump_speed;
			phys = new PhysicsEngine(new MyPointF(0, Statics.JUMP_Y), Statics.ROCK_SPEED, Statics.ROCK_GRAVITY);
		}
	}


	public boolean isJumping() {
		return this.jumping;
	}


	protected void performJumpingOrGravity() {
		is_on_ground_or_ladder = false;
		boolean is_on_ladder = false;
		float ladder_x = 0;
		boolean in_water = false;

		// Check for harmful blocks
		ArrayList<AbstractRectangle> colls = game.new_grid.getColliders(this.getWorldBounds());
		for (AbstractRectangle g : colls) {
			if (g instanceof Block) {
				Block b = (Block)g;
				if (Block.GetHarm(b.getType()) > 0) {
					this.health -= Block.GetHarm(b.getType());
				}
				if (b.getType() == Block.WATER) {
					in_water = true;
				}
				if (Block.BlocksDownMovement(b.getType())) {
					is_on_ground_or_ladder = true;
				}
				if (Block.IsLadder(b.getType())) {
					is_on_ladder = true;
					ladder_x = b.getWorldX();
				}
			}
		}

		// Align us with the ladder
		if (is_on_ladder && this instanceof PlayersAvatar) {
			PlayersAvatar player = (PlayersAvatar)this;
			if (player.move_x_offset == 0) { // add to Android?
				float adj_dist = (Statics.SQ_SIZE - this.getWidth())/2;
				float target_pos = ladder_x + adj_dist;
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

		if (jumping) {
			phys.process();
			if (is_on_ground_or_ladder && this.getJumpingYOff() >= 0) {
				this.jumping = false;
			} else {
				if (this.move(0, this.getJumpingYOff(), false) == false) { // Hit a ceiling?
					this.jumping = false;
				}
			}
		} else {
			if (is_on_ground_or_ladder == false) {
				// Gravity
				if (in_water && can_swim) {
					//is_on_ground_or_ladder = false; // So we can't jump
				} else {
					is_on_ground_or_ladder = (this.move(0, curr_fall_speed, true) == false);
					if (is_on_ground_or_ladder) {
						curr_fall_speed = Statics.PLAYER_FALL_SPEED; // Reset current fall speed
					} else {
						//this.move(0, curr_fall_speed); // Actually fall
						curr_fall_speed = curr_fall_speed * 2f;
						if (curr_fall_speed > Statics.MAX_FALL_SPEED) {
							curr_fall_speed = Statics.MAX_FALL_SPEED;
						}
					}
				}
			} else if (is_on_ladder && moving_down) {
				this.move(0, Statics.PLAYER_SPEED, false);
			}
		}

	}


	/*protected boolean checkIfCanFall() {
		RectF r = new RectF(this.getWorldBounds().left, this.getWorldBounds().top, this.getWorldBounds().right, this.getWorldBounds().bottom);
		r.offset(0,  1);
		// Check for harmful blocks
		ArrayList<AbstractRectangle> colls = game.new_grid.getColliders(r);
		for (AbstractRectangle g : colls) {
			if (g instanceof Block) {
				Block b = (Block)g;
				if (Block.BlocksDownMovement(b.getType())) {
					return false;
				}
			}
		}
		return true;
	}*/


	protected float getJumpingYOff() {
		if (phys != null) {
			return phys.offset.y;
		} else {
			return 0f;
		}
	}


}
