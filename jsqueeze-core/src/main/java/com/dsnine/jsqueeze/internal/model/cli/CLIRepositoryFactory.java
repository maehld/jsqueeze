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
 * $Id: CLIRepositoryFactory.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.internal.model.cli;

import com.dsnine.jsqueeze.Configuration;
import com.dsnine.jsqueeze.SqueezeException;
import com.dsnine.jsqueeze.cli.CLISession;
import com.dsnine.jsqueeze.internal.model.RepositoryFactory;

public class CLIRepositoryFactory extends RepositoryFactory {
	
	private SharedCLISession session;
	
	public CLIRepositoryFactory(Configuration configuration) {
		setAlbumRepository(new CLIAlbumRepository(this));
		setArtistRepository(new CLIArtistRepository(this));
		setSongRepository(new CLISongRepository(this));
		setGenreRepository(new CLIGenreRepository(this));
		session = SharedCLISession.get(configuration);
	}

	public CLISession getSession() throws SqueezeException {
		return session;
	}
	
	@Override
	public void doDispose() throws SqueezeException {
		session.close();
	}
}
