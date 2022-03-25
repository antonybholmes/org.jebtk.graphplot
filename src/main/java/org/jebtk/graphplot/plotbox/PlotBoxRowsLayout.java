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
import org.jebtk.modern.graphics.ImageUtils;

/**
 * Layout plots in rows with a fixed number of columns.
 */
public class PlotBoxRowsLayout extends PlotBoxLayout {

	private int mCols;

	public PlotBoxRowsLayout(int cols) {
		mCols = cols;
	}

	/**
	 * Gets the plot size recursive.
	 *
	 * @param plotBox the plot box
	 * @param dim     the dim
	 * @return the plot size recursive
	 */
	@Override
	public void plotSize(PlotBox plotBox, Dimension dim) {
		int rows = plotBox.getChildCount() / mCols + (plotBox.getChildCount() % mCols > 0 ? 1 : 0);
		int[] heights = new int[rows];
		int[] widths = new int[mCols];

		sizes(plotBox, rows, widths, heights);

		for (int w : widths) {
			dim.width += w;
		}

		for (int h : heights) {
			dim.height += h;
		}
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
	public void plot(Graphics2D g2, PlotBox plotBox, Dimension offset, DrawingContext context, Props params) {
		Graphics2D subg2 = ImageUtils.clone(g2);

		int rows = plotBox.getChildCount() / mCols + (plotBox.getChildCount() % mCols > 0 ? 1 : 0);
		int[] heights = new int[rows];
		int[] widths = new int[mCols];

		sizes(plotBox, rows, widths, heights);

		int tw = 0;

		for (int w : widths) {
			tw += w;
		}

		Dimension tempOffset = new Dimension(0, 0);

		int r = 0;
		int c = 0;

		try {
			for (PlotBox child : plotBox) {
				tempOffset.width = 0;
				tempOffset.height = 0;

				child.plot(subg2, tempOffset, context, params);

				subg2.translate(tempOffset.width, 0);

				++c;

				if (c % mCols == 0) {
					subg2.translate(-tw, heights[r]);
					c = 0;
					++r;
				}
			}
		} finally {
			subg2.dispose();
		}

		super.plot(g2, plotBox, offset, context, params);
	}

	private void sizes(PlotBox plotBox, int rows, int[] widths, int[] heights) {

		Dimension tmpDim = new Dimension(0, 0);

		int r = 0;
		int c = 0;

		for (PlotBox child : plotBox) {
			tmpDim.width = 0;
			tmpDim.height = 0;

			child.plotSize(tmpDim);

			widths[c] = Math.max(widths[c], tmpDim.width);

			heights[r] += Math.max(heights[r], tmpDim.height);

			++c;

			if (c % mCols == 0) {
				c = 0;
				++r;
			}
		}
	}
}
