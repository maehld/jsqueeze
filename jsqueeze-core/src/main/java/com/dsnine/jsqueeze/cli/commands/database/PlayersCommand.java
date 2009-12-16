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
 * $Id: PlayersCommand.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.cli.commands.database;

import com.dsnine.jsqueeze.cli.CLIQueryCommand;

public class PlayersCommand extends CLIQueryCommand {

	public PlayersCommand(int start, int itemsPerResponse) {
		super("players", "playerindex", start, itemsPerResponse);
	}

	public PlayersCommand() {
		this(0, Integer.MAX_VALUE);
	}

	public String getPlayerId() {
		return getCurrentResultValue("playerid");
	}

	public String getIp() {
		return getCurrentResultValue("ip");
	}

	public String getName() {
		return getCurrentResultValue("name");
	}

	public String getModel() {
		return getCurrentResultValue("model");
	}

	public String getDisplayType() {
		return getCurrentResultValue("displaytype");
	}

	public boolean isConnected() {
		String connectedString = getCurrentResultValue("connected");
		if (connectedString.equals("1")) {
			return true;
		} else {
			return false;
		}
	}

}
