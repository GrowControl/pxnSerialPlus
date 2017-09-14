package com.poixson.serialplus.enums;

import com.poixson.utils.Utils;

public enum StopBits {

	STOP_BITS_1  (1),
	STOP_BITS_1_5(2),
	STOP_BITS_2  (3);



	public static final StopBits DEFAULT_STOP_BITS = STOP_BITS_1;



	public final int value;

	private StopBits(final int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}



	public static StopBits fromString(final String value) {
		if (Utils.isEmpty(value)) {
			return null;
		}
		switch (value.toLowerCase()) {
		case "1":
			return STOP_BITS_1;
		case "1.5":
		case "1_5":
		case "15":
			return STOP_BITS_1_5;
		case "2":
			return STOP_BITS_2;
		}
		return null;
	}
	public static StopBits fromInt(final int value) {
		if (value <= 0) {
			return null;
		}
		switch (value) {
		case 1:
			return STOP_BITS_1;
		case 15:
			return STOP_BITS_1_5;
		case 2:
			return STOP_BITS_2;
		}
		return null;
	}



}
