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
 * $Id: GrowingByteBuffer.java 66 2007-04-09 13:58:19Z dominikm $
 *******************************************************************************/

package com.dsnine.jsqueeze.internal.cli;

import java.nio.ByteBuffer;

public class GrowingByteBuffer {
	private ByteBuffer buffer;
	private boolean useDirect;
	
	public GrowingByteBuffer(int initialCapacity, boolean useDirect) {
		this.useDirect = useDirect;
		buffer = allocateNewBuffer(initialCapacity);
		buffer.limit(0);
	}
	
	public void add(ByteBuffer toAdd) {
		int needed = toAdd.remaining();
		int available = buffer.capacity() - buffer.limit();
		
		if (needed > available) {
			ByteBuffer newBuffer = allocateNewBuffer(buffer.capacity() + needed);
			buffer.rewind();
			newBuffer.put(buffer);
			newBuffer.limit(newBuffer.position());
			buffer = newBuffer;
		}
		
		buffer.limit(buffer.limit() + needed);
		buffer.put(toAdd);
	}
	
	public void recycle() {
		buffer.rewind();
		buffer.limit(0);
	}
	
	public int size() {
		return buffer.limit();
	}
	
	public int capacity() {
		return buffer.capacity();
	}
	
	public byte get(int index) {
		return buffer.get(index);
	}
	
	public ByteBuffer getCopy() {
		ByteBuffer copy = ByteBuffer.allocate(size());
		buffer.rewind();
		copy.put(buffer);
		copy.rewind();
		return copy;
	}

	private ByteBuffer allocateNewBuffer(int capacity) {
		if (useDirect) {
			return ByteBuffer.allocateDirect(capacity);
		} else {
			return ByteBuffer.allocate(capacity);
		}
	}

}
