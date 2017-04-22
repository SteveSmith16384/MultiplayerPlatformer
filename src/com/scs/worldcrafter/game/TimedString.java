package com.scs.worldcrafter.game;

public class TimedString implements IProcessable {
	
	private String text = "";
	private long duration;
	private long expires;
	
	public TimedString(GameModule game, long dur) {
		duration = dur;
		this.setText("");
		
		game.addToProcess_Instant(this);
	}

	
	public String toString() {
		return this.text;
	}
	
	
	public void setText(String s) {
		text = s;
		expires = System.currentTimeMillis() + duration;
	}
	
	
	@Override
	public void process(long interpol) {
		//timer += interpol;
		if (this.expires < System.currentTimeMillis()) {
			this.text = "";
		}
		
	}
	
	

}
