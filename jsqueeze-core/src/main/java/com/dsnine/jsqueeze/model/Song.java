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
 * $Id: Song.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.model;

import com.dsnine.jsqueeze.SqueezeException;
import com.dsnine.jsqueeze.internal.model.RepositoryFactory;

public class Song extends Entity {

	private final String title;
	private final int disc;
	private final int track;
	private final String url;
	
	public Song(RepositoryFactory repositoryFactory, int id, String title, int disc, int track, String url) {
		super(repositoryFactory, id);
		this.title = title;
		this.disc = disc;
		this.track = track;
		this.url = url;
	}

	public int getDisc() {
		return disc;
	}

	public String getTitle() {
		return title;
	}

	public int getTrack() {
		return track;
	}
	
	public String getUrl() {
		return url;
	}
	
	public Artist getArtist() throws SqueezeException {
		return getRepositoryFactory().getArtistRepository().getArtistForSong(this);
	}
	
	public Album getAlbum() throws SqueezeException {
		return getRepositoryFactory().getAlbumRepository().getAlbumForSong(this);
	}

	public Genre getGenre() throws SqueezeException {
		return getRepositoryFactory().getGenreRepository().getGenreForSong(this);
	}
}
