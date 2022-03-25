package org.jebtk.graphplot.figure;

import org.jebtk.core.event.ChangeListeners;

public class AxisLimits extends ChangeListeners {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final double MIN_D = 0.1;

	private double mMin;
	private double mMax;

	public AxisLimits(double min, double max) {
		set(min, max);
	}

	public void set(double min, double max) {
		update(min, max);

		fireChanged();
	}

	public void update(double min, double max) {
		if (min > max) {
			double t = min;
			min = max;
			max = t;
		}

		// Max must always be greater than min
		max = Math.max(min + MIN_D, max);

		mMin = min;
		mMax = max;
	}

	public double getMin() {
		return mMin;
	}

	public double getMax() {
		return mMax;
	}

	public boolean withinBounds(double x) {
		return x >= mMin && x <= mMax;
	}

	public void setMin(double min) {
		set(min, mMax);
	}

	public void setMax(double max) {
		set(mMin, max);
	}

}
