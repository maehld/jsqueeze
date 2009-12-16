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
 * $Id: PlaylistAlbumCommand.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.cli.commands.playlist;

import com.dsnine.jsqueeze.cli.CLICommand;
import com.dsnine.jsqueeze.internal.cli.CLIData;

public class PlaylistAlbumCommand extends CLICommand {

	private String index;
	private String album;
	
	public PlaylistAlbumCommand(String playerId, String index) {
		this.playerId = playerId;
		this.index = index;
	}
	
	public String getAlbum() {
		return album;
	}
	
	public String getIndex() {
		return index;
	}
	
	public String getPlayerId() {
		return playerId;
	}
	
	@Override
	protected void buildRequest(CLIData request) throws Exception {
		request.setPlayerId(playerId);
		request.setCommand("playlist album");
		request.addParameter(index);
		request.addParameter("?");
	}

	@Override
	protected void parseResponse(CLIData response) throws Exception {
		playerId = response.getPlayerId();
		index = response.getParameters().get(0);
		album = response.getParameters().get(1);
	}

}

