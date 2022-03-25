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
 * Draws lines between points specified as x1,y1,x2,y2 in a nx4 matrix.
 *
 * @author Antony Holmes
 */
public class SegmentsPlotLayer extends PlotSeriesLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new unique xy layer.
	 *
	 * @param series the series
	 */
	public SegmentsPlotLayer(String series) {
		super(series);
	}

	@Override
	public String getType() {
		return "Segments Plot Layer";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.PlotSeriesLayer#plotLayer(
	 * java.awt.Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Figure,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Axes,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Plot,
	 * org.abh.lib.math.matrix.DataFrame,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.series.XYSeries)
	 */
	@Override
	public final void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes,
			Plot plot, DataFrame m, XYSeries series) {

		g2.setStroke(series.getStyle().getLineStyle().getStroke());
		g2.setColor(series.getStyle().getLineStyle().getColor());
		g2.setStroke(series.getStyle().getLineStyle().getStroke());

		for (int i = 0; i < m.getRows(); ++i) {
			int x1 = axes.toPlotX1(m.getValue(i, 0));
			int y1 = axes.toPlotY1(m.getValue(i, 1));
			// Ensure line is at least 1 pixel wide
			int x2 = Math.max(x1 + 1, axes.toPlotX1(m.getValue(i, 2)));
			int y2 = axes.toPlotY1(m.getValue(i, 3));

			g2.drawLine(x1, y1, x2, y2);
		}
	}
}