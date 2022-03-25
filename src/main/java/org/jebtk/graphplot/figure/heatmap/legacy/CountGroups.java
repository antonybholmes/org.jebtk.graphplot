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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.jebtk.math.matrix.Matrix;

/**
 * The Class CountGroups.
 */
public class CountGroups implements Iterable<CountGroup> {

	/** The m groups. */
	private List<CountGroup> mGroups = new ArrayList<CountGroup>();

	/**
	 * Instantiates a new count groups.
	 *
	 * @param groups the groups
	 */
	public CountGroups(CountGroup... groups) {
		mGroups.addAll(Arrays.asList(groups));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<CountGroup> iterator() {
		return mGroups.iterator();
	}

	/**
	 * Adds the.
	 *
	 * @param countGroup the count group
	 * @return the count groups
	 */
	public CountGroups add(CountGroup countGroup) {
		mGroups.add(countGroup);

		return this;
	}

	/**
	 * Default group.
	 *
	 * @param m the m
	 * @return the count groups
	 */
	public static CountGroups defaultGroup(Matrix m) {
		return new CountGroups(new CountGroup(0, m.getRows() - 1));
	}
}
