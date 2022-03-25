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

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.event.ChangeListeners;

/**
 * Represents a point in an XYSeries.
 * 
 * @author Antony Holmes
 *
 */
public class XYPoint extends ChangeListeners implements ChangeListener {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member name.
	 */
	private String mName = null;

	/**
	 * The member x.
	 */
	private double mX;

	/**
	 * The member y.
	 */
	private double mY;

	/**
	 * The member show label.
	 */
	private boolean mShowLabel = false;

	/**
	 * Instantiates a new XY point.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public XYPoint(double x, double y) {
		mX = x;
		mY = y;
	}

	/**
	 * Instantiates a new XY point.
	 *
	 * @param name the name
	 * @param x    the x
	 * @param y    the y
	 */
	public XYPoint(String name, double x, double y) {
		mX = x;
		mY = y;

		mName = name;
	}

	/**
	 * Clone a point.
	 *
	 * @param p the p
	 */
	public XYPoint(XYPoint p) {
		this(p.mName, p.mX, p.mY);
	}

	/**
	 * Clone a point, but change its position.
	 *
	 * @param p the p
	 * @param x the x
	 * @param y the y
	 */
	public XYPoint(XYPoint p, double x, double y) {
		this(p.mName, x, y);
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Sets the show label.
	 *
	 * @param showLabel the new show label
	 */
	public void setShowLabel(boolean showLabel) {
		mShowLabel = showLabel;

		fireChanged();
	}

	/**
	 * Gets the show label.
	 *
	 * @return the show label
	 */
	public boolean getShowLabel() {
		return mShowLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
	 */
	@Override
	public void changed(ChangeEvent e) {
		fireChanged();
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public double getX() {
		return mX;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public double getY() {
		return mY;
	}

	/**
	 * Returns true if the point is within the bounds of the graph.
	 * 
	 * @param point
	 * @param xAxis
	 * @param yAxis
	 * @return
	 */
	/*
	 * public static boolean withinGraphBounds(XYPoint point, AxisProperties xAxis,
	 * AxisProps yAxis) { return point.getX() >= xAxis.getLimits().getMin() &&
	 * point.getX() <= xAxis.getLimits().getMax() && point.getY() >=
	 * yAxis.getLimits().getMin() && point.getY() <= yAxis.getLimits().getMax(); }
	 */
}
