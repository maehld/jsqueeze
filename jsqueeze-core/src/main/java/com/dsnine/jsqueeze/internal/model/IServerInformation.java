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
 * $Id: IServerInformation.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.internal.model;

import com.dsnine.jsqueeze.SqueezeException;
import com.dsnine.jsqueeze.util.ServerVersion;

public interface IServerInformation {

	ServerVersion getVersion() throws SqueezeException;
	void dispose() throws SqueezeException;
	
}
