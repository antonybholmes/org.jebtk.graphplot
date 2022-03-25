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
import org.jebtk.core.geom.IntDim;

/**
 * The class DataPointShape.
 */
// Renders a data point shape
public class SizeProps extends ChangeListeners {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member size.
	 */
	protected IntDim mSize = null;

	/**
	 * The member half size.
	 */
	protected IntDim mHalfSize = null;

	/**
	 * Instantiates a new data point shape.
	 */
	public SizeProps() {
		setSize(1, 1);
	}

	/**
	 * Instantiates a new size properties.
	 *
	 * @param size the size
	 */
	public SizeProps(IntDim size) {
		setSize(size);
	}

	/**
	 * Sets the dimension.
	 *
	 * @param dimension the new dimension
	 */
	public void setSize(int dimension) {
		setSize(dimension, dimension);
	}

	/**
	 * Sets the size.
	 *
	 * @param w the w
	 * @param h the h
	 */
	public void setSize(int w, int h) {
		setSize(new IntDim(w, h));
	}

	/**
	 * Sets the dimension.
	 *
	 * @param dimension the new dimension
	 */
	public void setSize(IntDim dimension) {
		updateSize(dimension);

		fireChanged();
	}

	/**
	 * Update size.
	 *
	 * @param dimension the dimension
	 */
	public void updateSize(IntDim dimension) {
		mSize = dimension;
		mHalfSize = new IntDim(mSize.getW() / 2, mSize.getH() / 2);
	}

	/**
	 * Gets the dimension.
	 *
	 * @return the dimension
	 */
	public IntDim getDimension() {
		return mSize;
	}
}
