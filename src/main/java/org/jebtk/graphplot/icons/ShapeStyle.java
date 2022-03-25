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
package org.jebtk.graphplot.icons;

import org.jebtk.graphplot.figure.series.Marker;
import org.jebtk.graphplot.figure.series.MarkerBar;
import org.jebtk.graphplot.figure.series.MarkerCircle;
import org.jebtk.graphplot.figure.series.MarkerCross;
import org.jebtk.graphplot.figure.series.MarkerDiamond;
import org.jebtk.graphplot.figure.series.MarkerMinus;
import org.jebtk.graphplot.figure.series.MarkerPlus;
import org.jebtk.graphplot.figure.series.MarkerSquare;
import org.jebtk.graphplot.figure.series.MarkerTriangle;

/**
 * The enum ShapeStyle.
 */
public enum ShapeStyle {

	/**
	 * The bar.
	 */
	BAR,

	/**
	 * The circle.
	 */
	CIRCLE,

	/**
	 * The cross.
	 */
	CROSS,

	/**
	 * The diamond.
	 */
	DIAMOND,

	/**
	 * The minus.
	 */
	MINUS,

	/**
	 * The plus.
	 */
	PLUS,

	/**
	 * The square.
	 */
	SQUARE,

	/**
	 * The triangle.
	 */
	TRIANGLE;

	/**
	 * Gets the shape.
	 *
	 * @param type the type
	 * @return the shape
	 */
	public static Marker getShape(ShapeStyle type) {
		switch (type) {
		case BAR:
			return new MarkerBar();
		case PLUS:
			return new MarkerPlus();
		case DIAMOND:
			return new MarkerDiamond();
		case SQUARE:
			return new MarkerSquare();
		case CROSS:
			return new MarkerCross();
		case MINUS:
			return new MarkerMinus();
		case TRIANGLE:
			return new MarkerTriangle();
		default:
			return new MarkerCircle();
		}
	}
}
