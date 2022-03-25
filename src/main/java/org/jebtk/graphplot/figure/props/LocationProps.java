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

import org.jebtk.graphplot.figure.GridLocation;

/**
 * Describes how features that map to a grid location should appear.
 * 
 * @author Antony Holmes
 *
 */
public abstract class LocationProps extends VisibleProps {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member position.
	 */
	private GridLocation mPosition = GridLocation.NE;

	private boolean mInside = true;

	/**
	 * Sets the position.
	 *
	 * @param position the new position
	 */
	public void setPosition(GridLocation position) {
		mPosition = position;

		setVisible(true);
	}

	/**
	 * Set whether to plot the legend inside the boundaries of the axes or not.
	 * 
	 * @param inside
	 */
	public void setInside(boolean inside) {
		mInside = inside;

		setVisible(true);
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public GridLocation getPosition() {
		return mPosition;
	}

	/**
	 * Returns true if the legend should be plotted inside the boundaries of the
	 * axes.
	 * 
	 * @return
	 */
	public boolean isInside() {
		return mInside;
	}
}
