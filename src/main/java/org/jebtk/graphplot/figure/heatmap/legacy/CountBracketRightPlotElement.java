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
package org.jebtk.graphplot.figure.heatmap.legacy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import org.jebtk.core.Props;
import org.jebtk.core.geom.DoubleDim;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Displays brackets to indicate how many samples are up and down regulated
 * based on their z-score.
 *
 * @author Antony Holmes
 */
public class CountBracketRightPlotElement extends RowMatrixPlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** The m count groups. */
	private CountGroups mCountGroups;

	/** The m color. */
	private Color mColor;

	/**
	 * Instantiates a new count bracket right plot element.
	 *
	 * @param matrix      the matrix
	 * @param countGroups the count groups
	 * @param width       the width
	 * @param aspectRatio the aspect ratio
	 * @param color       the color
	 */
	public CountBracketRightPlotElement(DataFrame matrix, CountGroups countGroups, int width, DoubleDim aspectRatio,
			Color color) {
		super(matrix, aspectRatio, width);
		mColor = color;
		mCountGroups = countGroups;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.ModernPlotCanvas#plot(java.awt.
	 * Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext)
	 */
	@Override
	public void plot(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
		g2.setColor(mColor);

		int y1 = 0;
		int y2;
		int w = getPreferredSize().width;

		// int h = mBlockSize.getH();
		double r = mAspectRatio.getH();

		for (CountGroup countGroup : mCountGroups) {
			y1 = (int) (countGroup.getStart() * r);
			y2 = (int) ((countGroup.getEnd() + 1) * r);

			g2.drawLine(0, y1, w, y1);
			g2.drawLine(w, y1, w, y2);
			g2.drawLine(0, y2, w, y2);
		}

		super.plot(g2, offset, context, params);
	}
}
