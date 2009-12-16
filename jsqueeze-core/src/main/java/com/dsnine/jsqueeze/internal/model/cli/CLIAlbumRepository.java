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
 * $Id: CLIAlbumRepository.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.internal.model.cli;

import java.util.List;

import com.dsnine.jsqueeze.SqueezeException;
import com.dsnine.jsqueeze.cli.commands.database.AlbumTag;
import com.dsnine.jsqueeze.cli.commands.database.AlbumsCommand;
import com.dsnine.jsqueeze.cli.commands.database.InfoTotalAlbumsCommand;
import com.dsnine.jsqueeze.internal.model.IAlbumRepository;
import com.dsnine.jsqueeze.internal.util.Convert;
import com.dsnine.jsqueeze.model.Album;
import com.dsnine.jsqueeze.model.Artist;
import com.dsnine.jsqueeze.model.Song;

public class CLIAlbumRepository extends AbstractCLIRepository<Album, AlbumsCommand> implements IAlbumRepository {

	public CLIAlbumRepository(CLIRepositoryFactory repositoryFactory) {
		super(repositoryFactory);
	}

	public List<Album> getAllAlbums() throws SqueezeException {
		return executeAndMapQuery(createAlbumsCommand());
	}

	public List<Album> getAlbumsForArtist(Artist artist) throws SqueezeException {
		AlbumsCommand albumsCommand = createAlbumsCommand();
		albumsCommand.setArtistId(artist.getId());
		return executeAndMapQuery(albumsCommand);
	}

	public Album getAlbumForSong(Song song) throws SqueezeException {
		AlbumsCommand albumsCommand = createAlbumsCommand();
		albumsCommand.setTrackId(song.getId());
		List<Album> albums = executeAndMapQuery(albumsCommand);
		if (albums.size() != 0) {
			return albums.get(0);
		} else {
			return null;
		}
	}

	public int getAlbumCount() throws SqueezeException {
		return getRepositoryFactory().getSession().execute(new InfoTotalAlbumsCommand()).getAlbumCount();
	}

	public List<Album> searchAlbums(String expression) throws SqueezeException {
		AlbumsCommand albumsCommand = createAlbumsCommand();
		albumsCommand.setSearch(expression);
		return executeAndMapQuery(albumsCommand);
	}

	private AlbumsCommand createAlbumsCommand() {
		AlbumsCommand albumsCommand = new AlbumsCommand();
		albumsCommand.setTags(AlbumTag.ALBUM, AlbumTag.YEAR, AlbumTag.DISCCOUNT, AlbumTag.COMPILATION, AlbumTag.ARTWORK_TRACK_ID);
		return albumsCommand;
	}

	@Override
	protected Album mapResult(AlbumsCommand albumsCommand) {
		int id = albumsCommand.getId();
		int year = Convert.parseInt(albumsCommand.getTag(AlbumTag.YEAR), 0);
		String name = albumsCommand.getTag(AlbumTag.ALBUM);
		int discs = Convert.parseInt(albumsCommand.getTag(AlbumTag.DISCCOUNT), 1);
		boolean isCompilation = Convert.parseInt(albumsCommand.getTag(AlbumTag.COMPILATION), 0) == 1;
		int artworkSongId = Convert.parseInt(albumsCommand.getTag(AlbumTag.ARTWORK_TRACK_ID), 0);
		return new Album(getRepositoryFactory(), id, name, year, discs, isCompilation, artworkSongId);
	}

}
