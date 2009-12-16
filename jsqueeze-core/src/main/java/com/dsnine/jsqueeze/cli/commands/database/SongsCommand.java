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
 * $Id: SongsCommand.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.cli.commands.database;

import com.dsnine.jsqueeze.cli.CLIQueryCommand;

public class SongsCommand extends CLIQueryCommand {

	public enum SortOrder {
		TITLE, TRACKNUMBER;
	}

	public SongsCommand(int start, int itemsPerResponse) {
		super("songs", start, itemsPerResponse);
	}

	public SongsCommand() {
		super("songs");
	}

	public SongsCommand setSearch(String searchExpression) {
		setTaggedParameter("search", searchExpression);
		return this;
	}

	public SongsCommand setGenreId(int genreId) {
		setTaggedParameter("genre_id", genreId);
		return this;
	}

	public SongsCommand setArtistId(int artistId) {
		setTaggedParameter("artist_id", artistId);
		return this;
	}

	public SongsCommand setAlbumId(int albumId) {
		setTaggedParameter("album_id", albumId);
		return this;
	}

	public SongsCommand setSortOrder(SortOrder order) {
		switch (order) {
		case TITLE:
			setTaggedParameter("sort", "title");
			break;
		case TRACKNUMBER:
			setTaggedParameter("sort", "tracknum");
			break;
		}
		return this;
	}

	public SongsCommand setTags(SongTag... tags) {
		StringBuilder builder = new StringBuilder();
		for (SongTag tag : tags) {
			builder.append(tag.getIdentifier());
		}

		setTaggedParameter("tags", builder.toString());
		return this;
	}

	public String getTitle() {
		return getCurrentResultValue("title");
	}
	
	public String getTag(SongTag tag) {
		return getCurrentResultValue(tag.getName());
	}

}
