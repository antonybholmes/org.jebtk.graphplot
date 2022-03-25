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
import java.awt.geom.GeneralPath;
import java.util.List;

import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Fills in line series.
 * 
 * @author Antony Holmes
 *
 */
public abstract class FillPathsPlotLayer extends PathsPlotLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new fill plot layer.
	 *
	 * @param name   the name
	 * @param series the series
	 */
	public FillPathsPlotLayer(String series) {
		super(series);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.figure.PathPlotLayer#plotLayer(
	 * java.awt.Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Figure,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Axes,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Plot,
	 * org.abh.lib.math.matrix.DataFrame,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.series.XYSeries,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.UniqueXY,
	 * java.awt.geom.GeneralPath)
	 */
	@Override
	public void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes,
			Plot plot, DataFrame m, XYSeries series, UniqueXY xy, List<GeneralPath> paths) {

		if (series.getStyle().getFillStyle().getVisible()) {
			g2.setColor(series.getStyle().getFillStyle().getColor());

			for (GeneralPath path : paths) {
				g2.fill(path);
			}
		}
	}
}
