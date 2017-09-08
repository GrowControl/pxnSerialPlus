package com.poixson.serialplus;


public enum Parity {

	PARITY_NONE (0),
	PARITY_ODD  (1),
	PARITY_EVEN (2),
	PARITY_MARK (3),
	PARITY_SPACE(4);



	public final int value;

	private Parity(final int value) {
		this.value = value;
	}



	public int getValue() {
		return this.value;
	}



}
