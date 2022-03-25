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

import java.awt.Point;
import java.awt.geom.GeneralPath;

import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.math.matrix.DataFrame;

/**
 * Sets up a spline plot, caching the path to be drawn if necessary to reduce
 * calculation operations.
 * 
 * @author Antony Holmes
 */
public abstract class SplinePlotLayer extends PathPlotLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new spline plot layer.
	 *
	 * @param name     the name
	 * @param series   the series
	 * @param zeroEnds the zero ends
	 */
	public SplinePlotLayer(String series, boolean zeroEnds) {
		super(series, zeroEnds);
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

		// We can't work with less than 3 points
		if (n < 3) {
			return null;
		}

		Point p;

		// grab (x,y) coordinates of the control points
		double[] knotsX = new double[n];
		double[] knotsY = new double[n];

		p = xy.getPoint(0);

		for (int i = 0; i < n; ++i) {
			p = xy.getPoint(i);

			knotsX[i] = p.x;
			knotsY[i] = p.y;
		}

		// computes control points p1 and p2 for x and y direction
		double[] px = computeControlPoints(knotsX);
		double[] py = computeControlPoints(knotsY);

		GeneralPath path = new GeneralPath();

		path.moveTo(knotsX[0], knotsY[0]);

		int p2Index = n - 1;

		for (int i = 0; i < n - 1; ++i) {
			path.curveTo(px[i], py[i], px[p2Index + i], py[p2Index + i], knotsX[i + 1], knotsY[i + 1]);
		}

		return path;
	}

	/**
	 * Computes control points given knots K.
	 *
	 * @param k the k
	 * @return the double[]
	 */
	public static double[] computeControlPoints(double[] k) {
		// k knots means k -1 curves to draw
		int n = k.length - 1;

		double[] p = new double[2 * n];

		// rhs vector
		double[] a = new double[n];
		double[] b = new double[n];
		double[] c = new double[n];
		double[] r = new double[n];

		/* left most segment */
		a[0] = 0;
		b[0] = 2;
		c[0] = 1;
		r[0] = k[0] + 2 * k[1];

		// internal segments
		for (int i = 1; i < n - 1; ++i) {
			a[i] = 1;
			b[i] = 4;
			c[i] = 1;
			r[i] = 4 * k[i] + 2 * k[i + 1];
		}

		// right segment
		a[n - 1] = 2;
		b[n - 1] = 7;
		c[n - 1] = 0;
		r[n - 1] = 8 * k[n - 1] + k[n];

		double m;

		// solves Ax=b with the Thomas algorithm (from Wikipedia)
		for (int i = 1; i < n; ++i) {
			m = a[i] / b[i - 1];
			b[i] = b[i] - m * c[i - 1];
			r[i] = r[i] - m * r[i - 1];
		}

		p[n - 1] = r[n - 1] / b[n - 1];

		for (int i = n - 2; i >= 0; --i) {
			p[i] = (r[i] - c[i] * p[i + 1]) / b[i];
		}

		// p2 comes after p1 in the array

		// we have p1, now compute p2
		for (int i = 0; i < n - 1; ++i) {
			// p2 in terms of p1
			p[i + n] = 2 * k[i + 1] - p[i + 1];
		}

		// p2 in terms of p
		p[2 * n - 1] = 0.5 * (k[n] + p[n - 1]);

		return p;
	}
}
