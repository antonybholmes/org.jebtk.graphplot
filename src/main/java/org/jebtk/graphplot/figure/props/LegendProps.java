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
import org.jebtk.graphplot.figure.GridLocation;

/**
 * Describes how the legend should appear on a plot.
 * 
 * @author Antony Holmes
 *
 */
public class LegendProps extends LocationProps implements ChangeListener {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member style.
	 */
	private StyleProps mStyle = new StyleProps();

	/**
	 * The member font.
	 */
	private FontProps mFont = new FontProps();

	/**
	 * Instantiates a new legend properties.
	 */
	public LegendProps() {
		mStyle.addChangeListener(this);
		mFont.addChangeListener(this);

		// Default to a black outline around the legend
		mStyle.getLineStyle().setVisible(false); // setColor(Color.BLACK);
		mStyle.getFillStyle().setVisible(false);

		setInside(true);
		setPosition(GridLocation.NE);

		setVisible(false);
	}

	/**
	 * Gets the style.
	 *
	 * @return the style
	 */
	public StyleProps getStyle() {
		return mStyle;
	}

	/**
	 * Gets the font properties.
	 *
	 * @return the font properties
	 */
	public FontProps getFontProps() {
		return mFont;
	}

	@Override
	public void changed(ChangeEvent e) {
		fireChanged();
	}
}
