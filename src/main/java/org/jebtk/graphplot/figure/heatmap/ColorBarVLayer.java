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
package org.jebtk.graphplot.figure.heatmap;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jebtk.core.text.Formatter;
import org.jebtk.core.text.Formatter.NumberFormatter;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.Plot;
import org.jebtk.graphplot.figure.PlotLayer;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.ImageUtils;
import org.jebtk.modern.graphics.colormap.ColorMap;

/**
 * Draws a gradient color bar to represent the range of a color map.
 * 
 * @author Antony Holmes
 *
 */
public class ColorBarVLayer extends PlotLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	// private Dimension rangeBlockSize = new Dimension(2, 5);

	/**
	 * The member color map.
	 */
	private ColorMap mColorMap;

	/**
	 * The member min.
	 */
	private double mMin = -4;

	/**
	 * The member max.
	 */
	private double mMax = 4;

	/**
	 * The member mid.
	 */
	private double mMid;

	/**
	 * The bar height.
	 */
	private int barHeight = 20;

	/**
	 * The height.
	 */
	private int height = 40;

	/**
	 * Instantiates a new color bar v layer.
	 *
	 * @param colorMap the color map
	 */
	public ColorBarVLayer(ColorMap colorMap) {
		this(colorMap, colorMap.getMin(), colorMap.getMax());
	}

	/**
	 * Instantiates a new color bar v layer.
	 *
	 * @param colorMap the color map
	 * @param min      the min
	 * @param max      the max
	 */
	public ColorBarVLayer(ColorMap colorMap, double min, double max) {
		setColorMap(colorMap);

		mMin = min;
		mMax = max;
		mMid = (mMin + mMax) / 2.0;
	}

	@Override
	public String getType() {
		return "Color Bar V";
	}

	/**
	 * Sets the color map.
	 *
	 * @param colorMap the new color map
	 */
	public void setColorMap(ColorMap colorMap) {
		mColorMap = colorMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.figure.PlotLayer#plot(java.awt.
	 * Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Figure,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Axes,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Plot,
	 * org.abh.lib.math.matrix.DataFrame)
	 */
	@Override
	public void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes,
			Plot plot, DataFrame m) {
		int l = axes.getInternalSize().getH();

		if (l < 10) {
			return;
		}

		drawRangeBar(g2, axes, plot, l);
		drawRangeBarLabels(g2, axes, plot, l);
	}

	/**
	 * Draw range bar.
	 *
	 * @param g2   the g2
	 * @param axes the axes
	 * @param plot the plot
	 * @param l    the l
	 */
	private void drawRangeBar(Graphics2D g2, Axes axes, Plot plot, int l) {
		int x = axes.getPreferredSize().width;

		int n = mColorMap.getColorCount();

		int sy = axes.getMargins().getTop();

		int y = sy;

		double f = l / (double) n;

		for (int i = 0; i < l; ++i) {

			int colorIndex = (int) (i / f);

			g2.setColor(mColorMap.getColorByIndex(colorIndex));

			g2.fillRect(x, y, barHeight, 1);

			++y;
		}

		/*
		 * for (int i = 0; i < n; ++i) {
		 * 
		 * g2.setColor(mColorMap.getColorByIndex(i));
		 * 
		 * g2.fillRect(x, y, block, barHeight);
		 * 
		 * x += block; }
		 */

		// Draw a border box
		g2.setColor(Color.BLACK);
		g2.drawRect(x, sy, barHeight, l);
	}

	/**
	 * Draw range bar labels.
	 *
	 * @param g2   the g2
	 * @param axes the axes
	 * @param plot the plot
	 * @param l    the l
	 */
	private void drawRangeBarLabels(Graphics2D g2, Axes axes, Plot plot, int l) {
		int l1 = ImageUtils.getFontHeight(g2) / 2;

		g2.setColor(Color.BLACK);

		int sy = axes.getMargins().getTop();

		int x = axes.getPreferredSize().width + height;

		NumberFormatter nf = Formatter.number();

		String t = nf.format(mMin);
		int y = sy + l1;
		g2.drawString(t, x, y);

		t = nf.format(mMid);
		y = sy + l / 2 + l1;
		g2.drawString(t, x, y);

		t = nf.format(mMax);
		y = sy + l + l1;
		g2.drawString(t, x, y);
	}
}
