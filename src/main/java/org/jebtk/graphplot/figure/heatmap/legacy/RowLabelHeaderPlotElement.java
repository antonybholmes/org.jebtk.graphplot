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
 * The class RowLabelHeaderPlotElement.
 */
public class RowLabelHeaderPlotElement extends MatrixPlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member height.
	 */
	private int mHeight;

	/**
	 * The member width.
	 */
	private int mWidth;

	/**
	 * Instantiates a new row label header plot element.
	 *
	 * @param matrix      the matrix
	 * @param aspectRatio the aspect ratio
	 * @param name        the name
	 * @param width       the width
	 */
	public RowLabelHeaderPlotElement(DataFrame matrix, DoubleDim aspectRatio, String name, int width) {
		super(matrix, aspectRatio);

		mWidth = width;
		mHeight = (int) mBlockSize.getH();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.ModernPlotCanvas#plot(java.awt.
	 * Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext)
	 */
	@Override
	public void plot(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
		g2.setColor(Color.BLACK);

		int y = (mHeight + g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent()) / 2;

		g2.drawString(getName(), 0, y);

		super.plot(g2, offset, context, params);
	}

	@Override
	public void plotSize(Dimension d) {
		d.width += mWidth;
		d.height += mHeight;
	}
}
