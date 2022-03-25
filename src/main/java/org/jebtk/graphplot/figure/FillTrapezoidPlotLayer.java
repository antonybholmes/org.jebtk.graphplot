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
 * Fills in line series.
 * 
 * @author Antony Holmes
 *
 */
public class FillTrapezoidPlotLayer extends FillPlotLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new fill plot layer.
	 *
	 * @param series the series
	 */
	public FillTrapezoidPlotLayer(String series) {
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
	protected GeneralPath getPath(Figure figure, SubFigure subFigure, Axes axes, Plot plot, DataFrame m,
			XYSeries series, UniqueXY xy) {

		int yMin;

		if (axes.getY1Axis().getLimits().getMin() <= 0 && axes.getY1Axis().getLimits().getMax() > 0) {
			yMin = axes.toPlotY1(0);
		} else if (axes.getY1Axis().getLimits().getMin() >= 0) {
			yMin = axes.toPlotY1(axes.getY1Axis().getLimits().getMin());
		} else {
			yMin = axes.toPlotY1(axes.getY1Axis().getLimits().getMax());
		}

		GeneralPath path = new GeneralPath();

		Point ep = xy.getPoint(0);

		path.moveTo(ep.x, yMin);
		path.lineTo(ep.x, ep.y);

		for (Point p : xy) {
			path.lineTo(p.x, p.y);
		}

		ep = xy.getPoint(xy.getPointCount() - 1);

		// Ensure that the end point matches the start so that when joined
		if (ep.y != yMin) {
			path.lineTo(ep.x, yMin);
		}

		path.closePath();

		return path;

		/*
		 * List<GeneralPath> paths = new ArrayList<GeneralPath>();
		 * 
		 * for (int i = 0; i < xy.getPointCount() - 1; ++i) { GeneralPath path = new
		 * GeneralPath();
		 * 
		 * path.moveTo(xy.getPoint(i).x, yMin); path.lineTo(xy.getPoint(i).x,
		 * xy.getPoint(i).y); path.lineTo(xy.getPoint(i + 1).x, xy.getPoint(i + 1).y);
		 * path.lineTo(xy.getPoint(i + 1).x, yMin); path.closePath();
		 * 
		 * paths.add(path); }
		 * 
		 * return paths;
		 */
	}
}
