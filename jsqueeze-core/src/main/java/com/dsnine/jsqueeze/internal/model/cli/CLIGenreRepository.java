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
 * $Id: CLIGenreRepository.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.internal.model.cli;

import java.util.List;

import com.dsnine.jsqueeze.SqueezeException;
import com.dsnine.jsqueeze.cli.commands.database.GenresCommand;
import com.dsnine.jsqueeze.cli.commands.database.InfoTotalGenresCommand;
import com.dsnine.jsqueeze.cli.commands.database.SongTag;
import com.dsnine.jsqueeze.cli.commands.database.SonginfoCommand;
import com.dsnine.jsqueeze.internal.model.IGenreRepository;
import com.dsnine.jsqueeze.internal.util.Convert;
import com.dsnine.jsqueeze.model.Genre;
import com.dsnine.jsqueeze.model.Song;

public class CLIGenreRepository extends AbstractCLIRepository<Genre, GenresCommand> implements IGenreRepository {

	public CLIGenreRepository(CLIRepositoryFactory repositoryFactory) {
		super(repositoryFactory);
	}

	public List<Genre> getAllGenres() throws SqueezeException {
		return executeAndMapQuery(new GenresCommand());
	}

	public int getGenreCount() throws SqueezeException {
		return getRepositoryFactory().getSession().execute(new InfoTotalGenresCommand()).getGenreCount();
	}

	public Genre getGenreForSong(Song song) throws SqueezeException {
		//setSongId() is not supported in 6.3 so where sticking to songinfo for the moment
		SonginfoCommand songinfoCommand = new SonginfoCommand();
		songinfoCommand.setSongId(song.getId());
		songinfoCommand.setTags(SongTag.GENRE, SongTag.GENRE_ID);
		songinfoCommand = getRepositoryFactory().getSession().execute(songinfoCommand);
		if (!songinfoCommand.firstResult()) {
			return null;
		}
		return new Genre(getRepositoryFactory(), Convert.parseInt(songinfoCommand.getTag(SongTag.GENRE_ID), -1), songinfoCommand.getTag(SongTag.GENRE));
	}

	@Override
	protected Genre mapResult(GenresCommand genresCommand) {
		int id = genresCommand.getId();
		String name = genresCommand.getName();
		return new Genre(getRepositoryFactory(), id, name);
	}

}
