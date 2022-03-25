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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jebtk.core.collections.ArrayListMultiMap;
import org.jebtk.core.collections.DefaultHashMap;
import org.jebtk.core.collections.HashSetCreator;
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
public class BoxWhiskerScatterLayer2 extends PlotClippedLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new box whisker scatter layer.
	 */
	private double mX;

	/** The m hash id. */
	private String mHashId;

	/** The m point map. */
	private ListMultiMap<XYSeries, IntPos2D> mPointMap;

	/** The m point2 group map. */
	private Map<IntPos2D, Integer> mPoint2GroupMap;

	/** The m bin size. */
	private int mBinSize = 0;

	/** The m half bin size. */
	private int mHalfBinSize;

	/** The m gap size. */
	private int mGapSize;

	/** The m group size map. */
	private Map<Integer, Integer> mGroupSizeMap;

	/** The m W. */
	private double mW;

	/**
	 * Instantiates a new box whisker scatter layer2.
	 *
	 * @param x the x
	 */
	public BoxWhiskerScatterLayer2(double x) {
		this(x, 1);
	}

	/**
	 * Instantiates a new box whisker scatter layer 2.
	 *
	 * @param x the x
	 * @param w the w
	 */
	public BoxWhiskerScatterLayer2(double x, double w) {
		this(x, w, true);
	}

	/**
	 * Instantiates a new box whisker scatter layer2.
	 *
	 * @param x       the x
	 * @param w       the w
	 * @param visible the visible
	 */
	public BoxWhiskerScatterLayer2(double x, double w, boolean visible) {
		mX = x;
		mW = w;

		setVisible(visible);
	}

	@Override
	public String getType() {
		return "Box Whisker Scatter Layer";
	}

	@Override
	public final void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes,
			Plot plot, DataFrame m) {

		// the width of the arms of the plot
		int y;
		int x;

		int plotX = axes.toPlotX1(mX);

		int plotW = axes.toPlotX1(mW);

		if (mHashId == null || !mHashId.equals(axes.hashId())) {

			SetMultiMap<IntPos2D, Integer> point2YBinMap = HashSetMultiMap.create();

			ListMultiMap<Integer, IntPos2D> yBin2PointMap = ArrayListMultiMap.create();

			// Determine the maximum marker size
			for (XYSeries s : plot.getAllSeries()) {
				mBinSize = Math.max(mBinSize, s.getMarker().getSize());
			}

			mHalfBinSize = mBinSize / 2;

			mGapSize = mBinSize / 4;

			mPointMap = ArrayListMultiMap.create();

			for (XYSeries s : plot.getAllSeries()) {
				m = s.getMatrix();

				for (int i = 0; i < m.getRows(); ++i) {
					double p = m.getValue(i, 0);

					// Plot the limits
					y = axes.toPlotY1(p);

					int y1 = y - mHalfBinSize;
					int y2 = y + mHalfBinSize;

					int bin1 = y1 / mBinSize;
					int bin2 = y2 / mBinSize;

					// Clone point but update location
					IntPos2D newPoint = new IntPos2D(plotX, y);

					// if (!point2BinMap.containsKey(newPoint)) {
					// point2BinMap.put(newPoint, new HashSet<Integer>());
					// }

					point2YBinMap.get(newPoint).add(bin1);
					point2YBinMap.get(newPoint).add(bin2);

					// Points that might be overlapping will be in the same bin
					yBin2PointMap.get(bin1).add(newPoint);
					yBin2PointMap.get(bin2).add(newPoint);

					mPointMap.get(s).add(newPoint);
				}
			}

			//
			// See what overlaps
			//

			mPoint2GroupMap = new HashMap<IntPos2D, Integer>();

			Map<Integer, Set<IntPos2D>> overlap2PointMap = DefaultHashMap.create(new HashSetCreator<IntPos2D>());

			int group = 0;

			for (XYSeries s : mPointMap.keySet()) {
				for (IntPos2D p : mPointMap.get(s)) {
					// See what other points are in the bin
					for (int bin : point2YBinMap.get(p)) {
						for (IntPos2D p2 : yBin2PointMap.get(bin)) {
							// These points are broadly on the same y point, but
							// can have different x coordinates or overlap.
							// We now test whether they overlap so we can
							// separate the points by x-coordinate.

							if (Geometry.overlap(p, p2, mBinSize)) {

								int g;

								// The group is determined by whether there is
								// already a point in the map
								if (mPoint2GroupMap.containsKey(p)) {
									g = mPoint2GroupMap.get(p);
								} else if (mPoint2GroupMap.containsKey(p2)) {
									g = mPoint2GroupMap.get(p2);
								} else {
									g = group++;
								}

								// Only add the point if it is not already
								// in there. This means the point is assigned
								// to the first group and no other.
								if (!mPoint2GroupMap.containsKey(p)) {
									mPoint2GroupMap.put(p, g);
									overlap2PointMap.get(g).add(p);
								}

								if (!mPoint2GroupMap.containsKey(p2)) {
									mPoint2GroupMap.put(p2, g);
									overlap2PointMap.get(g).add(p2);
								}
							}
						}
					}
				}
			}

			// Now we need to count the number of points in each group
			// This is because the same coordinate will of course appear as a
			// unique entry in the mapping sets, but we need to know how wide
			// each group is

			mGroupSizeMap = DefaultHashMap.create(0);

			for (XYSeries s : mPointMap.keySet()) {
				for (IntPos2D p : mPointMap.get(s)) {
					int g = mPoint2GroupMap.get(p);

					mGroupSizeMap.put(g, mGroupSizeMap.get(g) + 1);
				}
			}

			mHashId = axes.hashId();
		}

		// We know the max width of the block, so we can determine the
		// spread of each group. e.g if there are two points in a group,
		// evenly spread those points into two groups

		Map<Integer, Integer> groupGapMap = DefaultHashMap.create(0);

		for (int g : mGroupSizeMap.keySet()) {
			int s = mGroupSizeMap.get(g);

			if (s == 1) {
				groupGapMap.put(g, 0);
			} else {
				groupGapMap.put(g, Math.min(mBinSize + mGapSize, plotW / (s - 1)));
			}
		}

		Map<Integer, Integer> groupOffsetMap = DefaultHashMap.create(0);

		for (int g : mGroupSizeMap.keySet()) {
			int s = mGroupSizeMap.get(g);

			int gap = groupGapMap.get(g);

			// Centre the width required about x
			int offsetX = plotX - (gap * (s - 1)) / 2;

			groupOffsetMap.put(g, offsetX);
		}

		Map<Integer, Integer> groupIndexMap = DefaultHashMap.create(0);

		for (XYSeries s : mPointMap.keySet()) {
			for (IntPos2D p : mPointMap.get(s)) {
				int g = mPoint2GroupMap.get(p);

				int gap = groupGapMap.get(g);

				x = groupOffsetMap.get(g) + groupIndexMap.get(g) * gap;

				// Increment the group index since we have used this position
				groupIndexMap.put(g, groupIndexMap.get(g) + 1);

				s.getMarker().plot(g2, s.getMarkerStyle(), new Point(x, p.getY()));
			}
		}
	}
}
