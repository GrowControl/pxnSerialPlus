package com.poixson.serialplus.drivers;

import java.io.IOException;

import com.poixson.serialplus.DeviceConfig;
import com.poixson.serialplus.Driver;
import com.poixson.serialplus.natives.NativeD2xx;
import com.poixson.utils.Utils;


public class DriverD2xx extends Driver {



	public DriverD2xx(final DeviceConfig cfg)
			throws IOException {
		super(
			cfg,
			new NativeD2xx()
		);
	}



	public NativeD2xx getNativeD2xx() {
		return (NativeD2xx) this.getNative();
	}



	@Override
	protected void open() throws IOException {
		synchronized (this.handle) {
			Utils.safeClose(this);
			
			// open the port
			
		}
		// configure the port
		this.setParams();
	}
	// configure the port
	@Override
	protected void setParams() throws IOException {
		final long h = this.getHandle();
		final DeviceConfig cfg = this.getConfig();
		
		
	}
	@Override
	protected void close(final long handle) throws IOException {
		// already closed
		if (handle == 0L)
			return;
		this.nat.natClosePort(handle);
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
