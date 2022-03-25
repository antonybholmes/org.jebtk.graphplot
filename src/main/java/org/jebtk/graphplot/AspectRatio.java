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
package org.jebtk.graphplot;

/**
 * The class AspectRatio.
 */
public class AspectRatio {

	/**
	 * The member x.
	 */
	private double mX = 1;

	/**
	 * The member y.
	 */
	private double mY = 1;

	/**
	 * The member y div x.
	 */
	private double mYDivX;
	// private double mZ = 1;

	/**
	 * Instantiates a new aspect ratio.
	 */
	public AspectRatio() {
		this(1, 1);
	}

	/**
	 * Adjust the y aspect ratio whilst maintaining x at 1. This is a common option
	 * because typically we want to adjust the height of a plot rather than its
	 * width.
	 *
	 * @param y the y
	 */
	public AspectRatio(double y) {
		this(1, y);
	}

	/**
	 * Instantiates a new aspect ratio.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public AspectRatio(double x, double y) {
		mX = x;
		mY = y;
		mYDivX = mY / mX;
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
	 * Gets the y div x.
	 *
	 * @return the y div x
	 */
	public double getYDixX() {
		return mYDivX;
	}
}
