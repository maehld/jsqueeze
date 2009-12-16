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
 * $Id: PlayerModelCommand.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.cli.commands.player;

import com.dsnine.jsqueeze.cli.CLICommand;
import com.dsnine.jsqueeze.internal.cli.CLIData;

/**
 * The "player model" command returns the model of the player, currently one of
 * "squeezebox2", "squeezebox", "slimp3" or "softsqueeze".
 */
public class PlayerModelCommand extends CLICommand {

	private String playerIndexOrId;

	private String playerModel;

	public PlayerModelCommand(String playerIndexOrId) {
		this.playerIndexOrId = playerIndexOrId;
	}

	@Override
	protected void parseResponse(CLIData response) {
		playerModel = response.getParameters().get(0);
	}

	@Override
	protected void buildRequest(CLIData request) {
		request.setCommand("player model");
		request.addParameter(playerIndexOrId);
		request.addParameter("?");
	}

	public String getPlayerModel() {
		return playerModel;
	}

}
