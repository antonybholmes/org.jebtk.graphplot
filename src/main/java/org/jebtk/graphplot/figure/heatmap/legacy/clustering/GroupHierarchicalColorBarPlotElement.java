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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Map;

import org.jebtk.core.Props;
import org.jebtk.core.geom.DoubleDim;
import org.jebtk.graphplot.figure.heatmap.legacy.ColumnMatrixPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.GroupProps;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.math.cluster.Cluster;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The class GroupHierarchicalColorBarPlotElement.
 */
public class GroupHierarchicalColorBarPlotElement extends ColumnMatrixPlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member max rows.
	 */
	private int mMaxRows;

	/**
	 * The member group map.
	 */
	private Map<Integer, XYSeriesGroup> mGroupMap;

	/**
	 * The member properties.
	 */
	private GroupProps mProps;

	/** The m gap. */
	private int mGap;

	// private Cluster rootCluster;

	/**
	 * Instantiates a new group hierarchical color bar plot element.
	 *
	 * @param matrix      the matrix
	 * @param aspectRatio the aspect ratio
	 * @param height      the height
	 * @param rootCluster the root cluster
	 * @param groups      the groups
	 * @param Props       the properties
	 */
	public GroupHierarchicalColorBarPlotElement(DataFrame matrix, DoubleDim aspectRatio, int height,
			Cluster rootCluster, XYSeriesGroup groups, GroupProps properties) {
		super(matrix, aspectRatio, height);

		mProps = properties;

		mMaxRows = 0;

		// These indices correspond to the original matrix ordering
		mGroupMap = XYSeriesGroup.arrangeGroupsByCluster(matrix, groups, rootCluster);

		for (int key : mGroupMap.keySet()) {
			mMaxRows = Math.max(mGroupMap.get(key).getCount(), mMaxRows);
		}

		mGap = 0; // mBlockSize.getH() / 2;

		setHeight(mBlockSize.getH() * mMaxRows + mGap * (mMaxRows - 1));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.ModernPlotCanvas#plot(java.awt.
	 * Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext)
	 */
	@Override
	public void plot(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
		drawGroups(g2);

		super.plot(g2, offset, context, params);
	}

	/**
	 * Draw groups.
	 *
	 * @param g2 the g2
	 */
	private void drawGroups(Graphics2D g2) {
		int x = 0;
		int y = 0;
		int h = mBlockSize.getH() + mGap;

		for (int column = 0; column < mMatrix.getCols(); ++column) {
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

				g2.fillRect(x, y, (int) mBlockSize.getW(), (int) mBlockSize.getH());

				if (mProps.showGrid) {
					g2.setColor(mProps.gridColor);
					g2.drawRect(x, y, (int) mBlockSize.getW(), (int) mBlockSize.getH());
				}

				y += h;
			}

			x += mBlockSize.getW();
		}

		/*
		 * for (MatrixGroup group : groups) { if (group.size() == 0) { continue; }
		 * 
		 * for (int i : group) { x = blockSize.width * i;
		 * 
		 * g2.setColor(group.getColor());
		 * 
		 * g2.fillRect(x, y, blockSize.width, getPreferredSize().height);
		 * 
		 * g2.setColor(Color.BLACK); g2.drawRect(x, y, blockSize.width,
		 * blockSize.height);
		 * 
		 * //x += blockSize.width; // * i; } }
		 */
	}
}
