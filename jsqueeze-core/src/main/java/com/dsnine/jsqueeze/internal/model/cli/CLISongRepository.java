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
 * $Id: CLISongRepository.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.internal.model.cli;

import java.util.List;

import com.dsnine.jsqueeze.SqueezeException;
import com.dsnine.jsqueeze.cli.commands.database.InfoTotalSongsCommand;
import com.dsnine.jsqueeze.cli.commands.database.SongTag;
import com.dsnine.jsqueeze.cli.commands.database.SonginfoCommand;
import com.dsnine.jsqueeze.cli.commands.database.SongsCommand;
import com.dsnine.jsqueeze.internal.model.ISongRepository;
import com.dsnine.jsqueeze.internal.util.Convert;
import com.dsnine.jsqueeze.model.Album;
import com.dsnine.jsqueeze.model.Artist;
import com.dsnine.jsqueeze.model.Song;
import com.dsnine.jsqueeze.util.ServerVersion;

public class CLISongRepository extends AbstractCLIRepository<Song, SongsCommand> implements ISongRepository {
	
	private SongTag[] tags = new SongTag[]{
			SongTag.DISC, SongTag.TRACKNUMBER, SongTag.REMOTE, SongTag.TYPE, SongTag.URL
	};
	
	public CLISongRepository(CLIRepositoryFactory repositoryFactory) {
		super(repositoryFactory);
	}

	public List<Song> getAllSongs() throws SqueezeException {
		return executeAndMapQuery(createSongsCommand());
	}
	
	public List<Song> getSongsForAlbum(Album album) throws SqueezeException {
		SongsCommand songsCommand = createSongsCommand();
		songsCommand.setAlbumId(album.getId());
		return executeAndMapQuery(songsCommand);
	}
	
	public List<Song> getSongsForArtist(Artist artist) throws SqueezeException {
		SongsCommand songsCommand = createSongsCommand();
		songsCommand.setArtistId(artist.getId());
		return executeAndMapQuery(songsCommand);
	}
	
	public Song getSongById(int id) throws SqueezeException {
		SonginfoCommand songinfoCommand = new SonginfoCommand();
		songinfoCommand.setSongId(id);
		songinfoCommand.setTags(tags);
		songinfoCommand = getRepositoryFactory().getSession().execute(songinfoCommand);
		
		if (!songinfoCommand.firstResult()) {
			return null;
		}
		
		//At the moment we don't want to handle remote songs 1/2
		if (getVersion().isAtLeast(ServerVersion.V6_5) && getVersion().isAtMost(ServerVersion.V6_5) && songinfoCommand.getTag(SongTag.REMOTE).equals("1")) {
			return null;
		}
		
		String title = songinfoCommand.getTitle();
		int disc = Convert.parseInt(songinfoCommand.getTag(SongTag.DISC), 1);
		int trackNumber = Convert.parseInt(songinfoCommand.getTag(SongTag.TRACKNUMBER), 0);
		String url = songinfoCommand.getTag(SongTag.URL);
		
		return new Song(getRepositoryFactory(), id, title, disc, trackNumber, url);
	}
	
	public int getSongCount() throws SqueezeException {
		return getRepositoryFactory().getSession().execute(new InfoTotalSongsCommand()).getSongCount();
	}
	
	private SongsCommand createSongsCommand() {
		SongsCommand songsCommand = new SongsCommand();
		songsCommand.setTags(tags);
		return songsCommand;
	}
	
	@Override
	protected Song mapResult(SongsCommand songsCommand) throws SqueezeException {
		//At the moment we don't want to handle remote songs 2/2
		if (getVersion().isAtLeast(ServerVersion.V6_5) && getVersion().isAtMost(ServerVersion.V6_5) && songsCommand.getTag(SongTag.REMOTE).equals("1")) {
			return null;
		}
		
		int id = songsCommand.getId();
		String title = songsCommand.getTitle();
		int disc = Convert.parseInt(songsCommand.getTag(SongTag.DISC), 1);
		int trackNumber = Convert.parseInt(songsCommand.getTag(SongTag.TRACKNUMBER), 0);
		String url = songsCommand.getTag(SongTag.URL);
		
		return new Song(getRepositoryFactory(), id, title, disc, trackNumber, url);
	}
}
