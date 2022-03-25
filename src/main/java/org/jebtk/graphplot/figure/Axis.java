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

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import org.jebtk.core.Mathematics;
import org.jebtk.core.NameGetter;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.text.TextUtils;
import org.jebtk.graphplot.figure.props.GridProps;
import org.jebtk.graphplot.figure.props.LineProps;
import org.jebtk.graphplot.figure.props.TickProps;
import org.jebtk.graphplot.figure.props.TitleProps;
import org.jebtk.graphplot.figure.props.VisibleProps;
import org.jebtk.math.Linspace;

/**
 * Adjust Props for a given axis.
 * 
 * @author Antony Holmes
 *
 */
public class Axis extends VisibleProps implements NameGetter, PlotHashProperty, ChangeListener {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member stroke.
	 */
	private LineProps mStroke = new LineProps();

	/**
	 * The member ticks.
	 */
	private TickProps mTicks = new TickProps();

	/**
	 * The member grid.
	 */
	private GridProps mGrid = new GridProps();

	/** The m title. */
	private TitleProps mTitle = new TitleProps();

	/**
	 * Whether to show a zeroth line.
	 */
	private boolean mShowZerothLine = false;

	/** The m name. */
	private String mName;

	private AxisLimits mLimits = new AxisLimits(0, 1);

	/**
	 * Instantiates a new axis.
	 *
	 * @param name the name
	 */
	public Axis(String name) {
		mName = name;

		setLimitsAutoRound(0, 1);

		mTicks.addChangeListener(this);
		mGrid.addChangeListener(this);
		mTitle.addChangeListener(this);
		mStroke.addChangeListener(this);
		mLimits.addChangeListener(this);
	}

