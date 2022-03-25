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

import java.util.HashMap;
import java.util.Map;

/**
 * The class RowLabelProperties.
 */
public class AnnotationProps {

	/** The m show annotations. */
	private Map<String, Boolean> mShowAnnotations = new HashMap<String, Boolean>();

	/**
	 * Sets the.
	 *
	 * @param name    the name
	 * @param visible the visible
	 */
	public void set(String name, boolean visible) {
		mShowAnnotations.put(name, visible);
	}

	/**
	 * Gets the visible count.
	 *
	 * @return the visible count
	 */
	public int getVisibleCount() {
		int c = 0;

		for (String name : mShowAnnotations.keySet()) {
			if (mShowAnnotations.get(name)) {
				++c;
			}
		}

		return c;
	}

	/**
	 * Checks if is visible.
	 *
	 * @param name the name
	 * @return true, if is visible
	 */
	public boolean isVisible(String name) {
		return mShowAnnotations.containsKey(name) && mShowAnnotations.get(name);
	}
}
