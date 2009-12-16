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
 * $Id: PlayCommand.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.cli.commands.playlist;

import com.dsnine.jsqueeze.cli.CLICommand;
import com.dsnine.jsqueeze.internal.cli.CLIData;

public class PlayCommand extends CLICommand {

	public PlayCommand(String playerId) {
		this.playerId = playerId;
	}

	public String getPlayerId() {
		return playerId;
	}
	
	@Override
	protected void parseResponse(CLIData response) throws Exception {
		playerId = response.getPlayerId();
	}

	@Override
	protected void buildRequest(CLIData request) throws Exception {
		request.setCommand("play");
		request.setPlayerId(playerId);
	}

}
