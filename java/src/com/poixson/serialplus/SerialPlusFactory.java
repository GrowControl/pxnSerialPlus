package com.poixson.serialplus;

import java.security.InvalidParameterException;

import com.poixson.serialplus.enums.Baud;
import com.poixson.serialplus.enums.DataBits;
import com.poixson.serialplus.enums.Parity;
import com.poixson.serialplus.enums.StopBits;
import com.poixson.utils.Utils;


public class SerialPlusFactory {

	private String   portName = null;
	private Baud     baud     = Baud.B9600;
	private DataBits byteSize = DataBits.DATA_BITS_8;
	private StopBits stopBits = StopBits.STOP_BITS_1;
	private Parity   parity   = Parity.PARITY_NONE;
	private boolean  rts      = false;
	private boolean  dtr      = false;
	private int      flags    = 0;
	private boolean  blocking = true;

//	private xTime readTimeout  = xTime.get();
//	private xTime readInterval = xTime.get();



	public SerialPlusFactory() {}



	public SerialPlus build() {
		if (Utils.isBlank(this.portName)) throw new InvalidParameterException("portName is required");
		if (this.baud == null)            throw new InvalidParameterException("baud is required");
		if (this.byteSize == null)        throw new InvalidParameterException("byteSize is required");
		if (this.stopBits == null)        throw new InvalidParameterException("stopBits is required");
		if (this.parity == null)          throw new InvalidParameterException("parity is required");
		final SerialPlus serial =
			new SerialPlus(
				this.portName,
				this.baud,
				this.byteSize,
				this.stopBits,
				this.parity,
				this.rts,
				this.dtr,
				this.flags,
				this.blocking
			);
		return serial;
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
		final Baud baud = Baud.fromInt(value);
		if (baud == null) throw new InvalidParameterException("Invalid baud: "+Integer.toString(value));
		return this.setBaud(baud);
	}



	// bit size
	public SerialPlusFactory setByteSize(final DataBits bits) {
		this.byteSize = bits;
		return this;
	}
	public SerialPlusFactory setByteSize(final int value) {
		final DataBits bits = DataBits.fromInt(value);
		if (bits == null) throw new InvalidParameterException("Invalid bit size: "+Integer.toString(value));
		return this.setByteSize(bits);
	}




	// stop bits
	public SerialPlusFactory setStopBits(final StopBits bits) {
		this.stopBits = bits;
		return this;
	}
	public SerialPlusFactory setStopBits(final String value) {
		final StopBits bits = StopBits.fromString(value);
		if (bits == null) throw new InvalidParameterException("Invalid stop bits: "+value);
		return this.setStopBits(bits);
	}
	public SerialPlusFactory setStopBits(final int value) {
		final StopBits bits = StopBits.fromInt(value);
		if (bits == null) throw new InvalidParameterException("Invalid stop bits: "+Integer.toString(value));
		return this.setStopBits(bits);
	}



	// parity
	public SerialPlusFactory setParity(final Parity parity) {
		this.parity = parity;
		return this;
	}
	public SerialPlusFactory setParity(final String value) {
		final Parity parity = Parity.fromString(value);
		if (parity == null) throw new InvalidParameterException("Invalid parity: "+value);
		return this.setParity(parity);
	}



	// line status
	public SerialPlusFactory setRTS(final boolean value) {
		this.rts = value;
		return this;
	}
	public SerialPlusFactory setDTR(final boolean value) {
		this.dtr = value;
		return this;
	}



	// flags
	public SerialPlusFactory setFlags(final int flags) {
		this.flags = flags;
		return this;
	}



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



}
