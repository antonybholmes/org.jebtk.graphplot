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
public class BoxWhiskerLayer extends PlotSeriesLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** The m x. */
	private double mX;

	/**
	 * Instantiates a new box whisker layer.
	 *
	 * @param name the name
	 * @param x    the x
	 */
	public BoxWhiskerLayer(String name, double x) {
		this(name, x, true);
	}

	/**
	 * Instantiates a new box whisker layer.
	 *
	 * @param name    the name
	 * @param x       the x
	 * @param visible the visible
	 */
	public BoxWhiskerLayer(String name, double x, boolean visible) {
		super(name);

		mX = x;

		setVisible(visible);
	}

	@Override
	public String getType() {
		return "Box Whisker Layer";
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
			Plot plot, DataFrame m, XYSeries series) {

		// int realX = 0;

		// the width of the arms of the plot
		int y;
		int x1;
		int x2;
		int xc;

		// for (XYSeries series : plot.getAllSeries()) {
		BoxWhiskerSummary s = (BoxWhiskerSummary) series;

		x1 = axes.toPlotX1(mX + 1 - plot.getBarWidth());
		x2 = axes.toPlotX1(mX + plot.getBarWidth());
		xc = axes.toPlotX1(mX + 0.5);

		// set the line color
		g2.setColor(s.getStyle().getLineStyle().getColor());

		y = axes.toPlotY1(s.getMedian());
		g2.drawLine(x1, y, x2, y);

		// Plot the limits
		y = axes.toPlotY1(s.getUpper());
		g2.drawLine(x1, y, x2, y);
		y = axes.toPlotY1(s.getLower());
		g2.drawLine(x1, y, x2, y);

		// the vertical line
		g2.drawLine(xc, axes.toPlotY1(s.getUpper()), xc, axes.toPlotY1(s.getLower()));

		// the median line
		y = axes.toPlotY1(s.getMedian());

		g2.drawLine(x1, y, x2, y);
	}
}
