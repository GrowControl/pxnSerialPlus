package com.poixson.serialplus.exceptions;

import com.poixson.utils.StringUtils;


public class SerialReadTimeoutException extends Exception {
	private static final long serialVersionUID = 1L;

	public final String portName;
	public final String methodName;
	public final long   timeout;
	public final int    size;



	public SerialReadTimeoutException(final String portName,
			final String methodName, final long timeout, final int size) {
		super(
			(new StringBuilder())
				.append("Serial read timeout port: ")
				.append(portName)
				.append(" method: ")
				.append(StringUtils.ForceEnds("()", methodName))
				.append(" timeout: ")
				.append(timeout)
				.append(" size: ")
				.append(size)
				.toString()
		);
		this.portName   = portName;
		this.methodName = methodName;
		this.timeout    = timeout;
		this.size       = size;
	}



	public String getPortName() {
		return this.portName;
	}
	public String getMethodName() {
		return this.methodName;
	}
	public long getTimeout() {
		return this.timeout;
	}
	public int getByteSize() {
		return this.size;
	}



}
