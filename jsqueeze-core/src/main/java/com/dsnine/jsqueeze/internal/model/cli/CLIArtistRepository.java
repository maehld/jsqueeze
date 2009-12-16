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
 * $Id: CLIArtistRepository.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.internal.model.cli;

import java.util.List;

import com.dsnine.jsqueeze.SqueezeException;
import com.dsnine.jsqueeze.cli.commands.database.ArtistsCommand;
import com.dsnine.jsqueeze.cli.commands.database.InfoTotalArtistsCommand;
import com.dsnine.jsqueeze.cli.commands.database.SongTag;
import com.dsnine.jsqueeze.cli.commands.database.SonginfoCommand;
import com.dsnine.jsqueeze.internal.model.IArtistRepository;
import com.dsnine.jsqueeze.model.Album;
import com.dsnine.jsqueeze.model.Artist;
import com.dsnine.jsqueeze.model.Song;

public class CLIArtistRepository extends AbstractCLIRepository<Artist, ArtistsCommand> implements IArtistRepository {

	public CLIArtistRepository(CLIRepositoryFactory repositoryFactory) {
		super(repositoryFactory);
	}

	public List<Artist> getAllArtists() throws SqueezeException {
		return executeAndMapQuery(new ArtistsCommand());
	}

	public List<Artist> getArtistsForAlbum(Album album) throws SqueezeException {
		ArtistsCommand artistsCommand = new ArtistsCommand();
		artistsCommand.setAlbumId(album.getId());
		// FIXME: Somehow bug 4625 now affects artists-queries.
		List<Artist> artists = executeAndMapQuery(artistsCommand);
		if (artists.isEmpty() && album.isCompilation()) {
			artists.add(new Artist(getRepositoryFactory(), 0, "Various Artists"));
		}
		return artists;
	}

	public int getArtistCount() throws SqueezeException {
		return getRepositoryFactory().getSession().execute(new InfoTotalArtistsCommand()).getArtistCount();
	}

	@Override
	protected Artist mapResult(ArtistsCommand artistsCommand) {
		int id = artistsCommand.getId();
		String name = artistsCommand.getName();
		return new Artist(getRepositoryFactory(), id, name);
	}

	public Artist getArtistForSong(Song song) throws SqueezeException {
		// FIXME: setSongId() is not supported in 6.3 so where sticking to
		// songinfo for
		// the moment
		// And there is bug 4625
		// (http://bugs.slimdevices.com/show_bug.cgi?id=4625), which prevents
		// getting an artist by song id if the
		// artist only occurs in Compilations
		SonginfoCommand songinfoCommand = new SonginfoCommand();
		songinfoCommand.setTags(SongTag.ARTIST, SongTag.ARTIST_ID);
		songinfoCommand.setSongId(song.getId());
		songinfoCommand = getRepositoryFactory().getSession().execute(songinfoCommand);
		songinfoCommand.firstResult();
		return new Artist(getRepositoryFactory(), Integer.parseInt(songinfoCommand.getTag(SongTag.ARTIST_ID)), songinfoCommand.getTag(SongTag.ARTIST));
	}

}
