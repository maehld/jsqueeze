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
 * $Id: GenresCommand.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.cli.commands.database;

import com.dsnine.jsqueeze.cli.CLIQueryCommand;

public class GenresCommand extends CLIQueryCommand {

	public GenresCommand() {
		super("genres");
	}

	public GenresCommand(int start, int itemsPerResponse) {
		super("genres", start, itemsPerResponse);
	}

	public void setArtistId(int artistId) {
		setTaggedParameter("artist_id", artistId);
	}

	public void setAlbumId(int albumId) {
		setTaggedParameter("album_id", albumId);
	}

	public void setSongId(int songId) {
		setTaggedParameter("track_id", songId);
	}
	
	public void setYear(int year) {
		setTaggedParameter("year", year);
	}
	
	public String getName() {
		return getCurrentResultValue("genre");
	}

	public void setSearch(String searchExpression) {
		setTaggedParameter("search", searchExpression);
	}

}
