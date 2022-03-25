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
import java.util.List;

import org.jebtk.core.Props;
import org.jebtk.core.geom.DoubleDim;
import org.jebtk.graphplot.figure.heatmap.legacy.MatrixPlotElement;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.math.cluster.Cluster;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The class GroupsHierarchicalPlotElement.
 */
public class GroupsHierarchicalPlotElement extends MatrixPlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member groups.
	 */
	private XYSeriesGroup mGroups;

	/**
	 * The member block.
	 */
	private int mBlock = 16;

	/**
	 * The member height.
	 */
	private int mHeight;

	/**
	 * The member width.
	 */
	private int mWidth;

	/**
	 * Instantiates a new groups hierarchical plot element.
	 *
	 * @param matrix      the matrix
	 * @param aspectRatio the aspect ratio
	 * @param width       the width
	 * @param rootCluster the root cluster
	 * @param groups      the groups
	 */
	public GroupsHierarchicalPlotElement(DataFrame matrix, DoubleDim aspectRatio, int width, Cluster rootCluster,
			XYSeriesGroup groups) {
		super(matrix, aspectRatio);

		mWidth = width;

		if (groups == null) {
			return;
		}

		mHeight = 0;

		mGroups = XYSeriesGroup.orderGroups(XYSeriesGroup.arrangeGroupsByCluster(matrix, groups, rootCluster));

		if (mGroups == null) {
			return;
		}

		List<List<Integer>> indices = XYSeriesGroup.findColumnIndices(matrix, groups);

		for (List<Integer> group : indices) {
			System.err.println("g test " + group);

			if (group.size() == 0) {
				continue;
			}

			mHeight += mBlock + mBlock / 2;
		}

		// Collections.sort(this.groups);
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
		if (mGroups == null || mGroups.getCount() == 0) {
			return;
		}

		int x = 0;
		int xt = 2 * mBlock;
		int y = 0;
		int yt = (mBlock + g2.getFontMetrics().getAscent()) / 2;

		// try to order groups so they are listed in the order
		// they are used on the plot

		for (XYSeries group : mGroups) {
			g2.setColor(group.getColor());

			g2.fillRect(x, y, mBlock, mBlock);

			g2.setColor(Color.BLACK);

			g2.drawRect(x, y, mBlock, mBlock);

			g2.drawString(group.getName(), xt, yt);

			// increase with gap between samples
			y += mBlock + mBlock / 2;
			yt += mBlock + mBlock / 2;
		}
	}

	@Override
	public void plotSize(Dimension d) {
		d.width += mWidth;
		d.height += mHeight;
	}
}
