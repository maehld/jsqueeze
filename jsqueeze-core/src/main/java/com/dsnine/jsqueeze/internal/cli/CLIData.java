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
 * $Id: CLIData.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.internal.cli;

import java.util.ArrayList;
import java.util.List;

public class CLIData {

	private String playerId;

	private String command;

	private List<String> parameters = new ArrayList<String>();

	private Integer dispatchId;

	private List<String> errors = new ArrayList<String>();

	public String getCommand() {
		return command;
	}

	public CLIData setCommand(String command) {
		this.command = command;
		return this;
	}

	public String getPlayerId() {
		return playerId;
	}

	public CLIData setPlayerId(String playerID) {
		playerId = playerID;
		return this;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public CLIData setParameter(int index, Object value) {
		parameters.add(index, value.toString());
		return this;
	}

	public CLIData addParameter(Object value) {
		parameters.add(value.toString());
		return this;
	}

	public void setDispatchId(Integer dispatchId) {
		this.dispatchId = dispatchId;
	}

	public Integer getDispatchId() {
		return dispatchId;
	}

	@Override
	public String toString() {
		return "[" + this.getClass().getSimpleName() + ":PLAYERID=" + playerId + ";COMMAND=" + command + ";PARAMETERS=" + parameters + "]";
	}

	public List<String> getErrors() {
		return errors;
	}
	
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	public void addError(String errorMessage) {
		errors.add(errorMessage);
	}
	
}
