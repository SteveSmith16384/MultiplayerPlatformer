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

public class ImageCache implements Runnable { 

	private static final String RESOURCE_DIR = "assets/gfx/";
	private static final String CACHE_DIR = "./data/images/";

	public transient Component c;
	private transient static ImageCache instance;

	private transient Hashtable<String, BufferedImage> cache;

	public static synchronized ImageCache GetInstance() {
		if (instance == null) {
			/*new File("./data/").mkdirs();
			if (new File(CACHE_FILE).exists()) {
				try {
					Statics.p("Loading image cache...");
					synchronized (CACHE_FILE) {
						instance = (ImageCache) Serialize.DeserializeObject(CACHE_FILE);
					}
					Statics.p("Finished loading image cache");
				} catch (Exception e) {
					e.printStackTrace();

					Statics.p("Deleting image cache");
					new File(CACHE_FILE).delete();

					instance = new ImageCache();
				}
			} else {*/
			instance = new ImageCache();
			//}
		}
		return instance;
	}


	private ImageCache() {
		super();

		new File(CACHE_DIR).mkdirs();
		cache = new Hashtable<String, BufferedImage>();

		//Runtime.getRuntime().addShutdownHook(new Thread(this, "ImageCache_Save"));

		Thread t = new Thread(this, "ImageCache_Load");
		//t.setPriority(Thread.MAX_PRIORITY);
		t.setDaemon(true);
		t.start();
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
				BufferedImage img = null;
				synchronized (cache) {
					img = cache.get(key);
				}
				if (img != null) {
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

					{
						BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
						scaled.getGraphics().drawImage(img, 0, 0, w, h, c);
						//Statics.p("Generated image " + filename + " of " + w + "," + h);
						img = scaled;
					}

					synchronized (cache) {
						cache.put(key, img);
					}

					// Save it
					File saveAs = new File(CACHE_DIR + key);
					if (saveAs.exists() == false) {
						ImageIO.write(img, "png", saveAs);
					}
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


	/*private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		synchronized (cache) {
			out.writeInt(cache.size()); // how many images are serialized?
			for (String key : cache.keySet()) {
				out.writeUTF(key);
				//out.writeBytes(key + "\n");
				BufferedImage img = cache.get(key); 

				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				ImageIO.write(img, "png", buffer);

				out.writeInt(buffer.size()); // Prepend image with byte count
				buffer.writeTo(out);         // Write image

				//ImageIO.write(img, "png", out);
			}
		}
	}


	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		final int imageCount = in.readInt();
		cache = new Hashtable<String, BufferedImage>();
		synchronized (cache) {
			for (int i=0; i<imageCount; i++) {
				String key = in.readUTF();
				//BufferedImage img = ImageIO.read(in);

				long size = in.readInt(); // Read byte count 

				byte[] buffer = new byte[size];
				in.readFully(buffer); // Make sure you read all bytes of the image

				BufferedImage img = ImageIO.read(new ByteArrayInputStream(buffer));

				cache.put(key, img);
			}
		}
	}


	public static void Save() {
		new Thread(GetInstance(), "ImageCache_Save").start();
	}*/


	@Override
	public void run() {
		/*try {
			Statics.p("Saving image cache...");
			synchronized (CACHE_FILE) {
				Serialize.SerializeObject(CACHE_FILE, instance);
			}
			Statics.p("Finished saving image cache");
		} catch (IOException e) {
			e.printStackTrace();
		}*/

		Statics.p("Loading image cache...");
		File files[] = new File(CACHE_DIR).listFiles();
		for(File file : files) {
			try {
				BufferedImage img = ImageIO.read(file);
				synchronized (cache) {
					cache.put(file.getName(), img);
					//Statics.p("Loaded " + file.getName());
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		Statics.p("Finished loading image cache");

	}

}
