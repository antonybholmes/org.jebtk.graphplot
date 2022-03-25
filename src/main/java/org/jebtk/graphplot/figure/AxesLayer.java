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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.jebtk.core.Props;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.ImageUtils;

/**
 * Each layer of the graph can respond to changes.
 * 
 * @author Antony Holmes
 *
 */
public abstract class AxesLayer extends Layer {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The m buffered image. */
	private BufferedImage mBufferedImage;

	/** The m cache axes. */
	private String mCacheAxes;

	public AxesLayer() {

	}

	public AxesLayer(String name) {
		super(name);
	}

	/**
	 * Plot.
	 *
	 * @param g2        the g2
	 * @param context   the context
	 * @param subFigure the sub figure
	 * @param axes      the axes
	 */
	@Override
	public void plot(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
		Figure figure = (Figure) params.get("fig");
		SubFigure subFigure = (SubFigure) params.get("subfig");
		Axes axes = (Axes) params.get("axes");

		drawPlot(g2, context, figure, subFigure, axes);
	}

	/**
	 * Aa plot.
	 *
	 * @param g2        the g 2
	 * @param context   the context
	 * @param subFigure the sub figure
	 * @param axes      the axes
	 */
	public void aaPlot(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes) {

		Graphics2D g2Temp = ImageUtils.createAATextGraphics(g2);

		try {
			drawPlot(g2Temp, context, figure, subFigure, axes);
		} finally {
			g2Temp.dispose();
		}
	}

	/**
	 * Cache plot.
	 *
	 * @param g2        the g 2
	 * @param context   the context
	 * @param subFigure the sub figure
	 * @param axes      the axes
	 */
	public void cachePlot(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes) {
		if (context == DrawingContext.OUTPUT) {
			drawPlot(g2, context, figure, subFigure, axes);
		} else {
			// Create an image version of the canvas and draw that to spped
			// up operations
			if (mBufferedImage == null || mCacheAxes == null || !axes.hashId().equals(mCacheAxes)) {
				// The canvas need only be the size of the available display
				mBufferedImage = ImageUtils.createImage(axes.getPreferredSize());

				Graphics2D g2Temp = ImageUtils.createAATextGraphics(mBufferedImage);

				try {
					drawPlot(g2Temp, context, figure, subFigure, axes);
				} finally {
					g2Temp.dispose();
				}

				mCacheAxes = axes.hashId();
			}

			g2.drawImage(mBufferedImage, 0, 0, null);
		}
	}

	/**
	 * Draw plot.
	 *
	 * @param g2        the g 2
	 * @param context   the context
	 * @param subFigure the sub figure
	 * @param axes      the axes
	 */
	public void drawPlot(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes) {
		// Do nothing
	}
}
