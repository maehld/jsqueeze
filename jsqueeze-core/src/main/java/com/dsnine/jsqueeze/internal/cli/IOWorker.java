/*******************************************************************************
 * Copyright (c) 2005, 2006, 2007 Dominik Maehl and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Dominik Maehl - initial API and implementation
 * 
 * $Id: IOWorker.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.internal.cli;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.dsnine.jsqueeze.ConnectionLostException;

public class IOWorker {

	private static final int BUFFER_SIZE = 2048;

	private long totalBytesWritten;
	
	private long totalBytesRead;
	
	private Queue<ByteBuffer> writeQueue;

	private SocketChannel channel;

	private Selector selector;

	private Thread ioThread;

	private boolean running;

	private byte endOfLineIndicator;

	private GrowingByteBuffer collectBuffer;

	private ByteBuffer readBuffer;

	private IIOCallback callback;
	
	public IOWorker(byte endOfLineIndicator, IIOCallback callback) {
		this.callback = callback;
		this.endOfLineIndicator = endOfLineIndicator;

		writeQueue = new ConcurrentLinkedQueue<ByteBuffer>();

		readBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
		collectBuffer = new GrowingByteBuffer(0, true);
	}

	public void connect(InetSocketAddress address) throws IOException {
		channel = SocketChannel.open(address);
		channel.configureBlocking(false);

		selector = Selector.open();
		channel.register(selector, SelectionKey.OP_READ);

		running = true;
		ioThread = new IOWorkerRunnable();
		ioThread.start();
	}

	public void put(ByteBuffer buffer) {
		writeQueue.add(buffer);
		channel.keyFor(selector).interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		selector.wakeup();
	}

	public void disconnect() throws IOException {
		running = false;
		selector.wakeup();
		try {
			ioThread.join();
		} catch (InterruptedException e) {
		}
		selector.close();
		channel.close();
	}

	public long getTotalBytesRead() {
		return totalBytesRead;
	}
	
	public long getTotalBytesWritten() {
		return totalBytesWritten;
	}
	
	private void doRead() throws Exception {
		readBuffer.clear();

		int bytesRead = channel.read(readBuffer);
		
		if (bytesRead == -1) {
			throw new ConnectionLostException();
		}
		
		totalBytesRead+=bytesRead;
		readBuffer.flip();

		boolean endOfLine = false;

		for (int i = 0; i < readBuffer.limit(); i++) {
			if (readBuffer.get(i) == endOfLineIndicator) {
				readBuffer.limit(i);
				endOfLine = true;
				break;
			}
		}

		collectBuffer.add(readBuffer);
		if (endOfLine) {
			callback.messageReceived(collectBuffer.getCopy());
			collectBuffer.recycle();
		}
	}

	private void doWrite() throws IOException {
		ByteBuffer byteBuffer = writeQueue.poll();
		if (byteBuffer != null) {
			int bytesWritten = channel.write(byteBuffer);
			totalBytesWritten += bytesWritten;
		} else {
			SelectionKey key = channel.keyFor(selector);
			key.interestOps(SelectionKey.OP_READ);
		}
	}

	private class IOWorkerRunnable extends Thread {

		public IOWorkerRunnable() {
			super("jSqueeze CLI I/O Thread");
			this.setDaemon(true);
		}
		
		@Override
		public void run() {
			while (running) {
				try {
					selector.select();
					
					for (SelectionKey key : selector.selectedKeys()) {
						if (key.isReadable()) {
							try {
								doRead();
							} catch (Exception e) {
								callback.exceptionOccured(e);
							}
						}
						
						if (key.isWritable()) {
							try {
								doWrite();
							} catch (Exception e) {
								callback.exceptionOccured(e);
							}
						}
						selector.selectedKeys().remove(key);
					}

				} catch (IOException e) {
					callback.exceptionOccured(e);
				}
			}
		}
	}

}
