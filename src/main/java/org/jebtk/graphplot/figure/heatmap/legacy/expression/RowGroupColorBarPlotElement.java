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
import org.jebtk.graphplot.figure.heatmap.legacy.GroupProps;
import org.jebtk.graphplot.figure.heatmap.legacy.RowMatrixPlotElement;
import org.jebtk.graphplot.figure.series.XYSeriesGroup;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The class RowGroupColorBarPlotElement.
 */
public class RowGroupColorBarPlotElement extends RowMatrixPlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	private static final int WIDTH = SettingsService.getInstance().getInt("graphplot.plot.group.block-size");

	/**
	 * The member map.
	 */
	private Map<Integer, XYSeriesGroup> mMap;

	/**
	 * Instantiates a new row group color bar plot element.
	 *
	 * @param m           the m
	 * @param groups      the groups
	 * @param width       the width
	 * @param aspectRatio the aspect ratio
	 */
	public RowGroupColorBarPlotElement(DataFrame matrix, DoubleDim aspectRatio, XYSeriesGroup groups,
			GroupProps properties) {
		super(matrix, aspectRatio, WIDTH);

		mMap = XYSeriesGroup.arrangeGroupsByIndex(matrix, groups);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.ModernPlotCanvas#plot(java.awt.
	 * Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext)
	 */
	@Override
	public void plot(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
		int x = 0;
		int y = 0;

		for (int i : mMap.keySet()) {
			System.err.println("row g " + i + " " + mMap.get(i));

			XYSeriesGroup groups = mMap.get(i);

			y = i * mBlockSize.getH();

			g2.setColor(groups.get(0).getColor());

			g2.fillRect(x, y, getPreferredSize().width, mBlockSize.getH());

			// y += blockSize.height;
		}

		super.plot(g2, offset, context, params);
	}
}
