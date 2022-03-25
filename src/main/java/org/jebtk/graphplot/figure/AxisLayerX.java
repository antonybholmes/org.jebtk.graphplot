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
import org.jebtk.graphplot.figure.props.TickMarkProps;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.ImageUtils;

/**
 * Draws an x axis on a plot
 * 
 * @author Antony Holmes
 *
 */
public abstract class AxisLayerX extends AxesLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The constant X_AXIS_Z.
	 */
	public static final int X_AXIS_Z = -100;

	/** The m hash id. */
	protected String mHashId = null;

	/** The m minor ticks. */
	protected List<Integer> mMinorTicks;

	/** The m major ticks. */
	protected List<Integer> mMajorTicks;

	/** The m major tick labels. */
	protected List<String> mMajorTickLabels;

	/**
	 * Plot.
	 *
	 * @param g2        the g2
	 * @param context   the context
	 * @param subFigure the sub figure
	 * @param axes      the axes
	 * @param axis      the axis
	 * @param y         the y
	 * @param inside    the inside
	 */
	public void plot(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes, Axis axis,
			int y, boolean inside) {

		int x;
		// int y;

		//
		// x axis
		//

		if (!axis.getVisible()) {
			return;
		}

		// The x axis line

		// y = axes.getMargins().getTop() + axes.getInternalPlotSize().getH() - 1;

		drawAxisLine(g2, axes, axis, y);

		cache(axes, axis);

		drawTicks(g2, axes, axis.getTicks().getMinorTicks(), inside, mMinorTicks, y);

		TickMarkProps ticks = axis.getTicks().getMajorTicks();

		drawTicks(g2, axes, ticks, inside, mMajorTicks, y);

		//
		// Axis tick labels
		//

		if (ticks.getFontStyle().getVisible()) {
			g2.setFont(ticks.getFontStyle().getFont());
			g2.setColor(ticks.getFontStyle().getColor());

			int fontHeight = ImageUtils.getFontHeight(g2);

			int offset = ticks.getTickSpacing();

			if (!axis.getTicks().getDrawInside()) {
				offset += ticks.getTickSize();
			}

			y += offset;

			double rotation = ticks.getRotation();

			if (rotation == Mathematics.HALF_PI) {

				for (int i = 0; i < mMajorTicks.size(); ++i) {
					x = mMajorTicks.get(i);

					String mark = mMajorTickLabels.get(i);

					// System.err.println("mark1 " + i + " " + mark);

					Graphics2D g2Temp = (Graphics2D) g2.create();

					g2Temp.translate(x, y);
					g2Temp.rotate(rotation);

					g2Temp.drawString(mark, 0,
							(g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent()) / 2);

					g2Temp.dispose();
				}
			} else if (rotation == -Mathematics.HALF_PI) {

				for (int i = 0; i < mMajorTicks.size(); ++i) {
					x = mMajorTicks.get(i);

					String mark = mMajorTickLabels.get(i);

					// System.err.println("mark2 " + i + " " + mark);

					Graphics2D g2Temp = (Graphics2D) g2.create();

					int w = ModernWidget.getStringWidth(g2Temp, mark);

					g2Temp.translate(x, y + w);
					g2Temp.rotate(rotation);

					g2Temp.drawString(mark, 0,
							(g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent()) / 2);

					g2Temp.dispose();
				}
			} else {
				y += fontHeight;

				for (int i = 0; i < mMajorTicks.size(); ++i) {
					x = mMajorTicks.get(i);

					String mark = mMajorTickLabels.get(i);

					x -= g2.getFontMetrics().stringWidth(mark) / 2;

					g2.drawString(mark, x, y);
				}
			}
		}

		//
		// Axis label
		//

		drawTitle(g2, axes, axis);
	}

	public abstract void drawTitle(Graphics2D g2, Axes axes, Axis axis);

	/**
	 * Draw axis line.
	 *
	 * @param g2   the g2
	 * @param axes the axes
	 * @param axis the axis
	 * @param y    the y
	 */
	public abstract void drawAxisLine(Graphics2D g2, Axes axes, Axis axis, int y);

	/**
	 * Cache.
	 *
	 * @param axes the axes
	 * @param axis the axis
	 */
	public abstract void cache(Axes axes, Axis axis);

	/**
	 * Draw ticks.
	 *
	 * @param g2         the g2
	 * @param axes       the axes
	 * @param ticks      the ticks
	 * @param drawInside the draw inside
	 * @param marks      the marks
	 * @param y          the y
	 */
	private static void drawTicks(Graphics2D g2, Axes axes, TickMarkProps ticks, boolean drawInside,
			List<Integer> marks, int y) {

		// int offset = axes.getMargins().getTop() +
		// axes.getInternalPlotSize().getH() - 1;

		if (ticks.getLineStyle().getVisible()) {
			g2.setColor(ticks.getLineStyle().getColor());
			g2.setStroke(ticks.getLineStyle().getStroke());

			/*
			 * x = axes.getMargins().getLeft();
			 * 
			 * if (drawInside) { for (double t : ticks) { if (t == 0) { continue; }
			 * 
			 * x = axes.toPlotX(t);
			 * 
			 * g2.drawLine(x, offset, x, offset - ticks.getTickSize()); } } else { for
			 * (double t : ticks) { if (t == 0) { continue; }
			 * 
			 * x = axes.toPlotX(t);
			 * 
			 * g2.drawLine(x, offset, x, offset + ticks.getTickSize()); } }
			 */

			if (drawInside) {
				y -= (ticks.getTickSize() - 1);
			}

			int y2 = y + ticks.getTickSize() - 1;

			for (int x : marks) {
				g2.drawLine(x, y, x, y2);
			}
		}
	}
}
