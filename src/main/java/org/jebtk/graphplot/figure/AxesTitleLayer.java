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

import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Add a centered title to a plot.
 * 
 * @author Antony Holmes
 */
public class AxesTitleLayer extends AxesLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	public AxesTitleLayer() {
		super("Axes Title");
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
		String title = axes.getTitle().getText();

		if (!axes.getTitle().getVisible() || title.length() == 0) {
			return;
		}

		Color color = axes.getTitle().getFontStyle().getColor();

		g2.setColor(color);
		g2.setFont(axes.getTitle().getFontStyle().getFont());

		int x;
		int y;

		int width = g2.getFontMetrics().stringWidth(title);
		int height = ModernWidget.getStringHeight(g2);

		if (axes.getTitle().isInside()) {
			switch (axes.getTitle().getPosition()) {
			case NW:
				x = 0;
				y = 0;
				break;
			case N:
				x = (axes.getInternalSize().getW() - width) / 2;
				y = 0;
				break;
			case NE:
				x = axes.getInternalSize().getW() - width;
				y = height;
				break;
			case W:
				x = 0;
				y = (axes.getInternalSize().getH() + height) / 2;
				break;
			case E:
				x = axes.getInternalSize().getW() - width;
				y = (axes.getInternalSize().getH() + height) / 2;
				break;
			case SW:
				x = 0;
				y = axes.getInternalSize().getH() - height;
				break;
			case S:
				x = (axes.getInternalSize().getW() - width) / 2;
				y = axes.getInternalSize().getH() - height;
				break;
			case SE:
				x = axes.getInternalSize().getW() - width;
				y = axes.getInternalSize().getH() - height;
				break;
			default:
				// Center
				x = (axes.getInternalSize().getW() - width) / 2;
				y = (axes.getInternalSize().getH() + height) / 2;
				break;
			}
		} else {
			// Plot legend at boundaries of margins
			switch (axes.getTitle().getPosition()) {
			case NW:
				x = -axes.getMargins().getLeft();
				y = -axes.getMargins().getTop() + height;
				break;
			case N:
				x = (axes.getInternalSize().getW() - width) / 2;
				y = -axes.getMargins().getTop() + height;
				break;
			case NE:
				// top right
				x = axes.getInternalSize().getW() + axes.getMargins().getRight() - width;
				y = -axes.getMargins().getTop() + height;
				break;
			case W:
				x = -axes.getMargins().getLeft();
				y = (axes.getInternalSize().getH() + height) / 2;
				break;
			case E:
				x = axes.getInternalSize().getW() + axes.getMargins().getRight() - width;
				y = (axes.getInternalSize().getH() + height) / 2;
				break;
			case SW:
				x = -axes.getMargins().getLeft();
				y = axes.getInternalSize().getH() + axes.getMargins().getBottom() - height;
				break;
			case S:
				x = (axes.getInternalSize().getW() - width) / 2;
				y = axes.getInternalSize().getH() + axes.getMargins().getBottom() - height;
				break;
			case SE:
				x = axes.getInternalSize().getW() + axes.getMargins().getRight() - width;
				y = axes.getInternalSize().getH() + axes.getMargins().getBottom() - height;
				break;
			default:
				// Center
				x = (axes.getInternalSize().getW() - width) / 2;
				y = (axes.getInternalSize().getH() + height) / 2;
				break;
			}
		}

		g2.drawString(title, x, y);
	}
}
