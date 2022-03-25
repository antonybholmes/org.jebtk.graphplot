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
 * Draw a line between points in a series.
 * 
 * @author Antony Holmes
 *
 */
public class LinePlotLayer extends PathPlotLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new line plot layer.
	 *
	 * @param series the series
	 */
	public LinePlotLayer(String series) {
		super(series);
	}

	@Override
	public String getType() {
		return "Line Plot Layer";
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

		if (series.getStyle().getLineStyle() != null) {
			g2.setStroke(series.getStyle().getLineStyle().getStroke());
			g2.setColor(series.getStyle().getLineStyle().getColor());

			g2.draw(path);
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
		int n = xy.getPointCount();

		if (n > 0) {

			Point p;

			GeneralPath path = new GeneralPath();

			// List<Point> allPoints = xy.getAllPoints();

			p = xy.getPoint(0); // )allPoints.get(0);
			path.moveTo(p.x, p.y);

			/*
			 * for (int i = 1; i < allPoints.size(); ++i) { p = allPoints.get(i);
			 * 
			 * System.err.println("huh " + p);
			 * 
			 * path.lineTo(p.x, p.y); }
			 */

			for (int i = 1; i < xy.getPointCount(); ++i) {
				p = xy.getPoint(i);

				path.lineTo(p.x, p.y);
			}

			return path;
		} else {
			return null;
		}
	}
}
