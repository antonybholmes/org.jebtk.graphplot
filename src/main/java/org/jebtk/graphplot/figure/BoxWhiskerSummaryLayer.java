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

import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Concrete implementation of Graph2dCanvas for generating scatter plots.
 *
 * @author Antony Holmes
 */
public class BoxWhiskerSummaryLayer extends PlotClippedLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getType() {
		return "Box Whisker Summary Layer";
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

		int realX = 0;

		// the width of the arms of the plot
		int lx;
		int y;
		int w;
		int h;
		int bw;
		int o;
		int x1;
		int x2;
		int w2;
		int xc;

		w = axes.toPlotX1(1) - axes.toPlotX1(0);
		w2 = w / 2;

		bw = (int) (w * plot.getBarWidth());
		o = (w - bw) / 2;

		Graphics2D g2Temp = (Graphics2D) g2.create();

		for (int i = 0; i < m.getCols(); ++i) {
			XYSeries s = plot.getAllSeries().get(i);

			lx = axes.toPlotX1(realX);

			x1 = lx + o;
			x2 = x1 + bw;
			xc = lx + w2;

			double median = m.getValue(0, i);
			double q1 = m.getValue(1, i);
			double q3 = m.getValue(2, i);
			double lower = m.getValue(3, i);
			double upper = m.getValue(4, i);

			// set the line color
			g2Temp.setColor(s.getStyle().getLineStyle().getColor());

			// upper limit
			y = axes.toPlotY1(upper);
			g2Temp.drawLine(x1, y, x2, y);
			// lower limit
			y = axes.toPlotY1(lower);
			g2Temp.drawLine(x1, y, x2, y);

			// the vertical line
			g2Temp.drawLine(xc, axes.toPlotY1(upper), xc, axes.toPlotY1(lower));

			// the box
			g2Temp.setColor(s.getStyle().getFillStyle().getColor());
			y = axes.toPlotY1(q3);

			h = axes.toPlotY1(q1) - axes.toPlotY1(q3);

			g2Temp.fillRect(x1, y, bw, h);

			g2Temp.setColor(s.getStyle().getLineStyle().getColor());

			g2Temp.drawRect(x1, y, bw, h);

			// the median line
			y = axes.toPlotY1(median);

			g2Temp.drawLine(x1, y, x2, y);

			realX += 1;
		}

		g2Temp.dispose();
	}
}
