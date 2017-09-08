package com.poixson.serialplus;


public enum DataBits {

	DATA_BITS_5 (5),
	DATA_BITS_6 (6),
	DATA_BITS_7 (7),
	DATA_BITS_8 (8);



	public final int value;

	private DataBits(final int value) {
		this.value = value;
	}



	public int getValue() {
		return this.value;
	}



}
