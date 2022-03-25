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

import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.Deque;

import org.jebtk.core.geom.DoubleDim;
import org.jebtk.graphplot.figure.heatmap.legacy.HeatMapPlotElement;
import org.jebtk.math.cluster.Cluster;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.colormap.ColorMap;

/**
 * A heatmap that allows for column and row swapping based upon clustering.
 * 
 * @author Antony Holmes
 *
 */
public class ClusterHeatMapPlotElement extends HeatMapPlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The row order.
	 */
	private int[] rowOrder;

	/**
	 * The column order.
	 */
	private int[] columnOrder;

	/**
	 * Instantiates a new cluster heat map plot element.
	 *
	 * @param matrix      the matrix
	 * @param colorMap    the color map
	 * @param aspectRatio the aspect ratio
	 */
	public ClusterHeatMapPlotElement(DataFrame matrix, ColorMap colorMap, DoubleDim aspectRatio) {
		super(matrix, colorMap, aspectRatio);

		setup();
	}

	/**
	 * Instantiates a new cluster heat map plot element.
	 *
	 * @param matrix        the matrix
	 * @param colorMap      the color map
	 * @param rowCluster    the row cluster
	 * @param columnCluster the column cluster
	 * @param aspectRatio   the aspect ratio
	 */
	public ClusterHeatMapPlotElement(DataFrame matrix, ColorMap colorMap, Cluster rowCluster, Cluster columnCluster,
			DoubleDim aspectRatio) {
		super(matrix, colorMap, aspectRatio);

		setup();

		setRowCluster(rowCluster);
		setColumnCluster(columnCluster);
	}

	/**
	 * Setup.
	 */
	private void setup() {
		// set the order so initially rows and columns
		// map as they should do

		rowOrder = new int[mMatrix.getRows()];
		columnOrder = new int[mMatrix.getCols()];

		for (int i = 0; i < rowOrder.length; ++i) {
			rowOrder[i] = i;
		}

		for (int i = 0; i < columnOrder.length; ++i) {
			columnOrder[i] = i;
		}
	}

	/**
	 * Sets the row cluster.
	 *
	 * @param cluster the new row cluster
	 */
	public void setRowCluster(Cluster cluster) {
		newIndices(cluster, rowOrder);
	}

	/**
	 * Sets the column cluster.
	 *
	 * @param cluster the new column cluster
	 */
	public void setColumnCluster(Cluster cluster) {
		newIndices(cluster, columnOrder);
	}

	/**
	 * New indices.
	 *
	 * @param rootCluster the root cluster
	 * @param order       the order
	 */
	private static void newIndices(Cluster rootCluster, int[] order) {
		if (rootCluster == null) {
			return;
		}

		Deque<Cluster> stack = new ArrayDeque<Cluster>();

		stack.push(rootCluster);

		int index = 0;

		while (stack.size() > 0) {
			Cluster cluster = stack.pop();

			if (cluster.isParent()) {
				stack.push(cluster.getChild2());
				stack.push(cluster.getChild1());
			} else {
				// end of the line
				// if a cluster has no children, it
				// is a leaf and has one child

				order[index] = cluster.getId();

				// System.err.println("order " + index + " " + order[index]);

				++index;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.heatmap.HeatMapPlotElement#
	 * drawMatrix(java.awt.Graphics2D)
	 */
	@Override
	protected void drawMatrix(Graphics2D g2, DrawingContext context) {
		int y = 0;

		if (context == DrawingContext.UI) {
			for (int i = 0; i < mMatrix.getRows(); ++i) {
				int x = 0;

				for (int j = 0; j < mMatrix.getCols(); ++j) {
					double v = mMatrix.getValue(rowOrder[i], columnOrder[j]);

					g2.drawImage(cacheCell(mColorMap.getColor(v)), x, y, null);

					x += mBlockSize.getW();
				}

				y += mBlockSize.getH();
			}
		} else {
			for (int i = 0; i < mMatrix.getRows(); ++i) {
				int x = 0;

				for (int j = 0; j < mMatrix.getCols(); ++j) {
					double v = mMatrix.getValue(rowOrder[i], columnOrder[j]);

					g2.setColor(mColorMap.getColor(v));
					g2.fillRect(x, y, mBlockSize.getW(), mBlockSize.getH());

					x += mBlockSize.getW();
				}

				y += mBlockSize.getH();
			}
		}
	}

}
