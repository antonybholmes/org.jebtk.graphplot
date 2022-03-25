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
public class JoinedBarsLayer extends UniqueXYLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new joined bars layer.
	 *
	 * @param series the series
	 */
	public JoinedBarsLayer(String series) {
		super(series);
	}

	@Override
	public String getType() {
		return "Joined Bars Layer";
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
		int h;
		int w;
		int y1 = axes.toPlotY1(0);

		Point p;
		Point p2;

		for (int i = 0; i < xy.getPointCount() - 1; ++i) {
			p = xy.getPoint(i);
			p2 = xy.getPoint(i + 1);

			h = y1 - p2.y;
			w = p2.x - p.x;

			if (series.getStyle().getFillStyle().getVisible()) {
				// System.err.println("has " + series.getTitle().getText());

				g2.setColor(series.getStyle().getFillStyle().getColor());

				// System.err.println("run " + x1 + " " + y1 + " " + y2 + " " + w);

				g2.fillRect(p.x, p2.y, w, h); // h);
			}

			if (series.getStyle().getLineStyle().getVisible()) {
				g2.setStroke(series.getStyle().getLineStyle().getStroke());
				g2.setColor(series.getStyle().getLineStyle().getColor());

				g2.drawRect(p.x, p2.y, w, h);
			}
		}
	}
}