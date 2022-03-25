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
package org.jebtk.graphplot.figure.heatmap;

import java.awt.Dimension;
import java.awt.Graphics2D;

import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.Plot;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.math.cluster.Cluster;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The class GroupHierarchicalColorBarLayer.
 */
public class GroupHierarchicalColorBarLayer extends GroupColorBarLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member root cluster.
	 */
	private Cluster mRootCluster;

	private int mWidth;

	// private Cluster rootCluster;

	/**
	 * Instantiates a new group hierarchical color bar layer.
	 *
	 * @param rootCluster the root cluster
	 */
	public GroupHierarchicalColorBarLayer(Cluster rootCluster, int width) {
		mRootCluster = rootCluster;
		mWidth = width;
	}

	@Override
	public void plotSize(Dimension d) {
		d.height = mWidth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.figure.PlotLayer#plot(java.awt.
	 * Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Figure,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Axes,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Plot,
	 * org.abh.lib.math.matrix.DataFrame)
	 */
	@Override
	public void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes,
			Plot plot, DataFrame m) {

		// List<Integer> columns = MatrixGroup.findColumnIndices(m, series);

		if (mGroupMap == null) {
			mGroupMap = XYSeriesGroup.arrangeGroupsByCluster(m, plot.getAllSeries(), mRootCluster);

			for (int key : mGroupMap.keySet()) {
				mMaxRows = Math.max(mGroupMap.get(key).getCount(), mMaxRows);
			}
		}

		int x = 0; // axes.getMargins().getLeft();
		int y = 0;

		int h = Math.max(1, plot.getPreferredSize().height / mMaxRows);

		int w = axes.toPlotX1(1) - axes.toPlotX1(0);

		for (int column = 0; column < m.getCols(); ++column) {
			y = 0;

			for (int r = 0; r < mMaxRows; ++r) {
				if (!mGroupMap.containsKey(column)) {
					break;
				}

				if (r == mGroupMap.get(column).getCount()) {
					// If there are not enough groups in this
					// index, go onto the next index.
					break;
				}

				g2.setColor(mGroupMap.get(column).get(r).getColor());

				g2.fillRect(x, y, w, h);

				// if (mProps.showGrid) {
				// g2.setColor(mProps.gridColor);
				// g2.drawRect(x, y, w, h2);
				// }

				y += h;
			}

			x += w;
		}
	}
}
