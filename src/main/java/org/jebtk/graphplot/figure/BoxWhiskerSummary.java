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

import java.util.Collections;
import java.util.List;

import org.jebtk.graphplot.figure.series.Marker;
import org.jebtk.graphplot.figure.series.MarkerCircle;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.statistics.Statistics;

/**
 * Box and whisker plot using a summary matrix.
 * 
 * @author Antony Holmes
 *
 */
public class BoxWhiskerSummary extends XYSeries {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * The member outlier.
	 */
	private double mOutlier;

	/**
	 * The member lower.
	 */
	private double mLower;

	/**
	 * The member upper.
	 */
	private double mUpper;

	/**
	 * The member lower outlier.
	 */
	private double mLowerOutlier;

	/**
	 * The member upper outlier.
	 */
	private double mUpperOutlier;

	/**
	 * The member median.
	 */
	private double mMedian;

	/**
	 * The member q1.
	 */
	private double mQ1;

	/**
	 * The member q3.
	 */
	private double mQ3;

	/**
	 * The member iqr.
	 */
	private double mIqr;

	/**
	 * The member outlier shape.
	 */
	private Marker mOutlierShape = new MarkerCircle();

	/** The m mean. */
	private double mMean;

	/**
	 * Instantiates a new box whisker summary.
	 *
	 * @param m      the m
	 * @param series the series
	 */
	public BoxWhiskerSummary(DataFrame m, XYSeries series) {
		super(series.getName(), series.getColor());

		List<Double> values = XYSeries.xValues(m, series);

		Collections.sort(values);

		mMean = Statistics.mean(values);
		mMedian = Statistics.median(values);

		mQ1 = Statistics.q1(values);
		mQ3 = Statistics.q3(values);

		mIqr = mQ3 - mQ1;

		mOutlier = 1.5 * mIqr;

		double mLowerThreshold = mQ1 - mOutlier;
		double mUpperThreshold = mQ3 + mOutlier;

		mLowerOutlier = values.get(0);
		mUpperOutlier = values.get(values.size() - 1);

		// find the upper and lower values within the 1.5 IQR outlier range

		for (int i = 0; i < values.size(); ++i) {
			double v = values.get(i);

			if (v >= mLowerThreshold) {
				mLower = v;
				break;
			}
		}

		for (int i = values.size() - 1; i >= 0; --i) {
			double v = values.get(i);

			if (v <= mUpperThreshold) {
				mUpper = v;
				break;
			}
		}

		System.err.println("box whisker " + mMedian + " " + mQ1 + " " + mQ3 + " " + mLower + " " + mUpper);

		mOutlierShape.addChangeListener(this);
	}

	/**
	 * Instantiates a new box whisker summary.
	 *
	 * @return the lower outlier
	 */
	/*
	 * public BoxWhiskerSummary(XYSeries series, double mean, double median, double
	 * q1, double q3, double upper, double lower) { super(series.getName());
	 * 
	 * mMean = mean; mMedian = median;
	 * 
	 * mQ1 = q1; mQ3 = q3;
	 * 
	 * mIqr = mQ3 - mQ1;
	 * 
	 * mOutlier = 1.5 * mIqr;
	 * 
	 * mLowerThreshold = mQ1 - mOutlier; mUpperThreshold = mQ3 + mOutlier;
	 * 
	 * 
	 * mLower = lower; mUpper = upper;
	 * 
	 * mOutlierShape.addChangeListener(this); }
	 */

	/**
	 * Gets the lower outlier.
	 *
	 * @return the lower outlier
	 */
	public double getLowerOutlier() {
		return mLowerOutlier;
	}

	/**
	 * Gets the upper outlier.
	 *
	 * @return the upper outlier
	 */
	public double getUpperOutlier() {
		return mUpperOutlier;
	}

	/**
	 * Gets the median.
	 *
	 * @return the median
	 */
	public double getMean() {
		return mMean;
	}

	/**
	 * Gets the median.
	 *
	 * @return the median
	 */
	public double getMedian() {
		return mMedian;
	}

	/**
	 * Gets the q1.
	 *
	 * @return the q1
	 */
	public double getQ1() {
		return mQ1;
	}

	/**
	 * Gets the q3.
	 *
	 * @return the q3
	 */
	public double getQ3() {
		return mQ3;
	}

	/**
	 * Gets the lower.
	 *
	 * @return the lower
	 */
	public double getLower() {
		return mLower;
	}

	/**
	 * Gets the upper.
	 *
	 * @return the upper
	 */
	public double getUpper() {
		return mUpper;
	}

	/**
	 * Gets the iqr.
	 *
	 * @return the iqr
	 */
	public double getIqr() {
		return mIqr;
	}

	/**
	 * Sets the outlier shape.
	 *
	 * @param shape the new outlier shape
	 */
	public void setOutlierShape(Marker shape) {
		mOutlierShape = shape;

		mOutlierShape.addChangeListener(this);

		fireChanged();
	}

	/**
	 * Gets the outlier shape.
	 *
	 * @return the outlier shape
	 */
	public Marker getOutlierShape() {
		return mOutlierShape;
	}
}
