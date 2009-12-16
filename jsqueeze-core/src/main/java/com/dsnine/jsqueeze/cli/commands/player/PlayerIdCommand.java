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
 * $Id: PlayerIdCommand.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.cli.commands.player;

import com.dsnine.jsqueeze.cli.CLICommand;
import com.dsnine.jsqueeze.internal.cli.CLIData;

/**
 * The "player id" command returns the player's unique MAC address. This value
 * is used when specifying a player in subsequent commands.
 */
public class PlayerIdCommand extends CLICommand {

	private String playerIndexOrId;

	private String playerId;

	public PlayerIdCommand(String playerIndexOrId) {
		this.playerIndexOrId = playerIndexOrId;
	}

	@Override
	protected void parseResponse(CLIData response) {
		playerId = response.getParameters().get(0);
	}

	@Override
	protected void buildRequest(CLIData request) {
		request.setCommand("player id");
		request.addParameter(playerIndexOrId);
		request.addParameter("?");
	}

	public String getPlayerId() {
		return playerId;
	}

}
