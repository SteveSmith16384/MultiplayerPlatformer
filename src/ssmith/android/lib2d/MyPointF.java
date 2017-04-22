package ssmith.android.lib2d;

import android.graphics.PointF;

public class MyPointF extends PointF {
	
	public MyPointF() {
		this(0, 0);
	}
	
	
	public MyPointF(float x, float y) {
		super(x, y);
	}
	
	
	public String toString() {
		return x + "," + y;
	}
	
	
	public MyPointF subtract(MyPointF o) {
		MyPointF newone = new MyPointF(this.x, this.y);
		
		newone.x -= o.x;
		newone.y -= o.y;
		
		return newone;
	}

	
	public MyPointF multiply(float o) {
		MyPointF newone = new MyPointF(this.x, this.y);
		
		newone.x *= o;
		newone.y *= o;
		
		return newone;
	}

	
	public MyPointF multiplyLocal(float o) {
		x *= o;
		y *= o;
		
		return this;
	}

	
	public MyPointF divideLocal(float o) {
		x /= o;
		y /= o;
		
		return this;
	}

	
	public MyPointF subtractLocal(MyPointF p) {
		x -= p.x;
		y -= p.y;
		
		return this;
	}

	
	public MyPointF notFactorial(float o) {
		MyPointF newone = new MyPointF(this.x, this.y);
		
		newone.x = o / newone.x;
		newone.y = o / newone.y;
		
		return newone;
	}

	
	public MyPointF notFactorialLocal(float o) {
		float len = this.length();
		len = o/len;
		x = x * len;
		y = y * len;
		
		return this;
	}

	
	public MyPointF subtract(float x, float y) {
		MyPointF newone = new MyPointF(this.x, this.y);
		
		newone.x -= x;
		newone.y -= y;
		
		return newone;
	}

	
	public MyPointF add(MyPointF other) {
		MyPointF pf = new MyPointF(x, y);
		pf.x = pf.x + other.x;
		pf.y = pf.y + other.y;
		return pf;
	}

	
	public MyPointF averageLocal(MyPointF other) {
		this.x = (this.x + other.x)/2;
		this.y = (this.y + other.y)/2;
		return this;
	}

	
	public MyPointF normalizeLocal() {
		float len = this.length();
		x = x / len;
		y = y / len;
		return this;
	}

	
	public MyPointF normalize() {
		float len = this.length();
		return new MyPointF(x / len, y / len);
	}


}
