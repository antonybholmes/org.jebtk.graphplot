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
import java.util.HashSet;
import java.util.Set;

/**
 * Can store x y positions in use. Useful for testing whether to draw a shape
 * over another one if they are occupying the same space.
 *
 * @author Antony Holmes
 */
public class Occupied2d {

	/**
	 * The member occupied.
	 */
	private Set<Point> mOccupied = new HashSet<Point>();

	/**
	 * Adds the.
	 *
	 * @param p the p
	 */
	public void add(Point p) {
		mOccupied.add(p);
	}

	/**
	 * Checks if is occupied.
	 *
	 * @param p the p
	 * @return true, if is occupied
	 */
	public boolean isOccupied(Point p) {
		return mOccupied.contains(p);
	}
}
