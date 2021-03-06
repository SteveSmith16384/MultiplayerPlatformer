package ssmith.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serialize {

	private Serialize() {
	}


	public static void serializeObject(String file, Object e) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(file);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(e);
		out.close();
		fileOut.close();
	}


	public static Object deserializeObject(String file) throws IOException, ClassNotFoundException {
		FileInputStream fileIn = null;
		ObjectInputStream in = null;
		try {
			fileIn = new FileInputStream(file);
			in = new ObjectInputStream(fileIn);
			Object e = in.readObject();
			return e;
		} finally {
			in.close();
			fileIn.close();
		}
	}

}
