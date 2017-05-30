package com.scs.multiplayerplatformer.graphics.mobs;

import java.awt.image.BufferedImage;

import ssmith.android.compatibility.Canvas;
import ssmith.android.lib2d.Camera;
import ssmith.lang.Functions;
import ssmith.lang.NumberFunctions;
import ssmith.util.ReturnObject;

import com.scs.multiplayerplatformer.Statics;
import com.scs.multiplayerplatformer.game.GameModule;
import com.scs.multiplayerplatformer.graphics.blocks.Block;

public class Wasp extends AbstractMob {

	private static final int TURN_DURATION = 500; // In case can't get to player

	private BufferedImage[][] a_bmp_left;// = new BufferedImage[2];
	private BufferedImage[][] a_bmp_right;// = new BufferedImage[2];
	private int off_x = 1, off_y;
	private boolean facing_left = true;
	private int left_right_timer, up_down_timer;
	//private long frame_time=0, frame_interval;


	public static void Factory(GameModule game, Block gen) { // gen == null for normal appearance
		if (game.getNumProcessInstant() < Statics.MAX_INSTANTS) {
			if (gen == null) {
				float start = game.root_cam.bottom + (Statics.WASP_HEIGHT);
				float left = game.root_cam.left - Statics.WASP_WIDTH;
				if (Functions.rnd(1, 2) == 1) {
					left = game.root_cam.right + Statics.PLAYER_WIDTH;
				}
				while (start >= game.root_cam.top - (Statics.WASP_HEIGHT)) {
					boolean res = Wasp.Subfactory(game, left, start);
					if (res) {
						break;
					} else {
						start -= (Statics.SQ_SIZE);
					}
				}
			} else {
				// Use generator
				Wasp.Subfactory(game, gen.getWorldX(), gen.getWorldY() - (Statics.WASP_HEIGHT));
			}
		}
	}


	public static boolean Subfactory(GameModule game, float x, float y) {
		if (game.isAreaClear(x, y, Statics.WASP_WIDTH, Statics.WASP_HEIGHT, true)) {
			new Wasp(game, x, y);
			return true;
		}
		return false;
	}


	private Wasp(GameModule _game, float x, float y) {
		super(_game, Wasp.class.getSimpleName(), x, y, Statics.WASP_WIDTH, Statics.WASP_HEIGHT, true, false, Statics.SD_ENEMY_SIDE);

		a_bmp_left = new BufferedImage[Statics.MAX_BMP_WIDTH][2];
		a_bmp_right = new BufferedImage[Statics.MAX_BMP_WIDTH][2];

		/*bmp_left[0] = Statics.img_cache.getImage(R.drawable.wasp_l1, Statics.WASP_WIDTH, Statics.WASP_HEIGHT);
		bmp_left[1] = Statics.img_cache.getImage(R.drawable.wasp_l2, Statics.WASP_WIDTH, Statics.WASP_HEIGHT);
		bmp_right[0] = Statics.img_cache.getImage(R.drawable.wasp_r1, Statics.WASP_WIDTH, Statics.WASP_HEIGHT);
		bmp_right[1] = Statics.img_cache.getImage(R.drawable.wasp_r2, Statics.WASP_WIDTH, Statics.WASP_HEIGHT);
*/
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol) {
		/*if (this.visible) {
			if (this.facing_left) {
				g.drawBitmap(bmp_left[Functions.rnd(0, 1)], this.getWorldX() - cam.left, this.getWorldY() - cam.top, paint);
			} else {
				g.drawBitmap(bmp_right[Functions.rnd(0, 1)], this.getWorldX() - cam.left, this.getWorldY() - cam.top, paint);
			}
		}*/
	}


	@Override
	public void doDraw(Canvas g, Camera cam, long interpol, float scale) {
		if (this.visible) {
			//frame_time += interpol;
			int width = (int)(this.getWidth() * scale);
			int frame = Functions.rnd(0, 1);
			if (facing_left) {
				if (a_bmp_left[width][frame] == null) {
					this.generateBitmaps(width, scale);
				}
				g.drawBitmap(a_bmp_left[width][frame], (this.getWorldX()) * scale - cam.left, (this.getWorldY()) * scale - cam.top, paint);
			} else {
				if (a_bmp_right[width][frame] == null) {
					this.generateBitmaps(width, scale);
				}
				g.drawBitmap(a_bmp_right[width][frame], getWindowX(cam, scale), getWindowY(cam, scale), paint);
			}
		}

	}

	
	@Override
	public void died() {
		//game.showToast("Wasp killed");
		this.remove();
	}


	@Override
	public void process(long interpol) {
		ReturnObject<PlayersAvatar> playerTemp = new ReturnObject<>();
		this.getDistanceToClosestPlayer(playerTemp);
		//if (this.isOnScreen(game.root_cam, game.current_scale)) { //dist < Statics.ACTIVATE_DIST) { // Only process if close
			PlayersAvatar player = playerTemp.toReturn;
			if (Functions.rnd(1, 10) == 1 || player == null) {
				// move randomly
				this.move(Functions.rnd(-1, 1) * Statics.WASP_SPEED, 0, false);
				this.move(0, Functions.rnd(-1, 1) * Statics.WASP_SPEED, false);
			} else {
				// Move left/right
				if (this.left_right_timer > 0) {
					this.left_right_timer -= interpol;
				} else {
					float diff = player.getWorldCentreX() - this.getWorldCentreX();
					off_x = NumberFunctions.sign(diff);
				}
				if (this.move(off_x * Statics.WASP_SPEED, 0, false) == false) {
					off_x = off_x * -1;
					this.left_right_timer = TURN_DURATION;
				}

				// Move up/down
				if (this.up_down_timer > 0) {
					this.up_down_timer -= interpol;
				} else {
					float diff = player.getWorldCentreY() - this.getWorldCentreY();
					off_y = NumberFunctions.sign(diff);
				}
				if (this.move(0, off_y * Statics.WASP_SPEED, false) == false) {
					off_y = off_y * -1;
					this.up_down_timer = TURN_DURATION;
				}
			}
		/*} else { //if (dist > Statics.DEACTIVATE_DIST) {
			this.remove();
			// Re-add them to list to create
			this.game.levelData.mobs.add(new SimpleMobData(AbstractMob.WASP, this.getWorldX(), this.getWorldY()));
		}*/
	}


	@Override
	public void remove() {
		//Explosion.CreateExplosion(game, 6, this.getWorldCentreX(), this.getWorldCentreY());
		this.removeFromParent();
		this.game.removeFromProcess(this);
	}


	/*@Override
	protected boolean hasCollidedWith(Geometry g) {
		if (g instanceof PlayersAvatar) {
			AbstractMob am = (AbstractMob)g;
			am.damage(2);
			//return true;
		}
		//return false;
		return true;
	}*/


	private void generateBitmaps(int size, float scale) {
		a_bmp_left[size][0] = Statics.img_cache.getImage("wasp_l1", Statics.WASP_WIDTH*scale, Statics.WASP_HEIGHT*scale);
		a_bmp_left[size][1] = Statics.img_cache.getImage("wasp_l2", Statics.WASP_WIDTH*scale, Statics.WASP_HEIGHT*scale);

		a_bmp_right[size][0] = Statics.img_cache.getImage("wasp_r1", Statics.WASP_WIDTH*scale, Statics.WASP_HEIGHT*scale);
		a_bmp_right[size][1] = Statics.img_cache.getImage("wasp_r2", Statics.WASP_WIDTH*scale, Statics.WASP_HEIGHT*scale);
	}


}
