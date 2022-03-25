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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jebtk.core.geom.DoublePos2D;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.MatrixGroup;

/**
 * Stores the max y for a given x so that you can find the unique values of x
 * and the max y at that x for plotting purposes. Should only be used for
 * painting coordinates and not graph plot points.
 * 
 * @author Antony Holmes
 *
 */
public class UniqueXY implements Iterable<Point>, Comparable<UniqueXY> {
	// private Map<Integer, Integer> mPointMap =
	// new TreeMap<Integer, Integer>();

	/**
	 * The member point original map.
	 */
	private Map<Integer, DoublePos2D> mPointOriginalMap = new TreeMap<Integer, DoublePos2D>();

	/**
	 * The member list.
	 */
	private List<Point> mList = Collections.emptyList();

	/** The m all list. */
	private List<Point> mAllList;

	/**
	 * Instantiates a new unique xy.
	 *
	 * @param m      the m
	 * @param series the series
	 * @param space  the space
	 */
	public UniqueXY(DataFrame m, XYSeries series, Axes space) {

		this(m, series, space, false);
	}

	/**
	 * Instantiates a new unique xy.
	 *
	 * @param m        the m
	 * @param series   the series
	 * @param axes     the axes
	 * @param zeroEnds the zero ends
	 */
	public UniqueXY(DataFrame m, XYSeries series, Axes axes, boolean zeroEnds) {

		Map<Integer, Point> pointMap = new TreeMap<Integer, Point>();

		List<Integer> columns = MatrixGroup.findColumnIndices(m, series);

		if (columns.size() > 0) {
			mAllList = new ArrayList<Point>(m.getRows());

			for (int i = 0; i < m.getRows(); ++i) {
				DoublePos2D point = new DoublePos2D(m.getValue(i, columns.get(0)), m.getValue(i, columns.get(1)));

				Point p = axes.toPlotX1Y1(point);

				// if (i < 10) {
				// System.err.println("unique " + point + " " + p);
				// }

				mAllList.add(p);

				if (axes.getX1Axis().withinBounds(point.getX())) {
					if (pointMap.containsKey(p.x)) {
						if (point.getY() >= 0) {
							// For points above zero keep the min y we find
							// (since pixel y coordinates are inverted)
							if (p.y < pointMap.get(p.x).y) {
								pointMap.put(p.x, p);

								mPointOriginalMap.put(p.x, point);
							}
						} else {
							if (p.y > pointMap.get(p.x).y) {
								pointMap.put(p.x, p);

								mPointOriginalMap.put(p.x, point);
							}
						}
					} else {
						pointMap.put(p.x, p);

						mPointOriginalMap.put(p.x, point);
					}
				}
			}

			mAllList = Collections.unmodifiableList(mAllList);

			// Create a sorted list of the points based on the x coordinate

			mList = new ArrayList<Point>(pointMap.size());

			for (int x : pointMap.keySet()) {
				mList.add(pointMap.get(x));
			}

			if (zeroEnds && mList.size() > 0) {
				int z = axes.toPlotY1(0);

				// Force the starting and end coordinates to have a y of zero
				mList.set(0, new Point(mList.get(0).x, z));
				mList.set(mList.size() - 1, new Point(mList.get(mList.size() - 1).x, z));

				mPointOriginalMap.put(mList.get(0).x, new DoublePos2D(mPointOriginalMap.get(mList.get(0).x).getX(), 0));

				mPointOriginalMap.put(mList.get(mList.size() - 1).x,
						new DoublePos2D(mPointOriginalMap.get(mList.get(mList.size() - 1).x).getX(), 0));
			}

			mList = Collections.unmodifiableList(mList);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Point> iterator() {
		return mList.iterator();
	}

	/**
	 * Gets the unique x.
	 *
	 * @return the unique x
	 */
	public List<Point> getUniqueX() {
		return mList;
	}

	/**
	 * Gets the point.
	 *
	 * @param i the i
	 * @return the point
	 */
	public Point getPoint(int i) {
		return mList.get(i);
	}

	/**
	 * Gets the point count.
	 *
	 * @return the point count
	 */
	public int getPointCount() {
		return mList.size();
	}

	/**
	 * Gets the all points.
	 *
	 * @return the all points
	 */
	public List<Point> getAllPoints() {
		return mAllList;
	}

	/**
	 * Original.
	 *
	 * @param p the p
	 * @return the point2 d double
	 */
	public DoublePos2D original(Point p) {
		return original(p.x);
	}

	/**
	 * Original.
	 *
	 * @param x the x
	 * @return the point2 d double
	 */
	public DoublePos2D original(int x) {
		return mPointOriginalMap.get(x);
	}

	/**
	 * Closest x.
	 *
	 * @param p the p
	 * @return the point
	 */
	public Point closestX(Point p) {
		return closestX(p.x);
	}

	/**
	 * Use a binary search to find the closest x in the set of coordinates to a
	 * given x. Returns null if there are no coordinates in the unique list.
	 *
	 * @param x the x
	 * @return the point
	 */
	public Point closestX(int x) {
		if (mList.size() == 0) {
			return null;
		}

		int si = 0;
		int se = mList.size() - 1;

		while (se - si > 1) {
			int i = (si + se) / 2;

			Point p = mList.get(i);

			if (x > p.x) {
				si = i;
			} else if (x < p.x) {
				se = i;
			} else {
				return p;
			}
		}

		Point x1 = mList.get(si);
		Point x2 = mList.get(se);

		int d1 = Math.abs(x - x1.x);
		int d2 = Math.abs(x - x2.x);

		if (d1 <= d2) {
			return x1;
		} else {
			return x2;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(UniqueXY e) {
		return 0;
	}
}
