package com.poixson.serialplus.enums;

import java.util.ArrayList;
import java.util.List;


public enum Baud {

	B50      (50),
	B75      (75),
	B110     (110),
	B134     (134),
	B150     (150),
	B200     (200),
	B300     (300),
	B600     (600),
	B1200    (1200),
	B1800    (1800),
	B2400    (2400),
	B4800    (4800),
	B9600    (9600),
	B19200   (19200),
	B38400   (38400),
	B57600   (57600),
	B115200  (115200),
	B230400  (230400),
	B460800  (460800),
	B500000  (500000),
	B576000  (576000),
	B921600  (921600),
	B1000000 (1000000),
	B1152000 (1152000),
	B1500000 (1500000),
	B2000000 (2000000),
	B2500000 (2500000),
	B3000000 (3000000),
	B3500000 (3500000),
	B4000000 (4000000);



	public static final Baud DEFAULT_BAUD = B9600;



	private static final List<Baud> bauds = new ArrayList<Baud>();
	static {
		bauds.add(B50);
		bauds.add(B75);
		bauds.add(B110);
		bauds.add(B134);
		bauds.add(B150);
		bauds.add(B200);
		bauds.add(B300);
		bauds.add(B600);
		bauds.add(B1200);
		bauds.add(B1800);
		bauds.add(B2400);
		bauds.add(B4800);
		bauds.add(B9600);
		bauds.add(B19200);
		bauds.add(B38400);
		bauds.add(B57600);
		bauds.add(B115200);
		bauds.add(B230400);
		bauds.add(B460800);
		bauds.add(B500000);
		bauds.add(B576000);
		bauds.add(B921600);
		bauds.add(B1000000);
		bauds.add(B1152000);
		bauds.add(B1500000);
		bauds.add(B2000000);
		bauds.add(B2500000);
		bauds.add(B3000000);
		bauds.add(B3500000);
		bauds.add(B4000000);
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
