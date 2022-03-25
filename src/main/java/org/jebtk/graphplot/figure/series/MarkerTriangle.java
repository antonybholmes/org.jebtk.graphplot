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
import java.awt.geom.GeneralPath;

import org.jebtk.graphplot.figure.props.StyleProps;
import org.jebtk.graphplot.icons.ShapeStyle;

/**
 * Draws a diamond at the given point. The square is centered about the point.
 * 
 * @author Antony Holmes
 *
 */
public class MarkerTriangle extends Marker {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** The m path. */
	private GeneralPath mPath;

	/**
	 * Instantiates a new marker triangle.
	 */
	public MarkerTriangle() {
		update();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.common.event.ChangeListeners#fireChanged()
	 */
	@Override
	public void fireChanged() {
		update();

		super.fireChanged();

	}

	/**
	 * Update.
	 */
	private void update() {
		int h = (int) (Math.sin(Math.PI / 3) * mDim.getW());
		int h2 = h / 2;

		mPath = new GeneralPath();

		mPath.moveTo(-mHalfSize.getW() + 1, h2);
		mPath.lineTo(0, -h2);
		mPath.lineTo(mHalfSize.getW() - 1, h2);
		mPath.closePath();
	}

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
		Graphics2D g2Temp = (Graphics2D) g2.create();

		g2Temp.translate(p.x, p.y);

		if (style.getFillStyle().getVisible()) {
			g2Temp.setColor(style.getFillStyle().getColor());
			g2Temp.fill(mPath);
		}

		if (style.getLineStyle().getVisible()) {
			g2Temp.setStroke(style.getLineStyle().getStroke());
			g2Temp.setColor(style.getLineStyle().getColor());
			g2Temp.draw(mPath);
		}

		g2Temp.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.graphplot.figure.series.Marker#getType()
	 */
	@Override
	public ShapeStyle getType() {
		return ShapeStyle.TRIANGLE;
	}

}
