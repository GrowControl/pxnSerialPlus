package com.poixson.serialplus.examples;

import com.poixson.serialplus.natives.NativeD2xx;


public class ExampleList implements Runnable {



	@Override
	public void run() {
		final NativeD2xx d2xx = new NativeD2xx();
		d2xx.natScanDevices();
	}



}
