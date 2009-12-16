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
 * $Id: ServerInformationFactory.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.internal.model;

import com.dsnine.jsqueeze.Configuration;
import com.dsnine.jsqueeze.SqueezeException;
import com.dsnine.jsqueeze.internal.model.cli.CLIServerInformation;

public class ServerInformationFactory {

	public static IServerInformation create(Configuration configuration) throws SqueezeException {
		return new CLIServerInformation(configuration);
	}

}
