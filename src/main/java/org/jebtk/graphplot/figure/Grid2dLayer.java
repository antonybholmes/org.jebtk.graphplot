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

import org.jebtk.modern.graphics.DrawingContext;

/**
 * Draws the grid on the plot.
 * 
 * @author Antony Holmes
 *
 */
public class Grid2dLayer extends AxesLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getType() {
		return "Grid 2D Layer";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.figure.AxesLayer#plot(java.awt.
	 * Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Figure,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Axes)
	 */
	@Override
	public void drawPlot(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes) {

		drawXAxis(g2, axes, axes.getX1Axis());
		drawXAxis(g2, axes, axes.getX2Axis());

		drawYAxis(g2, axes, axes.getY1Axis());
		drawYAxis(g2, axes, axes.getY2Axis());
	}

	private static void drawXAxis(Graphics2D g2, Axes axes, Axis axis) {
		if (axis.getGrid().getVisible()) {
			g2.setColor(axis.getGrid().getColor());
			g2.setStroke(axis.getGrid().getStroke());

			int x;
			int h = axes.getInternalSize().getH();

			for (int i = 0; i < axis.getTicks().getMajorTicks().getTickCount(); ++i) {
				x = axes.toPlotX1(axis.getTicks().getMajorTicks().getTick(i));

				g2.drawLine(x, 0, x, h);
			}
		}
	}

	private static void drawYAxis(Graphics2D g2, Axes axes, Axis axis) {
		if (axis.getGrid().getVisible()) {

			g2.setColor(axis.getGrid().getColor());
			g2.setStroke(axis.getGrid().getStroke());

			int x2 = axes.getInternalSize().getW();

			int y;

			for (int i = 0; i < axis.getTicks().getMajorTicks().getTickCount(); ++i) {
				y = axes.toPlotY1(axis.getTicks().getMajorTicks().getTick(i));

				g2.drawLine(0, y, x2, y);
			}
		}
	}
}
