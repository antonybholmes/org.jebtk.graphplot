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
package org.jebtk.graphplot.figure.heatmap.legacy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import org.jebtk.core.Props;
import org.jebtk.core.geom.DoubleDim;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Writes the number of samples that are up and down regulated on the plot.
 * 
 * @author Antony Holmes
 *
 */
public class UpDownDiffExpPlotElement extends RowMatrixPlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The color.
	 */
	private Color mColor;

	/**
	 * The up.
	 */
	private int up = 0;

	/**
	 * The down.
	 */
	private int down = 0;

	/**
	 * Instantiates a new up down diff exp plot element.
	 *
	 * @param matrix      the matrix
	 * @param width       the width
	 * @param aspectRatio the aspect ratio
	 * @param color       the color
	 */
	public UpDownDiffExpPlotElement(DataFrame matrix, int width, DoubleDim aspectRatio, Color color) {
		super(matrix, aspectRatio, width);

		mColor = color;

		double[] zscores = matrix.getIndex().getValues("Z-score");

		for (double zscore : zscores) {
			if (zscore >= 0) {
				++up;
			} else {
				++down;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.ModernPlotCanvas#plot(java.awt.
	 * Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext)
	 */
	@Override
	public void plot(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
		g2.setColor(mColor);

		int y;

		if (up > 0) {
			y = (int) ((up * mBlockSize.getH() + g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent())
					/ 2);

			String t = Integer.toString(up) + " up";

			g2.drawString(t, 0, y);
		}

		if (down > 0) {
			y = (int) (up * mBlockSize.getH()
					+ (down * mBlockSize.getH() + g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent())
							/ 2);

			String t = Integer.toString(down) + " down";

			g2.drawString(t, 0, y);
		}

		super.plot(g2, offset, context, params);
	}
}
