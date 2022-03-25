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
import java.util.Map;

import org.jebtk.core.Props;
import org.jebtk.core.geom.DoubleDim;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.graphplot.figure.heatmap.legacy.ColumnMatrixPlotElement;
import org.jebtk.graphplot.figure.heatmap.legacy.GroupProps;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The class GroupColorBarPlotElement.
 */
public class GroupColorBarPlotElement extends ColumnMatrixPlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** The Constant HEIGHT. */
	private static final int HEIGHT = SettingsService.getInstance().getInt("graphplot.plot.group.block-size");

	/**
	 * The max rows.
	 */
	private int mMaxRows;

	/**
	 * The group map.
	 */
	private Map<Integer, XYSeriesGroup> mGroupMap;

	/**
	 * The member properties.
	 */
	private GroupProps mProps;

	/** The m gap. */
	private int mGap;

	/**
	 * Instantiates a new group color bar plot element.
	 *
	 * @param matrix      the matrix
	 * @param aspectRatio the aspect ratio
	 * @param groups      the groups
	 * @param Props       the properties
	 */
	public GroupColorBarPlotElement(DataFrame matrix, DoubleDim aspectRatio, XYSeriesGroup groups,
			GroupProps properties) {
		super(matrix, aspectRatio, -1);

		mProps = properties;

		mMaxRows = 0;

		mGroupMap = XYSeriesGroup.arrangeGroupsByIndex(matrix, groups);

		for (int key : mGroupMap.keySet()) {
			mMaxRows = Math.max(mGroupMap.get(key).getCount(), mMaxRows);
		}

		mGap = 0; // mBlockSize.getH() / 2;

		setHeight(HEIGHT * mMaxRows + mGap * (mMaxRows - 1));
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
		int w = mBlockSize.w;
		int h = HEIGHT + mGap;

		for (int column = 0; column < mMatrix.getCols(); ++column) {
			y = 0;

			for (int r = 0; r < mMaxRows; ++r) {
				if (!mGroupMap.containsKey(column)) {
					break;
				}

				if (r == mGroupMap.get(column).getCount()) {
					break;
				}

				g2.setColor(mGroupMap.get(column).get(r).getColor());

				g2.fillRect(x, y, w, HEIGHT);

				if (mProps.showGrid) {
					g2.setColor(mProps.gridColor);
					g2.drawRect(x, y, w, HEIGHT);
				}

				y += h;
			}

			x += w;
		}
	}
}
