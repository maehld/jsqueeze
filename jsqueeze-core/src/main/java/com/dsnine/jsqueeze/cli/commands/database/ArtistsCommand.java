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
 * $Id: ArtistsCommand.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.cli.commands.database;

import com.dsnine.jsqueeze.cli.CLIQueryCommand;

public class ArtistsCommand extends CLIQueryCommand {

	public ArtistsCommand() {
		super("artists");
	}

	public ArtistsCommand(int start, int itemsPerResponse) {
		super("artists", start, itemsPerResponse);
	}

	public ArtistsCommand setSearch(String searchExpression) {
		setTaggedParameter("search", searchExpression);
		return this;
	}

	public ArtistsCommand setGenreId(int genreId) {
		setTaggedParameter("genre_id", genreId);
		return this;
	}

	public ArtistsCommand setAlbumId(int albumId) {
		setTaggedParameter("album_id", albumId);
		return this;
	}

	public ArtistsCommand setSongId(int songId) {
		setTaggedParameter("track_id", songId);
		return this;
	}
	
	public String getName() {
		return getCurrentResultValue("artist");
	}

}
