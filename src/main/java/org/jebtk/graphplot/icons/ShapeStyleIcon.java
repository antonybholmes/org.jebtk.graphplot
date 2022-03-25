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

import java.awt.Graphics2D;
import java.awt.Point;

import org.jebtk.core.Props;
import org.jebtk.graphplot.figure.props.StyleProps;
import org.jebtk.graphplot.figure.props.StyleProps;
import org.jebtk.graphplot.figure.series.Marker;
import org.jebtk.modern.graphics.icons.ModernVectorIcon;

/**
 * The class ShapeStyleIcon.
 */
public class ShapeStyleIcon extends ModernVectorIcon {

	/** The Constant DEFAULT_STYLE. */
	private static final StyleProps DEFAULT_STYLE = new StyleProps();

	/**
	 * The member shape.
	 */
	private Marker mShape;

	/**
	 * Instantiates a new shape style icon.
	 *
	 * @param shape the shape
	 */
	public ShapeStyleIcon(Marker shape) {
		mShape = shape;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.abh.common.ui.ui.icons.ModernIcon#drawForeground(java.awt.Graphics2D,
	 * java.awt.Rectangle)
	 */
	@Override
	public void drawIcon(Graphics2D g2, int x, int y, int w, int h, Props params) {
		mShape.setSize(h);

		mShape.plot(g2, DEFAULT_STYLE, new Point(x + w / 2, y + h / 2));
	}

}
