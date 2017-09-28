package com.poixson.serialplus;

import com.poixson.utils.NativeUtils;
import com.poixson.utils.xVars;


public class example {



	public static void main(final String[] args) {
		xVars.debug(true);

		final String libFileName = "serialplus-linux64.so";
		final boolean result =
			NativeUtils.LoadExtractLibrary(
				libFileName,
				example.class
			);
		if (!result) {
			System.out.println("Failed to load library!");
			System.exit(1);
		}

		// load the serial object
		final SerialPlus serial =
			(new SerialPlusFactory())
			.setPortName("/dev/ttyUSB0")
			.setBaud(9600)
				.build();

		// open the port
		if (serial.open()) {
			final long handle = serial.getHandle();
			System.out.print("Handle: ");
			System.out.println(handle);
		} else {
			System.out.println("Open failed!");
		}

		//final NativeSerial nat = serial.getNative();
		//nat.natSetVMinVTime(
		//	serial.getHandle(),
		//	10, // vMin
		//	300 // vTime
		//);

		while (true) {
			try {
				final String data = serial.readString(10);
				System.out.print(data);
			} catch (SerialReadTimeoutException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println();
			System.out.print(" > ");
		}



//final long handle = serial.getHandle();
//final NativeSerial nat = serial.getNative();
//final int avail = nat.natGetInputBytesCount(handle);
//System.out.print("available: ");
//System.out.println(avail);
//byte[] bytes = new byte[avail];
//nat.natReadBytes(
//	handle,
//	bytes,
//	avail
//);
//System.out.print("GOT DATA: ");
//System.out.println(new String(bytes));

//		serial.writeString("AbC");

//		System.out.println("Sleeping 1 second..");
//		ThreadUtils.Sleep("0.5s");

//	final Thread thread = new Thread() {
//	private volatile Thread otherThread = null;
//	public Thread init(final Thread otherThread) {
//		this.otherThread = otherThread;
//		return this;
//	}
//	public void run() {
//		ThreadUtils.Sleep("2s");
//		System.out.println("Interrupting!!!");
//		this.otherThread.interrupt();
//	}
//}.init(Thread.currentThread());
//thread.setDaemon(true);
//thread.start();

//System.out.println();
//System.out.println();
//System.out.println();

//		try {
//			serial.setReadTimeout("5s");
//			final String s = serial.readString(1);
//			System.out.println(s);
//		} catch (SerialReadTimeoutException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

//		final String str = serial.readString();
//		System.out.print("Got data: ");
//		System.out.println(str);

//		ThreadUtils.Sleep("0.5s");
//		System.out.println("Closing port");
//		Utils.safeClose(serial);

	}



}
