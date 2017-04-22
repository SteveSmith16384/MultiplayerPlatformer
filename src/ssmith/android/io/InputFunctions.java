package ssmith.android.io;

import android.view.MotionEvent;

public class InputFunctions {
	
	/*public static boolean WasDragEvent(MotionEvent ev) {
		float sx = ev.getX(0);
		float sy = ev.getY(0);
		float ex = ev.getX(ev.getPointerCount()-1);
		float ey = ev.getY(ev.getPointerCount()-1);
		return GeometryFuncs.distance(sx, sy, ex, ey) > 2;
	}*/

	
	public static void PrintSamples(MotionEvent ev) {
	     final int historySize = ev.getHistorySize();
	     final int pointerCount = ev.getPointerCount();
	     for (int h = 0; h < historySize; h++) {
	         System.out.printf("At time %d:", ev.getHistoricalEventTime(h));
	         for (int p = 0; p < pointerCount; p++) {
	             System.out.printf("  pointer %d: (%f,%f)",
	                 ev.getPointerId(p), ev.getHistoricalX(p, h), ev.getHistoricalY(p, h));
	         }
	     }
	     System.out.printf("At time %d:", ev.getEventTime());
	     for (int p = 0; p < pointerCount; p++) {
	         System.out.printf("  pointer %d: (%f,%f)",
	             ev.getPointerId(p), ev.getX(p), ev.getY(p));
	     }
	 }

}
