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
 * $Id: ServerDiscovery.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Map;

public class ServerDiscovery {

	private static final byte[] DISCOVERY_COMMAND = new byte[] {'d',0,'2',0x11,0,0,0,0,0,0,0,0,127,127,127,127,127,127};
	
	private Selector selector;
	private DatagramChannel channel;
	private Map<String, InetAddress> servers;
	
	/**
	 * Returns the discovered servers
	 * @return
	 */
	public Map<String, InetAddress> getServers() {
		return servers;
	}

	public ServerDiscovery() {
		this.servers = new HashMap<String, InetAddress>();
	}

	/**
	 * A convenience method that calls start(), waits timeout milliseconds, and then calls stop().
	 * @param timeout time to wait for responses
	 * @throws IOException
	 */
	public void discover(long timeout) throws IOException {
		start();
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
		}
		stop();
	}
	
	/**
	 * Sends the discovery message via broadcast and starts the waiting for answers
	 * @throws IOException
	 */
	public void start() throws IOException {
		channel = DatagramChannel.open();
		channel.configureBlocking(false);
		channel.socket().setBroadcast(true);
		
		selector = Selector.open();
		
		channel.register(selector, SelectionKey.OP_WRITE);
		
		selector.select();
		
		for (SelectionKey key : selector.selectedKeys()) {
			if (key.isWritable()) {
				channel.register(selector, SelectionKey.OP_READ);
				channel.send(ByteBuffer.wrap(DISCOVERY_COMMAND), getDestinationAddress());
			}
		}
	}
	
	/**
	 * Stops waiting for answers and processes the already received answers
	 * @throws IOException
	 */
	public void stop() throws IOException {
		selector.selectNow();
		
		ByteBuffer readBuffer = ByteBuffer.allocate(18);
		
		for (SelectionKey key : selector.selectedKeys()) {
			if (key.isReadable()) {
				InetSocketAddress senderAddress = (InetSocketAddress) channel.receive(readBuffer);
				servers.put(getServername(readBuffer), senderAddress.getAddress());
			}
		}
		
		channel.close();
		selector.close();
	}

	private String getServername(ByteBuffer readBuffer) {
		readBuffer.position(1);
		StringBuilder stringBuilder = new StringBuilder();
		while (readBuffer.hasRemaining()) {
			stringBuilder.append((char) readBuffer.get());
		}
		
		return stringBuilder.toString().trim();
	}
	
	private SocketAddress getDestinationAddress() throws IOException {
		return new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 3483);
	}

}
