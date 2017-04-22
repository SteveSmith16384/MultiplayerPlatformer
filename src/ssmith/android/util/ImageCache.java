package ssmith.android.util;

import java.util.Enumeration;
import java.util.Hashtable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageCache {
	
	//private static final long serialVersionUID = 1L;
	
	private Hashtable<String, Bitmap> ht = new Hashtable<String, Bitmap>();
	private Resources res;
	
	public ImageCache(Resources _res) {
		super();
		
		res = _res;
	}
	
	
	private static String CreateKey(int key, float w, float h) {
		return key + "_" + (int)w + "_" + (int)h;
	}
	
	
	public Bitmap getImage(int key, float w, float h) {
		String key2 = CreateKey(key, w, h);
		if (ht.containsKey(key2) == false) {
			Bitmap bmp = BitmapFactory.decodeResource(res, key);
			Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, (int)w, (int)h, false);
			if (bmp != bmp2) {
				bmp.recycle();
			}
			ht.put(key2, bmp2);
		}
		if (ht.get(key2) == null) {
			throw new RuntimeException("Error getting bitmap " + key);
		}
		return ht.get(key2);
	}


	public Bitmap getImageByKey_WidthOnly(int keyX, float w) {
		Bitmap bmp = BitmapFactory.decodeResource(res, keyX);
/*		return getImageByKey_WidthOnly(bmp, keyX, w);
	}
	

	public Bitmap getImageByKey_WidthOnly(Bitmap bmp, int keyX, float w) {*/
		float scale = w / bmp.getWidth();
		float h = bmp.getHeight() * scale;
		String key = CreateKey(keyX, w, h);
		if (ht.containsKey(key) == false) {
			Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, (int)w, (int)h, true);
			ht.put(key, bmp2);
		}
		if (bmp != ht.get(key)) {
			bmp.recycle();
		}
		return ht.get(key);
	}


	/**
	 * This one automatically keeps the proportions.
	 * @param key
	 * @param w
	 * @return
	 */
	public Bitmap getImageByKey_HeightOnly(int keyX, float h) {
		Bitmap bmp = BitmapFactory.decodeResource(res, keyX);
		float scale = h / bmp.getHeight();
		float w = bmp.getWidth() * scale;
		String key = CreateKey(keyX, w, h);
		if (ht.containsKey(key) == false) {
			Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, (int)w, (int)h, true);
			ht.put(key, bmp2);
		}
		if (bmp != ht.get(key)) {
			bmp.recycle();
		}
		return ht.get(key);
	}


	public Bitmap[] getImages(int key[], float w, float h) {
		Bitmap bmp[] = new Bitmap[key.length];
		for (int i=0 ; i<key.length ; i++) {
			bmp[i] = this.getImage(key[i], w, h);
		}
		return bmp;
	}
	

	public Bitmap[] getImages(int key0, int key1, float w, float h) {
		int key[] = new int[2];
		key[0] = key0;
		key[1] = key1;
		return getImages(key, w, h);
	}

	
	public Bitmap[] getImages(int key0, float w, float h) {
		int key[] = new int[1];
		key[0] = key0;
		return getImages(key, w, h);
	}
	
	
	public void clear() {
		Enumeration<Bitmap> enumr = this.ht.elements();
		while (enumr.hasMoreElements()) {
			Bitmap bmp = enumr.nextElement();
			bmp.recycle();
		}
		ht.clear();
		System.gc();
	}
	
	
	public void remove(int key) {
		Bitmap bmp = this.ht.remove(key);
		if (bmp != null) {
			bmp.recycle();
		}
	}
	

}


