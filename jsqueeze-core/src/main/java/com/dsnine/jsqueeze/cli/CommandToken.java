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
 * $Id: CommandToken.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.cli;

import com.dsnine.jsqueeze.internal.cli.CLIData;

public class CommandToken<T extends CLICommand> {
	private enum CommandState {
		WAITING, COMPLETED, FAILED;
	}
	
	private CLIData request;

	private CLIData response;

	private T command;

	private CommandState state;
	
	private Object blockMonitor;

	CommandToken() {
		blockMonitor = new Object();
		state = CommandState.WAITING;
	}

	public void waitFor(long timeout) {
		if (state == CommandState.WAITING) {
			synchronized (blockMonitor) {
				try {
					blockMonitor.wait(timeout);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	public void waitFor() {
		waitFor(0);
	}

	void setCompleted() {
		state = CommandState.COMPLETED;
		synchronized (blockMonitor) {
			blockMonitor.notifyAll();
		}
	}

	void setFailed() {
		state = CommandState.FAILED;
		synchronized(blockMonitor) {
			blockMonitor.notifyAll();
		}
	}
	
	public boolean isCompleted() {
		return (state == CommandState.COMPLETED);
	}

	public boolean isFailed() {
		return (state == CommandState.FAILED);
	}
	
	T getCommand() {
		return command;
	}

	void setCommand(T command) {
		this.command = command;
	}

	CLIData getRequest() {
		return request;
	}

	void setRequest(CLIData request) {
		this.request = request;
	}

	CLIData getResponse() {
		return response;
	}

	void setResponse(CLIData response) {
		this.response = response;
	}

}
