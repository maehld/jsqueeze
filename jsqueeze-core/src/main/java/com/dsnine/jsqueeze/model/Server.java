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
 * $Id: Server.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.model;

import java.util.List;

import com.dsnine.jsqueeze.Configuration;
import com.dsnine.jsqueeze.SqueezeException;
import com.dsnine.jsqueeze.internal.model.IServerInformation;
import com.dsnine.jsqueeze.internal.model.RepositoryFactory;
import com.dsnine.jsqueeze.internal.model.ServerInformationFactory;
import com.dsnine.jsqueeze.util.ServerVersion;

public class Server {
	public static Server create(Configuration configuration) throws SqueezeException {
		return new Server(configuration);
	}

	private RepositoryFactory repositoryFactory;
	private IServerInformation serverInformation;
	
	public Server(Configuration configuration) throws SqueezeException {
		serverInformation = ServerInformationFactory.create(configuration);
		repositoryFactory = RepositoryFactory.create(configuration);
	}
	
	public void disconnect() throws SqueezeException {
		repositoryFactory.dispose();
		serverInformation.dispose();
	}
	
	//getAll* entry points
	public List<Album> getAllAlbums() throws SqueezeException {
		return repositoryFactory.getAlbumRepository().getAllAlbums();
	}
	
	public List<Song> getAllSongs() throws SqueezeException {
		return repositoryFactory.getSongRepository().getAllSongs();
	}
	
	public List<Artist> getAllArtists() throws SqueezeException {
		return repositoryFactory.getArtistRepository().getAllArtists();
	}
	
	public List<Genre> getAllGenres() throws SqueezeException {
		return repositoryFactory.getGenreRepository().getAllGenres();
	}
	
	//get*ById entry points
	public Song getSongById(int id) throws SqueezeException {
		return repositoryFactory.getSongRepository().getSongById(id);
	}
		
	//*Count methods
	public int getSongCount() throws SqueezeException {
		return repositoryFactory.getSongRepository().getSongCount();
	}

	public int getAlbumCount() throws SqueezeException {
		return repositoryFactory.getAlbumRepository().getAlbumCount();
	}

	public int getArtistCount() throws SqueezeException {
		return repositoryFactory.getArtistRepository().getArtistCount();
	}

	public int getGenreCount() throws SqueezeException {
		return repositoryFactory.getGenreRepository().getGenreCount();
	}
	
	//search* methods
	public List<Album> searchAlbums(String expression) throws SqueezeException {
		return repositoryFactory.getAlbumRepository().searchAlbums(expression);
	}
	
	//Other methods
	public ServerVersion getVersion() throws SqueezeException {
		return serverInformation.getVersion();
	}

}
