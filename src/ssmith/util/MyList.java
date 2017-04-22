package ssmith.util;

import java.util.ArrayList;

import ssmith.lang.NumberFunctions;

public class MyList<E> extends ArrayList<E> {

	private static final long serialVersionUID = 1L;

	public static MyList<Integer> CreateIntsFromInts(int... args) {
		MyList<Integer> ml = new MyList<Integer>();
		for(int element : args) {
			ml.add(element);
		}
		return ml;
	}


	public static MyList<Integer> CreateFromCSVInts(String s) {
		MyList<Integer> list = new MyList<Integer>();
		String ints[] = s.split(",");
		for (int i=0 ; i<ints.length ; i++) {
			list.add(Integer.parseInt(ints[i]));
		}
		return list;
	}


	public static MyList<Byte> CreateFromCSVBytes(String s) {
		MyList<Byte> list = new MyList<Byte>();
		String bytes[] = s.split(",");
		for (int i=0 ; i<bytes.length ; i++) {
			list.add(NumberFunctions.ParseByte(bytes[i], true));
		}
		return list;
	}


	private MyList() {
		super();
	}


	/**
	 * Need this as we don't remove the item by id but by it's value.
	 */
	public void removeInt(int i) {
		boolean any_removed = true;
		while (any_removed) {
			any_removed = false;
			for(Object element : this) {
				int in = (Integer)element;
				if (in == i) {
					this.remove(element);
					any_removed = true;
					break;
				}
			}
		}
	}


	public void add(Object... args) {
		for(Object element : args) {
			super.add((E)element);
		}
	}


	public String toCSVString() {
		StringBuffer str = new StringBuffer();
		for (Object o : this) {
			str.append(o + ", ");
		}
		str.delete(str.length()-2, str.length()); // Remove last comma
		return str.toString();
	}


	public String toString(String pre, String post) {
		StringBuffer str = new StringBuffer();
		for (Object o : this) {
			str.append(pre + o + post);
		}
		return str.toString();
	}
	
	
	public void setArray(boolean arr[], boolean b) {
		for (Object o : this) {
			arr[Integer.parseInt(o.toString())] = b;
		}
		
	}

}
