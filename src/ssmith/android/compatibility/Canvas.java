package ssmith.android.compatibility;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.scs.worldcrafter.Statics;

public class Canvas {

	private Graphics g;

	public Canvas(Graphics _g) {
		g = _g;

		Graphics2D g2d = (Graphics2D)g;
		/*g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);*/

		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);   

	}


	public void drawText(String s, float x, float y, Paint p) {
		g.setFont(Statics.stdfnt); // Default
		if (p != null) {
			g.setColor(p.color);
			if (p.getTypeface() != null) {
				g.setFont(p.getTypeface());
			}
		}
		g.drawString(s, (int)x, (int)y);
	}


	public void drawLine(float x1, float y1, float x2, float y2, Paint p) {
		if (p != null) {
			g.setColor(p.color);
			Graphics2D g2 = (Graphics2D)g;
			g2.setStroke(new BasicStroke(p.getStrokeWidth()));
		}
		g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
	}


	public void drawCircleFromCentre(float x1, float y1, float r, Paint p) {
		if (p != null) {
			g.setColor(p.color);
			Graphics2D g2 = (Graphics2D)g;
			g2.setStroke(new BasicStroke(p.getStrokeWidth()));
		}
		g.drawOval((int)(x1-r/2), (int)(y1-r/2), (int)r, (int)r);
	}


	public void drawRect(float x1, float y1, float x2, float y2, Paint p) {
		if (p != null) {
			g.setColor(p.color);
			Graphics2D g2 = (Graphics2D)g;
			g2.setStroke(new BasicStroke(p.getStrokeWidth()));
		}
		g.fillRect((int)x1, (int)y1, (int)(x2-x1), (int)(y2-y1));
	}


	public void drawOval(RectF r, Paint p) {
		if (p != null) {
			g.setColor(p.color);
			Graphics2D g2 = (Graphics2D)g;
			g2.setStroke(new BasicStroke(p.getStrokeWidth()));
		}
		g.drawOval((int)r.left, (int)r.top, (int)(r.right-r.left), (int)(r.bottom-r.top));
	}


	public void drawRect(RectF r, Paint p) {
		drawRect(r.left, r.top, r.right, r.bottom, p);
	}


	public void drawBitmap(BufferedImage b, float x, float y, Paint p) {
		g.drawImage(b, (int)x, (int)y, null);
	}


	public void drawImage(BufferedImage b, float x, float y, Paint p) {
		g.drawImage(b, (int)x, (int)y, null);
	}


	public void translate(float x, float y) {
		g.translate((int)x, (int)y);
	}


	public Graphics getGraphics() {
		return g;
	}

}
