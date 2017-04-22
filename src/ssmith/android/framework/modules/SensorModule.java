package ssmith.android.framework.modules;

import ssmith.android.framework.AbstractActivity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public abstract class SensorModule extends AbstractModule implements SensorEventListener {

	private static Paint paint_icon_ink = new Paint();

	private float yaw; // Spin left/right
	private float roll;  // Tilt fwd/back
	private float pitch; // Tilt left/right
	private float yaw_offset, roll_offset, pitch_offset; // Tilt left/right
	private SensorManager mSensorManager = null;
	protected boolean got_offsets = false;

	static {
		paint_icon_ink.setARGB(255, 255, 0, 0);
		paint_icon_ink.setAntiAlias(true);
		//paint_icon_ink.setStyle(Style.STROKE);
		paint_icon_ink.setTextSize(16);
	}
	
	
	public SensorModule(AbstractActivity act, AbstractModule _return_to) {
		super(act, _return_to);

		mSensorManager = (SensorManager)act.getBaseContext().getSystemService(Context.SENSOR_SERVICE);

	}


	public void started() {
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
	}


	public void stopped() {
		try {
			mSensorManager.unregisterListener(this);
		} catch (Exception ex) {
			// Do nothing?
		}
	}


	public void resetOffsets() {
		this.yaw_offset = yaw;
		this.roll_offset = roll;
		this.pitch_offset = pitch;
	}


	public float getYaw() {
		return this.yaw - yaw_offset;
	}


	public float getPitch() {
		return this.pitch - pitch_offset;
	}


	public float getRoll() {
		return this.roll - roll_offset;
	}


	/**
	 * Draws the ship, fuel/speed bars, and background to the provided
	 * Canvas.
	 */
	/*public void doDraw(Canvas g, long interpol) {
		super.doDraw(g, interpol);

		g.drawText("Yaw: " + yaw, 5, 60, paint_icon_ink);
		g.drawText("Pitch: " + pitch, 5, 90, paint_icon_ink);
		g.drawText("Roll: " + roll, 5, 120, paint_icon_ink);

	}*/


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do nothing

	}


	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		synchronized (this) {
			if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
				/* [0] : yaw, rotation around z axis
				 * [1] : pitch, rotation around x axis
				 * [2] : roll, rotation around y axis */

				yaw = sensorEvent.values[0]; // Rotation on Y axis
				pitch = sensorEvent.values[1]; // Tilt left right.  positive numbers is angle left, negative is angle right
				roll = sensorEvent.values[2];  // tilt fwd/backwds.  0 is flat on desk, 90 is on wall

				if (got_offsets == false) {
					got_offsets = true;
					this.resetOffsets();
				}
			}
		}		
	}



}

