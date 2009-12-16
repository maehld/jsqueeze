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
 * $Id: PlayerIpCommand.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.cli.commands.player;

import com.dsnine.jsqueeze.cli.CLICommand;
import com.dsnine.jsqueeze.internal.cli.CLIData;

/**
 * The "player ip" command returns the IP address (along with port number) of
 * the specified player. Note that the IP address and or port may change if the
 * player reconnects or is rebooted. Use the playerid to uniquely identify the
 * player.
 */
public class PlayerIpCommand extends CLICommand {

	private String playerIp;

	private int playerIndex;

	private String playerId;
	
	public PlayerIpCommand(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	public PlayerIpCommand(String playerId) {
		this.playerId = playerId;
	}
	
	@Override
	protected void parseResponse(CLIData response) {
		playerIp = response.getParameters().get(0);
	}

	@Override
	protected void buildRequest(CLIData request) {
		request.setCommand("player ip");
		if (playerId == null) {
			request.addParameter(playerIndex);
		} else {
			request.setPlayerId(playerId);
		}
		request.addParameter("?");
	}

	public String getPlayerIp() {
		return playerIp;
	}

}
