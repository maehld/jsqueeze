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
 * $Id: ModelTest.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.model;

import java.util.List;

import com.dsnine.jsqueeze.Configuration;

public class ModelTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	
	@SuppressWarnings("all")
	public static void main(String[] args) throws Exception {
		long starttime = System.currentTimeMillis();
		
		Configuration configuration = Configuration.create();
		configuration.setServerAddress("192.168.0.3");
		configuration.setUsername("john");
		configuration.setPassword("doe");
		//configuration.setModelQueryStrategy(QueryStrategyType.CACHED);
		Server server = Server.create(configuration);
		
		System.out.println("SlimServer version: " + server.getVersion());
		System.out.println("Total songs:        " + server.getSongCount());
		System.out.println("Total albums:       " + server.getAlbumCount());
		System.out.println("Total artists:      " + server.getArtistCount());
		//System.out.println(server.getGenreCount());
		
		List<Album> albums = null;
		//albums = server.searchAlbums("Songs From Dawson's Creek");
		albums = server.getAllAlbums();
		/*
		Album album = albums.get(0);
			for (Song song : album.getSongs()) {
				Artist songArtist = song.getArtist();
				Genre songGenre = song.getGenre();
				System.out.println(album.isCompilation() +  " " + album.getName() + "(" + album.getId() + ") - " + song.getTitle() + "(" + song.getId() + "): Artist: " + songArtist.getName() + ", Genre: " + songGenre );
			}
		*/
	
		for (Artist artist : server.getAllArtists()) {
			System.out.println(artist.getName());

			for (Album artistAlbum : artist.getAlbums()) {
				System.out.println("  " + artistAlbum.getName() + " (" + artistAlbum.getArtworkSong().getGenre().getName() + ")");
			}
		}
		
		long endtime = System.currentTimeMillis();
		
		System.out.println((endtime - starttime) + "msecs");
		server.disconnect();
	}

}
