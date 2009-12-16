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
 * $Id: CLIDataCodec.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.internal.cli;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import com.dsnine.jsqueeze.SqueezeRuntimeException;

public class CLIDataCodec {

	public static final String DISPATCHID_TAG = "jsqID:";

	public static final String[] COMMANDS = new String[]{
		"login", "version","listen", "albums", "artists", "genres",
		"info total albums", "info total artists", "info total genres", "info total songs",
		"players", "songinfo", "songs", "player count", "player display type", "player id",
		"player ip", "player model", "player name",
		"playlist add", "playlist play", "playlist index", "playlist tracks", "play", "stop", "pause", "time",
		"playlist genre", "playlist artist", "playlist album", "playlist title", "playlist path",
		"playlist remote", "playlist duration" 
	};
	
	private CharsetEncoder encoder;

	private CharsetDecoder decoder;

	private String charsetName;

	private char endOfLineChar;

	public CLIDataCodec() {
		this("UTF-8");
	}

	private CLIDataCodec(String charsetName) {
		endOfLineChar = '\n';
		this.charsetName = charsetName;
		Charset charset = Charset.forName(charsetName);
		decoder = charset.newDecoder();
		encoder = charset.newEncoder();
	}

	public ByteBuffer encode(CLIData data) {
		String stringData = toString(data);
		CharBuffer charBuffer = CharBuffer.allocate(stringData.length());
		charBuffer.put(stringData.toString());
		charBuffer.clear();

		ByteBuffer byteBuffer;

		System.out.println(charBuffer.toString());
		
		try {
			byteBuffer = encoder.encode(charBuffer);
		} catch (Exception e) {
			throw new SqueezeRuntimeException("Could not encode data", e);
		}

		return byteBuffer;
	}

	public CLIData decode(ByteBuffer byteBuffer) {
		CharBuffer charBuffer;
		try {
			charBuffer = decoder.decode(byteBuffer);
		} catch (CharacterCodingException e) {
			throw new SqueezeRuntimeException("Could not decode data", e);
		}

		String stringData = charBuffer.toString();
		return fromString(stringData);
	}

	public String toString(CLIData data) {
		StringBuilder builder = new StringBuilder();

		if (data.getPlayerId() != null) {
			builder.append(encodeString(data.getPlayerId()));
			builder.append(' ');
		}

		if (data.getCommand() == null) {
			throw new SqueezeRuntimeException("Command must not be null");
		} else if (getCommandName(data.getCommand()) == null) {
			throw new SqueezeRuntimeException("Unsupported command: " + data.getCommand());
		}

		builder.append(data.getCommand());
		builder.append(' ');

		for (String parameter : data.getParameters()) {
			builder.append(encodeString(parameter));
			builder.append(' ');
		}

		builder.append(encodeString(DISPATCHID_TAG + data.getDispatchId()));

		builder.append(getEOLasChar());
		return builder.toString();
	}

	public CLIData fromString(String string) {
		CLIData data = new CLIData();

		String playerId = getPlayerId(string);
		if (playerId != null) {
			string = string.split(" ", 2)[1];
			data.setPlayerId(playerId);
		}
		
		String command = getCommandName(string);
		if (command != null) {
			string = string.replaceFirst(command, "");
			data.setCommand(command);
		} else {
			data.addError("Missing command name");
			data.setCommand(null);
		}
		
		String[] tokens = string.split(" ");

		for (int i = 0; i < tokens.length; i++) {
			String element = tokens[i].trim();
			String decodedElement = decodeString(element);
			
			if (element.length() == 0) {
				continue;
			}
			
			if (decodedElement.startsWith(DISPATCHID_TAG)) {
				data.setDispatchId(Integer.valueOf(decodedElement.replace(DISPATCHID_TAG, "")));
			} else {
				data.addParameter(decodedElement);
			}
		}

		return data;
	}

	private String encodeString(String text) {
		try {
			String pass1 = URLEncoder.encode(text, charsetName);
			String pass2 = pass1.replaceAll("\\+", "%20");
			return pass2;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Required encoding not supported: " + charsetName, e);
		}
	}

	private String decodeString(String text) {
		try {
			return URLDecoder.decode(text, charsetName);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Required encoding not supported: " + charsetName, e);
		}
	}

	private String getCommandName(String text) {
		String bestMatch = null;
		for (String command : COMMANDS) {
			if (text.indexOf(command) == 0 && (bestMatch==null || command.length()>bestMatch.length())) {
				bestMatch = command;
			}
		}
		
		return bestMatch;
	}
	
	private String getPlayerId(String text) {
		String[] parts = text.split(" ", 2);
		if (parts.length == 0) {
			return null;
		}
		
		String possibleId = decodeString(parts[0]);
		if (possibleId.contains(":")) {
			return possibleId;
		}
		
		return null;
	}
	
	public byte getEOLAsByte() {
		return (byte) endOfLineChar;
	}

	public char getEOLasChar() {
		return endOfLineChar;
	}
	
}
