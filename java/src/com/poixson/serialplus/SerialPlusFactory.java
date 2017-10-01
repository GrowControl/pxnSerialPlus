package com.poixson.serialplus;

import com.poixson.serialplus.enums.Baud;
import com.poixson.serialplus.enums.DataBits;
import com.poixson.serialplus.enums.Parity;
import com.poixson.serialplus.enums.StopBits;
import com.poixson.serialplus.exceptions.SerialInvalidParameterException;
import com.poixson.utils.ErrorMode;
import com.poixson.utils.Utils;
import com.poixson.utils.xTime;


public class SerialPlusFactory {

	public static final ErrorMode DEFAULT_ERROR_MODE = SerialPlus.DEFAULT_ERROR_MODE;
	private volatile ErrorMode errorMode = null;

	public static final long DEFAULT_READ_TIMEOUT  = 1000L;
	public static final long DEFAULT_READ_INTERVAL = 100L;

	private String   portName = null;
	private Baud     baud     = null;

	private DataBits byteSize = null;
	private StopBits stopBits = null;
	private Parity   parity   = null;

	private Boolean  rts      = null;
	private Boolean  dtr      = null;
	private Integer  flags    = null;

	private final xTime readTimeout  = xTime.get();
	private final xTime readInterval = xTime.get();



	public static SerialPlusFactory get() {
		return new SerialPlusFactory();
	}
	public static SerialPlusFactory get(final String portName) {
		return new SerialPlusFactory(portName);
	}
	public static SerialPlusFactory get(final String portName, final int baud) {
		return new SerialPlusFactory(portName, baud);
	}
	public static SerialPlusFactory get(final String portName, final Baud baud) {
		return new SerialPlusFactory(portName, baud);
	}



	public SerialPlusFactory() {
	}
	public SerialPlusFactory(final String portName) {
		this();
		this.setPortName(portName);
	}
	public SerialPlusFactory(final String portName, final int baud) {
		this();
		this.setPortName(portName);
		this.setBaud(baud);
	}
	public SerialPlusFactory(final String portName, final Baud baud) {
		this();
		this.setPortName(portName);
		this.setBaud(baud);
	}



	public SerialPlus build() throws SerialInvalidParameterException {
		final DeviceConfig cfg = this.getConfig();
		return new SerialPlus(cfg);
	}
	public DeviceConfig getConfig() throws SerialInvalidParameterException {
		final String portName = this.getPortName();
		if (Utils.isBlank(portName))
			throw new SerialInvalidParameterException("portName is required");
		return new DeviceConfig(
			portName,
			this.getBaud(),
			this.getByteSize(),
			this.getStopBits(),
			this.getParity(),
			this.getRTS(),
			this.getDTR(),
			this.getFlagsInt(),
			readTimeout,
			readInterval
		);
	}



	// error mode
	public ErrorMode getErrorMode() {
		final ErrorMode mode = this.errorMode;
		return (
			mode == null
			? DEFAULT_ERROR_MODE
			: mode
		);
	}
	public SerialPlusFactory setErrorMode(final ErrorMode mode) {
		this.errorMode = mode;
		return this;
	}



	// port name
	public SerialPlusFactory setPortName(final String portName) {
		this.portName = portName;
		return this;
	}



	// baud
	public SerialPlusFactory setBaud(final Baud baud) {
		this.baud = baud;
		return this;
	}
	public SerialPlusFactory setBaud(final int value) {
		final Baud baud = Baud.FromInt(value);
		if (baud == null) throw new InvalidParameterException("Invalid baud: "+Integer.toString(value));
		return this.setBaud(baud);
	}



	// bit size
	public SerialPlusFactory setByteSize(final DataBits bits) {
		this.byteSize = bits;
		return this;
	}
	public SerialPlusFactory setByteSize(final int value) {
		final DataBits bits = DataBits.FromInt(value);
		if (bits == null) throw new InvalidParameterException("Invalid bit size: "+Integer.toString(value));
		return this.setByteSize(bits);
	}



	// stop bits
	public SerialPlusFactory setStopBits(final StopBits bits) {
		this.stopBits = bits;
		return this;
	}
	public SerialPlusFactory setStopBits(final String value) {
		final StopBits bits = StopBits.FromString(value);
		if (bits == null) throw new InvalidParameterException("Invalid stop bits: "+value);
		return this.setStopBits(bits);
	}
	public SerialPlusFactory setStopBits(final double value)
			throws SerialInvalidParameterException {
		if (value == -1.0) {
			return this.setStopBits( (StopBits)null );
		}
		final StopBits bits = StopBits.FromDouble(value);
		if (bits == null) throw new SerialInvalidParameterException("stop bits", value);
		return this.setStopBits(bits);
	}
	public SerialPlusFactory setStopBits(final int value) {
		final StopBits bits = StopBits.FromInt(value);
		if (bits == null) throw new InvalidParameterException("Invalid stop bits: "+Integer.toString(value));
		return this.setStopBits(bits);
	}



	// parity
	public SerialPlusFactory setParity(final Parity parity) {
		this.parity = parity;
		return this;
	}
	public SerialPlusFactory setParity(final String value) {
		final Parity parity = Parity.FromString(value);
		if (parity == null) throw new InvalidParameterException("Invalid parity: "+value);
		return this.setParity(parity);
	}



	// line status
	public SerialPlusFactory setRTS(final boolean value) {
		this.rts = Boolean.valueOf(value);
		return this;
	}
	public SerialPlusFactory setDTR(final boolean value) {
		this.dtr = Boolean.valueOf(value);
		return this;
	}



	// flags
	public SerialPlusFactory setFlags(final int flags) {
		this.flags = Integer.valueOf(flags);
		return this;
	}



/*
	// blocking/non-blocking
	public SerialPlusFactory setBlocking(final boolean blocking) {
		this.blocking = blocking;
		return this;
	}
	public SerialPlusFactory setBlocking() {
		return this.setBlocking(true);
	}
	public SerialPlusFactory setNonBlocking() {
		return this.setBlocking(false);
	}
*/



}
