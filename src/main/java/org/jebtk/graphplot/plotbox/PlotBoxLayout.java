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
package org.jebtk.graphplot.plotbox;

import java.awt.Dimension;
import java.awt.Graphics2D;

import org.jebtk.core.Props;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The class PlotBox.
 */
public abstract class PlotBoxLayout {
	public Dimension getPreferredSize(PlotBox plotBox) {
		Dimension dim = new Dimension(0, 0);

		plotSize(plotBox, dim);

		return dim;
	}

	/**
	 * Calculates the size of the plot based on the layout of its children.
	 *
	 * @param plotBox the plot box
	 * @param dim     the dim
	 * @return the plot size recursive
	 */
	public abstract void plotSize(PlotBox plotBox, Dimension dim);

	public final void plot(Graphics2D g2, PlotBox plot, DrawingContext context, Props params) {
		plot(g2, plot, new Dimension(0, 0), context, params);
	}

	/**
	 * Draw recursive.
	 *
	 * @param g2      the g2
	 * @param plotBox the plot box
	 * @param offset  the offset
	 * @param context the context
	 */
	public void plot(Graphics2D g2, PlotBox plotBox, Dimension offset, DrawingContext context, Props params) {
		plotSize(plotBox, offset);
	}

}
