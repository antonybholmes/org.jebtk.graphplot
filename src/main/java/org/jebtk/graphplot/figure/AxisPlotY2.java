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

import java.awt.Dimension;

/**
 * Plot for drawing an axis. Responds to axis visibility properties.
 * 
 * @author Antony Holmes
 *
 */
public class AxisPlotY2 extends AxisPlot {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	private int mWidth;

	/**
	 * Instantiates a new axis plot y2.
	 *
	 * @param axis the axis
	 */
	public AxisPlotY2(Axis axis) {
		super("Axis Plot Y2", axis);

		setZ(new AxisLabelLayerY2());
		mWidth = autoSetLabelMargin(axis);
	}

	@Override
	public String getType() {
		return "Axis Y2 Label Plot Layer";
	}

	@Override
	public void plotSize(Dimension d) {
		d.height += mWidth;
	}

}
