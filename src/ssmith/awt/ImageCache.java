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

public class ImageCache extends Hashtable<String, BufferedImage> {//implements Runnable {

	private static final long serialVersionUID = 1L;

	private static final String RESOURCE_DIR = "assets/gfx/";
	public static final String CACHE_DIR = "./data/imagecache/";

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
		if (filename.indexOf(".") < 0) {
			filename = filename + ".png"; // Default
		}
		try {
			if (filename != null && filename.length() > 0) {
				String key = filename + "_" + w + "_" + h;

				// Is it in the hashmap?
				BufferedImage img = get(key);
				if (img != null) {
					return img;
				}

				// Does the file exist?
				File f = new File(CACHE_DIR + key); 
				if (f.exists()) {
					img = ImageIO.read(f);
					Statics.p("Loading " + key + " from cache");
					put(key, img); // Put it in the hashmap
					return img;
				}

				ClassLoader cl = this.getClass().getClassLoader();
				InputStream is = cl.getResourceAsStream(RESOURCE_DIR + filename);
				if (is != null) {
					img = ImageIO.read(is);

					// Resize it
					if (w < 0) {
						// Scale proportionally
						w = (int)(((float)img.getWidth() / (float)img.getHeight()) * h);
					}
					BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
					scaled.getGraphics().drawImage(img, 0, 0, w, h, c);
					//if (Statics.DEBUG) {
						Statics.p("Generated image " + filename + " of " + w + "," + h);
					//}
					img = scaled;
					put(key, img);

					ImageIO.write(img, "png", f); // Save it

					return img;
				} else {
					throw new FileNotFoundException(filename);
				}
			} else {
				return null;
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}

	}

}
