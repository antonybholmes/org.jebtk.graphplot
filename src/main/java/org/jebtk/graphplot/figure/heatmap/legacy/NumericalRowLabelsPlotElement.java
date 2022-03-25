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
import java.text.DecimalFormat;
import java.util.List;

import org.jebtk.core.Props;
import org.jebtk.core.geom.DoubleDim;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The class NumericalRowLabelsPlotElement.
 */
public class NumericalRowLabelsPlotElement extends RowMatrixPlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member values.
	 */
	private List<Double> mValues;

	/**
	 * The member color.
	 */
	private Color mColor;

	/**
	 * The member title.
	 */
	private String mTitle;

	/**
	 * The member formatter.
	 */
	private DecimalFormat mFormatter;

	/**
	 * The constant FORMAT.
	 */
	private static final String FORMAT = "##0.00";

	/**
	 * Instantiates a new numerical row labels plot element.
	 *
	 * @param matrix      the matrix
	 * @param values      the values
	 * @param width       the width
	 * @param aspectRatio the aspect ratio
	 */
	public NumericalRowLabelsPlotElement(DataFrame matrix, List<Double> values, int width, DoubleDim aspectRatio) {
		this(matrix, null, values, width, aspectRatio, Color.BLACK, FORMAT);
	}

	/**
	 * Instantiates a new numerical row labels plot element.
	 *
	 * @param matrix      the matrix
	 * @param title       the title
	 * @param values      the values
	 * @param width       the width
	 * @param aspectRatio the aspect ratio
	 * @param color       the color
	 */
	public NumericalRowLabelsPlotElement(DataFrame matrix, String title, List<Double> values, int width,
			DoubleDim aspectRatio, Color color) {
		this(matrix, title, values, width, aspectRatio, color, FORMAT);
	}

	/**
	 * Instantiates a new numerical row labels plot element.
	 *
	 * @param matrix      the matrix
	 * @param title       the title
	 * @param values      the values
	 * @param width       the width
	 * @param aspectRatio the aspect ratio
	 * @param color       the color
	 * @param format      the format
	 */
	public NumericalRowLabelsPlotElement(DataFrame matrix, String title, List<Double> values, int width,
			DoubleDim aspectRatio, Color color, String format) {
		super(matrix, aspectRatio, width);

		mTitle = title;
		mColor = color;
		mFormatter = new DecimalFormat(format);

		mValues = values;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.ModernPlotCanvas#plot(java.awt.
	 * Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext)
	 */
	@Override
	public void plot(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
		drawLabels(g2);
	}

	/**
	 * Draw labels.
	 *
	 * @param g2 the g2
	 */
	private void drawLabels(Graphics2D g2) {
		g2.setColor(mColor);

		int x = (getPreferredSize().width - g2.getFontMetrics().stringWidth(mTitle));

		if (mTitle != null) {
			g2.drawString(mTitle, x, (int) -mBlockSize.getH());
		}

		int y = (int) ((mBlockSize.getH() + g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent()) / 2);

		for (double v : mValues) {
			String s = mFormatter.format(v);

			x = getPreferredSize().width - g2.getFontMetrics().stringWidth(s);

			g2.drawString(s, x, y);

			y += mBlockSize.getH();
		}
	}
}
