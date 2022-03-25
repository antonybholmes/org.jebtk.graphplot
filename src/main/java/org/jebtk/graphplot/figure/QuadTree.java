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
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jebtk.core.IdObject;

/**
 * The class QuadTree.
 *
 * @param <T> the generic type
 */
public class QuadTree<T> extends IdObject implements Iterable<QuadValue<T>> {

	/**
	 * The north west.
	 */
	private QuadTree<T> northWest = null;

	/**
	 * The north east.
	 */
	private QuadTree<T> northEast = null;

	/**
	 * The south west.
	 */
	private QuadTree<T> southWest = null;

	/**
	 * The south east.
	 */
	private QuadTree<T> southEast = null;

	/**
	 * The member mid point.
	 */
	private Point2D mMidPoint = null;

	/**
	 * The member bounds.
	 */
	private Rectangle2D mBounds = null;

	/**
	 * The member points.
	 */
	private Set<QuadValue<T>> mPoints = new HashSet<QuadValue<T>>();

	/**
	 * Instantiates a new quad tree.
	 *
	 * @param bounds the bounds
	 */
	public QuadTree(Rectangle2D bounds) {
		mBounds = bounds;

		mMidPoint = midPoint(bounds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<QuadValue<T>> iterator() {
		return mPoints.iterator();
	}

	/**
	 * Gets the closest point.
	 *
	 * @param p the p
	 * @return the closest point
	 */
	public QuadValue<T> getClosestPoint(Point p) {
		// Make a bounding box for the p which is the area we
		// want to check

		// Rectangle2D bounds = new Rectangle2D.Double(p.x - radius, p.y - radius,
		// radius * 2, radius * 2);

		// We need to test all 4 points

		// List<Point2D> points = new ArrayList<Point2D>();

		// points.add(new Point2D.Double(bounds.getX(), bounds.getY()));
		// points.add(new Point2D.Double(bounds.getX() + bounds.getWidth(),
		// bounds.getY()));
		// points.add(new Point2D.Double(bounds.getX() + bounds.getWidth(),
		// bounds.getY() + bounds.getHeight()));
		// points.add(new Point2D.Double(bounds.getX(), bounds.getY() +
		// bounds.getHeight()));

		// Set<Integer> used = new HashSet<Integer>();

		QuadTree<T> tree = getClosest(p);

		double mind = Double.MAX_VALUE;

		QuadValue<T> ret = null;

		for (QuadValue<T> tp : tree) {
			double d = p.distance(tp.p);

			if (d < mind) {
				ret = tp;

				mind = d;
			}
		}

		return ret;
	}

	/**
	 * Gets the closest.
	 *
	 * @param p the p
	 * @return the closest
	 */
	public QuadTree<T> getClosest(Point p) {
		QuadTree<T> ret = this;

		boolean stop = false;

		// double d;

		// System.err.println("start " + ret);

		// double mind = Double.MAX_VALUE;

		while (!stop) {
			stop = true;

			if (p.getX() <= ret.mMidPoint.getX()) {
				// in the west

				if (p.getY() <= ret.mMidPoint.getY()) {
					// in the north

					if (ret.northWest != null) {
						ret = ret.northWest;
						stop = false;
					}
				} else {
					if (ret.southWest != null) {
						ret = ret.southWest;
						stop = false;
					}
				}
			} else {
				if (p.getY() <= ret.mMidPoint.getY()) {
					// in the north

					if (ret.northEast != null) {
						ret = ret.northEast;
						stop = false;
					}
				} else {
					if (ret.southEast != null) {
						ret = ret.southEast;
						stop = false;
					}
				}
			}

			/*
			 * // We stop unless there is a sub quad nearer to // our point of interest
			 * 
			 * stop = true;
			 * 
			 * double mind = Double.MAX_VALUE;
			 * 
			 * d = p.distance(ret.mMidPoint);
			 * 
			 * if (d < mind) { mind = d; }
			 * 
			 * //System.err.println("d " + d + " " + mind);
			 * 
			 * if (ret.northEast != null) {
			 * 
			 * 
			 * d = p.distance(ret.northEast.mMidPoint);
			 * 
			 * //System.err.println("ne " + d + " " + mind + " " + ret.northEast);
			 * 
			 * if (d < mind) { mind = d; ret = ret.northEast; stop = false; } }
			 * 
			 * if (ret.northWest != null) {
			 * 
			 * 
			 * d = p.distance(ret.northWest.mMidPoint);
			 * 
			 * //System.err.println("nw " + ret.northWest + " " + d + " " + mind);
			 * 
			 * if (d < mind) { mind = d; ret = ret.northWest; stop = false; } }
			 * 
			 * if (ret.southEast != null) { d = p.distance(ret.southEast.mMidPoint);
			 * 
			 * if (d < mind) { mind = d; ret = ret.southEast; stop = false; } }
			 * 
			 * if (ret.southWest != null) { d = p.distance(ret.southWest.mMidPoint);
			 * 
			 * if (d < mind) { mind = d; ret = ret.southWest; stop = false; } }
			 */
		}

		// System.err.println("ret " + ret);

		return ret;
	}

	/**
	 * Adds the.
	 *
	 * @param point the point
	 */
	public void add(QuadValue<T> point) {

		// Since this quad contains more than one child

		// Second, we must attempt to place each point into it own
		// quad to start with

		QuadTree<T> s = this;
		QuadValue<T> p;

		Deque<QuadTree<T>> treeStack = new ArrayDeque<QuadTree<T>>();
		Deque<QuadValue<T>> pointStack = new ArrayDeque<QuadValue<T>>();

		treeStack.push(this);
		pointStack.add(point);

		while (!treeStack.isEmpty()) {
			s = treeStack.pop();
			p = pointStack.pop();

			s.mPoints.add(p);

			// If this point is the only point, we are finished
			if (s.mPoints.size() == 1) {
				continue;
			}

			if (s.mPoints.size() == 2) {
				// System.err.println("aha2 " + p + " " + s.mBounds + " " +
				// s.mPoints.toString());

				// this node is was a leaf but has now become a parent
				// so its points need to be placed into their own quad

				for (QuadValue<T> sp : s.mPoints) {
					treeStack.push(decompose(s, sp));
					pointStack.push(sp);
				}
			} else {
				// We simply pass the point on to a sub-quad where it
				// it will be pushed into its own quad eventually
				treeStack.push(decompose(s, p));
				pointStack.push(p);
			}
		}
	}

	/**
	 * Decompose.
	 *
	 * @param <T>  the generic type
	 * @param tree the tree
	 * @param p    the p
	 * @return the quad tree
	 */
	// Create
	public static <T> QuadTree<T> decompose(QuadTree<T> tree, QuadValue<T> p) {
		double w = tree.mBounds.getWidth() / 2.0;
		double h = tree.mBounds.getHeight() / 2.0;

		QuadTree<T> ret;

		if (p.p.getX() <= tree.mMidPoint.getX()) {
			// in the west

			if (p.p.getY() <= tree.mMidPoint.getY()) {
				// in the north

				if (tree.northWest == null) {
					Rectangle2D bounds = new Rectangle2D.Double(tree.mBounds.getX(), tree.mBounds.getY(), w, h);

					tree.northWest = new QuadTree<T>(bounds);
				}

				ret = tree.northWest;
			} else {
				if (tree.southWest == null) {
					Rectangle2D bounds = new Rectangle2D.Double(tree.mBounds.getX(),
							tree.mBounds.getY() + tree.mBounds.getHeight() / 2, w, h);

					tree.southWest = new QuadTree<T>(bounds);
				}

				ret = tree.southWest;
			}
		} else {
			if (p.p.getY() <= tree.mMidPoint.getY()) {
				// in the north

				if (tree.northEast == null) {
					Rectangle2D bounds = new Rectangle2D.Double(tree.mBounds.getX() + tree.mBounds.getWidth() / 2,
							tree.mBounds.getY(), w, h);

					tree.northEast = new QuadTree<T>(bounds);
				}

				ret = tree.northEast;
			} else {
				if (tree.southEast == null) {
					Rectangle2D bounds = new Rectangle2D.Double(tree.mBounds.getX() + tree.mBounds.getWidth() / 2,
							tree.mBounds.getY() + tree.mBounds.getHeight() / 2, w, h);

					tree.southEast = new QuadTree<T>(bounds);
				}

				ret = tree.southEast;
			}
		}

		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return mMidPoint.toString();
	}

	/**
	 * Mid point.
	 *
	 * @param bounds the bounds
	 * @return the point2 d
	 */
	public static Point2D midPoint(Rectangle2D bounds) {
		return new Point2D.Double(bounds.getX() + bounds.getWidth() / 2.0, bounds.getY() + bounds.getHeight() / 2.0);
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		QuadTree<String> closest = new QuadTree<String>(new Rectangle(0, 0, 200, 200));

		closest.add(new QuadValue<String>(new Point(100, 100), "1"));
		closest.add(new QuadValue<String>(new Point(86, 1), "1"));
		closest.add(new QuadValue<String>(new Point(50, 50), "1"));

		System.err.println("closest " + closest.getClosestPoint(new Point(85, 1)).p);
	}

}
