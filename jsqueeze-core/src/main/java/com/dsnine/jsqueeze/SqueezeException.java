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
 * $Id: SqueezeException.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze;

public class SqueezeException extends Exception {

	private static final long serialVersionUID = -3945204346361392774L;

	public SqueezeException(String message) {
		super(message);
	}

	public SqueezeException(Throwable cause) {
		super(cause);
	}

	public SqueezeException(String message, Throwable cause) {
		super(message, cause);
	}

}
