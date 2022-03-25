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

import org.jebtk.core.Props;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.math.cluster.Cluster;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The class RowHierarchicalTreeLayer.
 */
public class RowHierarchicalTreeLayer extends HierarchicalTreeLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	private int mWidth;

	/**
	 * Instantiates a new row hierarchical tree layer.
	 *
	 * @param rootCluster the root cluster
	 * @param color       the color
	 */
	public RowHierarchicalTreeLayer(Cluster rootCluster, Color color, int width) {
		super(rootCluster, color);

		mWidth = width;
	}

	@Override
	public String getType() {
		return "Row Hierarchical Tree Layer";
	}

	@Override
	public void plotSize(Dimension d) {
		d.width = mWidth;
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
	public void plot(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
		Axes axes = (Axes) params.get("axes");

		g2.setColor(mColor);

		int w = axes.toPlotY1(0) - axes.toPlotY1(1);
		int h = axes.getMargins().getLeft() - GAP;

		int x1;
		int x2;
		int y1;
		int y2;
		double s1;
		double s2;

		int o = w / 2;

		// process all the parents
		for (Cluster cluster : mParents) {

			// a pair
			// Cluster cluster = parentStack.pop();
			Cluster c1 = cluster.getChild1();
			Cluster c2 = cluster.getChild2();

			y1 = (int) (mClusterOffsetMap.get(c1.getId()) * w + o);
			y2 = (int) (mClusterOffsetMap.get(c2.getId()) * w + o);

			// now do some drawing

			s1 = mClusterDistMap.get(cluster);

			x1 = h - (int) (s1 * h);

			// vertial line
			g2.drawLine(x1, y1, x1, y2);

			// horizontal lines

			s2 = mClusterDistMap.get(c1);

			x2 = h - (int) (s2 * h) - 1;

			g2.drawLine(x1, y1, x2, y1);

			s2 = mClusterDistMap.get(c2);

			x2 = h - (int) (s2 * h) - 1;

			g2.drawLine(x1, y2, x2, y2);
		}
	}
}
