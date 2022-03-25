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
import java.awt.geom.Arc2D;
import java.util.List;

import org.jebtk.core.Mathematics;
import org.jebtk.core.text.Formatter;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.DoubleMatrix;
import org.jebtk.math.matrix.MatrixGroup;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Draw a pie chart.
 *
 * @author Antony Holmes
 */
public class PieChartLayer extends PlotLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** The Constant LABEL_R. */
	private static final double LABEL_R = 0.55;

	@Override
	public String getType() {
		return "Pie Layer";
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
		drawCircle(g2, m, axes, plot.getAllSeries());

		drawLabels(g2, m, axes, plot.getAllSeries());
	}

	/**
	 * Draw circle.
	 *
	 * @param g2   the g2
	 * @param m    the m
	 * @param axes the axes
	 * @param sc   the sc
	 */
	private void drawCircle(Graphics2D g2, DataFrame m, Axes axes, XYSeriesGroup sc) {

		int x1 = axes.toPlotX1(0.25);
		int x2 = axes.toPlotX1(1.25);

		int y1 = axes.toPlotY1(0.25);
		int y2 = axes.toPlotY1(1.25);

		int hX = x2 - x1;
		int hY = y1 - y2;

		int startAngle = 90;

		double sum = DoubleMatrix.sum(m);

		int a = 0;

		for (XYSeries series : sc) {
			List<Integer> columns = MatrixGroup.findColumnIndices(m, series);

			int c = columns.get(0);

			a = (int) Math.round(360 * m.getValue(0, c) / sum);

			if (series.getStyle().getFillStyle().getVisible()) {
				// System.err.println("has " + series.getTitle().getText());

				g2.setColor(series.getStyle().getFillStyle().getColor());

				g2.fill(new Arc2D.Double(x1, y2, hX, hY, startAngle, -a, Arc2D.PIE));
			}

			if (series.getStyle().getLineStyle().getVisible()) {
				g2.setStroke(series.getStyle().getLineStyle().getStroke());
				g2.setColor(series.getStyle().getLineStyle().getColor());

				g2.draw(new Arc2D.Double(x1, y2, hX - 1, hY - 1, startAngle, -a, Arc2D.PIE));
			}

			startAngle -= a;
		}
	}

	/**
	 * Draw labels.
	 *
	 * @param g2   the g2
	 * @param m    the m
	 * @param axes the axes
	 * @param sc   the sc
	 */
	private void drawLabels(Graphics2D g2, DataFrame m, Axes axes, XYSeriesGroup sc) {

		double sum = DoubleMatrix.sum(m);

		double per;

		double startR = Mathematics.HALF_PI;

		for (XYSeries s1 : sc) {

			List<Integer> columns = MatrixGroup.findColumnIndices(m, s1);

			int c = columns.get(0);

			per = m.getValue(0, c) / sum;

			double r = Mathematics.TWO_PI * per;

			double r2 = startR - r / 2;

			int x = axes.toPlotX1(0.75 + LABEL_R * Math.cos(r2));
			int y = axes.toPlotY1(0.75 + LABEL_R * Math.sin(r2));

			g2.setColor(s1.getStyle().getLineStyle().getColor());

			String t = s1.getName() + " (" + Formatter.number().dp(1).format(per * 100) + "%)";

			g2.setFont(s1.getFontStyle().getFont());
			g2.setColor(s1.getFontStyle().getColor());

			if (r2 < -Mathematics.HALF_PI) {
				x -= g2.getFontMetrics().stringWidth(t);
			}

			g2.drawString(t, x, y);

			startR -= r;
		}
	}
}
