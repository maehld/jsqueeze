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
 * $Id: HTTPSession.java 68 2007-04-11 19:14:20Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.http;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;

import com.dsnine.jsqueeze.Configuration;
import com.dsnine.jsqueeze.SqueezeRuntimeException;

public class HTTPSession {
	private Configuration config;

	public HTTPSession(Configuration configuration) {
		config = configuration;
	}
	
	public Image getArtwork(int trackId) {
		Image artworkImage = null;
		
		try {
			String file = "/music/" + trackId + "/thumb.jpg";
			URL imageURL = new URL("http", config.getServerAddress().getHostAddress(), config.getHTTPPort(), file);
			artworkImage = Toolkit.getDefaultToolkit().createImage(imageURL);
		} catch (MalformedURLException e) {
			throw new SqueezeRuntimeException("Invalid URL created");
		}
		
		return artworkImage;
	}
	
}
