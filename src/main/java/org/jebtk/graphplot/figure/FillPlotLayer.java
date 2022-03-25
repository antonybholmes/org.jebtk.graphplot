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
import java.awt.Point;
import java.awt.geom.GeneralPath;

import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Fills in line series.
 * 
 * @author Antony Holmes
 *
 */
public class FillPlotLayer extends PathPlotLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new fill plot layer.
	 *
	 * @param series the series
	 */
	public FillPlotLayer(String series) {
		super(series);
	}

	@Override
	public String getType() {
		return "Fill Plot Layer";
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
			Plot plot, DataFrame m, XYSeries series, UniqueXY xy, GeneralPath path) {

		/*
		 * if (series.getStyle().getLineStyle().getVisible()) {
		 * g2.setColor(series.getStyle().getLineStyle().getColor());
		 * g2.setStroke(series.getStyle().getLineStyle().getStroke()); g2.draw(path); }
		 */

		if (series.getStyle().getFillStyle().getVisible()) {
			g2.setColor(series.getStyle().getFillStyle().getColor());
			g2.fill(path);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.PathPlotLayer#getPath(java.
	 * lang.String, edu.columbia.rdf.lib.bioinformatics.plot.figure.Figure,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Axes,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Plot,
	 * org.abh.lib.math.matrix.DataFrame,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.series.XYSeries,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.UniqueXY)
	 */
	@Override
	protected GeneralPath getPath(Figure figure, SubFigure subFigure, Axes axes, Plot plot, DataFrame m,
			XYSeries series, UniqueXY xy) {
		GeneralPath path = new GeneralPath();

		int y0 = Math.min(axes.toPlotY1(0), axes.toPlotY1(axes.getY1Axis().getLimits().getMin()));

		path.moveTo(xy.getPoint(0).x, y0);

		for (Point p : xy) {
			path.lineTo(p.x, p.y);
		}

		path.lineTo(xy.getPoint(xy.getPointCount() - 1).x, y0);

		path.closePath();

		return path;
	}
}
