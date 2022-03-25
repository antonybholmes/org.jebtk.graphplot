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
package org.jebtk.graphplot.figure.props;

import org.jebtk.core.event.ChangeListeners;

/**
 * Specify if a graph element is visible or not.
 *
 * @author Antony Holmes
 */
public abstract class VisibleProps extends ChangeListeners {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member visible.
	 */
	protected boolean mVisible = true;

	/**
	 * Copy.
	 *
	 * @param Props the properties
	 */
	public void copy(final VisibleProps properties) {
		mVisible = properties.mVisible;

		fireChanged();
	}

	/**
	 * Set whether the feature is visible or not.
	 * 
	 * @param visible Whether the feature is visible or not.
	 */
	public void setVisible(boolean visible) {
		updateVisible(visible);

		// System.err.println("set visible " + visible + " " + this);

		fireChanged();
	}

	/**
	 * Update visible.
	 *
	 * @param visible the visible
	 */
	public void updateVisible(boolean visible) {
		mVisible = visible;
	}

	/**
	 * Returns true if the feature is visible, false otherwise.
	 * 
	 * @return true if the feature is visible, false otherwise.
	 */
	public boolean getVisible() {
		return mVisible;
	}
}
