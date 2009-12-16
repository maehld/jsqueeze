/*******************************************************************************
 * Copyright (c) 2005, 2006, 2007, 2008 Dominik Maehl and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Dominik Maehl - initial API and implementation
 * 
 * $Id: Configuration.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import com.dsnine.jsqueeze.internal.util.Assert;
import com.dsnine.jsqueeze.util.ServerDiscovery;

public class Configuration {

	public enum QueryStrategyType {
		CLI, JSON_RPC
	}

	protected int cliPort;

	protected int httpPort;

	protected InetAddress serverAddress;

	protected QueryStrategyType modelQueryStrategy;

	protected String username;

	protected String password;

	protected Configuration() {
		setDefaults();
	}

	public void setServerAddress(String hostname) throws UnknownHostException {
		setServerAddress(InetAddress.getByName(hostname));
	}

	private void setServerAddress(InetAddress serverAddress) {
		this.serverAddress = serverAddress;
	}

	public void setCLIPort(int port) {
		Assert.isTrue(port > 0, "cliPort > 0");
		cliPort = port;
	}

	public void setHTTPPort(int port) {
		Assert.isTrue(port > 0, "httpPort > 0");
		httpPort = port;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setModelQueryStrategy(QueryStrategyType modelQueryStrategy) {
		Assert.isNotNull(modelQueryStrategy, "queryStrategyType != null");
		this.modelQueryStrategy = modelQueryStrategy;
	}

	public static Configuration create() {
		return new Configuration();
	}

	private void setDefaults() {
		modelQueryStrategy = QueryStrategyType.CLI;
		cliPort = 9090;
		httpPort = 9000;
		username = null;
		password = null;
	}

	public int getCLIPort() {
		return cliPort;
	}

	public int getHTTPPort() {
		return httpPort;
	}

	public InetAddress getServerAddress() {
		if (serverAddress == null) {
			ServerDiscovery discovery = new ServerDiscovery();
			Map<String, InetAddress> candidates;
			try {
				discovery.discover(5000L);
				candidates = discovery.getServers();
			} catch (IOException e) {
				throw new SqueezeRuntimeException("An exception occured during automatic server discovery.", e);
			}
			if (candidates.size() == 0) {
				throw new SqueezeRuntimeException("Server address could not be discovered automatically. No servers responded to the discovery request. Please specify the server manually.");
			} else if (candidates.size() > 1) {
				throw new SqueezeRuntimeException("Multiple servers were found during automatic discovery. Please specify the server manually.");
			}
			serverAddress = (InetAddress) candidates.entrySet().toArray()[0];
		}

		return serverAddress;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public QueryStrategyType getModelQueryStrategy() {
		return modelQueryStrategy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cliPort;
		result = prime * result + httpPort;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((serverAddress == null) ? 0 : serverAddress.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Configuration other = (Configuration) obj;
		if (cliPort != other.cliPort)
			return false;
		if (httpPort != other.httpPort)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (serverAddress == null) {
			if (other.serverAddress != null)
				return false;
		} else if (!serverAddress.equals(other.serverAddress))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
