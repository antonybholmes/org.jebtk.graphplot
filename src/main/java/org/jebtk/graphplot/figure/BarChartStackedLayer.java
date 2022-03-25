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
import java.util.List;

import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.MatrixGroup;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Draws lines between series dots.
 * 
 * @author Antony Holmes
 *
 */
public class BarChartStackedLayer extends PlotClippedLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getType() {
		return "Stacked Bar Chart Layer";
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
		int h;

		y1 = axes.toPlotY1(0);

		bw = plot.getBarWidth();
		bwp = axes.toPlotX1(bw) - axes.toPlotX1(0);

		offset = (1.0 - bw) / 2.0;

		x = 0; // axes.toPlotX(axes.getXAxis().getLimits().getMin());

		for (int i = 0; i < m.getCols(); ++i) {
			int y = y1;

			for (XYSeries series : plot.getAllSeries()) {
				List<Integer> rows = MatrixGroup.findRowIndices(m, series);

				int r = rows.get(0);

				x1 = axes.toPlotX1(x + offset);

				y2 = y1 - axes.toPlotY1(m.getValue(r, i));

				h = y2 + 1;

				if (series.getStyle().getFillStyle().getVisible()) {
					g2.setColor(series.getStyle().getFillStyle().getColor());

					g2.fillRect(x1, y - y2, bwp, h);
				}

				if (series.getStyle().getLineStyle().getVisible()) {
					g2.setStroke(series.getStyle().getLineStyle().getStroke());
					g2.setColor(series.getStyle().getLineStyle().getColor());

					g2.drawRect(x1, y - y2, bwp - 1, h - 1);
				}

				y -= y2;
			}

			++x;
		}
	}
}
