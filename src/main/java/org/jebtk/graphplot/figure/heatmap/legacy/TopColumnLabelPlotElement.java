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
import java.util.Map;

import org.jebtk.core.Props;
import org.jebtk.core.geom.DoubleDim;
import org.jebtk.core.text.TextUtils;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The class TopColumnLabelPlotElement.
 */
public class TopColumnLabelPlotElement extends ColumnMatrixPlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The color.
	 */
	private Color mColor;

	/** The m group map. */
	private Map<Integer, XYSeriesGroup> mGroupMap;

	/** The m color by group. */
	private boolean mColorByGroup;

	/**
	 * Instantiates a new top column label plot element.
	 *
	 * @param matrix      the matrix
	 * @param groups      the groups
	 * @param aspectRatio the aspect ratio
	 * @param Props       the properties
	 */
	public TopColumnLabelPlotElement(DataFrame matrix, XYSeriesGroup groups, DoubleDim aspectRatio, Props properties) {
		this(matrix, groups, aspectRatio, properties, Color.BLACK, 10, 50);
	}

	/**
	 * Instantiates a new top column label plot element.
	 *
	 * @param matrix      the matrix
	 * @param groups      the groups
	 * @param aspectRatio the aspect ratio
	 * @param Props       the properties
	 * @param color       the color
	 * @param charWidth   the char width
	 * @param maxChars    the max chars
	 */
	public TopColumnLabelPlotElement(DataFrame matrix, XYSeriesGroup groups, DoubleDim aspectRatio, Props properties,
			Color color, int charWidth, int maxChars) {
		super(matrix, aspectRatio, charWidth * TextUtils.maxLength(matrix.getColumnNames()));

		mColor = color;

		mGroupMap = XYSeriesGroup.arrangeGroupsByIndex(matrix, groups);

		mColorByGroup = properties.getBool("plot.labels.color-by-group");
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

		// (mBlockSize.getW() + g2.getFontMetrics().getAscent()) / 2;
		int xd = (int) ((mBlockSize.getW() - ModernWidget.getStringHeight(g2)) / 2);

		int x = BLOCK_SIZE + xd;

		for (int i = 0; i < mMatrix.getCols(); ++i) {
			Graphics2D g2Temp = (Graphics2D) g2.create();

			try {
				if (mColorByGroup && mGroupMap.containsKey(i)) {
					g2Temp.setColor(mGroupMap.get(i).get(0).getColor());
				} else {
					g2Temp.setColor(mColor);
				}

				g2Temp.translate(x, getPreferredSize().height);
				// g2Temp.rotate(Math.PI / 2.0);
				// g2Temp.translate(getPreferredSize().height -
				// g2Temp.getFontMetrics().stringWidth(mMatrix.getColumnName(i)), xd);
				g2Temp.rotate(-Math.PI / 2.0);
				// g2Temp.translate(-getPreferredSize().height, 0);

				g2Temp.drawString(mMatrix.getColumnName(i), 0, 0);
			} finally {
				g2Temp.dispose();
			}

			x += mBlockSize.getW();
		}
	}
}
