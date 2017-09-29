package com.poixson.serialplus;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import com.poixson.utils.Utils;
import com.poixson.utils.xCloseable;


public abstract class Driver implements xCloseable {

	protected final AtomicLong handle = new AtomicLong(0L);
	protected final DeviceConfig cfg;
	protected final NativeDevice nat;



	public Driver(final DeviceConfig cfg, final NativeDevice nat)
			throws IOException {
		if (cfg == null) throw new NullPointerException();
		if (nat == null) throw new NullPointerException();
		this.cfg = cfg;
		this.nat = nat;
		this.open();
	}
	public void finalize() {
		Utils.safeClose(this);
	}



	public long getHandle() {
		return this.handle.get();
	}
	public NativeDevice getNative() {
		return this.nat;
	}
	public DeviceConfig getConfig() {
		return this.cfg;
	}



	protected abstract void open()      throws IOException;
	protected abstract void setParams() throws IOException;



	protected abstract void close(final long handle) throws IOException;
	@Override
	public void close() throws IOException {
		final long h = this.handle.get();
		// already closed
		if (h == 0L) return;
		// attempt setting to 0
		if (this.handle.compareAndSet(h, 0L)) {
			this.close(
				this.handle.getAndSet(0L)
			);
		}
	}



	@Override
	public boolean isClosed() {
		return (this.getHandle() <= 0L);
	}
	public boolean isFailed() {
		return (this.getHandle() < 0L);
	}



//TODO:
//	public int readBytes(final byte[] bytes, final int length);
//	public boolean writeBytes(final byte[] bytes);



}
