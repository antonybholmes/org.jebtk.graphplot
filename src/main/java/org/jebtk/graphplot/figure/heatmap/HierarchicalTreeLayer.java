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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jebtk.core.settings.SettingsService;
import org.jebtk.graphplot.figure.PlotLayer;
import org.jebtk.math.cluster.Cluster;

/**
 * The class RowHierarchicalTreeLayer.
 */
public abstract class HierarchicalTreeLayer extends PlotLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** The Constant GAP. */
	protected static final int GAP = SettingsService.getInstance().getInt("graphplot.cluster.tree.gap");

	/** The Constant GROUP_GAP. */
	protected static final int GROUP_GAP = SettingsService.getInstance().getInt("graphplot.cluster.tree.groups.gap");

	/**
	 * The member root cluster.
	 */
	protected Cluster mRootCluster;

	/**
	 * The member color.
	 */
	protected Color mColor;

	/** The m cluster offset map. */
	protected Map<Cluster, Double> mClusterOffsetMap = new HashMap<Cluster, Double>();

	/** The m cluster dist map. */
	protected Map<Cluster, Double> mClusterDistMap = new HashMap<Cluster, Double>();

	/** The m parents. */
	protected List<Cluster> mParents = new ArrayList<Cluster>();

	/**
	 * Instantiates a new row hierarchical tree layer.
	 *
	 * @param name        the name
	 * @param rootCluster the root cluster
	 * @param color       the color
	 */
	public HierarchicalTreeLayer(Cluster rootCluster, Color color) {
		mRootCluster = rootCluster;
		mColor = color;

		setup();
	}

	@Override
	public String getType() {
		return "Hierarchical Tree Layer";
	}

	/**
	 * Setup.
	 */
	private void setup() {
		// If user doesn't opt to cluster by rows or columns, the root
		// cluster will be null
		if (mRootCluster == null) {
			return;
		}

		Deque<Cluster> stack = new ArrayDeque<Cluster>();

		stack.push(mRootCluster);

		double x = 0;

		while (!stack.isEmpty()) {
			Cluster cluster = stack.pop();

			double d = cluster.getLevel() / mRootCluster.getLevel();

			mClusterDistMap.put(cluster, d);

			if (cluster.isParent()) {
				mParents.add(cluster);

				stack.push(cluster.getChild2());
				stack.push(cluster.getChild1());

				mClusterOffsetMap.put(cluster, -1.0);
			} else {
				// Since we encounter the leaf clusters in the order
				// they appear in the tree, we can store their
				// x position in order
				mClusterOffsetMap.put(cluster, x++);
			}
		}

		// The list must be reversed so that parents closest to the leaves
		// are updated first so that branches further up the tree get the
		// corrected offsets rather than -1
		Collections.reverse(mParents);

		for (Cluster cluster : mParents) {

			Cluster c1 = cluster.getChild1();
			Cluster c2 = cluster.getChild2();

			double x1 = mClusterOffsetMap.get(c1);
			double x2 = mClusterOffsetMap.get(c2);

			double midX = (x1 + x2) / 2.0;

			// Update the mid point of this cluster

			mClusterOffsetMap.put(cluster, midX);
		}
	}
}
