package com.poixson.serialplus.enums;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.poixson.serialplus.DeviceConfig;
import com.poixson.serialplus.Driver;
import com.poixson.serialplus.drivers.DriverCh34x;
import com.poixson.serialplus.drivers.DriverD2xx;
import com.poixson.serialplus.drivers.DriverSerial;
import com.poixson.utils.Utils;


public enum DriverMode {

	AUTO   (0),
	SERIAL (1),
	D2XX   (2),
	CH34X  (3);



	public static final DriverMode DEFAULT_DRIVER_MODE = AUTO;



	private static final List<DriverMode> modes = new ArrayList<DriverMode>();
	static {
		modes.add(AUTO);
		modes.add(SERIAL);
		modes.add(D2XX);
		modes.add(CH34X);
	}



	public final int value;

	private DriverMode(final int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}



	public Driver getDriver(final DeviceConfig cfg)
			throws IOException {
		switch (this) {
		case AUTO:
//TODO:
return null;
		case SERIAL:
			return new DriverSerial(cfg);
		case D2XX:
			return new DriverD2xx(cfg);
		case CH34X:
			return new DriverCh34x(cfg);
		default:
		}
		return null;
	}



	public static DriverMode FromString(final String str) {
		if (Utils.isEmpty(str))
			return null;
		switch (str.toLowerCase()) {
		case "auto":
			return AUTO;
		case "serial":
			return SERIAL;
		case "d2xx":
			return D2XX;
		case "ch34x":
			return CH34X;
		default:
		}
		return null;
	}



}
