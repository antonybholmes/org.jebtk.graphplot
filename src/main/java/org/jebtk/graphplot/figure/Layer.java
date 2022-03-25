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
package org.jebtk.graphplot.figure;

import org.jebtk.graphplot.plotbox.PlotBox;

/**
 * A graph is built from layers which may or may not have a visual component
 * associated with them or may be just a sub collection of elements to make up a
 * plot.
 *
 * @author Antony Holmes
 */
public abstract class Layer extends PlotBox {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member visible.
	 */
	protected boolean mVisible = true;

	public Layer() {

	}

	public Layer(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.common.IdProperty#getId()
	 */
	@Override
	public String hashId() {
		return getName() + getId();
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	@Override
	public String getType() {
		return LayerType.LAYER;
	}

	/**
	 * Checks if is visible.
	 *
	 * @return true, if is visible
	 */
	@Override
	public boolean getVisible() {
		return mVisible;
	}

	/**
	 * Sets the visible.
	 *
	 * @param visible the new visible
	 */
	@Override
	public void setVisible(boolean visible) {
		mVisible = visible;

		fireChanged();
	}
}
