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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;

import org.jebtk.core.Mathematics;
import org.jebtk.core.collections.UniqueArrayList;
import org.jebtk.graphplot.Image;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.Plot;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.math.cluster.Cluster;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The class ClusterHeatMapFillPlotLayer.
 */
public class ClusterHeatMapFillPlotLayer extends HeatMapFillPlotLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member row order.
	 */
	private int[] mRowOrder = null;

	/**
	 * The member column order.
	 */
	private int[] mColumnOrder = null;

	/**
	 * The member row cluster.
	 */
	private Cluster mRowCluster;

	/**
	 * The member column cluster.
	 */
	private Cluster mColumnCluster;

	/**
	 * Instantiates a new cluster heat map fill plot layer.
	 *
	 * @param rowCluster    the row cluster
	 * @param columnCluster the column cluster
	 */
	public ClusterHeatMapFillPlotLayer(Cluster rowCluster, Cluster columnCluster) {
		mRowCluster = rowCluster;
		mColumnCluster = columnCluster;
	}

	@Override
	public String getType() {
		return "Cluster Heat Map Layer";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.graphplot.figure.heatmap.HeatMapFillPlotLayer#cache(org.abh.common.ui.
	 * graphics.DrawingContext, org.graphplot.figure.SubFigure,
	 * org.graphplot.figure.Axes, org.graphplot.figure.Plot,
	 * org.abh.common.math.matrix.DataFrame, int, int, int, int)
	 */
	@Override
	protected void cache(DrawingContext context, Figure figure, SubFigure subFigure, Axes axes, Plot plot, DataFrame m,
			int x1, int y1, int w, int h) {
		if (mHashId == null || !mHashId.equals(subFigure.hashId())) {
			newIndices(m, axes, x1, y1);

			mX = new UniqueArrayList<Integer>(m.getCols());

			for (int i = 0; i < m.getCols(); ++i) {
				int x = axes.toPlotX1(mColumnOrder[i]) - x1;

				mX.add(x);
			}

			mY = new UniqueArrayList<Integer>(m.getRows());

			for (int i = 0; i < m.getRows(); ++i) {
				int y = axes.toPlotY1(mRowOrder[m.getRows() - i - 1]) - y1;

				System.err.println("add y " + i + " " + y + " " + y1 + " " + axes.getInternalSize() + " "
						+ axes.toPlotY1(m.getRows() - 1) + " " + axes.toPlotY1(0));

				mY.add(y);
			}
		}

		// Monitor for changes in the color plot and the normalization method
		// which will cause the matrix to change

		if (mColorMap == null || !plot.getColorMap().equals(mColorMap) || mM == null || !m.equals(mM) || mHashId == null
				|| !mHashId.equals(subFigure.hashId())) {

			mColorsMap.clear();
			mColorTileMap.clear();

			for (int i = 0; i < m.getRows(); ++i) {
				int y = axes.toPlotY1(m.getRows() - i) - y1;

				for (int j = 0; j < m.getCols(); ++j) {
					int x = axes.toPlotX1(j) - x1;

					if (mColorsMap.get(x).containsKey(y)) {
						continue;
					}

					double v = m.getValue(i, j);

					Color c;

					if (Mathematics.isValidNumber(v)) {
						c = plot.getColorMap().getColor(v);
					} else {
						c = Color.WHITE;
					}

					mColorsMap.get(x).put(y, c);

					if (context == DrawingContext.UI) {
						if (!mColorTileMap.containsKey(c)) {
							BufferedImage img = Image.createBuffIm(w, h);

							Graphics2D g2Temp = (Graphics2D) img.createGraphics();
							g2Temp.setColor(c);
							g2Temp.fillRect(0, 0, img.getWidth(), img.getHeight());
							g2Temp.dispose();

							mColorTileMap.put(c, img);
						}
					}
				}
			}
		}

		mColorMap = plot.getColorMap();
		mM = m;
		mHashId = subFigure.hashId();
	}

	/**
	 * Cache the row and column order.
	 *
	 * @param m    the m
	 * @param axes the axes
	 * @param x1   the x 1
	 * @param y1   the y 1
	 */
	private void newIndices(final DataFrame m, final Axes axes, int x1, int y1) {
		mRowOrder = new int[m.getRows()];
		mColumnOrder = new int[m.getCols()];

		for (int i = 0; i < mRowOrder.length; ++i) {
			mRowOrder[i] = i;
		}

		newIndices(mRowCluster, mRowOrder);

		for (int i = 0; i < mColumnOrder.length; ++i) {
			mColumnOrder[i] = i;
		}

		newIndices(mColumnCluster, mColumnOrder);
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

				++index;
			}
		}
	}
}
