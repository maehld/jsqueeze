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
 * $Id: Artist.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.model;

import java.util.List;

import com.dsnine.jsqueeze.SqueezeException;
import com.dsnine.jsqueeze.internal.model.RepositoryFactory;

public class Artist extends Entity {

	private final String name;

	public Artist(RepositoryFactory repositoryFactory, int id, String name) {
		super(repositoryFactory, id);
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public List<Album> getAlbums() throws SqueezeException {
		return getRepositoryFactory().getAlbumRepository().getAlbumsForArtist(this);
	}

	public List<Song> getSongs() throws SqueezeException {
		return getRepositoryFactory().getSongRepository().getSongsForArtist(this);
	}

}
