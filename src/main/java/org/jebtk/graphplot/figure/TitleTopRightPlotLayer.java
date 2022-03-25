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

import org.jebtk.modern.graphics.DrawingContext;

/**
 * Add a centered title to a plot.
 *
 * @author Antony Holmes
 */
public class TitleTopRightPlotLayer extends AxesTitleLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The constant OFFSET.
	 */
	public static final int OFFSET = 10;

	/**
	 * The member y offset.
	 */
	private int mYOffset;

	/**
	 * Instantiates a new title top right plot layer.
	 *
	 * @param yOffset the y offset
	 */
	public TitleTopRightPlotLayer(int yOffset) {
		mYOffset = yOffset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.AxesTitleLayer#plot(java.
	 * awt.Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Figure,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Axes)
	 */
	@Override
	public void drawPlot(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes) {

		String title = axes.getTitle().getText();
		Color color = axes.getTitle().getFontStyle().getColor();

		if (axes.getTitle().getFontStyle().getVisible() || title == null || title.length() == 0) {
			return;
		}

		int x = OFFSET + axes.getPreferredSize().width - axes.getMargins().getRight();
		int y = axes.getMargins().getTop() + mYOffset;

		g2.setColor(color);
		g2.drawString(title, x, y);
	}
}
