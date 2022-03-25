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
package org.jebtk.graphplot.figure.series;

import java.awt.Graphics2D;
import java.awt.Point;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.geom.IntDim;
import org.jebtk.graphplot.figure.props.StyleProps;
import org.jebtk.graphplot.figure.props.VisibleProps;
import org.jebtk.graphplot.icons.ShapeStyle;

/**
 * The class DataPointShape.
 */
// Renders a data point shape
public abstract class Marker extends VisibleProps implements ChangeListener {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The constant DEFAULT_SIZE is the marker width in pixels.
	 */
	private static final int DEFAULT_SIZE = 12;

	/**
	 * The member size.
	 */
	protected IntDim mDim = null;

	/**
	 * The member half size.
	 */
	protected IntDim mHalfSize = null;

	/** The m size. */
	private int mSize;

	/**
	 * Instantiates a new data point shape.
	 */
	public Marker() {
		setSize(DEFAULT_SIZE);
	}

	/**
	 * Sets the dimension.
	 *
	 * @param size the new size
	 */
	public void setSize(int size) {
		mSize = size;
		mDim = new IntDim(size, size);
		mHalfSize = new IntDim(size / 2, size / 2);

		fireChanged();
	}

	/**
	 * Gets the dimension.
	 *
	 * @return the dimension
	 */
	public int getSize() {
		return mSize;
	}

	/**
	 * Render the shape at the given coordinates.
	 *
	 * @param g2    the g2
	 * @param style the style
	 * @param p     the p
	 */
	public abstract void plot(Graphics2D g2, StyleProps style, Point p);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.common.event.ChangeListener#changed(org.abh.common.event.
	 * ChangeEvent)
	 */
	@Override
	public final void changed(ChangeEvent e) {
		fireChanged(new ChangeEvent(this));
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public abstract ShapeStyle getType();
}
