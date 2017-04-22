package ssmith.android.lib2d;

import java.util.Comparator;
import java.util.PriorityQueue;

public class CollisionList extends PriorityQueue<CollisionItem> {//implements Comparator<CollisionItem> {

	private static final long serialVersionUID = 1L;

	public CollisionList() {
		super(5, new Comparator<CollisionItem>() {
	          public int compare(CollisionItem i, CollisionItem j) {
	              return 1;//i.distance - j.distance;
	            }
	          });
	}

	
}

