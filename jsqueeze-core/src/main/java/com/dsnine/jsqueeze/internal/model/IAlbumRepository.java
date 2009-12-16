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
 * $Id: IAlbumRepository.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.internal.model;

import java.util.List;

import com.dsnine.jsqueeze.SqueezeException;
import com.dsnine.jsqueeze.model.Album;
import com.dsnine.jsqueeze.model.Artist;
import com.dsnine.jsqueeze.model.Song;

public interface IAlbumRepository extends IRepository {

	List<Album> getAllAlbums() throws SqueezeException;

	List<Album> getAlbumsForArtist(Artist artist) throws SqueezeException;
	
	//LATER: Currently there seems to be now way to query by id --- Album getAlbumById(int id) throws SqueezeException;

	Album getAlbumForSong(Song song) throws SqueezeException;

	int getAlbumCount() throws SqueezeException;

	List<Album> searchAlbums(String expression) throws SqueezeException;

}
