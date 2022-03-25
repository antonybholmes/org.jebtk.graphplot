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
import org.jebtk.core.geom.IntPos2D;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.ImageUtils;

/**
 * The class PlotBox.
 */
public class PlotBoxFloatingLayout extends PlotBoxLayout {

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

		for (IntPos2D p : plot.getPositions()) {
			PlotBox child = plot.getChild(p);

			Dimension dim1 = new Dimension(0, 0);

			child.plotSize(dim1);

			width = Math.max(width, p.getX() + dim1.width);

			height = Math.max(height, p.getY() + dim1.height);
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
		for (IntPos2D p : plot.getPositions()) {
			PlotBox child = plot.getChild(p);

			Graphics2D g2Temp = ImageUtils.clone(g2);

			Dimension tempOffset = new Dimension(0, 0);

			try {
				g2Temp.translate(p.getX(), p.getY());

				child.plot(g2Temp, tempOffset, context, params);
			} finally {
				g2Temp.dispose();
			}
		}

		super.plot(g2, plot, offset, context, params);
	}
}
