package ssmith.android.framework;


public class ErrorReporter {

	private ErrorReporter() {
		
	}
	
	
	public static ErrorReporter getInstance() {
		return new ErrorReporter();
	}

}
