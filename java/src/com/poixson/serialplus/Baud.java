package com.poixson.serialplus;

import java.util.ArrayList;
import java.util.List;


public enum Baud {

	B9600 (9600);



	public static final Baud DEFAULT_BAUD = B9600;



	private static final List<Baud> bauds = new ArrayList<Baud>();
	static {
		bauds.add(B9600);
	}



	public final int value;

	private Baud(final int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}



	public static Baud fromInt(final int value) {
		if (value <= 0) {
			return null;
		}
		for (final Baud b : bauds) {
			if (b.value >= value) {
				return b;
			}
		}
		return null;
	}



}
