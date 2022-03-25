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
import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jebtk.core.geom.DoubleDim;
import org.jebtk.math.cluster.Cluster;
import org.jebtk.math.matrix.DataFrame;

/**
 * The class RowHierarchicalTreePlotElement.
 */
public class RowHTreeRightPlotElement extends RowHTreeLeftPlotElement {
	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new row H tree right plot element.
	 *
	 * @param matrix      the matrix
	 * @param width       the width
	 * @param aspectRatio the aspect ratio
	 * @param rootCluster the root cluster
	 * @param color       the color
	 */
	public RowHTreeRightPlotElement(DataFrame matrix, int width, DoubleDim aspectRatio, Cluster rootCluster,
			Color color) {
		super(matrix, width, aspectRatio, rootCluster, color);
	}

	/**
	 * Draw tree.
	 *
	 * @param g2 the g2
	 */
	protected void drawTree(Graphics2D g2) {
		if (mRootCluster == null) {
			return;
		}

		g2.setColor(mColor);

		Deque<Cluster> stack = new ArrayDeque<Cluster>();
		List<Cluster> parents = new ArrayList<Cluster>();

		stack.push(mRootCluster);

		Map<Integer, Integer> offsetMap = new HashMap<Integer, Integer>();

		int y = (int) (mBlockSize.getH() / 2);

		while (!stack.isEmpty()) {
			Cluster cluster = stack.pop();

			if (cluster.isParent()) {
				parents.add(cluster);

				stack.push(cluster.getChild2());
				stack.push(cluster.getChild1());

				offsetMap.put(cluster.getId(), -1);
			} else {
				// Since we encounter the leaf clusters in the order
				// they appear in the tree, we can store their
				// x position in order
				offsetMap.put(cluster.getId(), y);

				y += mBlockSize.getH();
			}
		}

		// Reverse the list since the first cluster is at the
		// end
		Collections.reverse(parents);

		int w = getPreferredSize().width;
		int x1;
		int x2;
		int y1;
		int y2;
		int midY;
		double s1;
		double s2;

		// process all the parents
		for (Cluster cluster : parents) {

			// a pair
			// Cluster cluster = parentStack.pop();
			Cluster c1 = cluster.getChild1();
			Cluster c2 = cluster.getChild2();

			y1 = offsetMap.get(c1.getId());
			y2 = offsetMap.get(c2.getId());

			midY = (y1 + y2) / 2;

			// Update the mid point of this cluster

			offsetMap.put(cluster.getId(), midY);

			// now do some drawing

			s1 = cluster.getLevel() / mRootCluster.getLevel();

			x1 = (int) (s1 * w);

			// vertial line
			g2.drawLine(x1, y1, x1, y2);

			// horizontal lines

			s2 = c1.getLevel() / mRootCluster.getLevel();

			x2 = (int) (s2 * w);

			g2.drawLine(x1, y1, x2, y1);

			s2 = c2.getLevel() / mRootCluster.getLevel();

			x2 = (int) (s2 * w);

			g2.drawLine(x1, y2, x2, y2);
		}
	}
}
