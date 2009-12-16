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
 * $Id: ListenCommand.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.cli.commands;

import com.dsnine.jsqueeze.SqueezeRuntimeException;
import com.dsnine.jsqueeze.cli.CLICommand;
import com.dsnine.jsqueeze.internal.cli.CLIData;

public class ListenCommand extends CLICommand {

	public ListenCommand() {
		throw new SqueezeRuntimeException("Please do not use <listen>, non-rpc style communication is not yet ready!");
	}

	@Override
	protected void parseResponse(CLIData response) {
	}

	@Override
	protected void buildRequest(CLIData request) {
		request.setCommand("listen");
		request.addParameter(1);
	}

}
