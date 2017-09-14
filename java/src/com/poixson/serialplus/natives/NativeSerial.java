package com.poixson.serialplus.natives;


public class NativeSerial {

	public static final long ERR_UNKNOWN_FAIL          = -1L;
	public static final long ERR_PORT_NOT_FOUND        = -2L;
	public static final long ERR_PORT_BUSY             = -3L;
	public static final long ERR_PERMISSION_DENIED     = -4L;
	public static final long ERR_INCORRECT_SERIAL_PORT = -5L;

	protected final String portName;
	protected volatile long handle = 0L;



	public NativeSerial(final String portName) {
		this.portName = portName;
		// check library version
//TODO:
	}



	// ------------------------------------------------------------------------------- //
	// native calls



	public native long natOpenPort(String portName);
	public native boolean natClosePort(long handle);

	public native long natSetParams(long handle, int baud,
		int byteSize, int stopBits, int parity, int flags);
	public native long natSetBlocking(long handle, boolean blocking);
	public native long natSetVMinVTime(long handle, int vMin, int vTime);
	public native long natSetLineStatus(long handle, boolean setRTS, boolean setDTR);

	public native int natGetInputBytesCount(long handle);
	public native int natGetOutputBytesCount(long handle);

	public native int natReadBytes(long handle, byte[] bytes, int length);
	public native boolean natWriteBytes(long handle, byte[] buffer);



	public long getHandle() {
		return this.handle;
	}



	// ------------------------------------------------------------------------------- //
	// open/close port



	public boolean openPort() {
		if (this.handle != 0L) {
			this.closePort();
		}
		final long h = this.natOpenPort(this.portName);
		this.handle = h;
		return (h > 0L);
	}
	public void closePort() {
		final long h = this.handle;
		this.handle = 0;
		if (h > 0L) {
			this.natClosePort(h);
		}
	}



	// baud/byte-size/stop-bits/parity/flags
	public boolean setParams(final int baud,
			final int byteSize, final int stopBits,
			final int parity, final int flags) {
		final long h = this.handle;
		if (h <= 0L) {
			return false;
		}
		final long result =
			this.natSetParams(
				h,
				baud,
				byteSize,
				stopBits,
				parity,
				flags
			);
		if (result <= 0L) {
			this.handle = result;
			return false;
		}
		return true;
	}
	// set blocking
	public boolean setBlocking(final boolean blocking) {
		final long h = this.handle;
		if (h <= 0L) {
			return false;
		}
		final long result =
			this.natSetBlocking(
				h,
				true
			);
		if (result <= 0L) {
			this.handle = result;
			return false;
		}
		return true;
	}
	// set line status
	public boolean setLineStatus(final boolean setRTS, final boolean setDTR) {
		final long h = this.handle;
		if (h <= 0L) {
			return false;
		}
		final long result =
			this.natSetLineStatus(
				h,
				false,
				false
			);
		if (result <= 0L) {
			this.handle = result;
			return false;
		}
		return true;
	}



	// ------------------------------------------------------------------------------- //
	// read/write



//	protected void waitBytesTimeout(final String methodName, final int size,
//			final long timeout, final long readInterval)
//			throws SerialReadTimeoutException, InterruptedException {
//		final Thread thread = Thread.currentThread();
//		final long startTime = Utils.getSystemMillis();
//		while (true) {
//			// check input buffer
//			final int count = this.getInputBytesCount();
//			if (count < 0) {
//TODO: throw an exception
//				return;
//			}
//			if (count >= size) {
//				return;
//			}
//			// check interrupted
//			if (thread.isInterrupted()) {
//				throw new InterruptedException();
//			}
//			// check timeout
//			final long now = Utils.getSystemMillis();
//			final long sinceStart = now - startTime;
//			if (sinceStart >= timeout) {
//				throw new SerialReadTimeoutException(
//					this.portName,
//					methodName,
//					timeout,
//					size
//				);
//			}
//			// wait a moment
//			ThreadUtils.Sleep(readInterval);
//TODO: remove this
//System.out.print(" .");
//		}
//	}



	public int getInputBytesCount() {
		final long h = this.handle;
		if (h <= 0L) {
			return -1;
		}
		return this.natGetInputBytesCount(h);
	}
	public int getOutputBytesCount() {
		final long h = this.handle;
		if (h <= 0L) {
			return -1;
		}
		return this.natGetOutputBytesCount(h);
	}



	public byte[] readBytes(final int length) {
		final long h = this.handle;
		if (h <= 0L) {
			return null;
		}
		byte[] bytes = new byte[length];
		final int result =
			this.natReadBytes(
				handle,
				bytes,
				length
			);
		if (result > 0) {
			return bytes;
		}
		return null;
	}



	public boolean writeBytes(final byte[] bytes) {
		final long h = this.handle;
		if (h <= 0L) {
			return false;
		}
		return
			this.natWriteBytes(
				h,
				bytes
			);
	}



}
