/**
 * Copyright 2016 Antony Holmes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jebtk.graphplot.figure.heatmap.legacy;

import org.jebtk.core.NameGetter;
import org.jebtk.core.text.TextUtils;

/**
 * The Class CountGroup.
 */
public class CountGroup implements NameGetter {

	/** The m name. */
	private String mName;

	/** The m start. */
	private int mStart;

	/** The m end. */
	private int mEnd;

	/** The m size. */
	private int mSize;

	/**
	 * Instantiates a new count group.
	 *
	 * @param start the start
	 * @param end   the end
	 */
	public CountGroup(int start, int end) {
		this(TextUtils.EMPTY_STRING, start, end);
	}

	/**
	 * Instantiates a new count group.
	 *
	 * @param name  the name
	 * @param start the start
	 * @param end   the end
	 */
	public CountGroup(String name, int start, int end) {
		mName = name;
		mStart = start;
		mEnd = end;
		mSize = end - start + 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.common.NameProperty#getName()
	 */
	@Override
	public String getName() {
		return mName;
	}

	/**
	 * Gets the start.
	 *
	 * @return the start
	 */
	public int getStart() {
		return mStart;
	}

	/**
	 * Gets the end.
	 *
	 * @return the end
	 */
	public int getEnd() {
		return mEnd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Integer.toString(mSize) + " " + mName;
	}

	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size() {
		return mSize;
	}
}
