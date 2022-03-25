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

import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Concrete implementation of Graph2dCanvas for generating scatter plots.
 *
 * @author Antony Holmes
 */
public class RightLabelPlotLayer extends LabelPlotLayer {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new scatter plot layer.
	 *
	 * @param name the name
	 * @param x    the x
	 * @param y    the y
	 */
	public RightLabelPlotLayer(String name, double x, double y) {
		this(name, x, y, 0, 0);
	}

	/**
	 * Instantiates a new right label plot layer.
	 *
	 * @param name          the name
	 * @param x             the x
	 * @param y             the y
	 * @param offsetXPixels the offset x pixels
	 * @param offsetYPixels the offset y pixels
	 */
	public RightLabelPlotLayer(String name, double x, double y, int offsetXPixels, int offsetYPixels) {
		super(name, x, y, offsetXPixels, offsetYPixels);
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
	public void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes,
			Plot plot, DataFrame m) {
		Point p = axes.toPlotX1Y1(mX, mY);

		g2.drawString(mLabel, p.x - g2.getFontMetrics().stringWidth(mLabel) + mOffsetXPixels, p.y + mOffsetYPixels);
	}
}
