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

import org.jebtk.graphplot.figure.props.FillPattern;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.DoubleMatrix;
import org.jebtk.math.matrix.MatrixGroup;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Draws lines between series dots.
 * 
 * @author Antony Holmes
 *
 */
public class BarChartLayer extends PlotClippedLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getType() {
		return "Bar Chart Layer";
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

		double[] means = DoubleMatrix.columnMeans(m);

		for (XYSeries series : plot.getAllSeries()) {
			List<Integer> columns = MatrixGroup.findColumnIndices(m, series);

			if (columns.size() == 0) {
				continue;
			}

			int c = columns.get(0);

			x1 = axes.toPlotX1(x + offset);

			y2 = axes.toPlotY1(means[c]);

			h = y1 - y2 + 1;

			if (series.getStyle().getFillStyle().getVisible()) {
				// System.err.println("has " + series.getTitle().getText());

				g2.setColor(series.getStyle().getFillStyle().getColor());

				// g2.fillRect(x1, y2, bwp, h);

				patternFill(g2, series.getStyle().getFillStyle().getPattern(), x1, y2, bwp, h);
			}

			if (series.getStyle().getLineStyle().getVisible()) {
				g2.setStroke(series.getStyle().getLineStyle().getStroke());
				g2.setColor(series.getStyle().getLineStyle().getColor());

				g2.drawRect(x1, y2, bwp, h);
			}

			++x;
		}
	}

	/**
	 * Pattern fill.
	 *
	 * @param g2      the g2
	 * @param pattern the pattern
	 * @param x       the x
	 * @param y       the y
	 * @param w       the w
	 * @param h       the h
	 */
	public static void patternFill(Graphics2D g2, FillPattern pattern, int x, int y, int w, int h) {
		if (pattern == FillPattern.SOLID) {
			g2.fillRect(x, y, w, h);
			return;
		}

		Graphics2D g2Temp = (Graphics2D) g2.create();

		g2Temp.translate(x, y);

		g2Temp.clipRect(0, 0, w, h);

		switch (pattern) {
		case CROSS_HATCH:
			crossHatching(g2Temp, x, y, w, h);
			break;
		case BACK_HATCH:
			backSlashHatching(g2Temp, x, y, w, h);
			break;
		case FORWARD_HATCH:
			forwardSlashHatching(g2Temp, x, y, w, h);
			break;
		case HOZ_HATCH:
			hozHatching(g2Temp, x, y, w, h);
			break;
		case VERT_HATCH:
			vertHatching(g2Temp, x, y, w, h);
			break;
		default:
			break;
		}

		g2Temp.dispose();
	}

	/**
	 * Cross hatching.
	 *
	 * @param g2 the g2
	 * @param x  the x
	 * @param y  the y
	 * @param w  the w
	 * @param h  the h
	 */
	public static void crossHatching(Graphics2D g2, int x, int y, int w, int h) {
		forwardSlashHatching(g2, x, y, w, h);
		backSlashHatching(g2, x, y, w, h);
	}

	/**
	 * Forward slash hatching.
	 *
	 * @param g2 the g2
	 * @param x  the x
	 * @param y  the y
	 * @param w  the w
	 * @param h  the h
	 */
	public static void forwardSlashHatching(Graphics2D g2, int x, int y, int w, int h) {

		int x1 = 0;
		int y1 = 10;

		int x2 = w;
		int y2 = y1 - w;

		while (y2 < h) {
			g2.drawLine(x1, y1, x2, y2);

			y1 += 10;
			y2 += 10;
		}
	}

	/**
	 * Back slash hatching.
	 *
	 * @param g2 the g2
	 * @param x  the x
	 * @param y  the y
	 * @param w  the w
	 * @param h  the h
	 */
	public static void backSlashHatching(Graphics2D g2, int x, int y, int w, int h) {

		int x2 = w;
		int y2 = 10;

		int x1 = 0;
		int y1 = y2 - w;

		while (y1 < h) {
			g2.drawLine(x1, y1, x2, y2);

			y1 += 10;
			y2 += 10;
		}
	}

	/**
	 * Hoz hatching.
	 *
	 * @param g2 the g2
	 * @param x  the x
	 * @param y  the y
	 * @param w  the w
	 * @param h  the h
	 */
	public static void hozHatching(Graphics2D g2, int x, int y, int w, int h) {

		int x1 = 0;
		int y1 = 10;

		int x2 = w;
		int y2 = 10;

		while (y2 < h) {
			g2.drawLine(x1, y1, x2, y2);

			y1 += 10;
			y2 += 10;
		}
	}

	/**
	 * Vert hatching.
	 *
	 * @param g2 the g2
	 * @param x  the x
	 * @param y  the y
	 * @param w  the w
	 * @param h  the h
	 */
	public static void vertHatching(Graphics2D g2, int x, int y, int w, int h) {

		int x1 = 10;
		int y1 = 0;

		int x2 = 10;
		int y2 = h;

		while (x1 < w) {
			g2.drawLine(x1, y1, x2, y2);

			x1 += 10;
			x2 += 10;
		}
	}

}
