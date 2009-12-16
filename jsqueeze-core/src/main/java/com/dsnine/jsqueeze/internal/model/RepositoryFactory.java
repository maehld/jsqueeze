/*******************************************************************************
 * Copyright (c) 2005, 2006, 2007, 2008 Dominik Maehl and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Dominik Maehl - initial API and implementation
 * 
 * $Id: RepositoryFactory.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.internal.model;

import com.dsnine.jsqueeze.Configuration;
import com.dsnine.jsqueeze.SqueezeException;
import com.dsnine.jsqueeze.Configuration.QueryStrategyType;
import com.dsnine.jsqueeze.internal.model.cli.CLIRepositoryFactory;
import com.dsnine.jsqueeze.internal.util.Assert;

abstract public class RepositoryFactory {

	public static RepositoryFactory create(com.dsnine.jsqueeze.Configuration configuration) throws SqueezeException {
		RepositoryFactory instance = null;

		if (configuration.getModelQueryStrategy() == QueryStrategyType.CLI) {
			instance = new CLIRepositoryFactory(configuration);
		}

		// TODO: Enable caching by uncommenting the next line
		// CachingRepositoryFactoryEnhancer.enhance(instance);

		Assert.isNotNull(instance, "Query strategy <" + configuration.getModelQueryStrategy().toString() + "> not implemented");

		return instance;
	}

	private Configuration configuration;
	private IAlbumRepository albumRepository;
	private ISongRepository songRepository;
	private IGenreRepository genreRepository;
	private IArtistRepository artistRepository;

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setAlbumRepository(IAlbumRepository albumRepository) {
		this.albumRepository = albumRepository;
	}

	public void setArtistRepository(IArtistRepository artistRepository) {
		this.artistRepository = artistRepository;
	}

	public void setGenreRepository(IGenreRepository genreRepository) {
		this.genreRepository = genreRepository;
	}

	public void setSongRepository(ISongRepository songRepository) {
		this.songRepository = songRepository;
	}

	final public IAlbumRepository getAlbumRepository() {
		return albumRepository;
	}

	final public IArtistRepository getArtistRepository() {
		return artistRepository;
	}

	final public IGenreRepository getGenreRepository() {
		return genreRepository;
	}

	final public ISongRepository getSongRepository() {
		return songRepository;
	}

	final public void dispose() throws SqueezeException {
		artistRepository.dispose();
		songRepository.dispose();
		genreRepository.dispose();
		albumRepository.dispose();
		doDispose();
	}

	protected void doDispose() throws SqueezeException {
	}

}
