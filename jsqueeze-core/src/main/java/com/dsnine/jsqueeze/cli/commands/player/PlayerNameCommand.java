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
 * $Id: PlayerNameCommand.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.cli.commands.player;

import com.dsnine.jsqueeze.cli.CLICommand;
import com.dsnine.jsqueeze.internal.cli.CLIData;

/**
 * The "player name" command returns the human-readable name for the specified
 * player. If the name has not been specified by the user in the Player
 * Settings, then a default name will be used, usually the IP address.
 */
public class PlayerNameCommand extends CLICommand {

	private String playerIndexOrId;

	private String playerName;

	public PlayerNameCommand(String playerIndexOrId) {
		this.playerIndexOrId = playerIndexOrId;
	}

	@Override
	protected void parseResponse(CLIData response) {
		playerName = response.getParameters().get(0);
	}

	@Override
	protected void buildRequest(CLIData request) {
		request.setCommand("player name");
		request.addParameter(playerIndexOrId);
		request.addParameter("?");
	}

	public String getPlayerName() {
		return playerName;
	}

}
