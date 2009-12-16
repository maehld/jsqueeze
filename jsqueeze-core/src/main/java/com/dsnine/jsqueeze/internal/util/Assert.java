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
 * $Id: Assert.java 68 2007-04-11 19:14:20Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.internal.util;

import com.dsnine.jsqueeze.SqueezeRuntimeException;

public class Assert {
	private Assert() {
	}
	
	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new SqueezeRuntimeException("Assertion failed: " + message);
		}
	}

	public static void isFalse(boolean expression, String message) {
		isTrue(!expression, message);
	}
	
	public static void isNotNull(Object object, String message) {
		isTrue(object != null, message);
	}

	public static void isNull(Object object, String message) {
		isTrue(object == null, message);
	}
}
