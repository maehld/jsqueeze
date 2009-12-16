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
 * $Id: ISongRepository.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.internal.model;

import java.util.List;

import com.dsnine.jsqueeze.SqueezeException;
import com.dsnine.jsqueeze.model.Album;
import com.dsnine.jsqueeze.model.Artist;
import com.dsnine.jsqueeze.model.Song;

public interface ISongRepository extends IRepository {

	List<Song> getAllSongs() throws SqueezeException;
	
	List<Song> getSongsForAlbum(Album album) throws SqueezeException;

	Song getSongById(int id) throws SqueezeException;

	List<Song> getSongsForArtist(Artist artist) throws SqueezeException;

	int getSongCount() throws SqueezeException;
	
}
