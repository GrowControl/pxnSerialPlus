package com.poixson.serialplus;


public enum StopBits {

	STOP_BITS_1  (1),
	STOP_BITS_1_5(2),
	STOP_BITS_2  (3);



	public final int value;

	private StopBits(final int value) {
		this.value = value;
	}



	public int getValue() {
		return this.value;
	}



}
