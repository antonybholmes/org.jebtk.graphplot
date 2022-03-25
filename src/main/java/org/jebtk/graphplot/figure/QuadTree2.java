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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jebtk.core.IdObject;

/**
 * The class QuadTree2.
 */
public class QuadTree2 extends IdObject implements Iterable<Rectangle> {

	/**
	 * The max objects.
	 */
	private int MAX_OBJECTS = 10;

	/**
	 * The max levels.
	 */
	private int MAX_LEVELS = 5;

	/**
	 * The member level.
	 */
	private int mLevel;

	/**
	 * The member rects.
	 */
	private Set<Rectangle> mRects = new HashSet<Rectangle>();

	/**
	 * The member nodes.
	 */
	private QuadTree2[] mNodes = new QuadTree2[4];

	/**
	 * The member mid point.
	 */
	private Point2D mMidPoint = null;

	/**
	 * The member bounds.
	 */
	private Rectangle mBounds = null;

	/**
	 * Instantiates a new quad tree2.
	 *
	 * @param level  the level
	 * @param bounds the bounds
	 */
	public QuadTree2(int level, Rectangle bounds) {
		mLevel = level;

		mBounds = bounds;

		mMidPoint = midPoint(bounds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Rectangle> iterator() {
		return mRects.iterator();
	}

	/**
	 * Gets the index.
	 *
	 * @param pRect the rect
	 * @return the index
	 */
	private int getIndex(Rectangle pRect) {
		int index = -1;

		double midx = mMidPoint.getX();
		double midy = mMidPoint.getY();

		// Object can completely fit within the top quadrants
		boolean topQuadrant = (pRect.getY() < midy && pRect.getY() + pRect.getHeight() < midy);
		// Object can completely fit within the bottom quadrants
		boolean bottomQuadrant = (pRect.getY() > midy);

		// Object can completely fit within the left quadrants
		if (pRect.getX() < midx && pRect.getX() + pRect.getWidth() < midx) {
			if (topQuadrant) {
				index = 0;
			} else if (bottomQuadrant) {
				index = 2;
			} else {
				index = -1;
			}
		} else if (pRect.getX() > midx) {
			// Object can completely fit within the right quadrants
			if (topQuadrant) {
				index = 1;
			} else if (bottomQuadrant) {
				index = 3;
			} else {
				index = -1;
			}
		}

		return index;
	}

	/**
	 * Split.
	 */
	private void split() {
		int subWidth = (int) (mBounds.getWidth() / 2);
		int subHeight = (int) (mBounds.getHeight() / 2);
		int x = (int) mBounds.getX();
		int y = (int) mBounds.getY();

		// top left
		mNodes[0] = new QuadTree2(mLevel + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));

		// top right
		mNodes[1] = new QuadTree2(mLevel + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));

		// bottom left
		mNodes[2] = new QuadTree2(mLevel + 1, new Rectangle(x, y, subWidth, subHeight));

		// bottom right
		mNodes[3] = new QuadTree2(mLevel + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
	}

	/**
	 * Retrieve.
	 *
	 * @param rect  the rect
	 * @param rects the rects
	 */
	public void retrieve(Rectangle rect, Set<Rectangle> rects) {
		int index = getIndex(rect);

		if (index != -1 && mNodes[0] != null) {
			mNodes[index].retrieve(rect, rects);
		}

		rects.addAll(mRects);
	}

	/**
	 * Insert.
	 *
	 * @param rect the rect
	 */
	public void insert(Rectangle rect) {
		if (mNodes[0] != null) {
			int index = getIndex(rect);

			if (index != -1) {
				mNodes[index].insert(rect);

				return;
			}
		}

		mRects.add(rect);

		if (mRects.size() > MAX_OBJECTS && mLevel < MAX_LEVELS) {
			if (mNodes[0] == null) {
				split();
			}

			Set<Rectangle> remove = new HashSet<Rectangle>();

			for (Rectangle r : mRects) {
				int index = getIndex(r);

				if (index != -1) {
					mNodes[index].insert(r);

					remove.add(r);
				}
			}

			for (Rectangle r : remove) {
				mRects.remove(r);
			}
		}
	}

	/**
	 * Clear.
	 */
	public void clear() {
		mRects.clear();

		for (int i = 0; i < mNodes.length; i++) {
			if (mNodes[i] != null) {
				mNodes[i].clear();
				mNodes[i] = null;
			}
		}
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
	public static Point2D midPoint(Rectangle bounds) {
		return new Point.Double(bounds.getX() + bounds.getWidth() / 2.0, bounds.getY() + bounds.getHeight() / 2.0);
	}
}
