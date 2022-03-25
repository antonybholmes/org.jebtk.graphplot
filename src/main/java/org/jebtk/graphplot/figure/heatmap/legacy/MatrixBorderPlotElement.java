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
 * The class MatrixBorderPlotElement.
 */
public class MatrixBorderPlotElement extends MatrixPlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new matrix border plot element.
	 *
	 * @param matrix      the matrix
	 * @param aspectRatio the aspect ratio
	 */
	public MatrixBorderPlotElement(DataFrame matrix, DoubleDim aspectRatio) {
		super(matrix, aspectRatio);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.ModernPlotCanvas#plot(java.awt.
	 * Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext)
	 */
	@Override
	public void plot(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
		drawBorder(g2);

		super.plot(g2, offset, context, params);
	}

	/**
	 * Draw border.
	 *
	 * @param g2 the g2
	 */
	protected void drawBorder(Graphics2D g2) {
		g2.setColor(Color.DARK_GRAY);

		int w = getPreferredSize().width;
		int h = getPreferredSize().height;

		g2.drawRect(0, 0, w, h);
	}

	@Override
	public void plotSize(Dimension d) {
		d.width += mMatrix.getCols() * mBlockSize.getW();
		d.height += mMatrix.getRows() * mBlockSize.getH();
	}
}
