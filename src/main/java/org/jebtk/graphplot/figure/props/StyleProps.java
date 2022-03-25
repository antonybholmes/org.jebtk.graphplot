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

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.event.ChangeListeners;

/**
 * Set the color of a plot element.
 * 
 * @author Antony Holmes
 *
 */
public class StyleProps extends ChangeListeners implements ChangeListener {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member line style.
	 */
	private LineProps mLineStyle = new LineProps();

	/**
	 * The member fill style.
	 */
	private FillProps mFillStyle = new FillProps();

	/**
	 * Instantiates a new style properties.
	 */
	public StyleProps() {
		mLineStyle.addChangeListener(this);
		mFillStyle.addChangeListener(this);
	}

	/**
	 * Copy.
	 *
	 * @param style the style
	 */
	public void copy(final StyleProps style) {
		mLineStyle.copy(style.getLineStyle());
		mFillStyle.copy(style.getFillStyle());

		fireChanged();
	}

	/**
	 * Gets the line style.
	 *
	 * @return the line style
	 */
	public LineProps getLineStyle() {
		return mLineStyle;
	}

	/**
	 * Gets the fill style.
	 *
	 * @return the fill style
	 */
	public FillProps getFillStyle() {
		return mFillStyle;
	}

	/**
	 * Sets the visible.
	 *
	 * @param visible the new visible
	 */
	public void setVisible(boolean visible) {
		updateVisible(visible);

		fireChanged();
	}

	/**
	 * Update visible.
	 *
	 * @param visible the visible
	 */
	public void updateVisible(boolean visible) {
		mLineStyle.updateVisible(visible);
		mFillStyle.updateVisible(visible);
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

}
