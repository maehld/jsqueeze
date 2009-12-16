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
 * $Id: CLIServerInformation.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.internal.model.cli;

import com.dsnine.jsqueeze.Configuration;
import com.dsnine.jsqueeze.SqueezeException;
import com.dsnine.jsqueeze.cli.CLISession;
import com.dsnine.jsqueeze.cli.commands.VersionCommand;
import com.dsnine.jsqueeze.internal.model.IServerInformation;
import com.dsnine.jsqueeze.util.ServerVersion;

public class CLIServerInformation implements IServerInformation {

	private CLISession session;

	public CLIServerInformation(Configuration configuration) throws SqueezeException {
		session = SharedCLISession.get(configuration);
	}

	public ServerVersion getVersion() throws SqueezeException {
		return ServerVersion.fromString(session.execute(new VersionCommand()).getVersion());
	}

	public void dispose() throws SqueezeException {
		session.close();
	}
	
}
