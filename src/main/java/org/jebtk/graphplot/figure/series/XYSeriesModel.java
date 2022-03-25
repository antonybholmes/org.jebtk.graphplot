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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.jebtk.core.event.ChangeListeners;

/**
 * The class XYSeriesModel keeps track of which series are visible on a plot
 * and.
 */
public class XYSeriesModel extends ChangeListeners implements Iterable<XYSeries> {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** The Constant EMPTY_SERIES. */
	public static final XYSeriesModel EMPTY_SERIES = new XYSeriesModel(XYSeriesGroup.EMPTY_GROUP);

	/**
	 * The member groups.
	 */
	private XYSeriesGroup mGroups = new XYSeriesGroup();

	/** The m visible map. */
	private Map<XYSeries, Boolean> mVisibleMap = new HashMap<XYSeries, Boolean>();

	/**
	 * Instantiates a new XY series model.
	 *
	 * @param group the group
	 */
	public XYSeriesModel(XYSeriesGroup group) {
		mGroups = group;

		for (XYSeries series : group) {
			setVisible(series, true);
		}
	}

	/**
	 * Gets the visible.
	 *
	 * @param series the series
	 * @return the visible
	 */
	public boolean getVisible(XYSeries series) {
		return mVisibleMap.containsKey(series) && mVisibleMap.get(series);
	}

	/**
	 * Sets the visible.
	 *
	 * @param series  the series
	 * @param visible the visible
	 */
	public void setVisible(XYSeries series, boolean visible) {
		mVisibleMap.put(series, visible);

		fireChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<XYSeries> iterator() {
		return mGroups.iterator();
	}

	/**
	 * Returns the number of visible groups.
	 *
	 * @return the count
	 */
	public int getCount() {
		int c = 0;

		for (Entry<XYSeries, Boolean> e : mVisibleMap.entrySet()) {
			if (e.getValue()) {
				++c;
			}
		}

		return c;
	}

	/**
	 * Gets the total count.
	 *
	 * @return the total count
	 */
	public int getTotalCount() {
		return mGroups.getCount();
	}

	/**
	 * Creates the.
	 *
	 * @param group the group
	 * @return the XY series model
	 */
	public static XYSeriesModel create(XYSeriesGroup group) {
		return new XYSeriesModel(group);
	}
}