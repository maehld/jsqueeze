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
 * $Id: IGenreRepository.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.internal.model;

import java.util.List;

import com.dsnine.jsqueeze.SqueezeException;
import com.dsnine.jsqueeze.model.Genre;
import com.dsnine.jsqueeze.model.Song;

public interface IGenreRepository extends IRepository {

	List<Genre> getAllGenres() throws SqueezeException;

	Genre getGenreForSong(Song song) throws SqueezeException;
	
	//LATER: Currently there seems to be now way to query by id --- Genre getGenreById(int id) throws SqueezeException;
	
	int getGenreCount() throws SqueezeException;

}
