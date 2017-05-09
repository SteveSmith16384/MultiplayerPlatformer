package ssmith.android.lib2d.layouts;

import java.awt.Point;
import java.util.ArrayList;

import ssmith.android.compatibility.Canvas;
import ssmith.android.compatibility.Paint;
import ssmith.android.compatibility.RectF;
import ssmith.android.lib2d.Camera;
import ssmith.android.lib2d.shapes.AbstractRectangle;

public class EfficientGridLayout extends AbstractRectangle {

	public static int objects_being_drawn;

	private static Paint default_paint = new Paint();

	static {
		default_paint.setARGB(255, 255, 255, 255);
		default_paint.setAntiAlias(true);
	}

	protected AbstractRectangle blocks[][];
	private float tile_size;
	private int draw_width;
	private int draw_height;
	private int blocks_width, blocks_height;

	public EfficientGridLayout(int w, int h, float _tile_size) {
		super("EfficientGridLayout", null);

		blocks_width = w;
		blocks_height = h;
		blocks = new AbstractRectangle[w][h];
		tile_size = _tile_size;

		this.updateCoordsXYWH(0, 0, w * tile_size, h * tile_size);

		this.collides = false;
	}


	/**
	 * Only GameModule should call this as we'll need to check shadows etc...
	 * @param b
	 * @param x
	 * @param y
	 */
	public void setRectAtMap(AbstractRectangle b, int x, int y) {
		blocks[x][y] = b;
		if (b != null) {
			b.setByLTRB(x*tile_size, y*tile_size, ((x+1)*tile_size)-1, ((y+1)*tile_size)-1); // SCS
			b.updateGeometricState();
		}
	}


	public void removeRectAtMap(int x, int y) {
		blocks[x][y] = null;
	}


	public AbstractRectangle getBlockAtPixel_MaybeNull(float x, float y) {
		int x2 = (int)(x / tile_size);
		int y2 = (int)(y / tile_size);
		if (x2 >=  0 && y2 >= 0 && x2 < blocks.length && y2 < blocks[0].length) {
			return blocks[x2][y2];
		} else {
			return null;
		}
	}


	public Point getMapCoordsFromPixels(float x, float y) {
		int x2 = (int)(x / tile_size);
		int y2 = (int)(y / tile_size);

		return new Point(x2, y2);
	}


	public AbstractRectangle getBlockAtMap_MaybeNull(int x, int y) {
		try {
			return blocks[x][y];
		} catch (ArrayIndexOutOfBoundsException ex) {
			// Do nothing
		}
		return null;
	}


	public ArrayList<AbstractRectangle> getColliders(RectF rect) {
		ArrayList<AbstractRectangle> colls = new ArrayList<AbstractRectangle>();

		draw_width = (int)((rect.right - rect.left) / tile_size);
		draw_height = (int)((rect.bottom - rect.top) / tile_size);

		int s_x = (int)(rect.left / tile_size);
		int s_y = (int)(rect.top / tile_size);

		for (int y=s_y ; y<=s_y + draw_height+1 ; y++) {
			if (y < blocks[0].length) {
				for (int x=s_x ; x<=s_x + draw_width+1 ; x++) {
					if (x >= 0 && y >= 0 && x < blocks.length) {
						if (blocks[x][y] != null) {
							AbstractRectangle block = blocks[x][y];
							if (RectF.intersects(rect, block.getWorldBounds())) {
								colls.add(block);
							}
						}
					}
				}
			}
		}
		return colls;
	}


	public void doDraw(Canvas g, Camera cam, long interpol) {
		draw_width = (int)((cam.right - cam.left) / tile_size);
		draw_height = (int)((cam.bottom - cam.top) / tile_size);

		int s_x = (int)(cam.left / tile_size);
		int s_y = (int)(cam.top / tile_size);

		objects_being_drawn = 0;

		for (int y=s_y ; y<=s_y + draw_height+1 ; y++) {
			if (y >= 0 && y < blocks[0].length) {
				for (int x=s_x ; x<=s_x + draw_width+1 ; x++) {
					if (x >= 0 && x < blocks.length) {
						try {
							if (blocks[x][y] != null) {
								AbstractRectangle block = blocks[x][y];
								block.doDraw(g, cam, interpol); // block.bmp.getWidth()
								objects_being_drawn++;
							}
						} catch (ArrayIndexOutOfBoundsException ex) {
							//AbstractActivity.HandleError(null, ex);
						}
					}
				}
			}
		}

	}


	public int getBlocksWidth() {
		return this.blocks_width;
	}


	public int getBlocksHeight() {
		return this.blocks_height;
	}


}
