package ssmith.android.compatibility;

public class RectF {

	public float left, top, right, bottom;

	public RectF() {
	}


	public RectF(float l, float t, float r, float b) {
		left = l;
		top = t;
		right = r;
		bottom = b;
	}


	public void set(float l, float t, float r, float b) {
		left = l;
		top = t;
		right = r;
		bottom = b;
	}


	public void set(RectF r) {
		left = r.left;
		top = r.top;
		right = r.right;
		bottom = r.bottom;
	}


	public boolean intersect(float left, float top, float right, float bottom) {
		if (this.left < right && left < this.right
				&& this.top < bottom && top < this.bottom) {
			if (this.left < left) {
				this.left = left;
			}
			if (this.top < top) {
				this.top = top;
			}
			if (this.right > right) {
				this.right = right;
			}
			if (this.bottom > bottom) {
				this.bottom = bottom;
			}
			return true;
		}
		return false;
	}


	public static boolean intersects(RectF a, RectF b) {
		return a.left < b.right && b.left < a.right
				&& a.top < b.bottom && b.top < a.bottom;
	}


	public boolean contains(float x, float y) {
		return left < right && top < bottom  // check for empty first
				&& x >= left && x < right && y >= top && y < bottom;
	}



	public final float centerX() {
		return (left + right) * 0.5f;
	}


	public final float centerY() {
		return (top + bottom) * 0.5f;
	}


	public final boolean isEmpty() {
        return left >= right || top >= bottom;
    }

    /**
     * @return the rectangle's width. This does not check for a valid rectangle
     * (i.e. left <= right) so the result may be negative.
     */
    public final float width() {
        return right - left;
    }

    /**
     * @return the rectangle's height. This does not check for a valid rectangle
     * (i.e. top <= bottom) so the result may be negative.
     */
    public final float height() {
        return bottom - top;
    }
    
    
    public void moveTo(float x, float y) {
    	float w = width();
    	float h = height();
    	
    	left = x;
    	top = y;
    	right = left + w;
    	bottom = top + h;
    }


    public void moveBy(float x, float y) {
    	float w = width();
    	float h = height();
    	
    	left += x;
    	top += y;
    	right = left + w;
    	bottom = top + h;
    }
}
