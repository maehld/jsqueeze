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
 * $Id: Album.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.model;

import java.util.List;

import com.dsnine.jsqueeze.SqueezeException;
import com.dsnine.jsqueeze.internal.model.RepositoryFactory;

public class Album extends Entity {

	private final String name;
	private final int year;
	private final int discs;
	private final boolean compilation;
	private final int artworkSongId;

	public Album(RepositoryFactory repositoryFactory, int id, String name, int year, int discs, boolean compilation, int artworkSongId) {
		super(repositoryFactory, id);
		this.name = name;
		this.year = year;
		this.discs = discs;
		this.compilation = compilation;
		this.artworkSongId = artworkSongId;
	}

	public String getName() {
		return name;
	}
	
	public int getDiscs() {
		return discs;
	}
	
	public int getYear() {
		return year;
	}
	
	public boolean isCompilation() {
		return compilation;
	}
	
	public Song getArtworkSong() throws SqueezeException {
		return getRepositoryFactory().getSongRepository().getSongById(artworkSongId);
	}
	
	public List<Artist> getArtists() throws SqueezeException {
		return getRepositoryFactory().getArtistRepository().getArtistsForAlbum(this);
	}
	
	public List<Song> getSongs() throws SqueezeException {
		return getRepositoryFactory().getSongRepository().getSongsForAlbum(this);
	}
	
}
