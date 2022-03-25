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
package org.jebtk.graphplot.figure.heatmap.legacy.expression;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jebtk.core.Props;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.geom.DoubleDim;
import org.jebtk.graphplot.figure.heatmap.legacy.ColumnMatrixPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.GroupProps;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.MatrixGroup;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The class GroupLabelPlotElement.
 */
public class GroupLabelPlotElement extends ColumnMatrixPlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member groups.
	 */
	private XYSeriesGroup mGroups = null;

	/**
	 * The member row block map.
	 */
	private Map<Integer, List<MatrixGroup>> mRowBlockMap = new HashMap<Integer, List<MatrixGroup>>();

	/**
	 * The member properties.
	 */
	private GroupProps mProps;

	/**
	 * The constant BRACKET_HEIGHT.
	 */
	private static final int BRACKET_HEIGHT = 8;

	/**
	 * Instantiates a new group label plot element.
	 *
	 * @param matrix      the matrix
	 * @param aspectRatio the aspect ratio
	 * @param groups      the groups
	 * @param Props       the properties
	 */
	public GroupLabelPlotElement(DataFrame matrix, DoubleDim aspectRatio, XYSeriesGroup groups, GroupProps properties) {
		super(matrix, aspectRatio, -1);

		mGroups = groups;
		mProps = properties;

		init();
	}

	/**
	 * Inits the.
	 */
	private void init() {
		// Need to marshall the groups into the largest contiguous
		// blocks and draw the labels over the middle of these blocks

		Map<Integer, List<MatrixGroup>> groupMap = new HashMap<Integer, List<MatrixGroup>>();

		// keep track of the max row in use for a given indice
		Map<Integer, Integer> maxRowMap = new HashMap<Integer, Integer>();

		for (MatrixGroup group : mGroups) {
			List<Integer> indices = MatrixGroup.findColumnIndices(mMatrix, group);

			for (int i : indices) {
				if (!groupMap.containsKey(i)) {
					groupMap.put(i, new ArrayList<MatrixGroup>());
				}

				groupMap.get(i).add(group);
			}
		}

		for (MatrixGroup group : mGroups) {
			List<Integer> indices = MatrixGroup.findColumnIndices(mMatrix, group);

			Set<Integer> used = new HashSet<Integer>();

			for (int i : indices) {
				if (used.contains(i)) {
					continue;
				}

				int start = i;
				int end = i;

				while (indices.contains(end)) {
					++end;
				}

				--end;

				// the longest span of columns for this group

				// add the indices for this block

				for (int j = start; j <= end; ++j) {
					used.add(j);
				}

				// what row to add this group at
				int row = 0;

				// check to see if we need to add it to a higher row

				for (int j = start; j <= end; ++j) {
					if (maxRowMap.containsKey(j)) {
						// we need to a new row if this index is already in use
						row = Math.max(row, maxRowMap.get(j) + 1);
					}
				}

				// set the new max for these elements

				for (int j = start; j <= end; ++j) {
					maxRowMap.put(j, row);
				}

				if (!mRowBlockMap.containsKey(row)) {
					mRowBlockMap.put(row, new ArrayList<MatrixGroup>());
				}

				mRowBlockMap.get(row).add(group);
			}
		}

		// set the height based on the number of rows required
		// We need 1 row for the labels and one for the brackets

		setHeight(mBlockSize.getH() * 2 * mRowBlockMap.size());

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
		if (mProps.color == null) {
			return;
		}

		int x = 0;
		int y;
		int y2;
		int y3;
		int width;

		List<Integer> rows = CollectionUtils.sort(mRowBlockMap.keySet());

		for (int row : rows) {
			y = 2 * row * mBlockSize.getH() + mBlockSize.getH();
			y2 = y + mBlockSize.getH() / 2;
			y3 = y2 + BRACKET_HEIGHT;

			for (MatrixGroup group : mRowBlockMap.get(row)) {
				List<Integer> indices = MatrixGroup.findColumnIndices(mMatrix, group);

				if (indices.size() == 0) {
					continue;
				}

				x = indices.get(0) * mBlockSize.getW();
				width = mBlockSize.getW() * indices.size();

				String name = group.getName();

				g2.drawString(name, x + (width - g2.getFontMetrics().stringWidth(name)) / 2, y);

				g2.drawLine(x, y2, x + width, y2);
				g2.drawLine(x, y2, x, y3);
				g2.drawLine(x + width, y2, x + width, y3);
			}
		}

		/*
		 * g2.setColor(color);
		 * 
		 * int width;
		 * 
		 * 
		 * 
		 * for (MatrixGroup group : mGroups) { if (group.size() == 0) { continue; }
		 * 
		 * String name = group.getName();
		 * 
		 * width = blockSize.width * group.size();
		 * 
		 * g2.drawString(name, x + (width - g2.getFontMetrics().stringWidth(name)) / 2,
		 * y);
		 * 
		 * x += width; }
		 */
	}

}
