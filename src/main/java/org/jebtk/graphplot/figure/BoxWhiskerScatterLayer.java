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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jebtk.core.collections.ArrayListMultiMap;
import org.jebtk.core.collections.HashSetMultiMap;
import org.jebtk.core.collections.ListMultiMap;
import org.jebtk.core.collections.SetMultiMap;
import org.jebtk.core.geom.IntPos2D;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.math.Geometry;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Concrete implementation of Graph2dCanvas for generating scatter plots.
 *
 * @author Antony Holmes
 */
public class BoxWhiskerScatterLayer extends PlotSeriesLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new box whisker scatter layer.
	 */
	private double mX;

	/**
	 * Instantiates a new box whisker scatter layer.
	 *
	 * @param name the name
	 * @param x    the x
	 */
	public BoxWhiskerScatterLayer(String name, double x) {
		this(name, x, true);
	}

	/**
	 * Instantiates a new box whisker scatter layer.
	 *
	 * @param name    the name
	 * @param x       the x
	 * @param visible the visible
	 */
	public BoxWhiskerScatterLayer(String name, double x, boolean visible) {
		super(name);

		mX = x + 0.5;

		setVisible(visible);
	}

	@Override
	public String getType() {
		return "Box Whisker Scatter Layer";
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
	public void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes,
			Plot plot, DataFrame m, XYSeries series) {

		// the width of the arms of the plot
		int y;
		int x;

		BoxWhiskerSummary s = (BoxWhiskerSummary) series;

		// Map<IntPos2D, Set<Integer>> point2BinMap =
		// new HashMap<IntPos2D, Set<Integer>>();

		// Multimap<IntPos2D, Integer> point2BinMap =
		// HashMultimap.create();

		SetMultiMap<IntPos2D, Integer> point2BinMap = HashSetMultiMap.create();

		// Map<Integer, List<IntPos2D>> bin2PointMap =
		// new HashMap<Integer, List<IntPos2D>>();

		ListMultiMap<Integer, IntPos2D> bin2PointMap = ArrayListMultiMap.create(); // ArrayListMultimap.create();

		int plotX = axes.toPlotX1(mX);

		int binSize = s.getMarker().getSize();
		int b2 = binSize / 2;

		List<IntPos2D> points = new ArrayList<IntPos2D>();

		for (double p : XYSeries.values(m, series)) {
			// Plot the limits
			y = axes.toPlotY1(p);

			int y1 = y - b2;
			int y2 = y + b2;

			int bin1 = y1 / binSize;
			int bin2 = y2 / binSize;

			// Clone point but update location
			IntPos2D newPoint = new IntPos2D(plotX, y);

			// if (!point2BinMap.containsKey(newPoint)) {
			// point2BinMap.put(newPoint, new HashSet<Integer>());
			// }

			point2BinMap.get(newPoint).add(bin1);
			point2BinMap.get(newPoint).add(bin2);

			/// if (!bin2PointMap.containsKey(bin1)) {
			// bin2PointMap.put(bin1, new UniqueList<IntPos2D>());
			// }

			// if (!bin2PointMap.containsKey(bin2)) {
			// bin2PointMap.put(bin2, new UniqueList<IntPos2D>());
			// }

			bin2PointMap.get(bin1).add(newPoint);
			bin2PointMap.get(bin2).add(newPoint);

			// Points that might be overlapping will be in the same bin
			points.add(newPoint);
		}

		// See who overlaps

		Map<IntPos2D, Integer> point2OverlapMap = new HashMap<IntPos2D, Integer>();

		// Map<Integer, List<IntPos2D>> overlap2PointMap =
		// new HashMap<Integer, List<IntPos2D>>();

		// Multimap<Integer, IntPos2D> overlap2PointMap =
		// ArrayListMultimap.create();

		ListMultiMap<Integer, IntPos2D> overlap2PointMap = ArrayListMultiMap.create();

		int g = 0;

		for (IntPos2D p : points) {
			boolean overlap = false;

			for (int bin : point2BinMap.get(p)) {
				for (IntPos2D p2 : bin2PointMap.get(bin)) {
					if (p2.equals(p)) {
						continue;
					}

					// System.err.println("cp " + p + " " + p2 + " " + Geometry.overlap(p,
					// p2, binSize) + " " + bin + " " + point2OverlapMap.containsKey(p) +
					// " " + point2OverlapMap.containsKey(p2));

					if (Geometry.overlap(p, p2, binSize)) {
						if (point2OverlapMap.containsKey(p2)) {
							if (!point2OverlapMap.containsKey(p)) {
								point2OverlapMap.put(p, point2OverlapMap.get(p2));
								overlap2PointMap.get(point2OverlapMap.get(p2)).add(p);
							}

							overlap = true;
						} else {
							overlap = false;
						}
					}
				}
			}

			// If the point cannot be allocated to any overlap, create an
			// overlap for itself
			if (!overlap && !point2OverlapMap.containsKey(p)) {
				// overlap2PointMap.put(g, new UniqueList<IntPos2D>());
				overlap2PointMap.get(g).add(p);
				// overlap2PointMap.get(g).add(p2);

				point2OverlapMap.put(p, g);
				// point2OverlapMap.put(p2, g);

				++g;
			}
		}

		// Arrange the points so that overlapping ones are separated
		// about the center

		for (int group : overlap2PointMap.keySet()) {
			int i = 0;

			int offsetX = plotX
					- (binSize * overlap2PointMap.get(group).size() + b2 * (overlap2PointMap.get(group).size() - 1)) / 2
					+ b2;

			for (IntPos2D p : overlap2PointMap.get(group)) {
				// binSize / 2 at the end required to shift point so that
				// the center of the shape is on x rather than the left edge
				x = offsetX + i * (binSize + b2);

				series.getMarker().plot(g2, series.getMarkerStyle(), new Point(x, p.getY()));

				System.err.println("p " + p + " " + group);

				++i;
			}
		}

		//
		// draw outliers
		//

		int xc = axes.toPlotX1(mX + 0.5);

		if (s.getUpperOutlier() > s.getUpper()) {
			s.getOutlierShape().plot(g2, s.getMarkerStyle(), new Point(xc, axes.toPlotY1(s.getUpperOutlier())));
		}

		if (s.getLowerOutlier() < s.getLower()) {
			s.getOutlierShape().plot(g2, s.getMarkerStyle(), new Point(xc, axes.toPlotY1(s.getLowerOutlier())));
		}

	}

	/*
	 * public BoxWhiskerScatterCanvas(XYSeriesCollection allSeries,
	 * BoxWhiskerCollection boxWhiskers) { super(boxWhiskers);
	 * 
	 * // Add a layer that renders points addLayer(new BoxWhiskerScatterLayer());
	 * 
	 * addLayer(new LegendLayerCanvas(allSeries, getGraphProperties())); }
	 */
}
