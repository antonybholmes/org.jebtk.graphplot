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
import java.util.List;

import org.jebtk.core.Props;
import org.jebtk.core.geom.DoubleDim;
import org.jebtk.core.text.Formatter;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Details how the matrix was transformed and writes this on the plot.
 * 
 * @author Antony Holmes
 *
 */
public class MatrixSummaryPlotElement extends RowMatrixPlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member color.
	 */
	private Color mColor;

	/**
	 * The member history.
	 */
	private List<String> mHistory;

	/**
	 * Instantiates a new matrix summary plot element.
	 *
	 * @param matrix      the matrix
	 * @param aspectRatio the aspect ratio
	 * @param width       the width
	 */
	public MatrixSummaryPlotElement(DataFrame matrix, DoubleDim aspectRatio, int width) {
		this(matrix, aspectRatio, width, Color.BLACK);
	}

	/**
	 * Instantiates a new matrix summary plot element.
	 *
	 * @param matrix      the matrix
	 * @param history     the history
	 * @param aspectRatio the aspect ratio
	 * @param width       the width
	 */
	public MatrixSummaryPlotElement(DataFrame matrix, List<String> history, DoubleDim aspectRatio, int width) {
		this(matrix, history, aspectRatio, width, Color.BLACK);
	}

	/**
	 * Instantiates a new matrix summary plot element.
	 *
	 * @param matrix      the matrix
	 * @param aspectRatio the aspect ratio
	 * @param width       the width
	 * @param color       the color
	 */
	public MatrixSummaryPlotElement(DataFrame matrix, DoubleDim aspectRatio, int width, Color color) {
		this(matrix, null, aspectRatio, width, color);
	}

	/**
	 * Instantiates a new matrix summary plot element.
	 *
	 * @param matrix      the matrix
	 * @param history     the history
	 * @param aspectRatio the aspect ratio
	 * @param width       the width
	 * @param color       the color
	 */
	public MatrixSummaryPlotElement(DataFrame matrix, List<String> history, DoubleDim aspectRatio, int width,
			Color color) {
		super(matrix, aspectRatio, width);

		mHistory = history;
		mColor = color;
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

		super.plot(g2, offset, context, params);
	}

	/**
	 * Draw labels.
	 *
	 * @param g2 the g2
	 */
	private void drawLabels(Graphics2D g2) {
		g2.setColor(mColor);

		int y = (BLOCK_SIZE + g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent()) / 2;

		g2.drawString(Formatter.number().format(mMatrix.getCols()) + " experiments", 0, y);

		y += BLOCK_SIZE;

		g2.drawString(Formatter.number().format(mMatrix.getRows()) + " features", 0, y);

		y += 2 * BLOCK_SIZE;

		if (mHistory == null || mHistory.size() == 0) {
			return;
		}

		g2.drawString("Data transformations:", 0, y);

		y += BLOCK_SIZE;

		for (String item : mHistory) {
			g2.drawString(item, 0, y);

			y += BLOCK_SIZE;
		}
	}

	@Override
	public void plotSize(Dimension d) {
		d.width += mWidth;
		d.height += ((mHistory != null ? mHistory.size() : 0) + 5) * BLOCK_SIZE;
	}
}
