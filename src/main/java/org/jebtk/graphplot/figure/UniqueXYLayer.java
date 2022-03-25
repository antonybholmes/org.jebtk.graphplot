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

import org.jebtk.core.KeyValuePair;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Draws a series as set of bars.
 *
 * @author Antony Holmes
 */
public abstract class UniqueXYLayer extends PlotSeriesLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member zero ends.
	 */
	private boolean mZeroEnds;

	/**
	 * The member unique.
	 */
	private KeyValuePair<String, UniqueXY> mUnique = null;

	/**
	 * Instantiates a new unique xy layer.
	 *
	 * @param name   the name
	 * @param series the series
	 */
	public UniqueXYLayer(String series) {
		this(series, false);
	}

	/**
	 * Instantiates a new unique xy layer.
	 *
	 * @param name     the name
	 * @param series   the series
	 * @param zeroEnds the zero ends
	 */
	public UniqueXYLayer(String series, boolean zeroEnds) {
		super(series);

		mZeroEnds = zeroEnds;
	}

	@Override
	public String getType() {
		return "Unique XY Layer";
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
	public final void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes,
			Plot plot, DataFrame m, XYSeries series) {

		String id = getId(m, axes);

		if (mUnique == null || !mUnique.getKey().equals(id)) {
			if (m.getCols() % 2 == 0) {
				// There must be at least two columns in the matrix otherwise
				// there cannot possibly be an x and y column.

				UniqueXY xy = new UniqueXY(m, series, axes, mZeroEnds);

				if (xy.getPointCount() > 0) {
					mUnique = new KeyValuePair<String, UniqueXY>(id, xy);
				}
			}
		}

		if (mUnique != null) {
			plotLayer(g2, context, figure, subFigure, axes, plot, m, series, mUnique.getValue());
		}
	}

	/**
	 * Plot clipped.
	 *
	 * @param g2      the g2
	 * @param context the context
	 * @param figure  the figure
	 * @param axes    the axes
	 * @param plot    the plot
	 * @param m       the m
	 * @param series  the series
	 * @param xy      the xy
	 */
	public abstract void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes,
			Plot plot, DataFrame m, XYSeries series, UniqueXY xy);
}