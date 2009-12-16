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
 * $Id: AbstractCLIRepository.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.internal.model.cli;

import java.util.ArrayList;
import java.util.List;

import com.dsnine.jsqueeze.SqueezeException;
import com.dsnine.jsqueeze.cli.CLIQueryCommand;
import com.dsnine.jsqueeze.internal.model.IRepository;
import com.dsnine.jsqueeze.util.ServerVersion;

abstract public class AbstractCLIRepository<T, Q extends CLIQueryCommand> implements IRepository {
	private CLIRepositoryFactory repositoryFactory;

	public AbstractCLIRepository(CLIRepositoryFactory repositoryFactory) {
		this.repositoryFactory = repositoryFactory;
	}
	
	public CLIRepositoryFactory getRepositoryFactory() {
		return repositoryFactory;
	}
	
	protected List<T> executeAndMapQuery(Q query) throws SqueezeException {
		List<T> results = new ArrayList<T>();
		
		query = repositoryFactory.getSession().execute(query);
		while (query.nextResult()) {
			T result = mapResult(query);
			if (result != null) {
				results.add(result);
			}
		}
		
		return results;
	}

	protected final ServerVersion getVersion() throws SqueezeException {
		return repositoryFactory.getSession().getVersion();
	}
	
	abstract protected T mapResult(Q queryCommand) throws SqueezeException;
	
	public void dispose() {
	}
	
}
