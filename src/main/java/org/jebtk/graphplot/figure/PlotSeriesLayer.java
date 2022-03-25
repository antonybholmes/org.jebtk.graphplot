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

import java.awt.Graphics2D;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Draw a specific series.
 * 
 * @author Antony Holmes
 */
public abstract class PlotSeriesLayer extends PlotClippedLayer implements ChangeListener {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member series.
	 */
	private String mSeriesName;

	/** The m series. */
	private XYSeries mSeries;

	/**
	 * Instantiates a new plot series layer.
	 *
	 * @param name   the name
	 * @param series the group
	 */
	public PlotSeriesLayer(String series) {
		setSeries(series);
	}

	/**
	 * Sets the series.
	 *
	 * @param series the new series
	 */
	public void setSeries(String series) {
		mSeriesName = series;

		mSeries = null;

		fireChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.PlotClippedLayer#plotLayer(
	 * java.awt.Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Figure,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Axes,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Plot,
	 * org.abh.lib.math.matrix.DataFrame)
	 */
	@Override
	public final void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes,
			Plot plot, DataFrame m) {

		if (m == null) {
			return;
		}

		if (mSeries == null) {
			mSeries = plot.getAllSeries().get(mSeriesName);
		}

		plotLayer(g2, context, figure, subFigure, axes, plot, m, mSeries);
	}

	/**
	 * Generate a plot given an XYSeries and the colums in the matrix that the
	 * column refers to.
	 *
	 * @param g2      the g2
	 * @param context the context
	 * @param figure  the figure
	 * @param axes    the axes
	 * @param plot    the plot
	 * @param m       the m
	 * @param series  the series
	 */
	public abstract void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes,
			Plot plot, DataFrame m, XYSeries series);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
	 */
	@Override
	public void changed(ChangeEvent e) {
		fireChanged();
	}
}
