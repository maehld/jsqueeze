/*******************************************************************************
 * Copyright (c) 2005, 2006, 2007, 2008 Dominik Maehl and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Dominik Maehl - initial API and implementation
 * 
 * $Id: CLIQueryCommand.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dsnine.jsqueeze.internal.cli.CLIData;
import com.dsnine.jsqueeze.internal.util.Assert;

public abstract class CLIQueryCommand extends CLICommand {
	private int start;

	private int itemsPerResponse;

	private Map<String, String> taggedParameters;

	private List<Map<String, String>> results;

	private int resultCursor = -1;

	private String resultDelimiter;

	private String commandName;

	protected CLIQueryCommand(String commandName, String resultDelimiter, int start, int itemsPerResponse) {
		this.commandName = commandName;
		this.resultDelimiter = resultDelimiter;
		taggedParameters = new HashMap<String, String>();
		this.start = start;
		this.itemsPerResponse = itemsPerResponse;
	}

	protected CLIQueryCommand(String commandName, int start, int itemsPerResponse) {
		this(commandName, "id", start, itemsPerResponse);
	}

	public CLIQueryCommand(String commandName) {
		this(commandName, 0, Integer.MAX_VALUE);
	}

	final protected void setTaggedParameter(String name, Object value) {
		taggedParameters.put(name, value.toString());
	}

	final protected Map<String, String> getTaggedParameters() {
		return taggedParameters;
	}

	@Override
	final protected void parseResponse(CLIData response) throws Exception {
		results = new ArrayList<Map<String, String>>();
		Map<String, String> result = null;

		for (String parameter : response.getParameters()) {
			if (parameter.contains(":")) {
				String[] parts = parameter.split(":", 2);
				if (parts[0].equals(resultDelimiter)) {
					if (result != null) {
						results.add(result);
					}

					result = new HashMap<String, String>();
				}

				if (result == null) {
					taggedParameters.put(parts[0], parts[1]);
				} else {
					result.put(parts[0], parts[1]);
				}
			}
		}

		if (result != null) {
			results.add(result);
		}

	}

	@Override
	final protected void buildRequest(CLIData request) throws Exception {
		request.setCommand(commandName);
		request.addParameter(start);
		request.addParameter(itemsPerResponse);

		for (String name : taggedParameters.keySet()) {
			request.addParameter(name + ":" + taggedParameters.get(name));
		}
	}

	final protected String getCurrentResultValue(String name) {
		Assert.isFalse(getCount() == 0, "Response contains no results");
		Assert.isFalse(resultCursor == -1, "Cursor must be initialized before use");
		Assert.isFalse(resultCursor == -2, "Cursor points to an invalid result position");

		Map<String, String> values = results.get(resultCursor);
		if (values.containsKey(name)) {
			return values.get(name);
		} else {
			return null;
		}
	}

	final public boolean nextResult() {
		resultCursor++;
		return validateCursorPosition();
	}

	final public boolean previousResult() {
		resultCursor--;
		return validateCursorPosition();
	}

	final public boolean firstResult() {
		resultCursor = 0;
		return validateCursorPosition();
	}

	final public boolean lastResult() {
		resultCursor = getCount() - 1;
		return validateCursorPosition();
	}

	final private boolean validateCursorPosition() {
		if (resultCursor < 0 || resultCursor >= getCount()) {
			resultCursor = -2;
			return false;
		} else {
			return true;
		}
	}

	public int getTotalCount() {
		return Integer.parseInt(getTaggedParameters().get("count"));
	}

	public int getCount() {
		return results.size();
	}

	public boolean isScanning() {
		return getTaggedParameters().containsKey("rescan");
	}

	public int getId() {
		return Integer.valueOf(getCurrentResultValue(resultDelimiter));
	}

	@Override
	public String toString() {
		return CLIQueryCommand.class.getSimpleName() + ":NAME=" + commandName + ";PARAMETERS=" + taggedParameters + ";START=" + start + ";MAXITEMS=" + itemsPerResponse;
	}

}
