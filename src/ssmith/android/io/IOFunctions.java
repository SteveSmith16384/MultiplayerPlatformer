package ssmith.android.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;

/**
 * 			IOFunctions.WriteSettings(context, "test.txt", "this is a test", Context.MODE_WORLD_READABLE);
			String s = IOFunctions.ReadSettings(context, "test.txt");

			String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
			IOFunctions.AppendLog(sdcard + "/file.txt", "test2");

 *
 */
public class IOFunctions {

	public static String ReadRawText(Activity act, int id) throws IOException {
		BufferedInputStream inputStream = new BufferedInputStream(act.getResources().openRawResource(id));
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		int i = inputStream.read();
		while (i != -1) {
			byteArrayOutputStream.write(i);
			i = inputStream.read();
		}
		inputStream.close();

		return byteArrayOutputStream.toString();
	}


	public static void WriteSettings(Context context, String filename, String data, int mode) throws Exception{
		FileOutputStream fOut = null;
		OutputStreamWriter osw = null;

		try{
			fOut = context.openFileOutput(filename, mode);//Context.MODE_PRIVATE);      
			osw = new OutputStreamWriter(fOut);
			osw.write(data);
			osw.flush();
		}
		catch (Exception e) {
			//e.printStackTrace();
			throw e;
		}
		finally {
			try {
				osw.close();
				fOut.close();
			} catch (IOException e) {
				// Do nothing
			}
		}

	}



	// Read settings
	public static String ReadSettings(Context context, String filename) throws Exception{
		FileInputStream fIn = null;
		InputStreamReader isr = null;

		char[] inputBuffer = new char[255];
		StringBuffer str = new StringBuffer();

		try {
			fIn = context.openFileInput(filename);      
			isr = new InputStreamReader(fIn);
			while (true) {
				int len = isr.read(inputBuffer);
				if (len >= 0) {
					String data = new String(inputBuffer, 0, len);
					str.append(data);
				} else {
					break;
				}
			}
			//Toast.makeText(context, "Settings read",Toast.LENGTH_SHORT).show();
		}
		catch (Exception e) {      
			//e.printStackTrace();
			//Toast.makeText(context, "Settings not read",Toast.LENGTH_SHORT).show();
			throw e;
		}
		finally {
			try {
				isr.close();
				fIn.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		return str.toString();
	}


	public static void SaveText(String filename, String text, boolean append) throws IOException {
		File logFile = new File(filename);
		if (!logFile.exists()) {
			logFile.createNewFile();
		}
		//BufferedWriter for performance, true to set append to file flag
		//BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, append));
		FileWriter buf = new FileWriter(logFile, append);
		if (append) {
			buf.append(text);
		} else {
			buf.write(text);
		}
		//buf.newLine();
		buf.close();
	}


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


	public static void Serialize(Context context, String filename_no_sep, Object obj) throws IOException {	
		FileOutputStream fos = context.openFileOutput(filename_no_sep, Context.MODE_PRIVATE);
		ObjectOutputStream os = new ObjectOutputStream(fos);
		os.writeObject(obj);
		os.close();

	}


	public static Object Deserialize(Context context, String filename_no_sep) throws IOException, ClassNotFoundException {	
		FileInputStream fis = context.openFileInput(filename_no_sep);
		ObjectInputStream is = new ObjectInputStream(fis);
		Object simpleClass = (Object) is.readObject();
		is.close();
		return simpleClass;
	}


	public static boolean FileExists(Context context, String filename_no_sep) {	
		try {
			context.openFileInput(filename_no_sep);
			return true;
		} catch (FileNotFoundException ex) {
			return false;
		}
	}


	public static void Vibrate(Context c, long l) {
		Vibrator v = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);

		// 1. Vibrate for 1000 milliseconds
		v.vibrate(l);
	}
}

