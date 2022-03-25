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
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The class BottomColumnLabelPlotElement.
 */
public class BottomColumnLabelPlotElement extends ColumnMatrixPlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member color.
	 */
	private Color mColor;

	/**
	 * Instantiates a new bottom column label plot element.
	 *
	 * @param matrix      the matrix
	 * @param aspectRatio the aspect ratio
	 */
	public BottomColumnLabelPlotElement(DataFrame matrix, DoubleDim aspectRatio) {
		this(matrix, aspectRatio, Color.BLACK, 10, 50);
	}

	/**
	 * Instantiates a new bottom column label plot element.
	 *
	 * @param matrix      the matrix
	 * @param aspectRatio the aspect ratio
	 * @param color       the color
	 * @param charWidth   the char width
	 * @param maxChars    the max chars
	 */
	public BottomColumnLabelPlotElement(DataFrame matrix, DoubleDim aspectRatio, Color color, int charWidth,
			int maxChars) {
		super(matrix, aspectRatio, charWidth * TextUtils.maxLength(matrix.getColumnNames()));

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

		int xd = (int) ((mBlockSize.getW() - ModernWidget.getStringHeight(g2)) / 2);

		int x = xd;

		for (int i = 0; i < mMatrix.getCols(); ++i) {

			Graphics2D g2Temp = (Graphics2D) g2.create();

			try {
				g2Temp.translate(x, 0);
				g2Temp.rotate(Math.PI / 2.0);
				// g2Temp.translate(0, xd);

				g2Temp.drawString(mMatrix.getColumnName(i), 0, 0);
			} finally {
				g2Temp.dispose();
			}

			x += mBlockSize.getW();
		}
	}
}
