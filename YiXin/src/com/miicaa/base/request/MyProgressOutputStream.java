package com.miicaa.base.request;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;



public class MyProgressOutputStream extends FilterOutputStream{

	private final ProgressOutStreamListener listener;
	private Double transferred;
	 
	public MyProgressOutputStream(final OutputStream out,
	final ProgressOutStreamListener listener) {
	super(out);
	this.listener = listener;
	this.transferred = 0.0;
	}
	 
	public void write(byte[] b, int off, int len) throws IOException {
	out.write(b, off, len);
	this.transferred += len;
	this.listener.transfer(this.transferred);
	}
	 
	public void write(int b) throws IOException {
	out.write(b);
	this.transferred++;
	this.listener.transfer(this.transferred);
	}

 
}
