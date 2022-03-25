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

import java.awt.Graphics2D;
import java.awt.Point;

import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Draws a series as set of bars.
 *
 * @author Antony Holmes
 */
public class BarsLayer extends UniqueXYLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new bars layer.
	 *
	 * @param series the series
	 */
	public BarsLayer(String series) {
		super(series);
	}

	@Override
	public String getType() {
		return "Bars Layer";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.figure.UniqueXYLayer#plotLayer(
	 * java.awt.Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Figure,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Axes,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Plot,
	 * org.abh.lib.math.matrix.DataFrame,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.series.XYSeries,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.UniqueXY)
	 */
	@Override
	public void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes,
			Plot plot, DataFrame m, XYSeries series, UniqueXY xy) {

		int y1 = axes.toPlotY1(0);

		int w = Math.max(1, axes.toPlotX1(1) - axes.toPlotX1(0));

		int bw = Math.max(1, (int) (w * plot.getBarWidth()));
		int offset = (w - bw) / 2;
		int x;
		int h;

		for (Point p : xy) {
			x = p.x + offset;

			h = y1 - p.y; // + 1;

			if (series.getStyle().getFillStyle().getVisible()) {
				g2.setColor(series.getStyle().getFillStyle().getColor());

				g2.fillRect(x, p.y, bw, h); // h);
			}

			if (series.getStyle().getLineStyle().getVisible()) {
				g2.setStroke(series.getStyle().getLineStyle().getStroke());
				g2.setColor(series.getStyle().getLineStyle().getColor());

				g2.drawRect(x, p.y, bw, h);
			}
		}
	}
}