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
package org.jebtk.graphplot.figure.props;

import java.awt.BasicStroke;
import java.awt.Stroke;

import org.jebtk.modern.theme.ModernTheme;

/**
 * The Enum StrokeStyle.
 */
public enum StrokeStyle {

	/** The single. */
	SINGLE,

	/** The dashed. */
	DASHED,

	/** The dotted. */
	DOTTED,

	/** The long dash. */
	LONG_DASH,

	/** The dashed dotted. */
	DASHED_DOTTED;

	/**
	 * Creates the single stroke.
	 *
	 * @param w the w
	 * @return the stroke
	 */
	public static final Stroke createSingleStroke(int w) {
		return new BasicStroke(w);
	}

	/**
	 * The constant DASHED_LINE_STROKE.
	 *
	 * @param w the w
	 * @return the stroke
	 */
	public static final Stroke createDashedStroke(int w) {
		return new BasicStroke(w, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, ModernTheme.DASH_MARKS, 0);
	}

	/**
	 * Creates the dotted stroke.
	 *
	 * @param w the w
	 * @return the stroke
	 */
	public static final Stroke createDottedStroke(int w) {
		return new BasicStroke(w, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, ModernTheme.DOTTED_MARKS, 0);
	}

	/**
	 * Creates the long dash stroke.
	 *
	 * @param w the w
	 * @return the stroke
	 */
	public static final Stroke createLongDashStroke(int w) {
		return new BasicStroke(w, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, ModernTheme.LONG_DASH_MARKS, 0);
	}

	/**
	 * Creates the dash dotted stroke.
	 *
	 * @param w the w
	 * @return the stroke
	 */
	public static final Stroke createDashDottedStroke(int w) {
		return new BasicStroke(w, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, ModernTheme.DASH_DOT_MARKS, 0);
	}

	/**
	 * Gets the stroke.
	 *
	 * @param style the style
	 * @param w     the w
	 * @return the stroke
	 */
	public static Stroke getStroke(StrokeStyle style, int w) {
		switch (style) {
		case DASHED:
			return createDashedStroke(w);
		case DOTTED:
			return createDottedStroke(w);
		case LONG_DASH:
			return createLongDashStroke(w);
		case DASHED_DOTTED:
			return createDashDottedStroke(w);
		default:
			return createSingleStroke(w);
		}
	}
}
