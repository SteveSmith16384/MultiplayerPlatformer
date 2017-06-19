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

	private BufferedImage[][] bmpLeft;// = new BufferedImage[2];
	private BufferedImage[][] bmpRight;// = new BufferedImage[2];
	private int offX = 1, offY;
	private boolean facingLeft = true;
	private int leftRightTimer, upDownTimer;

	public static void factory(GameModule game, Block gen) { // gen == null for normal appearance
		if (game.getNumProcessInstant() < Statics.MAX_INSTANTS) {
			if (gen == null) {
				float start = game.rootCam.bottom + (Statics.WASP_HEIGHT);
				float left = game.rootCam.left - Statics.WASP_WIDTH;
				if (Functions.rnd(1, 2) == 1) {
					left = game.rootCam.right + Statics.PLAYER_WIDTH;
				}
				while (start >= game.rootCam.top - (Statics.WASP_HEIGHT)) {
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
		super(_game, Wasp.class.getSimpleName(), x, y, Statics.WASP_WIDTH, Statics.WASP_HEIGHT, false, Statics.SD_ENEMY_SIDE);

		bmpLeft = new BufferedImage[Statics.MAX_BMP_WIDTH][2];
		bmpRight = new BufferedImage[Statics.MAX_BMP_WIDTH][2];

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
			if (facingLeft) {
				if (bmpLeft[width][frame] == null) {
					this.generateBitmaps(width, scale);
				}
				g.drawBitmap(bmpLeft[width][frame], (this.getWorldX()) * scale - cam.left, (this.getWorldY()) * scale - cam.top, paint);
			} else {
				if (bmpRight[width][frame] == null) {
					this.generateBitmaps(width, scale);
				}
				g.drawBitmap(bmpRight[width][frame], getWindowX(cam, scale), getWindowY(cam, scale), paint);
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
				if (this.leftRightTimer > 0) {
					this.leftRightTimer -= interpol;
				} else {
					float diff = player.getWorldCentreX() - this.getWorldCentreX();
					offX = NumberFunctions.sign(diff);
				}
				if (this.move(offX * Statics.WASP_SPEED, 0, false) == false) {
					offX = offX * -1;
					this.leftRightTimer = TURN_DURATION;
				}

				// Move up/down
				if (this.upDownTimer > 0) {
					this.upDownTimer -= interpol;
				} else {
					float diff = player.getWorldCentreY() - this.getWorldCentreY();
					offY = NumberFunctions.sign(diff);
				}
				if (this.move(0, offY * Statics.WASP_SPEED, false) == false) {
					offY = offY * -1;
					this.upDownTimer = TURN_DURATION;
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
		bmpLeft[size][0] = Statics.img_cache.getImage("wasp_l1", Statics.WASP_WIDTH*scale, Statics.WASP_HEIGHT*scale);
		bmpLeft[size][1] = Statics.img_cache.getImage("wasp_l2", Statics.WASP_WIDTH*scale, Statics.WASP_HEIGHT*scale);

		bmpRight[size][0] = Statics.img_cache.getImage("wasp_r1", Statics.WASP_WIDTH*scale, Statics.WASP_HEIGHT*scale);
		bmpRight[size][1] = Statics.img_cache.getImage("wasp_r2", Statics.WASP_WIDTH*scale, Statics.WASP_HEIGHT*scale);
	}


	@Override
	void hitBlockCheck(Block b, float off_x, float off_y) {
		// Do nothing
		
	}


}
