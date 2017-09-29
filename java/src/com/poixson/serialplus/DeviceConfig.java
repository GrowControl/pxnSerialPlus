package com.poixson.serialplus;

import com.poixson.serialplus.enums.Baud;
import com.poixson.serialplus.enums.DataBits;
import com.poixson.serialplus.enums.Parity;
import com.poixson.serialplus.enums.StopBits;
import com.poixson.serialplus.exceptions.SerialInvalidParameterException;
import com.poixson.utils.Utils;
import com.poixson.utils.xTime;


public class DeviceConfig {

	public static final Baud     DEFAULT_BAUD      = Baud.DEFAULT_BAUD;
	public static final DataBits DEFAULT_BYTE_SIZE = DataBits.DEFAULT_BYTE_SIZE;
	public static final StopBits DEFAULT_STOP_BITS = StopBits.DEFAULT_STOP_BITS;
	public static final Parity   DEFAULT_PARITY    = Parity.DEFAULT_PARITY;
	public static final int      DEFAULT_FLAGS     = 0;

	public static final boolean DEFAULT_RTS = false;
	public static final boolean DEFAULT_DTR = false;

	private final String   portName;
	private final Baud     baud;

	private final DataBits byteSize;
	private final StopBits stopBits;
	private final Parity   parity;

	private final boolean  rts;
	private final boolean  dtr;
	private final int      flags;

	private final xTime readTimeout;
	private final xTime readInterval;



	public DeviceConfig(final String portName, final Baud baud,
			final DataBits byteSize, final StopBits stopBits, final Parity parity,
			final Boolean rts, final Boolean dtr, final Integer flags,
			final xTime readTimeout, final xTime readInterval) {
		if (Utils.isEmpty(portName)) throw new SerialInvalidParameterException("portName");
		this.portName = portName;
		this.baud = (
			baud == null
			? DEFAULT_BAUD
			: baud
		);
		this.byteSize = (byteSize == null ? DEFAULT_BYTE_SIZE : byteSize           );
		this.stopBits = (stopBits == null ? DEFAULT_STOP_BITS : stopBits           );
		this.parity   = (parity   == null ? DEFAULT_PARITY    : parity             );
		this.rts      = (rts      == null ? DEFAULT_RTS       : rts.booleanValue() );
		this.dtr      = (dtr      == null ? DEFAULT_DTR       : dtr.booleanValue() );
		this.flags    = (flags    == null ? DEFAULT_FLAGS     : flags.intValue()   );
		this.readTimeout  = xTime.get(readTimeout).setFinal();
		this.readInterval = xTime.get(readInterval).setFinal();
	}



	// port name
	public String getPortName() {
		return this.portName;
	}



	// baud rate
	public Baud getBaud() {
		final Baud baud = this.baud;
		return (
			baud == null
			? Baud.DEFAULT_BAUD
			: baud
		);
	}
	public int getBaudInt() {
		final Baud baud = this.getBaud();
		if (baud == null) throw new NullPointerException("Baud is null");
		return baud.value;
	}



	// byte size
	public DataBits getDataBits() {
		final DataBits bits = this.byteSize;
		return (
			bits == null
			? DataBits.DEFAULT_BYTE_SIZE
			: bits
		);
	}
	public int getDataBitsInt() {
		final DataBits bits = this.getDataBits();
		if (bits == null) throw new NullPointerException("DataBits is null");
		return bits.value;
	}



	// stop bits
	public StopBits getStopBits() {
		final StopBits bits = this.stopBits;
		return (
			bits == null
			? StopBits.DEFAULT_STOP_BITS
			: bits
		);
	}
	public int getStopBitsInt() {
		final StopBits bits = this.getStopBits();
		if (bits == null) throw new NullPointerException("StopBits is null");
		return bits.value;
	}



	// parity
	public Parity getParity() {
		final Parity parity = this.parity;
		return (
			parity == null
			? Parity.DEFAULT_PARITY
			: parity
		);
	}
	public int getParityInt() {
		final Parity parity = this.getParity();
		if (parity == null) throw new NullPointerException("Parity is null");
		return parity.value;
	}



	// line status
	public boolean getRTS() {
		return this.rts;
	}
	public boolean getDTR() {
		return this.dtr;
	}



	// flags
	public int getFlagsInt() {
		return this.flags;
	}



}
