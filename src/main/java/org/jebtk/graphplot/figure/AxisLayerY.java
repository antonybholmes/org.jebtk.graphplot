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

import java.awt.Graphics2D;
import java.util.List;

import org.jebtk.core.Mathematics;

/**
 * The Y1 and Y2 labels are essentially the same accept for where the labels are
 * positioned.
 * 
 * @author Antony Holmes
 *
 */
public abstract class AxisLayerY extends AxesLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The constant Y_AXIS_Z.
	 */
	public static final int Y_AXIS_Z = -200;

	/**
	 * The constant Y_ROTATION.
	 */
	public static final double Y_ROTATION = Mathematics.TWO_PI * 0.75;

	/**
	 * Draw labels.
	 *
	 * @param g2               the g2
	 * @param axis             the axis
	 * @param majorTicks       the major ticks
	 * @param majorTicksLabels the major ticks labels
	 * @param x                the x
	 */
	protected abstract void drawLabels(Graphics2D g2, Axis axis, List<Integer> majorTicks,
			List<String> majorTicksLabels, int x);

	/**
	 * Draw title.
	 *
	 * @param g2   the g2
	 * @param axes the axes
	 * @param axis the axis
	 * @param x    the x
	 */
	public static void drawTitle(Graphics2D g2, Axes axes, Axis axis, int x) {

		if (axis.getTitle().getFontStyle().getVisible()) {
			Graphics2D g2Temp = (Graphics2D) g2.create();

			try {
				g2Temp.translate(x, axes.getInternalSize().getH()
						- (axes.getInternalSize().getH() - g2.getFontMetrics().stringWidth(axis.getTitle().getText()))
								/ 2);

				// Default to drawing the label vertically
				g2Temp.rotate(Y_ROTATION);
				g2Temp.setColor(axis.getTitle().getFontStyle().getColor());
				g2Temp.setFont(axis.getTitle().getFontStyle().getFont());
				g2Temp.drawString(axis.getTitle().getText(), 0, 0);
			} finally {
				g2Temp.dispose();
			}
		}
	}

	/**
	 * Draw line.
	 *
	 * @param g2   the g2
	 * @param axes the axes
	 * @param axis the axis
	 * @param x    the x
	 */
	protected static void drawLine(Graphics2D g2, Axes axes, Axis axis, int x) {
		if (axis.getLineStyle().getVisible()) {
			g2.setColor(axis.getLineStyle().getColor());
			g2.setStroke(axis.getLineStyle().getStroke());

			// the y axis line
			g2.drawLine(x, 0, x, axes.getInternalSize().getH());

			// Draw a line at y = 0
			if (axis.getShowZerothLine() && axis.getLimits().getMin() < 0 && axis.getLimits().getMax() > 0) {
				x = 0;
				int y = axes.toPlotY1(0);

				g2.drawLine(x, y, axes.getInternalSize().getW(), y);
			}
		}
	}

}
