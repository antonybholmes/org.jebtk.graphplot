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

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.text.TextUtils;
import org.jebtk.graphplot.figure.props.MarginProps;
import org.jebtk.modern.ModernWidget;

/**
 * Plot for drawing an axis. Responds to axis visibility properties.
 * 
 * @author Antony Holmes
 *
 */
public class AxisPlot extends Plot {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The m axis. */
	private Axis mAxis;

	/**
	 * Instantiates a new plot.
	 *
	 * @param name the name
	 * @param axis the axis
	 */
	public AxisPlot(String name, Axis axis) {
		super(name);

		mAxis = axis;

		setVisible(mAxis.getVisible());

		mAxis.addChangeListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent e) {
				setVisible(mAxis.getVisible());
			}
		});
	}

	/**
	 * Auto set label margin.
	 *
	 * @param axis the axis
	 * @return the int
	 */
	public static int autoSetLabelMargin(Axis axis) {

		int w = 0;

		String text = TextUtils.maxString(axis.getTicks().getMajorTicks().getLabels());

		w = ModernWidget.getStringWidth(axis.getTicks().getMajorTicks().getFontStyle().getFont(), text);

		w += axis.getTicks().getMajorTicks().getTickSpacing();

		if (!axis.getTicks().getDrawInside()) {
			w += axis.getTicks().getMajorTicks().getTickSize();
		}

		// Increase by 20%
		w = w * 12 / 10;

		// axes.getMargins().setBottom(Math.max(w,
		// MarginProps.DEFAULT_MARGIN));

		return Math.max(w, MarginProps.DEFAULT_SIZE);
	}
}
