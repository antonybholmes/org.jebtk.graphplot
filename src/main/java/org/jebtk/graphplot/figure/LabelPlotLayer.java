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
package org.jebtk.graphplot.figure;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Concrete implementation of Graph2dCanvas for generating scatter plots.
 *
 * @author Antony Holmes
 */
public class LabelPlotLayer extends PlotLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** The m label. */
	protected String mLabel;

	/** The m x. */
	protected double mX;

	/** The m y. */
	protected double mY;

	/** The m offset x pixels. */
	protected int mOffsetXPixels;

	/** The m offset y pixels. */
	protected int mOffsetYPixels;

	/** The m center X. */
	private boolean mCenterX;

	/** The m center Y. */
	private boolean mCenterY;

	/** The m color. */
	private Color mColor;

	/**
	 * Instantiates a new scatter plot layer.
	 *
	 * @param name the name
	 * @param x    the x
	 * @param y    the y
	 */
	public LabelPlotLayer(String name, double x, double y) {
		this(name, x, y, 0, 0);
	}

	/**
	 * Instantiates a new label plot layer.
	 *
	 * @param name          the name
	 * @param x             the x
	 * @param y             the y
	 * @param offsetXPixels the offset x pixels
	 * @param offsetYPixels the offset y pixels
	 */
	public LabelPlotLayer(String name, double x, double y, int offsetXPixels, int offsetYPixels) {
		this(name, x, y, false, false, offsetXPixels, offsetYPixels);
	}

	/**
	 * Instantiates a new label plot layer.
	 *
	 * @param name    the name
	 * @param x       the x
	 * @param y       the y
	 * @param centerX the center X
	 * @param centerY the center Y
	 */
	public LabelPlotLayer(String name, double x, double y, boolean centerX, boolean centerY) {
		this(name, x, y, centerX, centerY, 0, 0);
	}

	/**
	 * Instantiates a new label plot layer.
	 *
	 * @param name          the name
	 * @param x             the x
	 * @param y             the y
	 * @param centerX       the center X
	 * @param centerY       the center Y
	 * @param offsetXPixels the offset X pixels
	 * @param offsetYPixels the offset Y pixels
	 */
	public LabelPlotLayer(String name, double x, double y, boolean centerX, boolean centerY, int offsetXPixels,
			int offsetYPixels) {
		this(name, x, y, centerX, centerY, offsetXPixels, offsetYPixels, Color.BLACK);
	}

	/**
	 * Instantiates a new label plot layer.
	 *
	 * @param name          the name
	 * @param x             the x
	 * @param y             the y
	 * @param centerX       the center X
	 * @param centerY       the center Y
	 * @param offsetXPixels the offset X pixels
	 * @param offsetYPixels the offset Y pixels
	 * @param color         the color
	 */
	public LabelPlotLayer(String name, double x, double y, boolean centerX, boolean centerY, int offsetXPixels,
			int offsetYPixels, Color color) {
		mLabel = name;
		mX = x;
		mY = y;
		mCenterX = centerX;
		mCenterY = centerY;
		mOffsetXPixels = offsetXPixels;
		mOffsetYPixels = offsetYPixels;
		mColor = color;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.PlotSeriesLayer#plotLayer(
	 * java.awt.Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Figure,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Axes,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Plot,
	 * org.abh.lib.math.matrix.DataFrame,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.series.XYSeries)
	 */
	@Override
	public void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes,
			Plot plot, DataFrame m) {
		Point p = axes.toPlotX1Y1(mX, mY);

		int x = p.x;
		int y = p.y;

		if (mCenterX) {
			x -= ModernWidget.getStringWidth(g2, mLabel) / 2;
		}

		if (mCenterY) {
			y += ModernWidget.getStringHeight(g2) / 2;
		}

		g2.setColor(mColor);
		g2.drawString(mLabel, x + mOffsetXPixels, y + mOffsetYPixels);
	}
}
