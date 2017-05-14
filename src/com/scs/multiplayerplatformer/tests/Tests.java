package com.scs.multiplayerplatformer.tests;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;

import ssmith.android.compatibility.PointF;
import ssmith.awt.ImageCache;
import ssmith.lang.GeometryFuncs;

import com.scs.multiplayerplatformer.XMLHelper;

public class Tests {

	public Tests() {
		for (int i=0 ; i<360 ; i += 10) {
			System.out.println("angle=" + i);
			PointF p = GeometryFuncs.GetPointFromAngle(i, 60);
			System.out.println(p);
		}

		JFrame frame = new JFrame();
		frame.setVisible(true);
		ImageCache imageCache = new ImageCache(frame);
		BufferedImage bi = imageCache.getImage("ninja_background2", 100, 100);
		if (bi == null) {
			throw new RuntimeException("Image is null");
		}

		XMLHelper xml = new XMLHelper();
		String s = xml.getString("app_name");
		if (s.equalsIgnoreCase("app_name")) {
			throw new RuntimeException("Cannot get XML");
		}

		InputStream in = this.getClass().getResourceAsStream("/assets/maps/testlevel1.csv");
		if (in == null) {
			throw new RuntimeException("Map file is null");
		} else {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		frame.setVisible(false);
		System.out.println("Tests finished successfully.");
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new Tests();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.exit(0);

	}

}
