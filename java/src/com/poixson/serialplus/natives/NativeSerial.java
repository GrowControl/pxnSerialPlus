package com.poixson.serialplus.natives;

import com.poixson.serialplus.NativeDevice;


public class NativeSerial implements NativeDevice {



	public NativeSerial() {
//TODO:
		// check library version
//		throw new MismatchedLibraryVersion();
	}



	@Override
	public native int natScanDevices();


	@Override
	public native long natOpenPort(String portName);
	@Override
	public native boolean natClosePort(long handle);


	@Override
	public native long natSetParams(long handle, int baud,
		int byteSize, int stopBits, int parity, int flags);
	@Override
	public native long natSetBlocking(long handle, boolean blocking);
	@Override
	public native long natSetVMinVTime(long handle, int vMin, int vTime);
	@Override
	public native long natSetLineStatus(long handle, boolean setRTS, boolean setDTR);


	@Override
	public native int natGetInputBytesCount(long handle);
	@Override
	public native int natGetOutputBytesCount(long handle);


	@Override
	public native int natReadBytes(long handle, byte[] bytes, int length);
	@Override
	public native boolean natWriteBytes(long handle, byte[] buffer);



}
