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
 * $Id: Entity.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.model;

import com.dsnine.jsqueeze.internal.model.RepositoryFactory;

public abstract class Entity {
	
	private final int id;
	private final RepositoryFactory repositoryFactory;
	
	protected Entity(RepositoryFactory repositoryFactory, int id) {
		this.id = id;
		this.repositoryFactory = repositoryFactory;
	}

	public int getId() {
		return id;
	}
	
	protected RepositoryFactory getRepositoryFactory() {
		return repositoryFactory;
	}

	@Override
	public boolean equals(Object thatObject) {
		if (!(thatObject instanceof Entity)) {
			return false;
		}

		if (!thatObject.getClass().equals(this.getClass())) {
			return false;
		}
		
		Entity that = (Entity) thatObject;
		
		return that.getId() == this.getId();
	}
	
	@Override
	public int hashCode() {
		return (this.getClass().getName() + id).hashCode(); 
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ":" + this.getId();
	}
	
}
