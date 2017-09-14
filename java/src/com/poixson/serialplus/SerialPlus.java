package com.poixson.serialplus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.poixson.serialplus.natives.NativeSerial;
import com.poixson.utils.xCloseable;
import com.poixson.utils.xTime;


public class SerialPlus implements xCloseable {

	public static final long DEFAULT_READ_TIMEOUT  = 1000L;
	public static final long DEFAULT_READ_INTERVAL = 100L;

	protected final String  portName;
	protected final int     baud;
	protected final int     byteSize;
	protected final int     stopBits;
	protected final int     parity;
	protected final boolean setRTS;
	protected final boolean setDTR;
	protected final int     flags;
	protected final boolean blocking;

	protected final xTime timeout  = xTime.get();
	protected final xTime interval = xTime.get();

	protected volatile NativeSerial nat = null;



	public SerialPlus(final String portName, final int baud) {
		this(
			portName,
			Baud.fromInt(baud),
			DataBits.DATA_BITS_8,
			StopBits.STOP_BITS_1,
			Parity.PARITY_NONE,
			false,
			false,
			0,
			true
		);
	}
	public SerialPlus(final String portName,
			final Baud baud, final DataBits byteSize,
			final StopBits stopBits, final Parity parity,
			final boolean setRTS, final Boolean setDTR,
			final int flags, final boolean blocking) {
		this(
			portName,
			( baud     == null ? Baud.DEFAULT_BAUD.value          : baud.value     ),
			( byteSize == null ? DataBits.DEFAULT_BYTE_SIZE.value : byteSize.value ),
			( stopBits == null ? StopBits.DEFAULT_STOP_BITS.value : stopBits.value ),
			( parity   == null ? Parity.DEFAULT_PARITY.value      : parity.value   ),
			setRTS,
			setDTR,
			flags,
			blocking
		);
	}
	public SerialPlus(final String portName,
			final int baud, final int byteSize,
			final int stopBits, final int parity,
			final boolean setRTS, final Boolean setDTR,
			final int flags, final boolean blocking) {
		this.portName = portName;
		this.baud     = baud;
		this.byteSize = byteSize;
		this.stopBits = stopBits;
		this.parity   = parity;
		this.setRTS   = setRTS;
		this.setDTR   = setDTR;
		this.flags    = flags;
		this.blocking = blocking;
		this.nat = new NativeSerial(portName);
	}



	public String getPortName() {
		return this.portName;
	}
	public NativeSerial getNative() {
		return this.nat;
	}
	public long getHandle() {
		return this.nat.getHandle();
	}



	// ------------------------------------------------------------------------------- //
	// open/close port



	public boolean open() {
		if (!this.nat.openPort()) {
			return false;
		}
		if (!this.nat.setParams(
			this.baud,
			this.byteSize,
			this.stopBits,
			this.parity,
			this.flags
			)) {
				return false;
		}
		if (!this.nat.setLineStatus(
			this.setRTS,
			this.setDTR
			)) {
				return false;
		}
		if (!this.nat.setBlocking(
			this.blocking
			)) {
				return false;
		}
		return true;
	}



	@Override
	public void close() throws IOException {
		this.nat.closePort();
	}
	public boolean isFailed() {
		return (this.getHandle() < 0L);
	}
	@Override
	public boolean isClosed() {
		return (this.getHandle() <= 0L);
	}



	// ------------------------------------------------------------------------------- //
	// read (size)



//TODO:
/*
	public void setReadTimeout(final long timeout) {
		this.timeout.set(timeout, xTimeU.MS);
	}
	public void setReadTimeout(final String timeoutStr) {
		this.timeout.set(timeoutStr);
	}
	public void setReadTimeout(final xTime time) {
		this.timeout.set(time);
	}

	public long getTimeout() {
		final long tim = this.timeout.getMS();
		return (
			tim < 1L
			? DEFAULT_READ_TIMEOUT
			: tim
		);
	}



	public void setReadInterval(final long interval) {
		this.interval.set(interval, xTimeU.MS);
	}
	public void setReadInterval(final String intervalStr) {
		this.interval.set(intervalStr);
	}
	public void setReadInterval(final xTime intervalTime) {
		this.interval.set(intervalTime);
	}

	public long getReadInterval() {
		final long intvl = this.interval.getMS();
		return (
			intvl < 1L
			? DEFAULT_READ_INTERVAL
			: intvl
		);
	}
*/



