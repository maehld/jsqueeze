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
 * $Id: ServerVersion.java 67 2007-04-11 19:08:34Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.util;

public class ServerVersion implements Comparable<ServerVersion> {

	private int major;
	private int minor;
	private int service;

	public static final ServerVersion V6_3 = new ServerVersion(6, 3, 0);

	public static final ServerVersion V6_5 = new ServerVersion(6, 5, 0);

	public static final ServerVersion V7_0 = new ServerVersion(7, 0, 0);

	public static final ServerVersion V7_1 = new ServerVersion(7, 1, 0);

	public static final ServerVersion V7_2 = new ServerVersion(7, 2, 0);

	public static ServerVersion fromString(String versionString) {
		ServerVersion instance = null;

		int major = 0;
		int minor = 0;
		int service = 0;

		try {
			String[] versionParts = versionString.split("\\.");
			if (versionParts.length >= 1) {
				major = Integer.parseInt(versionParts[0]);
			}
			if (versionParts.length >= 2) {
				minor = Integer.parseInt(versionParts[1]);
			}
			if (versionParts.length >= 3) {
				service = Integer.parseInt(versionParts[2]);
			}
		} catch (NumberFormatException e) {
		}

		instance = new ServerVersion(major, minor, service);

		return instance;
	}

	public ServerVersion(int major, int minor, int service) {
		this.major = major;
		this.minor = minor;
		this.service = service;
	}

	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}

	public int getService() {
		return service;
	}

	public boolean isAtLeast(ServerVersion least) {
		return (compareTo(least) >= 0) ? true : false;
	}

	public boolean isAtMost(ServerVersion most) {
		return (compareTo(most) <= 0) ? true : false;
	}

	public boolean isInRange(ServerVersion lower, ServerVersion upper) {
		System.out.println(toNumber());
		return (toNumber() >= lower.toNumber() && toNumber() <= upper.toNumber());
	}

	@Override
	public boolean equals(Object thatObject) {
		if (!(thatObject instanceof ServerVersion)) {
			return false;
		}

		ServerVersion that = (ServerVersion) thatObject;

		return (that.major == major && that.minor == minor && that.service == service);
	}

	@Override
	public String toString() {
		return String.format("%d.%d.%d", major, minor, service);
	}

	private int toNumber() {
		return (major * 10000) + (minor * 100) + service;
	}

	public int compareTo(ServerVersion that) {
		return new Integer(toNumber()).compareTo(new Integer(that.toNumber()));
	}

}
