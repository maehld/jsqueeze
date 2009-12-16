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
 * $Id: ConnectionLostException.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze;

public class ConnectionLostException extends SqueezeException {

	private static final long serialVersionUID = -1914335883118457764L;

	public ConnectionLostException() {
		super("Lost connection to server");
	}
		
}
