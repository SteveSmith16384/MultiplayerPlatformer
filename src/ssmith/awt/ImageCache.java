package ssmith.awt;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.scs.multiplayerplatformer.Statics;

public class ImageCache extends Hashtable<String, BufferedImage> implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private static final String IMAGES_DIR = "assets/gfx/";
	private static final String CACHE_DIR = "./data/imagecache";

	private Component c;

	public ImageCache(Component _c) {
		super();

		c = _c;
		
		new File(CACHE_DIR).mkdirs();
	}


	public BufferedImage getImageByKey_HeightOnly(String filename, float h) {
		return getImage(filename, (int)-1, (int)h);
	}
	
	
	public BufferedImage getImage(String filename, float w, float h) {
		return getImage(filename, (int)w, (int)h);
	}
	
	
	public BufferedImage getImage(String filename, int w, int h) {
		String origFilename = filename;
		if (filename != null && filename.length() > 0) {// && !filename.endsWith("/") && !filename.endsWith("\\")) {
			if (!filename.contains(".")) {
				filename = filename + ".png";
			}
			filename = IMAGES_DIR + filename;
			String key = filename + "_" + w + "_" + h;
			BufferedImage img = get(key);
			if (img == null) {
				try {
					String res_filename = filename;
					if (res_filename.startsWith(".")) {
						res_filename = res_filename.substring(2);
					}
					
					BufferedImage bi = null;
					ClassLoader cl = this.getClass().getClassLoader();
					InputStream is = cl.getResourceAsStream(res_filename);
					if (is != null) {
						bi = ImageIO.read(is);
					} else {
						if (filename.endsWith(".png")) {
							// Try jpg
							return getImage(origFilename + ".jpg", w, h);
						} else {
							throw new FileNotFoundException(filename);
						}
					}
					img = bi;

					// Resize it
					if (w < 0) {
						// Scale proportionally
						w = (int)(((float)img.getWidth() / (float)img.getHeight()) * h);
					}
					BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
					scaled.getGraphics().drawImage(img, 0, 0, w, h, c);
					if (Statics.DEBUG) {
						Statics.p("Generated image " + filename + " of " + w + "," + h);
					}
					img = scaled;
					put(key, img);
					
					//ImageIO.write(arg0, arg1, arg2)
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			return img;           
		} else {
			return null;
		}
	}

}