	public byte[] readBytes(final int length)
			throws SerialReadTimeoutException, InterruptedException {
		return this.nat.readBytes(length);
	}
	public String readString(final int length)
			throws SerialReadTimeoutException, InterruptedException {
		final byte[] bytes =
			this.readBytes(length);
		return (
			bytes == null
			? null
			: new String(bytes)
		);
	}



//TODO:
/*
	public String readHex(final int size)
			throws SerialReadTimeoutException, InterruptedException {
		return
			this.readHex(
				size,
				" "
			);
	}
	public String readHex(final int size, final String delim)
			throws SerialReadTimeoutException, InterruptedException {
		final String[] buffer =
			this.readHexArray(
				size
			);
		final StringBuilder result = new StringBuilder();
		boolean insert = false;
		for (final String str : buffer) {
			if (insert) {
				result.append(delim);
			} else {
				insert = true;
			}
			result.append(str);
		}
		return result.toString();
	}
	public String[] readHexArray(final int size)
			throws SerialReadTimeoutException, InterruptedException {
		final int[] buffer =
			this.readIntArray(
				size
			);
		final int len = buffer.length;
		String[] result = new String[len];
		for (int i=0; i<len; i++) {
			final String str =
				Integer.toHexString(
					buffer[i]
				).toUpperCase();
			if (str.length() == 1) {
				result[i] = "0"+str;
			} else {
				result[i] = str;
			}
		}
		return result;
	}



	public int[] readIntArray(final int size)
			throws SerialReadTimeoutException, InterruptedException {
		final byte[] buffer =
			this.nat.readBytes(
				size,
				this.getTimeout(),
				this.getReadInterval()
			);
		final int len = buffer.length;
		int[] result = new int[len];
		for (int i=0; i<len; i++) {
			final int value = (int) buffer[i];
			if (buffer[i] < 0) {
				result[i] = 256 + value;
			} else {
				result[i] = value;
			}
		}
		return result;
	}
*/



	// ------------------------------------------------------------------------------- //
	// read (all)



//TODO:
/*
	public int getInputBytesCount() {
		return this.nat.getInputBytesCount();
	}



	public byte[] readBytes() {
		final int size = this.getInputBytesCount();
		if (size <= 0) {
			return null;
		}
		try {
			return this.readBytes(size);
		} catch (SerialReadTimeoutException ignore) {
		} catch (InterruptedException ignore) {}
		return null;
	}
	public String readString() {
		final int size = this.getInputBytesCount();
		if (size <= 0) {
			return null;
		}
		try {
			return this.readString(size);
		} catch (SerialReadTimeoutException ignore) {
		} catch (InterruptedException ignore) {}
		return null;
	}



	public String readHex() {
		final int size = this.getInputBytesCount();
		if (size <= 0) {
			return null;
		}
		try {
			return this.readHex(size);
		} catch (SerialReadTimeoutException ignore) {
		} catch (InterruptedException ignore) {}
		return null;
	}
	public String readHex(final String delim) {
		final int size = this.getInputBytesCount();
		if (size <= 0) {
			return null;
		}
		try {
			return this.readHex(size, delim);
		} catch (SerialReadTimeoutException ignore) {
		} catch (InterruptedException ignore) {}
		return null;
	}
	public String[] readHexArray() {
		final int size = this.getInputBytesCount();
		if (size <= 0) {
			return null;
		}
		try {
			return
				this.readHexArray(
					size
				);
		} catch (SerialReadTimeoutException ignore) {
		} catch (InterruptedException ignore) {}
		return null;
	}



	public int[] readIntArray() {
		final int size = this.getInputBytesCount();
		if (size <= 0) {
			return null;
		}
		try {
			return
				this.readIntArray(
					size
				);
		} catch (SerialReadTimeoutException ignore) {
		} catch (InterruptedException ignore) {}
		return null;
	}
*/



	// ------------------------------------------------------------------------------- //
	// write



	public boolean writeByte(final byte b) {
		return
			this.nat.writeBytes(
				new byte[] {b}
			);
	}
	public boolean writeBytes(final byte[] bytes) {
		return this.nat.writeBytes(bytes);
	}



	public boolean writeString(final String str) {
		return
			this.writeBytes(
				str.getBytes()
			);
	}
	public boolean writeString(final String str, final String charset)
			throws UnsupportedEncodingException {
		return
			this.writeBytes(
				str.getBytes(charset)
			);
	}



	public boolean writeInt(final int value) {
		final byte b = (byte) value;
		return
			this.writeBytes(
				new byte[] {b}
			);
	}
	public boolean writeIntArray(final int[] values) {
		final int size = values.length;
		byte[] b = new byte[size];
		for (int i=0; i<size; i++) {
			b[i] = (byte) values[i];
		}
		return
			this.writeBytes(
				b
			);
	}



}
