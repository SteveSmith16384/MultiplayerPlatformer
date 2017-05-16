package ssmith.android.framework;


public final class ErrorReporter {

	private ErrorReporter() {
		
	}
	
	
	public static ErrorReporter getInstance() {
		return new ErrorReporter();
	}

	
	public void handleSilentException(Throwable t) {
		t.printStackTrace();
	}
}
