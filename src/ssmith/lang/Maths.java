package ssmith.lang;

public class Maths {

	public static int sign(int a) {
		if (a == 0) {
			return 0;
		}
		else if (a > 0) {
			return 1;
		}
		else {
			return -1;
		}
	}

	
	public static int sign(float a) {
		if (a == 0) {
			return 0;
		}
		else if (a > 0) {
			return 1;
		}
		else {
			return -1;
		}
	}

	
	public static int sign(double a) {
		if (a == 0) {
			return 0;
		}
		else if (a > 0) {
			return 1;
		}
		else {
			return -1;
		}
	}

	
	public static float MakeSameSignAs(float num, float s) {
		if (sign(num) != sign(s)) {
			return num * -1;
		} else {
			return num;
		}
	}

	
	public static int mod(int x) {
		if (x >= 0) {
			return x;
		}
		else {
			return x * -1;
		}
	}

	
	public static float mod(float x) {
		if (x >= 0) {
			return x;
		}
		else {
			return x * -1;
		}
	}

	
	public static double mod(double x) {
		if (x >= 0) {
			return x;
		}
		else {
			return x * -1;
		}
	}

	
	public int remainder(int a, int d) {
		int r = (int) Math.IEEEremainder( (double) a, (double) d);
		if (r < 0) {
			r = r + d;
		}
		return r;
	}

	
}
