package com.poixson.serialplus.drivers;

import java.io.IOException;

import com.poixson.serialplus.DeviceConfig;
import com.poixson.serialplus.Driver;
import com.poixson.serialplus.NativeDevice;
import com.poixson.serialplus.natives.NativeSerial;
import com.poixson.utils.Utils;


public class DriverSerial extends Driver {

	public static final long ERR_UNKNOWN_FAIL          = -1L;
	public static final long ERR_PORT_NOT_FOUND        = -2L;
	public static final long ERR_PORT_BUSY             = -3L;
	public static final long ERR_PERMISSION_DENIED     = -4L;
	public static final long ERR_INCORRECT_SERIAL_PORT = -5L;



	public DriverSerial(final DeviceConfig cfg)
			throws IOException {
		super(
			cfg,
			new NativeSerial()
		);
	}



	public NativeSerial getNativeSerial() {
		return (NativeSerial) this.nat;
	}



	@Override
	protected void open() throws IOException {
		synchronized (this.handle){
			Utils.safeClose(this);
			final String portName = this.cfg.getPortName();
			final NativeDevice nat = this.getNative();
			// open the port
			final long h = nat.natOpenPort(portName);
			if (!this.handle.compareAndSet(0L, h)) {
				Utils.safeClose(this);
				throw new IOException("Port handle already set?!");
			}
			if (h <= 0L) {
				throw new IOException("Failed to open port, error code: "+Long.toString(h));
			}
			// configure the port
			this.setParams();
		}
	}
	// configure port
	@Override
	protected void setParams() throws IOException {
		final long h = this.getHandle();
		final DeviceConfig cfg = this.getConfig();
		if (h <= 0L) {
			throw new IOException("Invalid port handle: "+Long.toString(h));
		}
		final NativeDevice nat = this.getNative();
		// configure port
		{
			final long result =
				nat.natSetParams(
					h,
					cfg.getBaudInt(),
					cfg.getDataBitsInt(),
					cfg.getStopBitsInt(),
					cfg.getParityInt(),
					cfg.getFlagsInt()
				);
			// failed to set params
			if (result <= 0L) {
				this.handle.set(result);
				throw new IOException("Failed to configure port, error code: "
						+Long.toString(result));
			}
		}
		// set line status
		{
			final long result =
				nat.natSetLineStatus(
					h,
					cfg.getRTS(),
					cfg.getDTR()
				);
			// failed to set line status
			if (result <= 0L) {
				this.handle.set(result);
				throw new IOException("Failed to set line status on port, error code: "
						+Long.toString(result));
			}
		}
		// configure blocking mode
		{
			final long result =
				nat.natSetBlocking(h, true);
			// failed to set blocking mode
			if (result <= 0L) {
				this.handle.set(result);
				throw new IOException("Failed to set blocking mode on port, error code: "
						+Long.toString(result));
			}
		}
	}



	@Override
	protected void close(final long handle) throws IOException {
		// already closed
		if (handle == 0L)
			return;
		final NativeDevice nat = this.getNative();
		nat.natClosePort(handle);
	}



//TODO:
//	@Override
//	public int readBytes(final byte[] bytes, final int length) {
//return 0;
//	}
//	@Override
//	public boolean writeBytes(final byte[] bytes) {
//return false;
//	}



}
