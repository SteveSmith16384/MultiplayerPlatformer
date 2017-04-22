package com.scs.worldcrafter;

import java.net.URLEncoder;

import ssmith.android.io.WGet_Android_2;


public class SendEventThread extends Thread {
	
	private int eventid;

	public SendEventThread(int _eventid) {
		super("SendEventThread");

		eventid = _eventid;

		start();
	}

	public void run() {
		try {
			new WGet_Android_2("http://www.stellarforces.com/datacapture.cls?p=" + URLEncoder.encode(Statics.NAME) + "&v=" + Statics.VERSION_NAME + "&e=" + eventid, "");
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

}
