package ssmith.util;

public class Profiler {
	
	public long start_time, duration;
	
	
	public Profiler() {
		super();
	}
	
	
	public void start() {
		this.start_time = System.currentTimeMillis();
	}
	
	
	public void finish() {
		duration = System.currentTimeMillis() - start_time;
	}
	

}
