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
package org.jebtk.graphplot.figure.heatmap.legacy.clustering;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import org.jebtk.core.Props;
import org.jebtk.core.geom.DoubleDim;
import org.jebtk.core.text.TextUtils;
import org.jebtk.graphplot.figure.heatmap.legacy.ColumnMatrixPlotElement;
import org.jebtk.math.cluster.Cluster;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Sort the labels using the hierarchical tree.
 * 
 * @author Antony Holmes
 *
 */
public class ColumnHLabelBottomPlotElement extends ColumnMatrixPlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member ids.
	 */
	protected ArrayList<String> mIds;

	/**
	 * The member color.
	 */
	protected Color mColor;

	/**
	 * Instantiates a new column hierarchical label plot element.
	 *
	 * @param matrix      the matrix
	 * @param aspectRatio the aspect ratio
	 * @param rootCluster the root cluster
	 * @param color       the color
	 * @param charWidth   the char width
	 * @param maxRowChars the max row chars
	 */
	public ColumnHLabelBottomPlotElement(DataFrame matrix, DoubleDim aspectRatio, Cluster rootCluster, Color color,
			int charWidth, int maxRowChars) {
		super(matrix, aspectRatio, charWidth * TextUtils.maxLength(matrix.getColumnNames()));

		mColor = color;

		Deque<Cluster> stack = new ArrayDeque<Cluster>();

		stack.push(rootCluster);

		mIds = new ArrayList<String>();

		while (stack.size() > 0) {
			Cluster cluster = stack.pop();

			if (cluster.isParent()) {
				Cluster c1 = cluster.getChild1();
				Cluster c2 = cluster.getChild2();

				stack.push(c2);
				stack.push(c1);
			} else {
				mIds.add(matrix.getColumnName(cluster.getId()));
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
		drawLabels(g2);

		super.plot(g2, offset, context, params);
	}

	/**
	 * Draw labels.
	 *
	 * @param g2 the g2
	 */
	protected void drawLabels(Graphics2D g2) {
		g2.setColor(mColor);

		// (blockSize.width + g2.getFontMetrics().getAscent()) / 2;
		int xd = (mBlockSize.getW() - g2.getFontMetrics().getAscent()) / 2;

		int x = xd;

		for (String id : mIds) {

			Graphics2D g2Temp = (Graphics2D) g2.create();

			try {
				g2Temp.translate(x, 0);
				g2Temp.rotate(Math.PI / 2.0);
				// g2Temp.translate(0, -);

				g2Temp.drawString(id, 0, 0);
			} finally {
				g2Temp.dispose();
			}

			x += mBlockSize.getW();
		}
	}
}
