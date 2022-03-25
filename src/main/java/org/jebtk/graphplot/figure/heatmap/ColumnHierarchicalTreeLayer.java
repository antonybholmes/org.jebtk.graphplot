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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Map;

import org.jebtk.core.Props;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.Plot;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.math.cluster.Cluster;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The class ColumnHierarchicalTreeLayer.
 */
public class ColumnHierarchicalTreeLayer extends HierarchicalTreeLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The max rows.
	 */
	protected int mMaxRows = -1;

	/**
	 * The member group map.
	 */
	protected Map<Integer, XYSeriesGroup> mGroupMap = null;

	private int mWidth;

	/**
	 * Instantiates a new column hierarchical tree layer.
	 *
	 * @param rootCluster the root cluster
	 * @param color       the color
	 */
	public ColumnHierarchicalTreeLayer(Cluster rootCluster, Color color, int width) {
		super(rootCluster, color);

		mWidth = width;
	}

	@Override
	public String getType() {
		return "Column Tree Layer";
	}

	@Override
	public void plotSize(Dimension d) {
		d.height += mWidth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.columbia.rdf.lib.bioinformatics.plot.subFigure.PlotLayer#plot(java.awt.
	 * Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext,
	 * edu.columbia.rdf.lib.bioinformatics.plot.subFigure.Figure,
	 * edu.columbia.rdf.lib.bioinformatics.plot.subFigure.Axes,
	 * edu.columbia.rdf.lib.bioinformatics.plot.subFigure.Plot,
	 * org.abh.lib.math.matrix.DataFrame)
	 */
	@Override
	public void plot(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
		Figure figure = (Figure) params.get("fig");
		SubFigure subFigure = (SubFigure) params.get("subfig");
		Axes axes = (Axes) params.get("axes");
		Plot plot = (Plot) params.get("plot");

		DataFrame m = (DataFrame) params.get("matrix");

		if (m == null) {
			m = plot.getMatrix();
		}

		if (m == null) {
			return;
		}

		maxRows(plot, m);

		/*
		 * plotColorBars(g2, context, axes, plot, m);
		 */

		plotTree(g2, context, figure, subFigure, axes, plot, m);

		offset.height += mWidth;
	}

	/**
	 * Plot tree.
	 *
	 * @param g2        the g 2
	 * @param context   the context
	 * @param subFigure the subFigure
	 * @param axes      the axes
	 * @param plot      the plot
	 * @param m         the m
	 */
	public void plotTree(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes,
			Plot plot, DataFrame m) {

		g2.setColor(mColor);

		int w = axes.toPlotX1(1) - axes.toPlotX1(0);

		int h = plot.getPreferredSize().height - GAP - mMaxRows * GroupColorBarLayer.HEIGHT - GROUP_GAP;

		int x1;
		int x2;
		int y1;
		int y2;
		double s1;
		double s2;

		int offset = w / 2; // axes.getMargins().getLeft();

		// process all the parents
		for (Cluster cluster : mParents) {
			// a pair
			// Cluster cluster = parentStack.pop();
			Cluster c1 = cluster.getChild1();
			Cluster c2 = cluster.getChild2();

			x1 = (int) (mClusterOffsetMap.get(c1) * w + offset);
			x2 = (int) (mClusterOffsetMap.get(c2) * w + offset);

			// now do some drawing

			s1 = mClusterDistMap.get(cluster);

			y1 = h - (int) (s1 * h);

			// horizontal line
			g2.drawLine(x1, y1, x2, y1);

			// vertical lines

			s2 = mClusterDistMap.get(c1);

			y2 = h - (int) (s2 * h) - 1;

			g2.drawLine(x1, y1, x1, y2);

			s2 = mClusterDistMap.get(c2);

			y2 = h - (int) (s2 * h) - 1;

			g2.drawLine(x2, y1, x2, y2);
		}
	}

	/**
	 * Max rows.
	 *
	 * @param plot the plot
	 * @param m    the m
	 */
	public void maxRows(Plot plot, DataFrame m) {
		if (mGroupMap == null) {
			mGroupMap = XYSeriesGroup.arrangeGroupsByCluster(m, plot.getAllSeries(), mRootCluster);

			for (int key : mGroupMap.keySet()) {
				mMaxRows = Math.max(mGroupMap.get(key).getCount(), mMaxRows);
			}
		}
	}

	/*
	 * public void plotColorBars(Graphics2D g2, DrawingContext context, Axes axes,
	 * Plot plot, DataFrame m) {
	 * 
	 * int w = axes.toPlotX1(1) - axes.toPlotX1(0);
	 * 
	 * int x = w / 2;//axes.getMargins().getLeft(); int y = 0;
	 * 
	 * 
	 * 
	 * int yoffset = plot.getInternalPlotSize().getH() - GroupColorBarLayer.HEIGHT *
	 * mMaxRows - GAP;
	 * 
	 * for (int column = 0; column < m.getColumnCount(); ++column) { y = yoffset;
	 * 
	 * for (int r = 0; r < mMaxRows; ++r) { if (!mGroupMap.containsKey(column)) {
	 * break; }
	 * 
	 * if (r == mGroupMap.get(column).getCount()) { // If there are not enough
	 * groups in this // index, go onto the next index. break; }
	 * 
	 * g2.setColor(mGroupMap.get(column).get(r).getColor());
	 * 
	 * g2.fillRect(x, y, w, GroupColorBarLayer.HEIGHT);
	 * 
	 * //if (mProps.showGrid) { // g2.setColor(mProps.gridColor); // g2.drawRect(x,
	 * y, w, h2); //}
	 * 
	 * y += GroupColorBarLayer.HEIGHT; }
	 * 
	 * x += w; } }
	 */
}
