package com.poixson.serialplus;


public enum Baud {

	B9600 (9600);



	public final int value;

	private Baud(final int value) {
		this.value = value;
	}



	public int getValue() {
		return this.value;
	}



}
