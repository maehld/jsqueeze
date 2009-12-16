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
 * $Id: AlbumTag.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.cli.commands.database;

public enum AlbumTag {
	ALBUM("album", 'l'), YEAR("year", 'y'), ARTWORK_TRACK_ID("artwork_track_id", 'j'), TITLE("title", 't'), DISC("disc", 'i'), DISCCOUNT("disccount", 'q'), COMPILATION("compilation", 'w'), ARTIST("artist", 'a');

	private char identifier;

	private String name;

	AlbumTag(String name, char identifier) {
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
