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
 * Concrete implementation of Graph2dCanvas for generating scatter plots.
 *
 * @author Antony Holmes
 */
public class SplineFillPlotLayer extends SplinePlotLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new spline fill plot layer.
	 *
	 * @param series the series
	 */
	public SplineFillPlotLayer(String series) {
		super(series, false);
	}

	@Override
	public String getType() {
		return "Spline Fill Layer";
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

		// create zero points
		int n = xy.getPointCount();

		if (n < 3) {
			return null;
		}

		Point p;

		// grab (x,y) coordinates of the control points
		double[] knotsX = new double[n];
		double[] knotsY = new double[n];

		// zero point
		// knotsX[0] = xy.getPoint(0).getX();
		// knotsY[0] = Math.min(axes.toPlotY1(0),
		// axes.toPlotY1(axes.getY1Axis().getLimits().getMin()));

		for (int i = 0; i < xy.getPointCount(); ++i) {
			p = xy.getPoint(i);

			knotsX[i] = p.x;
			knotsY[i] = p.y;
		}

		// zero end point
		// knotsX[n - 1] = xy.getPoint(xy.getPointCount() - 1).getX();
		// knotsY[n - 1] = knotsY[0];

		// computes control points p1 and p2 for x and y direction
		double[] px = computeControlPoints(knotsX);
		double[] py = computeControlPoints(knotsY);

		GeneralPath path = new GeneralPath();

		int miny = axes.toPlotY1(Math.max(0, axes.getY1Axis().getLimits().getMin()));

		path.moveTo(knotsX[0], miny);
		path.lineTo(knotsX[0], knotsY[0]);

		int p2Index = n - 1;

		for (int i = 0; i < n - 1; ++i) {
			p = xy.getPoint(i + 1);
			path.curveTo(px[i], py[i], px[p2Index + i], py[p2Index + i], knotsX[i + 1], knotsY[i + 1]);
		}

		path.lineTo(knotsX[knotsX.length - 1], miny);
		path.closePath();

		return path;
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

		// System.err.println("spline " + plot.getName() + " " + plot.getId() + " "
		// + series.getStyle().getFillStyle().getVisible());

		if (series.getStyle().getFillStyle().getVisible()) {
			g2.setColor(series.getStyle().getFillStyle().getColor());
			g2.fill(path);
		}
	}
}
