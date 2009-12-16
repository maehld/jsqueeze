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
 * $Id: SongTag.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.cli.commands.database;

public enum SongTag {
	// Genre name. Only if known.
	GENRE("genre", 'g'),
	// Genre ID. Only if known.
	GENRE_ID("genre_id", 'p'),
	// Artist name. Only if known.
	ARTIST("artist", 'a'),
	// Artist ID. Only if known.
	ARTIST_ID("artist_id", 's'),
	// Composer name. Only if known.
	COMPOSER("composer", 'c'),
	// Band name. Only if known.
	BAND("band", 'b'),
	// Conductor name. Only if known.
	CONDUCTOR("conductor", 'h'),
	// Album name. Only if known.
	ALBUM("album", 'l'),
	// Album ID. Only if known.
	ALBUM_ID("album_id", 'e'),
	// Song duration in seconds.
	DURATION("duration", 'd'),
	// Disc number. Only if known.
	DISC("disc", 'i'),
	// Number of discs. Only if known.
	DISCCOUNT("disccount", 'q'),
	// Track number. Only if known.
	TRACKNUMBER("tracknum", 't'),
	// Song year. Only if known.
	YEAR("year", 'y'),
	// Beats per minute. Only if known.
	BPM("bpm", 'm'),
	// Song comments, if any.
	COMMENT("comment", 'k'),
	// Content type. Only if known.
	TYPE("type", 'o'),
	// Version of tag information in song file. Only if known.
	TAGVERSION("tagversion", 'v'),
	// Song bitrate. Only if known.
	BITRATE("bitrate", 'r'),
	// Song file length in bytes. Only if known.
	FILESIZE("filesize", 'f'),
	// Digital rights information. Only if known.
	DRM("drm", 'z'),
	// 1 if coverart is available for this song. Not listed otherwise.
	COVERART("coverart", 'j'),
	// Date and time song file was last changed.
	MODIFICATIONTIME("modificationTime", 'n'),
	// Song file url. Used as <item> parameter for the "playlist add" command,
	// for example.
	URL("url", 'u'),
	// Lyrics. Only if known.
	LYRICS("lyrics", 'w'),
	// Remote track.
	REMOTE("remote", 'x');

	private char identifier;

	private String name;

	SongTag(String name, char identifier) {
		this.name = name;
		this.identifier = identifier;
	}

	public String getName() {
		return name;
	}

	public char getIdentifier() {
		return identifier;
	}
}
