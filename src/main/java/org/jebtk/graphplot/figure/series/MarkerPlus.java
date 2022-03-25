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
package org.jebtk.graphplot.figure.series;

import java.awt.Graphics2D;
import java.awt.Point;

import org.jebtk.graphplot.figure.props.StyleProps;
import org.jebtk.graphplot.icons.ShapeStyle;

/**
 * Draws a square at the given point. The square is centered about the point.
 * 
 * @author Antony Holmes
 *
 */
public class MarkerPlus extends Marker {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.figure.series.DataPointShape#
	 * render(java.awt.Graphics2D,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.properties.StyleProps,
	 * java.awt.Point)
	 */
	@Override
	public void plot(Graphics2D g2, StyleProps style, Point p) {

		if (style.getLineStyle().getVisible()) {
			g2.setColor(style.getLineStyle().getColor());
			g2.setStroke(style.getLineStyle().getStroke());

			g2.drawLine(p.x, p.y - mHalfSize.getW() + 1, p.x, p.y + mHalfSize.getW() - 1);

			g2.drawLine(p.x - mHalfSize.getW() + 1, p.y, p.x + mHalfSize.getW() - 1, p.y);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.graphplot.figure.series.Marker#getType()
	 */
	@Override
	public ShapeStyle getType() {
		return ShapeStyle.PLUS;
	}
}
