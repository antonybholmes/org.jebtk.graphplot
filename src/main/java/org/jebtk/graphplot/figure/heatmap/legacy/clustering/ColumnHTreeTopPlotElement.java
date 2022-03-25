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
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jebtk.core.Props;
import org.jebtk.core.geom.DoubleDim;
import org.jebtk.graphplot.figure.heatmap.legacy.ColumnMatrixPlotElement;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.math.cluster.Cluster;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Draws a column based dendrogram.
 */
public class ColumnHTreeTopPlotElement extends ColumnMatrixPlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member root cluster.
	 */
	private Cluster mRootCluster;

	/**
	 * The member color.
	 */
	private Color mColor;

	/** The m color leaf. */
	private boolean mColorLeaf;

	/** The m group map. */
	private Map<Integer, XYSeriesGroup> mGroupMap;

	/** The m offset map. */
	private Map<Integer, Integer> mOffsetMap;

	/** The m order map. */
	private Map<Integer, Integer> mOrderMap;

	/** The m parents. */
	private List<Cluster> mParents;

	/** The m Y 1 map. */
	private Map<Integer, Integer> mY1Map;

	/** The m Y 2 map. */
	private Map<Integer, Integer> mY2Map;

	/** The m Y map. */
	private Map<Integer, Integer> mYMap;

	/** The m ordered cluster array. */
	private ArrayList<Integer> mOrderedClusterArray;

	/**
	 * Instantiates a new column hierarchical tree plot element.
	 *
	 * @param matrix      the matrix
	 * @param aspectRatio the aspect ratio
	 * @param height      the height
	 * @param rootCluster the root cluster
	 * @param color       the color
	 */
	public ColumnHTreeTopPlotElement(DataFrame matrix, DoubleDim aspectRatio, int height, Cluster rootCluster,
			Color color) {
		super(matrix, aspectRatio, height);

		mRootCluster = rootCluster;
		mColor = color;
	}

	/**
	 * Instantiates a new column H tree top plot element.
	 *
	 * @param matrix      the matrix
	 * @param groups      the groups
	 * @param aspectRatio the aspect ratio
	 * @param height      the height
	 * @param rootCluster the root cluster
	 * @param Props       the properties
	 */
	public ColumnHTreeTopPlotElement(DataFrame matrix, XYSeriesGroup groups, DoubleDim aspectRatio, int height,
			Cluster rootCluster, Props properties) {
		super(matrix, aspectRatio, height);

		mRootCluster = rootCluster;
		mColor = properties.getColor("plot.tree.vert.color");
		mColorLeaf = properties.getBool("plot.tree.vert.leaf.color");

		// map indices to the groups occupying them
		mGroupMap = XYSeriesGroup.arrangeGroupsByIndex(matrix, groups);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.ModernPlotCanvas#plot(java.awt.
	 * Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext)
	 */
	@Override
	public void plot(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
		drawTree(g2);

		super.plot(g2, offset, context, params);
	}

	/**
	 * Draw tree.
	 *
	 * @param g2 the g2
	 */
	private void drawTree(Graphics2D g2) {
		if (mRootCluster == null) {
			return;
		}

		int h = getPreferredSize().height;
		int x1;
		int x2;
		int y1;
		int y2;
		double s1;
		int id;
		int id1;
		int id2;

		if (mParents == null) {
			mParents = new ArrayList<Cluster>(mMatrix.getCols());
			mOffsetMap = new HashMap<Integer, Integer>();
			mOrderMap = new HashMap<Integer, Integer>();

			Deque<Cluster> stack = new ArrayDeque<Cluster>();

			stack.push(mRootCluster);

			int x = (int) (mBlockSize.getW() / 2);

			int order = 0;

			while (!stack.isEmpty()) {
				Cluster cluster = stack.pop();

				if (cluster.isParent()) {
					mParents.add(cluster);

					stack.push(cluster.getChild2());
					stack.push(cluster.getChild1());

					mOffsetMap.put(cluster.getId(), -1);
				} else {
					// Since we encounter the leaf clusters in the order
					// they appear in the tree, we can store their
					// x position in order
					mOffsetMap.put(cluster.getId(), x);

					// Since the plotted matrix is re-ordered to match the
					// clustering. We need to map the cluster id to where it
					// is on the matrix so that we can pick the right color
					mOrderMap.put(cluster.getId(), order++);

					// offsetMap.put(cluster.getId(),
					// x + cluster.getId() * mBlockSize.getW());

					x += mBlockSize.getW();
				}
			}

			// Reverse the list so that the first cluster (the root) is at
			// the end, since we must update and correct the sub nodes of
			// a node before we can render a node.
			Collections.reverse(mParents);

			// Update the parents location using the midpoint of the children
			for (Cluster c : mParents) {
				Cluster c1 = c.getChild1();
				Cluster c2 = c.getChild2();

				// Update the mid point of this cluster
				x1 = mOffsetMap.get(c1.getId());
				x2 = mOffsetMap.get(c2.getId());

				int midX = (x1 + x2) / 2;
				mOffsetMap.put(c.getId(), midX);
			}

			// Cache the y coordinates

			mYMap = new HashMap<Integer, Integer>();
			mY1Map = new HashMap<Integer, Integer>();
			mY2Map = new HashMap<Integer, Integer>();

			mOrderedClusterArray = new ArrayList<Integer>(mParents.size() * 3);

			for (Cluster c : mParents) {
				Cluster c1 = c.getChild1();
				Cluster c2 = c.getChild2();

				id = c.getId();
				id1 = c1.getId();
				id2 = c2.getId();

				s1 = c.getLevel() / mRootCluster.getLevel();
				y1 = h - (int) (s1 * h);
				mYMap.put(id, y1);

				s1 = c1.getLevel() / mRootCluster.getLevel();
				y1 = h - (int) (s1 * h);
				mY1Map.put(id1, y1);

				s1 = c2.getLevel() / mRootCluster.getLevel();
				y1 = h - (int) (s1 * h);
				mY2Map.put(id2, y1);

				mOrderedClusterArray.add(id);
				mOrderedClusterArray.add(id1);
				mOrderedClusterArray.add(id2);
			}
		}

		// process all the nodes
		for (int i = 0; i < mOrderedClusterArray.size(); i += 3) {
			g2.setColor(mColor);

			id = mOrderedClusterArray.get(i);
			id1 = mOrderedClusterArray.get(i + 1);
			id2 = mOrderedClusterArray.get(i + 2);

			x1 = mOffsetMap.get(id1);
			x2 = mOffsetMap.get(id2);

			// now do some drawing

			y1 = mYMap.get(id);

			// horizontal line
			g2.drawLine(x1, y1, x2, y1);

			//
			// Vertical lines
			//

			// c1

			if (mColorLeaf && mGroupMap.containsKey(id1)) {
				// System.err.println("C1 " + c1.getId() + " " +
				// mOrderMap.get(c1.getId()) + " " + mMatrix.getColumnName(c1.getId()) +
				// " " + mGroupMap.get(c1.getId()).get(0).getName() + " " + x1);
				// Get the first group on the index and use its color
				g2.setColor(mGroupMap.get(mOrderMap.get(id1)).get(0).getColor());
			} else {
				g2.setColor(mColor);
			}

			y2 = mY1Map.get(id1);
			g2.drawLine(x1, y1, x1, y2);

			// c2

			if (mColorLeaf && mGroupMap.containsKey(id2)) {
				g2.setColor(mGroupMap.get(mOrderMap.get(id2)).get(0).getColor());
			} else {
				g2.setColor(mColor);
			}

			y2 = mY2Map.get(id2);
			g2.drawLine(x2, y1, x2, y2);
		}
	}
}
