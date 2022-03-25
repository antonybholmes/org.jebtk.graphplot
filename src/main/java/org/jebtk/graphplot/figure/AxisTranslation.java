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

import java.util.HashMap;
import java.util.Map;

import org.jebtk.core.Mathematics;
import org.jebtk.core.text.TextUtils;

/**
 * Translate between graph space and pixel space.
 * 
 * @author Antony Holmes
 *
 */
public abstract class AxisTranslation {

	/** The m x map. */
	protected Map<Double, Integer> mXMap = new HashMap<Double, Integer>();

	/** The m x norm map. */
	protected Map<Double, Double> mXNormMap = new HashMap<Double, Double>();

	/** The m x min. */
	protected double mXMin;

	/** The m x diff. */
	protected double mXDiff;

	/** The m max x. */
	protected int mMaxX;

	/** The m axis. */
	protected Axis mAxis;

	private Axes mAxes;

	protected int mXPixels;

	private String mHashId = TextUtils.EMPTY_STRING;

	/**
	 * Instantiates a new axis translation.
	 *
	 * @param axis the axis
	 */
	public AxisTranslation(Axes axes, Axis axis) {
		mAxes = axes;
		mAxis = axis;

		// axes.addCanvasListener(this);
		// axis.addChangeListener(this);

		cache();
	}

	public Axes getAxes() {
		return mAxes;
	}

	public abstract int getPixels();

	/**
	 * To plot.
	 *
	 * @param x the x
	 * @return the int
	 */
	public int toPlot(double x) {
		cacheCheck();

		if (!mXMap.containsKey(x)) {
			mXMap.put(x, (int) Math.round(plotNormalize(x) * mXPixels));
		}

		return mXMap.get(x);
	}

	protected void cacheCheck() {
		// To prevent race conditions where the plot refreshes before
		// axis translator updates, do a check before a normalization whether
		// we need to clear the caches. We do this as an alternative to a
		// listening model.

		String id = mAxes.hashId();

		if (!mHashId.equals(id)) {
			cache();

			mHashId = id;
		}
	}

	/**
	 * Redraw.
	 */
	private void cache() {
		mXMin = mAxis.getLimits().getMin();
		mXDiff = mAxis.getLimits().getMax() - mXMin;
		mXPixels = getPixels();

		clearCaches();
	}

	/**
	 * Plot normalize.
	 *
	 * @param x the x
	 * @return the double
	 */
	public double plotNormalize(double x) {
		if (!mXNormMap.containsKey(x)) {
			mXNormMap.put(x, Mathematics.bound((x - mXMin) / mXDiff, 0, 1));
		}

		return mXNormMap.get(x);
	}

	/**
	 * Clear caches.
	 */
	public void clearCaches() {
		mXMap.clear();

		mXNormMap.clear();
	}
}
