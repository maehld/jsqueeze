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
 * $Id: SharedCLISession.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.internal.model.cli;

import java.util.HashMap;
import java.util.Map;

import com.dsnine.jsqueeze.Configuration;
import com.dsnine.jsqueeze.SqueezeException;
import com.dsnine.jsqueeze.SqueezeRuntimeException;
import com.dsnine.jsqueeze.cli.CLICommand;
import com.dsnine.jsqueeze.cli.CLISession;
import com.dsnine.jsqueeze.cli.CommandToken;

public class SharedCLISession extends CLISession {

	private static Map <Configuration, SharedCLISession> sharedSessions = new HashMap<Configuration, SharedCLISession>();
	
	public static SharedCLISession get(Configuration configuration) {
		if (!sharedSessions.containsKey(configuration)) {
			synchronized (sharedSessions) {
				if (!sharedSessions.containsKey(configuration)) {
					sharedSessions.put(configuration, new SharedCLISession(configuration));
				}
			}
		}
		
		SharedCLISession sharedCLISession = sharedSessions.get(configuration);
		synchronized (sharedCLISession) {
			sharedCLISession.useCount++;
		}
		return sharedCLISession;
	}

	private int useCount = 0;

	private SharedCLISession(Configuration configuration) {
		super(configuration);
	}

	@Override
	public void open() throws SqueezeException {
		throw new SqueezeRuntimeException("open() cannot be called on shared CLI sessions");
	}
	
	@Override
	public void close() throws SqueezeException {
		synchronized (this) {
			useCount--;
			if (useCount == 0 && super.isOpen()) {
				super.close();
			}
		}
	}
	
	@Override
	public <T extends CLICommand> T execute(T command) throws SqueezeException {
		prepareSessionForExecution();
		return super.execute(command);
	}
	
	@Override
	public <T extends CLICommand> CommandToken<T> dispatch(T command) throws SqueezeException {
		prepareSessionForExecution();
		return super.dispatch(command);
	}
	
	@Override
	public <T extends CLICommand> T collect(CommandToken<T> token) throws SqueezeException {
		prepareSessionForExecution();
		return super.collect(token);
	}

	private void prepareSessionForExecution() throws SqueezeException {
		if (!isOpen()) {
			synchronized (this) {
				if (!isOpen()) {
					super.open();
				}
			}
		}
	}
}
