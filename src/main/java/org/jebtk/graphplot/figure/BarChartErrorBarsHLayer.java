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

import java.awt.Color;
import java.awt.Graphics2D;

import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Draws lines between series dots.
 * 
 * @author Antony Holmes
 *
 */
public class BarChartErrorBarsHLayer extends PlotClippedLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getType() {
		return "Bar Chart Error Bars Layer";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.PlotClippedLayer#plotLayer(
	 * java.awt.Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Figure,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Axes,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Plot,
	 * org.abh.lib.math.matrix.DataFrame)
	 */
	@Override
	public void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes,
			Plot plot, DataFrame m) {
		double x;
		int x1;
		int y1;
		int y2;
		double bw;
		int bwp;
		double offset;
		double y;
		double sd;

		int x2;

		y1 = axes.toPlotX1(0);

		bw = plot.getBarWidth();
		bwp = axes.toPlotY1(0) - axes.toPlotY1(bw);

		int bwp2 = bwp / 2;

		offset = (1.0 - bw) / 2.0;

		x = 0; // axes.toPlotX(axes.getXAxis().getLimits().getMin());

		for (XYSeries series : plot.getAllSeries()) {
			y = XYSeries.mean(m, series);
			sd = XYSeries.sd(m, series);

			x1 = axes.toPlotY1(x + offset);
			x2 = axes.toPlotY1(x + offset + bw);

			y2 = axes.toPlotX1(y + sd);
			y1 = axes.toPlotX1(y);

			g2.setColor(Color.BLACK);

			g2.drawLine(y2, x1, y2, x2);

			x1 -= bwp2;

			g2.drawLine(y1, x1, y2, x1);

			x++;
		}
	}
}
