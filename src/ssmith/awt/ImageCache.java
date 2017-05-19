package ssmith.awt;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import ssmith.io.Serialize;

import com.scs.multiplayerplatformer.Statics;

public class ImageCache implements Runnable, Serializable { //extends Hashtable<String, BufferedImage> 

	private static final long serialVersionUID = 1L;

	private static final String RESOURCE_DIR = "assets/gfx/";
	private static final String CACHE_FILE = "./data/imagecache.dat";

	public transient Component c;
	private transient static ImageCache instance;

	private transient Hashtable<String, BufferedImage> cache;

	public static ImageCache GetInstance() {
		if (instance == null) {
			new File("./data/").mkdir();
			if (new File(CACHE_FILE).exists()) {
				try {
					if (Statics.DEBUG) {
						Statics.p("Loading image cache");
					}
					instance = (ImageCache) Serialize.DeserializeObject(CACHE_FILE);
					if (Statics.DEBUG) {
						Statics.p("Finished loading image cache");
					}
				} catch (Exception e) {
					new File(CACHE_FILE).delete();
					e.printStackTrace();
					instance = new ImageCache();
				}
			} else {
				instance = new ImageCache();
			}
		}
		return instance;
	}

	private ImageCache() {
		super();

		cache = new Hashtable<String, BufferedImage>();

		Runtime.getRuntime().addShutdownHook(new Thread(this, "ImageCache_Save"));
	}


	public BufferedImage getImageByKey_HeightOnly(String filename, float h) {
		return getImage(filename, (int)-1, (int)h);
	}


	public BufferedImage getImage(String filename, float w, float h) {
		return getImage(filename, (int)w, (int)h);
	}


	public BufferedImage getImage(String filename, int w, int h) {
		if (c == null) {
			throw new RuntimeException("No component passed to imagecache");
		}

		if (filename.indexOf(".") < 0) {
			filename = filename + ".png"; // Default
		}
		try {
			if (filename != null && filename.length() > 0) {
				String key = filename + "_" + w + "_" + h;

				// Is it in the hashmap?
				BufferedImage img = cache.get(key);
				if (img != null) {
					return img;
				}

				// Does the file exist?
				/*File f = new File(CACHE_DIR + key); 
				if (f.exists()) {
					img = ImageIO.read(f);
					Statics.p("Loading " + key + " from cache");
					put(key, img); // Put it in the hashmap
					return img;
				}*/

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
					//Statics.p("Generated image " + filename + " of " + w + "," + h);
					//}
					img = scaled;
					cache.put(key, img);

					//ImageIO.write(img, "png", f); // Save it

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


	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeInt(cache.size()); // how many images are serialized?
		for (String key : cache.keySet()) {
			out.writeUTF(key);
			ImageIO.write(cache.get(key), "png", out);
		}
	}


	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		final int imageCount = in.readInt();
		cache = new Hashtable<String, BufferedImage>();
		//images = new ArrayList<BufferedImage>(imageCount);
		for (int i=0; i<imageCount; i++) {
			String key = in.readUTF();
			BufferedImage img = ImageIO.read(in);
			cache.put(key, img);
		}
	}


	@Override
	public void run() {
		try {
			if (Statics.DEBUG) {
				Statics.p("Saving image cache");
			}
			Serialize.SerializeObject(CACHE_FILE, instance);
			if (Statics.DEBUG) {
				Statics.p("Finished saving image cache");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
