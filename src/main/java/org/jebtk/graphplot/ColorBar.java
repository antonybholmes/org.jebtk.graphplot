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
package org.jebtk.graphplot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import org.jebtk.core.Props;
import org.jebtk.core.geom.IntDim;
import org.jebtk.core.text.Formatter;
import org.jebtk.core.text.Formatter.NumberFormatter;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.colormap.ColorMap;

/**
 * Draws a gradient color bar to represent the range of a color map.
 * 
 * @author Antony Holmes
 *
 */
public class ColorBar extends PlotElementFixedSize {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  private static final int TICK_SIZE = 8;

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
   * Instantiates a new color bar.
   *
   * @param colorMap the color map
   */
  public ColorBar(ColorMap colorMap, IntDim size) {
    this(colorMap, colorMap.getMin(), colorMap.getMax(), size);
  }

  /**
   * Instantiates a new color bar.
   *
   * @param colorMap the color map
   * @param min the min
   * @param max the max
   */
  public ColorBar(ColorMap colorMap, double min, double max, IntDim size) {
    super("color-bar", size);

    setColorMap(colorMap);

    mMin = min;
    mMax = max;
    mMid = (mMin + mMax) / 2.0;
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
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.ModernPlotCanvas#plot(java.awt.
	 * Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext)
	 */
	@Override
	public void plot(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
		NumberFormatter nf = Formatter.number().dp(2);

		int l1 = g2.getFontMetrics().stringWidth(nf.format(mMin));
		int l2 = g2.getFontMetrics().stringWidth(nf.format(mMax));

		drawRangeBar(g2, l1, l2, nf);
		drawRangeBarTicks(g2, l1, l2);
		drawRangeBarLabels(g2, l1, l2, nf);

		super.plot(g2, offset, context, params);
	}

	/**
	 * Draw range bar.
	 *
	 * @param g2 the g2
	 * @param nf the nf
	 */
	private void drawRangeBar(Graphics2D g2, int l1, int l2, NumberFormatter nf) {

		int l = getPreferredSize().width - (l1 + l2) / 2;

		if (l < 10) {
			return;
		}

		int x = l1 / 2;

		int h = getPreferredSize().height / 3;

		/*
		 * LinearGradientPaint paint = mColorMap.getAnchorColors().toGradientPaint(new
		 * Point2D.Float(x, 0), new Point2D.Float(l, 0)); g2.setPaint(paint);
		 * g2.fillRect(l1 / 2, 0, l, h);
		 */

		double lf = (double) l;

		for (int i = 0; i < l; ++i) {

			double r = i / lf;

			g2.setColor(mColorMap.getColor(r));

			g2.fillRect(x, 0, l - i, h);

			++x;
		}

		// Draw a border box
		g2.setColor(Color.BLACK);
		g2.drawRect(l1 / 2, 0, l, h);
	}

	/**
	 * Draw range bar labels.
	 *
	 * @param g2 the g2
	 * @param nf the nf
	 */
	private void drawRangeBarLabels(Graphics2D g2, int l1, int l2, NumberFormatter nf) {
		g2.setColor(Color.BLACK);

		int y = getPreferredSize().height * 3 / 5;

		String t = nf.format(mMin);
		int offset = 0; // g2.getFontMetrics().stringWidth(t) / 2;
		g2.drawString(t, offset, y);

		t = nf.format(mMid);

		int x = (l1 + getPreferredSize().width - (l1 + l2) / 2) / 2;

		offset = x - g2.getFontMetrics().stringWidth(t) / 2;
		g2.drawString(t, offset, y);

		t = nf.format(mMax);
		offset = g2.getFontMetrics().stringWidth(t);
		g2.drawString(t, getPreferredSize().width - offset, y);
	}

	private void drawRangeBarTicks(Graphics2D g2, int l1, int l2) {
		g2.setColor(Color.BLACK);

		int y = getPreferredSize().height / 3 - TICK_SIZE - 1;

		int offset = l1 / 2; // g2.getFontMetrics().stringWidth(t) / 2;
		g2.drawLine(offset, y, offset, y + TICK_SIZE);

		offset = l1 + (getPreferredSize().width - l1 - l2) / 2;
		g2.drawLine(offset, y, offset, y + TICK_SIZE);

		offset = getPreferredSize().width - l2 / 2;
		g2.drawLine(offset, y, offset, y + TICK_SIZE);
	}
}
