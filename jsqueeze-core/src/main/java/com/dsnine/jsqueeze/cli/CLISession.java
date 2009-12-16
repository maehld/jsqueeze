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
 * $Id: CLISession.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.cli;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dsnine.jsqueeze.Configuration;
import com.dsnine.jsqueeze.SqueezeException;
import com.dsnine.jsqueeze.cli.commands.LoginCommand;
import com.dsnine.jsqueeze.cli.commands.VersionCommand;
import com.dsnine.jsqueeze.internal.cli.CLIData;
import com.dsnine.jsqueeze.internal.cli.CLIDataCodec;
import com.dsnine.jsqueeze.internal.cli.IIOCallback;
import com.dsnine.jsqueeze.internal.cli.IOWorker;
import com.dsnine.jsqueeze.internal.util.Assert;
import com.dsnine.jsqueeze.util.ServerVersion;

public class CLISession {

	public static final ServerVersion MINIMUM_VERSION = new ServerVersion(6, 3, 0);

	public static final ServerVersion MAXIMUM_VERSION = new ServerVersion(7, 1, 99);

	private InetSocketAddress serverAddress;

	private IOWorker ioWorker;

	private CLIDataCodec codec;

	private IIOCallback ioCallback;

	private List<CommandToken<?>> tokens;

	private int nextDispatchId;

	private Configuration config;

	private Throwable exception;

	private final Logger log = LoggerFactory.getLogger(CLISession.class);

	private ServerVersion version;

	public CLISession(com.dsnine.jsqueeze.Configuration configuration) {
		Assert.isNotNull(configuration, "Configuration must not be null");

		config = configuration;

		serverAddress = new InetSocketAddress(config.getServerAddress(), config.getCLIPort());

		codec = new CLIDataCodec();

		tokens = Collections.synchronizedList(new ArrayList<CommandToken<?>>());

		ioCallback = new IIOCallback() {

			public void exceptionOccured(Throwable t) {
				processException(t);
			}

			public void messageReceived(ByteBuffer receivedBuffer) {
				processReceivedMessage(receivedBuffer);
			}

		};
	}

	public void open() throws SqueezeException {
		Assert.isFalse(isOpen(), "Session is already open");

		ioWorker = new IOWorker(codec.getEOLAsByte(), ioCallback);

		try {
			ioWorker.connect(serverAddress);
		} catch (IOException e) {
			throw new SqueezeException("Could not connect to server", e);
		}

		if (config.getUsername() != null && config.getUsername().length() > 0) {
			execute(new LoginCommand(config.getUsername(), (config.getPassword() != null ? config.getPassword() : "")));
		}

		VersionCommand versionCommand = execute(new VersionCommand());
		version = ServerVersion.fromString(versionCommand.getVersion());
		assertVersion();
	}

	public <T extends CLICommand> T execute(T command) throws SqueezeException {
		CommandToken<T> commandToken = dispatch(command);
		commandToken.waitFor();
		return collect(commandToken);
	}

	public <T extends CLICommand> CommandToken<T> dispatch(T command) throws SqueezeException {
		Assert.isNotNull(command, "Command cannot be null");
		Assert.isTrue(isOpen(), "Session is not opened");

		rethrowOccuredException(command);

		CommandToken<T> token = new CommandToken<T>();
		token.setCommand(command);

		CLIData request = new CLIData();
		token.setRequest(request);

		try {
			command.buildRequest(request);
		} catch (Exception e) {
			throw new SqueezeException("Could not build request", e);
		}

		request.setDispatchId(nextDispatchId++);
		tokens.add(token);
		ioWorker.put(codec.encode(request));
		log.debug("sent: {}: {}", request.getDispatchId(), request);

		return token;
	}

	public <T extends CLICommand> T collect(CommandToken<T> token) throws SqueezeException {
		Assert.isNotNull(token, "Token cannot be null");

		T command = token.getCommand();

		rethrowOccuredException(command);

		try {
			command.parseResponse(token.getResponse());
		} catch (Exception e) {
			throw new SqueezeException("Could not parse response", e);
		}

		tokens.remove(command);

		return command;
	}

	public void close() throws SqueezeException {
		Assert.isTrue(isOpen(), "Session must be open");

		try {
			ioWorker.disconnect();
			log.debug("CLI session closed (in: {} out: {})", ioWorker.getTotalBytesRead(), ioWorker.getTotalBytesWritten());
		} catch (IOException e) {
			throw new SqueezeException("Could not properly disconnect from server", e);
		}
	}

	public boolean isOpen() {
		if (ioWorker != null) {
			return true;
		} else {
			return false;
		}
	}

	public ServerVersion getVersion() {
		return version;
	}

	private void processReceivedMessage(ByteBuffer receivedBuffer) {
		CLIData response;
		response = codec.decode(receivedBuffer);

		if (response.getDispatchId() == null) {

			log.debug("Out-of-band message received: {}", response.toString());
		}

		CommandToken<?> associatedToken = null;

		synchronized (tokens) {
			for (CommandToken<?> token : tokens) {
				if (responseMatchesRequest(response, token.getRequest())) {
					associatedToken = token;
					break;
				}
			}
		}

		if (associatedToken != null) {
			associatedToken.setResponse(response);
			log.debug("recv: {}: {}", response.getDispatchId(), response);
			tokens.remove(associatedToken);
			associatedToken.setCompleted();
		} else {
			log.warn("No token found for response: {}: {}", response.getDispatchId(), response);
		}
	}

	private void rethrowOccuredException(CLICommand command) throws SqueezeException {
		if (exception != null) {
			throw new SqueezeException("Exception occured in asynchronous operation", exception);
		}
	}

	private void processException(Throwable t) {
		exception = t;
		synchronized (tokens) {
			for (CommandToken<?> token : tokens) {
				token.setFailed();
			}
		}
	}

	private void assertVersion() {
		Assert.isNotNull(version, "Server version could not be determined");
		//FIXME: Version detection broken!: Assert.isTrue(version.isInRange(MINIMUM_VERSION, MAXIMUM_VERSION), "This version of jSqueeze only works with SlimServer versions from " + MINIMUM_VERSION + " up to " + MAXIMUM_VERSION);
	}

	private boolean responseMatchesRequest(CLIData response, CLIData request) {
		if (response.getDispatchId() != null && response.getDispatchId().equals(request.getDispatchId())) {
			return true;
		}

		return false;
	}

}
