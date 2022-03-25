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
public class PlotBoxZLayout extends PlotBoxLayout {

	/**
	 * Gets the plot size recursive.
	 *
	 * @param plotBox the plot box
	 * @param dim     the dim
	 * @return the plot size recursive
	 */
	@Override
	public void plotSize(PlotBox plot, Dimension dim) {
		int width = 0;
		int height = 0;

		for (PlotBox child : plot) {
			Dimension dim1 = new Dimension(0, 0);

			child.plotSize(dim1);

			width = Math.max(width, dim1.width);

			height = Math.max(height, dim1.height);
		}

		dim.width += width;
		dim.height += height;
	}

	/**
	 * Draw recursive.
	 *
	 * @param g2      the g2
	 * @param plotBox the plot box
	 * @param offset  the offset
	 * @param context the context
	 */
	@Override
	public void plot(Graphics2D g2, PlotBox plot, Dimension offset, DrawingContext context, Props params) {
		Dimension tempOffset = new Dimension(0, 0);

		for (int z : plot.getZ()) {
			tempOffset.width = 0;
			tempOffset.height = 0;
			
			System.err.println(plot + " " + z + " " + plot.getChild(z));

			PlotBox child = plot.getChild(z);

			child.plot(g2, tempOffset, context, params);
		}

		super.plot(g2, plot, offset, context, params);
	}
}
