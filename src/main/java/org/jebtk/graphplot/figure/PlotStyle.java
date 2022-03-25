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

/**
 * Contols how peaks are displayed.
 * 
 * @author Antony Holmes
 *
 */
public enum PlotStyle {

	/**
	 * The joined.
	 */
	JOINED,

	/**
	 * The filled.
	 */
	FILLED,

	/**
	 * The lines.
	 */
	LINES,

	/**
	 * The bars.
	 */
	BARS,

	/**
	 * The scatter.
	 */
	SCATTER,

	/**
	 * The filled smooth.
	 */
	FILLED_SMOOTH,

	/**
	 * The joined smooth.
	 */
	JOINED_SMOOTH_ZERO_ENDS,

	/** The joined smooth. */
	JOINED_SMOOTH,
	/** The filled trapezoid. */
	FILLED_TRAPEZOID,

	/** The segments. */
	SEGMENTS,

	/** The heatmap. */
	HEATMAP,

	/** Bar chart requiring xy data in matrix form. */
	BAR_PLOT,

	/** BARS fill the width of a tick and only the y data is required. */
	JOINED_BARS,

	VLINES;

	/**
	 * Parses the.
	 *
	 * @param text the text
	 * @return the figure style
	 */
	public static PlotStyle parse(String text) {
		if (text.equals("joined")) {
			return PlotStyle.JOINED;
		} else if (text.equals("filled")) {
			return PlotStyle.FILLED;
		} else if (text.equals("lines")) {
			return PlotStyle.LINES;
		} else if (text.equals("scatter")) {
			return PlotStyle.SCATTER;
		} else if (text.equals("filled_smooth")) {
			return PlotStyle.FILLED_SMOOTH;
		} else if (text.equals("joined_smooth")) {
			return PlotStyle.JOINED_SMOOTH;
		} else if (text.equals("segments")) {
			return PlotStyle.SEGMENTS;
		} else if (text.equals("Heat Map")) {
			return PlotStyle.HEATMAP;
		} else {
			return PlotStyle.BARS;
		}
	}
}
