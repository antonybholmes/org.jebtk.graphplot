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

import org.jebtk.core.StringId;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.math.matrix.DataFrame;

/**
 * A collection of series groups.
 * 
 * @author Antony Holmes
 *
 */
public class XYAxesGroup extends Group<XYSeriesGroup> implements ChangeListener {

	/**
	 * The constant NEXT_ID.
	 */
	private static final StringId NEXT_ID = new StringId("XY Axes Group");

	/**
	 * The member name.
	 */
	private String mName;

	/**
	 * Instantiates a new XY axes group.
	 */
	public XYAxesGroup() {
		this(NEXT_ID.getNextId());
	}

	/**
	 * Instantiates a new XY axes group.
	 *
	 * @param name the name
	 */
	public XYAxesGroup(String name) {
		mName = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.lib.NameProperty#getName()
	 */
	@Override
	public String getName() {
		return mName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.series.Group#add(org.abh.
	 * lib.NameProperty)
	 */
	@Override
	public boolean add(XYSeriesGroup g) {
		g.addChangeListener(this);

		return super.add(g);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.series.Group#autoCreate()
	 */
	@Override
	public XYSeriesGroup autoCreate() {
		XYSeriesGroup group = new XYSeriesGroup();

		add(group);

		return group;
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
	 * Gets the x max.
	 *
	 * @param m     the m
	 * @param group the group
	 * @return the x max
	 */
	public static double getXMax(DataFrame m, XYAxesGroup group) {
		double ret = Double.MIN_VALUE;

		for (XYSeriesGroup p : group) {
			double v = XYSeriesGroup.getXMax(m, p);

			if (v > ret) {
				ret = v;
			}
		}

		return ret;
	}

	/**
	 * Gets the y max.
	 *
	 * @param m     the m
	 * @param group the group
	 * @return the y max
	 */
	public static double getYMax(DataFrame m, XYAxesGroup group) {
		double ret = Double.MIN_VALUE;

		for (XYSeriesGroup p : group) {
			double v = XYSeriesGroup.getYMax(m, p);

			if (v > ret) {
				ret = v;
			}
		}

		return ret;
	}

	/**
	 * Gets the x min.
	 *
	 * @param m     the m
	 * @param group the group
	 * @return the x min
	 */
	public static double getXMin(DataFrame m, XYAxesGroup group) {
		double ret = Double.MAX_VALUE;

		for (XYSeriesGroup p : group) {
			double min = XYSeriesGroup.getXMin(m, p);

			if (min < ret) {
				ret = min;
			}
		}

		return ret;
	}

	/**
	 * Gets the y min.
	 *
	 * @param m     the m
	 * @param group the group
	 * @return the y min
	 */
	public static double getYMin(DataFrame m, XYAxesGroup group) {
		double ret = Double.MAX_VALUE;

		for (XYSeriesGroup p : group) {
			double min = XYSeriesGroup.getYMin(m, p);

			if (min < ret) {
				ret = min;
			}
		}

		return ret;
	}

	/**
	 * Returns the sum of the y values in the group.
	 *
	 * @param m     the m
	 * @param group the group
	 * @return the y sum
	 */
	public static double getYSum(DataFrame m, XYAxesGroup group) {
		double ret = 0;

		for (XYSeriesGroup s : group) {
			ret += XYSeriesGroup.getYSum(m, s);
		}

		return ret;
	}

	/**
	 * Gets the y max sum.
	 *
	 * @param m     the m
	 * @param group the group
	 * @return the y max sum
	 */
	public static double getYMaxSum(DataFrame m, XYAxesGroup group) {
		double ret = Double.MIN_VALUE;

		for (XYSeriesGroup p : group) {
			double v = XYSeriesGroup.getYSum(m, p);

			if (v > ret) {
				ret = v;
			}
		}

		return ret;
	}

}
