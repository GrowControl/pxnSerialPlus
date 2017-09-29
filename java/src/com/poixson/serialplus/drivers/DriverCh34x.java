package com.poixson.serialplus.drivers;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import com.poixson.serialplus.DeviceConfig;
import com.poixson.serialplus.Driver;
import com.poixson.serialplus.NativeDevice;
import com.poixson.serialplus.natives.NativeCh34x;
import com.poixson.utils.Utils;


public class DriverCh34x extends Driver {



	public DriverCh34x(final DeviceConfig cfg)
			throws IOException {
		super(
			cfg,
			new NativeCh34x()
		);
	}



	public NativeCh34x getNativeCh34x() {
		return (NativeCh34x) this.nat;
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
