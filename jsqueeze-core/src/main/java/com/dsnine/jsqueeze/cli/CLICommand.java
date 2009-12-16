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
 * $Id: CLICommand.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.cli;

import com.dsnine.jsqueeze.internal.cli.CLIData;

public abstract class CLICommand {

	protected String playerId;

	protected abstract void parseResponse(CLIData response) throws Exception;

	protected abstract void buildRequest(CLIData request) throws Exception;

}
