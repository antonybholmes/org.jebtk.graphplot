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
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.jebtk.core.Mathematics;
import org.jebtk.core.geom.DoublePos2D;
import org.jebtk.graphplot.figure.series.Marker;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.math.Linspace;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.MatrixGroup;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Concrete implementation of Graph2dCanvas for generating scatter plots.
 *
 * @author Antony Holmes
 */
public class ScatterBestFitPlotLayer extends PlotSeriesLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new scatter best fit plot layer.
	 *
	 * @param series the series
	 */
	public ScatterBestFitPlotLayer(String series) {
		super(series);
	}

	@Override
	public String getType() {
		return "Scatter Plot Best Fit Layer";
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
			Plot plot, DataFrame m, XYSeries series) {

		updateSplines(g2, figure, subFigure, axes, m, series);
	}

	/**
	 * Compute control points.
	 *
	 * @param k the k
	 * @return the double[]
	 */
	/*
	 * computes control points given knots K, this is the brain of the operation
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
		for (int i = 1; i < n - 1; i++) {
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
		for (int i = 1; i < n; i++) {
			m = a[i] / b[i - 1];
			b[i] = b[i] - m * c[i - 1];
			r[i] = r[i] - m * r[i - 1];
		}

		p[n - 1] = r[n - 1] / b[n - 1];

		for (int i = n - 2; i >= 0; --i) {
			p[i] = (r[i] - c[i] * p[i + 1]) / b[i];
		}

		// p2 comes after p1 in the array

		/* we have p1, now compute p2 */
		for (int i = 0; i < n - 1; i++) {
			// p2 in terms of p1
			p[i + n] = 2 * k[i + 1] - p[i + 1];
		}

		// p2 in terms of p
		p[2 * n - 1] = 0.5 * (k[n] + p[n - 1]);

		return p;
	}

	/**
	 * Update splines.
	 *
	 * @param g2     the g2
	 * @param figure the figure
	 * @param axes   the axes
	 * @param m      the m
	 * @param series the series
	 */
	public static void updateSplines(Graphics2D g2, Figure figure, SubFigure subFigure, Axes axes, DataFrame m,
			XYSeries series) {

		int n = m.getRows();

		/* grab (x,y) coordinates of the control points */
		double[] knotsX = new double[n];
		double[] knotsY = new double[n];

		// List<Point> points = axes.convertToPlot(series);

		DoublePos2D p1;
		DoublePos2D p2;

		List<Integer> columns = MatrixGroup.findColumnIndices(m, series);

		WeightedObservedPoints obs = new WeightedObservedPoints();

		for (int i = 0; i < n; ++i) {
			p1 = new DoublePos2D(m.getValue(i, columns.get(0)), m.getValue(i, columns.get(1)));

			knotsX[i] = p1.getX();
			knotsY[i] = p1.getY();

			obs.add(p1.getX(), p1.getY());
		}

		System.err.println("C" + columns + " " + Arrays.toString(knotsX) + " " + axes.getX1Axis().getLimits().getMin()
				+ " " + axes.getX1Axis().getLimits().getMax());

		//
		// Create an iterpolation
		//

		// UnivariateInterpolator interpolator = new SplineInterpolator();
		// UnivariateFunction function = interpolator.interpolate(knotsX, knotsY);

		PolynomialCurveFitter fitter = PolynomialCurveFitter.create(3);

		// Retrieve fitted parameters (coefficients of the polynomial function).
		final double[] c = fitter.fit(obs.toList());

		PolynomialFunction function = new PolynomialFunction(c);

		double minX = Mathematics.min(knotsX);
		double maxX = Mathematics.max(knotsX);

		// the max x we interpolate cannot be >= the maximum given to the
		// iterpolator, so we pick a maximum slightly less than the true
		// max.
		knotsX = Linspace.generate2(minX, maxX * 0.999);

		//
		// Use the interpolation to create the bezier curves.
		//

		n = knotsX.length;

		knotsY = new double[n];

		List<DoublePos2D> points = new ArrayList<DoublePos2D>(n);

		for (int i = 0; i < n; ++i) {
			knotsY[i] = function.value(knotsX[i]);

			points.add(new DoublePos2D(knotsX[i], knotsY[i]));
		}

		/* computes control points p1 and p2 for x and y direction */
		double[] px = computeControlPoints(knotsX);
		double[] py = computeControlPoints(knotsY);

		GeneralPath path = new GeneralPath();

		p1 = points.get(0);

		path.moveTo(axes.toPlotX1(p1.getX()), axes.toPlotY1(p1.getY()));

		int p2Index = n - 1;

		for (int i = 0; i < n - 1; ++i) {
			p1 = points.get(i);
			p2 = points.get(i + 1);

			path.curveTo(axes.toPlotX1(px[i]), axes.toPlotY1(py[i]), axes.toPlotX1(px[p2Index + i]),
					axes.toPlotY1(py[p2Index + i]), axes.toPlotX1(p2.getX()), axes.toPlotY1(p2.getY()));
		}

		// path.closePath();

		g2.setColor(Color.RED);

		g2.draw(path);

		Marker shape = series.getMarker();

		for (DoublePos2D p : points) {
			shape.plot(g2, series.getMarkerStyle(), axes.toPlotX1Y1(p));
		}

		/* updates path settings, the browser will draw the new spline */
		// for (i=0;i<3;i++)
		// S[i].setAttributeNS(null,"d",
		// path(x[i],y[i],px.p1[i],py.p1[i],px.p2[i],py.p2[i],x[i+1],y[i+1]));
	}
}
