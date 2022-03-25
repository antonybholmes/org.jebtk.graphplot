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
import org.jebtk.graphplot.figure.heatmap.legacy.RowMatrixPlotElement;
import org.jebtk.math.cluster.Cluster;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The class RowHierarchicalTreePlotElement.
 */
public class RowHTreeLeftPlotElement extends RowMatrixPlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member root cluster.
	 */
	protected Cluster mRootCluster;

	/**
	 * The member color.
	 */
	protected Color mColor;

	/**
	 * Instantiates a new row hierarchical tree plot element.
	 *
	 * @param matrix      the matrix
	 * @param width       the width
	 * @param aspectRatio the aspect ratio
	 * @param rootCluster the root cluster
	 * @param color       the color
	 */
	public RowHTreeLeftPlotElement(DataFrame matrix, int width, DoubleDim aspectRatio, Cluster rootCluster,
			Color color) {
		super(matrix, aspectRatio, width);

		mColor = color;
		mRootCluster = rootCluster;
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

			x1 = w - (int) (s1 * w);

			// vertial line
			g2.drawLine(x1, y1, x1, y2);

			// horizontal lines

			s2 = c1.getLevel() / mRootCluster.getLevel();

			x2 = w - (int) (s2 * w);

			g2.drawLine(x1, y1, x2, y1);

			s2 = c2.getLevel() / mRootCluster.getLevel();

			x2 = w - (int) (s2 * w);

			g2.drawLine(x1, y2, x2, y2);
		}

		/*
		 * Deque<Cluster> stack = new ArrayDeque<Cluster>(); //Deque<Cluster>
		 * parentStack = new ArrayDeque<Cluster>(); Deque<Integer> offsetStack = new
		 * ArrayDeque<Integer>();
		 * 
		 * stack.push(rootCluster); offsetStack.push(0);
		 * 
		 * int w = getPreferredSize().width;
		 * 
		 * while (stack.size() > 0) { Cluster cluster = stack.pop();
		 * 
		 * int offset1 = offsetStack.pop();
		 * 
		 * if (!cluster.isParent()) { continue; }
		 * 
		 * 
		 * Cluster c1 = cluster.getChild1(); Cluster c2 = cluster.getChild2();
		 * 
		 * //System.err.println(cluster.getId() + " child " + c1.getId() + " " +
		 * c2.getId());
		 * 
		 * int height1 = c1.getCumulativeChildCount() * blockSize.height; int height2 =
		 * c2.getCumulativeChildCount() * blockSize.height;
		 * 
		 * int offset2 = offset1 + height1;
		 * 
		 * int m1 = height1 / 2; int m2 = height2 / 2;
		 * 
		 * int y1 = offset1 + m1; int y2 = offset2 + m2;
		 * 
		 * 
		 * 
		 * double s = cluster.getLevel() / rootCluster.getLevel();
		 * 
		 * 
		 * 
		 * int x = w - (int)(s * w);
		 * 
		 * g2.drawLine(x, y1, x, y2);
		 * 
		 * // horizontal lines
		 * 
		 * s = c1.getLevel() / rootCluster.getLevel();
		 * 
		 * int x2 = w - (int)(s * w);
		 * 
		 * g2.drawLine(x, y1, x2, y1);
		 * 
		 * s = c2.getLevel() / rootCluster.getLevel();
		 * 
		 * x2 = w - (int)(s * w);
		 * 
		 * g2.drawLine(x, y2, x2, y2);
		 * 
		 * stack.push(c2); offsetStack.push(offset2);
		 * 
		 * stack.push(c1); offsetStack.push(offset1); }
		 */
	}
}
