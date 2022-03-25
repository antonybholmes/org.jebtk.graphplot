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
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.TreeMap;

import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.graphplot.plotbox.PlotBox;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The class LegendLayer.
 */
public class LegendLayer extends AxesLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** The Constant GAP. */
	private static final int GAP = 5;

	/** The Constant TEXT_OFFSET. */
	private static final int TEXT_OFFSET = 48;

	/** The Constant LINE_WIDTH. */
	private static final int LINE_WIDTH = 32;

	@Override
	public String getType() {
		return "Legend";
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
		// determine the size of the legend

		if (!axes.getLegend().getVisible()) {
			return;
		}

		// In case there are repetitive series within groups (e.g. grouped
		// bars), try to extract only the unique names.
		Map<String, XYSeries> uniqueSeries = new TreeMap<String, XYSeries>();

		Deque<PlotBox> stack = new ArrayDeque<PlotBox>(100);

		stack.push(axes);

		while (!stack.isEmpty()) {
			PlotBox p = stack.pop();

			// System.err.println("leggend " + p.getName() + " " + p);

			if (p instanceof Plot) {
				Plot plot = (Plot) p;
				for (XYSeries series : plot.getAllSeries()) {
					uniqueSeries.put(series.getName(), series);

					// System.err.println("series " + series.getName() + " " +
					// series.getMarker() + " " +
					// series.getMarkerStyle().getFillStyle().getColor());
				}
			}

			for (PlotBox c : p) {
				stack.push(c);
			}
		}

		int lineHeight = g2.getFontMetrics().getAscent() + g2.getFontMetrics().getDescent();

		// The height of the bounding box needs to be slightly bigger than
		// the number of rows * line height to account for the font descent.
		int height = (lineHeight + GAP) * uniqueSeries.size();// +
																// ModernWidget.DOUBLE_PADDING;

		String longest = null;

		for (String s : uniqueSeries.keySet()) {
			if (longest == null || s.length() > longest.length()) {
				longest = s;
			}
		}

		if (longest == null) {
			// There are no series to form a legend.
			return;
		}

		int width = (int) (g2.getFontMetrics().stringWidth(longest)) + 64;

		// determine the location

		int x;
		int y;

		if (axes.getLegend().isInside()) {
			switch (axes.getLegend().getPosition()) {
			case NW:
				x = 0;
				y = 0;
				break;
			case N:
				x = (axes.getInternalSize().getW() - width) / 2;
				y = 0;
				break;
			case W:
				x = 0;
				y = (axes.getInternalSize().getH() - height) / 2;
				break;
			case NE:
				// top right
				x = axes.getInternalSize().getW() - width;
				y = 0;
				break;
			case E:
				x = axes.getInternalSize().getW() - width;
				y = (axes.getInternalSize().getH() - height) / 2;
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
				x = (axes.getInternalSize().getW() - width) / 2;
				y = (axes.getInternalSize().getH() - height) / 2;
				break;
			}
		} else {
			// Plot legend at boundaries of margins
			switch (axes.getLegend().getPosition()) {
			case NW:
				x = -axes.getMargins().getLeft();
				y = -axes.getMargins().getTop();
				break;
			case N:
				x = (axes.getInternalSize().getW() - width) / 2;
				y = -axes.getMargins().getTop();
				break;
			case NE:
				// top right
				x = axes.getInternalSize().getW() + axes.getMargins().getRight() - width;
				y = -axes.getMargins().getTop();
				break;
			case W:
				x = -axes.getMargins().getLeft();
				y = (axes.getInternalSize().getH() - height) / 2;
				break;
			case E:
				x = axes.getInternalSize().getW() + axes.getMargins().getRight() - width;
				y = (axes.getInternalSize().getH() - height) / 2;
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
				x = (axes.getInternalSize().getW() - width) / 2;
				y = (axes.getInternalSize().getH() - height) / 2;
				break;
			}
		}

		if (axes.getLegend().getStyle().getFillStyle().getVisible()) {
			g2.setColor(axes.getLegend().getStyle().getFillStyle().getColor());
			g2.fillRect(x, y, width, height);
		}

		if (axes.getLegend().getStyle().getLineStyle().getVisible()) {
			g2.setColor(axes.getLegend().getStyle().getLineStyle().getColor());
			g2.drawRect(x, y, width, height);
		}

		// draw the labels

		x += ModernWidget.PADDING;
		y += ModernWidget.PADDING;

		int textX;
		int textY;

		// draw the lines

		textX = x;
		textY = y;

		for (String name : uniqueSeries.keySet()) {
			XYSeries s = uniqueSeries.get(name);

			if (s.getStyle().getLineStyle().getVisible()) {
				Graphics2D g2Temp = (Graphics2D) g2.create();

				g2Temp.translate(textX, textY + lineHeight / 2);
				g2Temp.setColor(s.getStyle().getLineStyle().getColor());
				g2Temp.setStroke(s.getStyle().getLineStyle().getStroke());

				g2Temp.drawLine(0, 0, LINE_WIDTH, 0);

				g2Temp.dispose();
			}

			textY += lineHeight + GAP;
		}

		// draw the markers

		textX = x + ModernWidget.PADDING;
		textY = y;

		for (String name : uniqueSeries.keySet()) {
			XYSeries s = uniqueSeries.get(name);

			if (s.getMarker().getVisible()) {
				s.getMarker().plot(g2, s.getMarkerStyle(),
						new Point(textX + (LINE_WIDTH - s.getMarker().getSize()) / 2, textY + lineHeight / 2));
			}

			textY += lineHeight + GAP;
		}

		// draw the labels

		textX = x + TEXT_OFFSET;
		textY = y + lineHeight - g2.getFontMetrics().getDescent();

		for (String name : uniqueSeries.keySet()) {
			XYSeries s = uniqueSeries.get(name);

			g2.setColor(s.getFontStyle().getColor());
			g2.setFont(s.getFontStyle().getFont());

			g2.drawString(name, textX, textY);

			textY += lineHeight + GAP;
		}

	}
}