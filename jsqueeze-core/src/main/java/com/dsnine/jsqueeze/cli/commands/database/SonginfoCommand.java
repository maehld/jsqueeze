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
 * $Id: SonginfoCommand.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.cli.commands.database;

import com.dsnine.jsqueeze.cli.CLIQueryCommand;

public class SonginfoCommand extends CLIQueryCommand {

	public SonginfoCommand() {
		super("songinfo");
	}

	public SonginfoCommand(int start, int itemsPerResponse) {
		super("songinfo", start, itemsPerResponse);
	}

	public void setUrl(String url) {
		setTaggedParameter("url", url);
	}

	public void setSongId(int songId) {
		setTaggedParameter("track_id", songId);
	}

	public void setTags(SongTag... tags) {
		StringBuilder builder = new StringBuilder();
		for (SongTag tag : tags) {
			builder.append(tag.getIdentifier());
		}

		setTaggedParameter("tags", builder.toString());
	}

	public String getTitle() {
		return getCurrentResultValue("title");
	}

	public String getTag(SongTag tag) {
		return getCurrentResultValue(tag.getName());
	}

}
