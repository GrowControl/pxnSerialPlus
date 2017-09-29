package com.poixson.serialplus.examples;

import com.poixson.serialplus.SerialPlus;
import com.poixson.serialplus.SerialPlusFactory;
import com.poixson.serialplus.enums.Baud;
import com.poixson.serialplus.exceptions.SerialReadTimeoutException;
import com.poixson.utils.ErrorMode;
import com.poixson.utils.Utils;


public class ExampleEcho implements Runnable {

	private String portName = null;
	private Baud   baud     = null;



	public ExampleEcho setPortName(final String portName) {
		this.portName = (
			Utils.isEmpty(portName)
			? null
			: portName
		);
		return this;
	}
	public ExampleEcho setBaud(final String baudStr) {
		this.baud = Baud.FromString(baudStr);
		return this;
	}



	@Override
	public void run() {
		final String portName = (
			Utils.isEmpty(this.portName)
			? "/dev/ttyUSB0"
			: this.portName
		);
		final Baud baud = (
			this.baud == null
			? Baud.B9600
			: this.baud
		);
		System.out.println();
		System.out.println(
			(new StringBuilder())
				.append("Testing echo port: ")
				.append(portName)
				.append(" baud: ")
				.append(baud.getValue())
				.toString()
		);
		// load serial object
		final SerialPlusFactory factory =
			(new SerialPlusFactory())
				.setErrorMode(ErrorMode.LOG)
				.setPortName(portName)
				.setBaud(baud)
//				.setReadInterval("1s")
//				.setReadTimeout("30s");
;
		final SerialPlus serial = factory.build();
		if (!serial.open()) {
			throw new RuntimeException("Failed to open port!");
		}
		try {
			final String data = serial.readString(20);
System.out.print("GOT DATA: ");
System.out.println(data);
		} catch (SerialReadTimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}



}