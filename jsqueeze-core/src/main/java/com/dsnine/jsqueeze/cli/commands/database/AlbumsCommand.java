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
 * $Id: AlbumsCommand.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.cli.commands.database;

import com.dsnine.jsqueeze.cli.CLIQueryCommand;

public class AlbumsCommand extends CLIQueryCommand {

	public AlbumsCommand(int start, int itemsPerResponse) {
		super("albums", start, itemsPerResponse);
	}

	public AlbumsCommand() {
		super("albums");
	}

	public AlbumsCommand setSearch(String searchExpression) {
		setTaggedParameter("search", searchExpression);
		return this;
	}

	public AlbumsCommand setGenreId(int genreId) {
		setTaggedParameter("genre_id", genreId);
		return this;
	}

	public AlbumsCommand setArtistId(int artistId) {
		setTaggedParameter("artist_id", artistId);
		return this;
	}

	public AlbumsCommand setTrackId(int trackId) {
		setTaggedParameter("track_id", trackId);
		return this;
	}
	
	public AlbumsCommand setYear(String year) {
		setTaggedParameter("year", year);
		return this;
	}
	
	public AlbumsCommand setCompilationsOnly(boolean compilationsOnly) {
		setTaggedParameter("compilation", compilationsOnly ? "1" : "0");
		return this;
	}
	
	public AlbumsCommand setTags(AlbumTag... tags) {
		StringBuilder builder = new StringBuilder();
		for (AlbumTag tag : tags) {
			builder.append(tag.getIdentifier());
		}
		
		setTaggedParameter("tags", builder.toString());
		return this;
	}
	
	public String getTag(AlbumTag tag) {
		return getCurrentResultValue(tag.getName());
	}
	
}
