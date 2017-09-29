package com.poixson.serialplus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.util.concurrent.atomic.AtomicReference;

import com.poixson.serialplus.drivers.DriverSerial;
import com.poixson.serialplus.exceptions.SerialReadTimeoutException;
import com.poixson.utils.ErrorMode;
import com.poixson.utils.xCloseable;
import com.poixson.utils.xTime;
import com.poixson.utils.exceptions.IORuntimeException;
import com.poixson.utils.xLogger.xLog;


public class SerialPlus implements xCloseable {

	public static final ErrorMode DEFAULT_ERROR_MODE = ErrorMode.LOG;
	protected volatile ErrorMode errorMode = null;

	protected final xTime timeout  = xTime.get();
	protected final xTime interval = xTime.get();

	protected final DeviceConfig cfg;
	protected final AtomicReference<Driver> driver = new AtomicReference<Driver>(null);



	public SerialPlus(final DeviceConfig cfg) {
		this.cfg = cfg;
	}



	public String getPortName() {
		return this.cfg.getPortName();
	}
	public long getHandle() {
		final Driver driver = this.getDriver();
		return (
			driver == null
			? -1
			: driver.getHandle()
		);
	}



	public Driver getDriver() {
		return this.driver.get();
	}
	public NativeDevice getNative() {
		final Driver driver = this.getDriver();
		return (
			driver == null
			? null
			: driver.getNative()
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
	public SerialPlus setErrorMode(final ErrorMode mode) {
		this.errorMode = mode;
		return this;
	}



	// ------------------------------------------------------------------------------- //
	// open/close port



	// load driver and open port
	@SuppressWarnings("resource")
	public boolean open() {
		final ErrorMode errorMode = this.getErrorMode();
		if (this.driver.get() != null) {
			if (ErrorMode.EXCEPTION.equals(errorMode)) {
				throw new RuntimeException("Port is already open!");
			} else
			if (ErrorMode.LOG.equals(errorMode)) {
				this.log().warning("Port is already open!");
			}
			return false;
		}
		final Driver driver;
		try {
			driver = new DriverSerial(this.cfg);
		} catch (IOException e) {
			if (ErrorMode.EXCEPTION.equals(errorMode)) {
				throw new IORuntimeException(e);
			} else
			if (ErrorMode.LOG.equals(errorMode)) {
				this.log().trace(e);
			}
			return false;
		}
		if (!this.driver.compareAndSet(null, driver)) {
			throw new RuntimeException("Port is already open!");
		}
		return true;
	}



	@Override
	public void close() throws IOException {
		final Driver driver = this.getDriver();
		if (driver != null) {
			driver.close();
		}
	}
	public boolean isFailed() {
		final Driver driver = this.getDriver();
		if (driver != null) {
			return driver.isFailed();
		}
		return false;
	}
	@Override
	public boolean isClosed() {
		final Driver driver = this.getDriver();
		if (driver != null) {
			return driver.isClosed();
		}
		return true;
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
return null;
//		return this.driver.readBytes(length);
	}
	public String readString(final int length)
			throws SerialReadTimeoutException, InterruptedException {
return null;
//		final byte[] bytes =
//			this.readBytes(length);
//		return (
//			bytes == null
//			? null
//			: new String(bytes)
//		);
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
return false;
//		return
//			this.driver.writeBytes(
//				new byte[] {b}
//			);
	}
	public boolean writeBytes(final byte[] bytes) {
return false;
//		return this.driver.writeBytes(bytes);
	}



	public boolean writeString(final String str) {
return false;
//		return
//			this.writeBytes(
//				str.getBytes()
//			);
	}
	public boolean writeString(final String str, final String charset)
			throws UnsupportedEncodingException {
return false;
//		return
//			this.writeBytes(
//				str.getBytes(charset)
//			);
	}



	public boolean writeInt(final int value) {
return false;
//		final byte b = (byte) value;
//		return
//			this.writeBytes(
//				new byte[] {b}
//			);
	}
	public boolean writeIntArray(final int[] values) {
return false;
//		final int size = values.length;
//		byte[] b = new byte[size];
//		for (int i=0; i<size; i++) {
//			b[i] = (byte) values[i];
//		}
//		return
//			this.writeBytes(
//				b
//			);
	}



	// logger
	private volatile SoftReference<xLog> _log = null;
	public xLog log() {
		if (this._log != null) {
			final xLog log = this._log.get();
			if (log != null)
				return log;
		}
		final xLog log =
			xLog.getRoot()
				.get("LibLoader");
		this._log = new SoftReference<xLog>(log);
		return log;
	}



}