	/**
	 * Sets the font.
	 *
	 * @param font  the font
	 * @param color the color
	 */
	public void setFont(Font font, Color color) {
		mTicks.getMajorTicks().getFontStyle().setFont(font, color);
		mTitle.getFontStyle().setFont(font, color);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.common.NameProperty#getName()
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Returns the tick Props for this axis.
	 * 
	 * @return A tick Props object.
	 */
	public TickProps getTicks() {
		return mTicks;
	}

	/**
	 * Returns the grid Props for this axis.
	 * 
	 * @return A grid Props object.
	 */
	public GridProps getGrid() {
		return mGrid;
	}

	/**
	 * Returns the title Props for this axis.
	 * 
	 * @return A title Props object.
	 */
	public TitleProps getTitle() {
		return mTitle;
	}

	/**
	 * Returns true if a line at x=0 should be drawn when min < 0 and max > 0.
	 *
	 * @return the show zeroth line
	 */
	public boolean getShowZerothLine() {
		return mShowZerothLine;
	}

	/**
	 * Set whether to show a line at x=0 when the min < 0 and max > 0.
	 *
	 * @param show the new show zeroth line
	 */
	public void setShowZerothLine(boolean show) {
		mShowZerothLine = show;

		fireChanged();
	}

	public AxisLimits getLimits() {
		return mLimits;
	}

	/**
	 * Returns the line Props object for the axis.
	 *
	 * @return the line style
	 */
	public LineProps getLineStyle() {
		return mStroke;
	}

	/**
	 * Returns true if the point is within the bounds of the graph.
	 *
	 * @param x the x
	 * @return true, if successful
	 */
	public boolean withinBounds(double x) {
		return mLimits.withinBounds(x);
	}

	/**
	 * Bounds a point so it is within the graph limits.
	 *
	 * @param x the x
	 * @return the double
	 */
	public double bound(double x) {
		return Math.min(mLimits.getMax(), Math.max(mLimits.getMin(), x));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
	 */
	@Override
	public final void changed(ChangeEvent e) {
		fireChanged();
	}

	/**
	 * Sets the limits.
	 *
	 * @param min  the min
	 * @param max  the max
	 * @param step the step
	 */
	public void setLimits(double min, double max, double step) {
		mLimits.update(min, max);

		// Extend ticks past end so that if the end is not a multiple of the
		// step size, the ticks do not end abruptly.
		getTicks().setTicks(Linspace.evenlySpaced(mLimits.getMin(), mLimits.getMax() + step, step));
	}

	/**
	 * Set the limits of the axis.
	 * 
	 * @param min The min limit.
	 * @param max The max limit.
	 */
	public void setLimits(double min, double max) {
		double step = calcStepSize(max - min);

		setLimits(min, max, step);
	}

	/**
	 * Sets the limits auto round.
	 *
	 * @param max the new limits auto round
	 */
	public void setLimitsAutoRound(double max) {
		setLimitsAutoRound(0, max);
	}

	/**
	 * Set the limits and auto round the min and max to multiples of the step size
	 * to make the plot more aesthetically pleasing.
	 *
	 * @param min the min
	 * @param max the max
	 */
	public void setLimitsAutoRound(double min, double max) {

		double step = calcStepSize(max - min);

		if (min >= 0) {
			min = step * (int) (min / step);
		} else {
			// Treat min like max and extend beyond the min
			min = step * ((int) (min / step) - 1);
		}

		if (max >= 0) {
			max = step * ((int) (max / step) + 1);
		} else {
			max = step * (int) (max / step);
		}

		setLimits(min, max, step);
	}

	/**
	 * Auto adjust the axis limits.
	 *
	 * @param min the min
	 * @param max the max
	 */
	public void setLimitsAuto(double min, double max) {
		setLimits(adjustedMin(min, max), adjustedMax(min, max));
	}

	/**
	 * Display tick marks only at the minimum and maximum of the axis.
	 */
	public void startEndTicksOnly() {
		List<Double> range = Linspace.range(mLimits.getMin(), mLimits.getMax());

		getTicks().setTicks(range);
	}

	/**
	 * Returns an id describing Props of an axis. Comparing hashes can indicate when
	 * an axis Props have changed.
	 *
	 * @return the string
	 */
	@Override
	public String hashId() {
		return TextUtils.join(TextUtils.COLON_DELIMITER, mLimits.getMin(), mLimits.getMax(),
				getTicks().getMajorTicks().getTickCount(), getTicks().getMinorTicks().getTickCount());
	}

	/**
	 * Adjusted min.
	 *
	 * @param min the min
	 * @param max the max
	 * @return the double
	 */
	public static double adjustedMin(double min, double max) {
		if (min == 0) {
			return 0;
		}

		if (min > 0) {
			return Math.max(0, min - (max - min) / 100.0);
		} else {
			return min - (max - min) / 100.0;
		}
	}

	/**
	 * Adjusted max.
	 *
	 * @param min the min
	 * @param max the max
	 * @return the double
	 */
	public static double adjustedMax(double min, double max) {
		if (max == 0) {
			return 0;
		}

		if (max < 0) {
			return Math.min(0, max + (max - min) / 100.0);
		} else {
			return max + (max - min) / 100.0;
		}
	}

	/**
	 * Use an empirical rule to determine how big the tick size should be
	 * automatically based on the min max range.
	 *
	 * @param range the range
	 * @return the float
	 */
	public static double calcStepSize(double range) {
		if (range < 1) {
			return range;
		}

		// Negative powers mess up the scaling
		double power = Mathematics.log10(range);
		int ipower = (int) power;
		double factor = Math.pow(10, power - ipower);

		double scalar;

		if (factor < 1.2) {
			scalar = 0.2;
		} else if (factor < 2.5) {
			scalar = 0.2;
		} else if (factor < 5) {
			scalar = 0.5;
		} else if (factor < 10) {
			scalar = 1.0;
		} else {
			scalar = 2.0;
		}

		double step = scalar * Math.pow(10, ipower);

		return step;

		/*
		 * // calculate an initial guess at step size double tempStep = range /
		 * targetSteps;
		 * 
		 * // get the magnitude of the step size float mag =
		 * (float)Math.round(Mathematics.log10(tempStep)); float magPow =
		 * (float)Math.pow(10, mag);
		 * 
		 * // calculate most significant digit of the new step size float magMsd =
		 * (int)(tempStep / magPow + 0.5);
		 * 
		 * // promote the MSD to either 1, 2, or 5 if (magMsd > 5.0) { magMsd = 10.0f; }
		 * else if (magMsd > 2.0) { magMsd = 5.0f; } else if (magMsd > 1.0) { magMsd =
		 * 2.0f; }
		 * 
		 * return magMsd * magPow;
		 */
	}

	/**
	 * Disable all features.
	 *
	 * @param axis the axis
	 */
	public static void disableAllFeatures(Axis axis) {
		enableAllFeatures(axis, false);
	}

	/**
	 * Enable all features.
	 *
	 * @param axis the axis
	 */
	public static void enableAllFeatures(Axis axis) {
		enableAllFeatures(axis, true);
	}

	/**
	 * Enable all features.
	 *
	 * @param axis   the axis
	 * @param enable the enable
	 */
	public static void enableAllFeatures(Axis axis, boolean enable) {
		axis.getTitle().getFontStyle().setVisible(enable);
		axis.getGrid().setVisible(enable);
		axis.getLineStyle().setVisible(enable);

		axis.getTicks().getMajorTicks().getLineStyle().setVisible(enable);
		axis.getTicks().getMajorTicks().getFontStyle().setVisible(enable);
		axis.getTicks().getMinorTicks().getLineStyle().setVisible(enable);
		axis.getTicks().getMinorTicks().getFontStyle().setVisible(enable);
	}
}
