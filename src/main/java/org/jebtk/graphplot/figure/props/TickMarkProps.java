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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.event.ChangeListeners;
import org.jebtk.core.text.TextUtils;
import org.jebtk.graphplot.figure.series.XYAxesGroup;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;

/**
 * Describes the ticks on an axis.
 * 
 * @author Antony Holmes
 *
 */
public class TickMarkProps extends ChangeListeners implements Iterable<Tick>, ChangeListener {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member tick size.
	 */
	private int mTickSize = 4;

	/**
	 * The member spacing.
	 */
	private int mSpacing = 4;

	/**
	 * The member style.
	 */
	private LineProps mStyle = new LineProps();

	/**
	 * The member labels.
	 */
	private List<Tick> mTicks = new ArrayList<Tick>();

	/**
	 * The member font.
	 */
	private FontProps mFont = new FontProps();

	/**
	 * The member rotation.
	 */
	private double mRotation = 0;

	/**
	 * Instantiates a new tick mark properties.
	 */
	public TickMarkProps() {
		mFont.addChangeListener(this);
		mStyle.addChangeListener(this);
	}

	/**
	 * Copy.
	 *
	 * @param ticks the ticks
	 */
	public void copy(TickMarkProps ticks) {
		mStyle.copy(ticks.mStyle);
	}

	/**
	 * Returns the number of tick marks.
	 *
	 * @return the tick count
	 */
	public int getTickCount() {
		return mTicks.size();
	}

	/**
	 * Gets the min.
	 *
	 * @return the min
	 */
	public double getMin() {
		return mTicks.get(0).getValue();
	}

	/**
	 * Gets the max.
	 *
	 * @return the max
	 */
	public double getMax() {
		return mTicks.get(mTicks.size() - 1).getValue();
	}

	/**
	 * Sets the ticks.
	 *
	 * @param ticks the new ticks
	 */
	public void setTicks(List<Double> ticks) {
		updateTicks(ticks);

		fireChanged();
	}

	/**
	 * Returns a copy of the tick positions. Use the update/set methods to adjust
	 * the tick positions.
	 *
	 * @return the ticks
	 */
	public List<Tick> getTicks() {
		return Collections.unmodifiableList(mTicks);
	}

	/**
	 * Update ticks.
	 *
	 * @param ticks the ticks
	 */
	private void updateTicks(List<Double> ticks) {
		mTicks = CollectionUtils.sort(Tick.toTicks(ticks));
	}

	/**
	 * Sets the ticks.
	 *
	 * @param ticks  the ticks
	 * @param labels the labels
	 */
	public void setTicks(List<Double> ticks, List<String> labels) {
		updateTicks(ticks);
		setLabels(labels);
	}

	/**
	 * Sets the label.
	 *
	 * @param i     the i
	 * @param label the label
	 */
	public void setLabel(int i, String label) {
		mTicks.get(i).setName(label);
	}

	/**
	 * Set the tick labels. If labels exceeds the length of the ticks, they will be
	 * truncated.
	 *
	 * @param groups the new labels
	 */
	// public void setLabels(List<String> labels) {
	// updateLabels(labels);
	//
	// fireChanged();
	// }

	public void setLabels(XYAxesGroup groups) {
		setLabels(CollectionUtils.toString(groups));
	}

	/**
	 * Sets the labels.
	 *
	 * @param groups the new labels
	 */
	public void setLabels(XYSeriesGroup groups) {
		setLabels(CollectionUtils.toString(groups));
	}

	/**
	 * Set the tick labels. If labels is too short, tick labels will be padded with
	 * empty strings. If labels is too long, elements exceeding the number of ticks
	 * will be ignored.
	 *
	 * @param labels the new labels
	 */
	public void setLabels(List<String> labels) {
		if (labels == null) {
			return;
		}

		// If the labels length too short, pad with empty strings
		labels = CollectionUtils.pad(labels, TextUtils.EMPTY_STRING, mTicks.size());

		for (int i = 0; i < labels.size(); ++i) {
			mTicks.get(i).setName(labels.get(i));
		}

		mFont.setVisible(true);
	}

	public void setLabels(String[] labels) {
		if (labels == null) {
			return;
		}

		// If the labels length too short, pad with empty strings
		labels = TextUtils.pad(labels, TextUtils.EMPTY_STRING, mTicks.size());

		for (int i = 0; i < labels.length; ++i) {
			mTicks.get(i).setName(labels[i]);
		}

		mFont.setVisible(true);
	}

	public List<String> getLabels() {
		return Tick.toString(mTicks);
	}

	/**
	 * Gets the tick.
	 *
	 * @param i the i
	 * @return the tick
	 */
	public double getTick(int i) {
		return mTicks.get(i).getValue();
	}

	/**
	 * Gets the label.
	 *
	 * @param i the i
	 * @return the label
	 */
	public String getLabel(int i) {
		return mTicks.get(i).getName();
	}

	/**
	 * Gets the tick size.
	 *
	 * @return the tick size
	 */
	public int getTickSize() {
		return mTickSize;
	}

	/**
	 * Sets the tick size.
	 *
	 * @param tickSize the new tick size
	 */
	public void setTickSize(int tickSize) {
		mTickSize = tickSize;

		fireChanged();
	}

	/**
	 * Gets the tick spacing.
	 *
	 * @return the tick spacing
	 */
	public int getTickSpacing() {
		return mSpacing;
	}

	/**
	 * Sets the tick spacing.
	 *
	 * @param spacing the new tick spacing
	 */
	public void setTickSpacing(int spacing) {
		mSpacing = spacing;

		fireChanged();
	}

	/**
	 * Gets the line style.
	 *
	 * @return the line style
	 */
	public LineProps getLineStyle() {
		return mStyle;
	}

	public void setVisible(boolean visible) {
		updateVisible(visible);

		fireChanged();
	}

	public void updateVisible(boolean visible) {
		getFontStyle().updateVisible(visible);
		getLineStyle().updateVisible(visible);
	}

	/**
	 * Gets the rotation.
	 *
	 * @return the rotation
	 */
	public double getRotation() {
		return mRotation;
	}

	/**
	 * Set the label rotation in radians.
	 *
	 * @param rotation the new rotation
	 */
	public void setRotation(double rotation) {
		mRotation = rotation;

		fireChanged();
	}

	/**
	 * Gets the font style.
	 *
	 * @return the font style
	 */
	public FontProps getFontStyle() {
		return mFont;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Tick> iterator() {
		return mTicks.iterator();
	}

}
