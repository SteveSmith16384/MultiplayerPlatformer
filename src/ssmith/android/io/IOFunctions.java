package ssmith.android.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * 			IOFunctions.WriteSettings(context, "test.txt", "this is a test", Context.MODE_WORLD_READABLE);
			String s = IOFunctions.ReadSettings(context, "test.txt");

			String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
			IOFunctions.AppendLog(sdcard + "/file.txt", "test2");

 *
 */
public class IOFunctions {

/*	public static void SaveText(String filename, String text, boolean append) throws IOException {
		if (text == null) {
			text = "";
		}
		File logFile = new File(filename);
		if (!logFile.exists()) {
			logFile.createNewFile();
		}
		//BufferedWriter for performance, true to set append to file flag
		BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, append));
		if (append) {
			buf.append(text);
		} else {
			buf.write(text);
		}
		buf.newLine();
		buf.close();
	}

*/
	public static String LoadText(String filename) throws IOException {
		File logFile = new File(filename);
		if (!logFile.exists()) {
			return null;
		} else {
			StringBuffer str = new StringBuffer();
			BufferedReader buf = new BufferedReader(new FileReader(logFile));
			String s = buf.readLine();
			while (s != null) {
				str.append(s + "\n");
				s = buf.readLine();
			}
			buf.close();
			return str.toString();
		}
	}


	public static void AppendLog(String filename, String text, boolean add_cr) throws IOException {
		File logFile = new File(filename);
		if (!logFile.exists()) {
			logFile.createNewFile();
		}
		//BufferedWriter for performance, true to set append to file flag
		BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
		buf.append(text);
		if (add_cr) {
			buf.newLine();
		}
		buf.close();
	}
/*

	public static void WaitForData(InputStream is, int time) throws IOException {
		// Wait for data
		Functions.delay(100);
		long now = System.currentTimeMillis();
		while (is.available() <= 0) {
			Functions.delay(100);
			if (System.currentTimeMillis() - now > time) {
				throw new IOException("Timeout waiting for data");
			}
		}


	}

*/


}

