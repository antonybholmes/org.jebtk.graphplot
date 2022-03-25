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

import org.jebtk.core.collections.UniqueArrayList;
import org.jebtk.graphplot.figure.props.Tick;
import org.jebtk.modern.ModernWidget;

/**
 * Draws an x axis on a plot
 * 
 * @author Antony Holmes
 *
 */
public class AxisLayerX2 extends AxisLayerX {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getType() {
		return "X2 Axis";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.graphplot.figure.AxisLayerX#drawAxisLine(java.awt.Graphics2D,
	 * org.graphplot.figure.Axes, org.graphplot.figure.Axis, int)
	 */
	@Override
	public void drawAxisLine(Graphics2D g2, Axes axes, Axis axis, int y) {

		if (axis.getLineStyle().getVisible()) {
			g2.setStroke(axis.getLineStyle().getStroke());

			g2.setColor(axis.getLineStyle().getColor());

			g2.drawLine(axes.getMargins().getLeft(), y, axes.getMargins().getLeft() + axes.getInternalSize().getW() - 1,
					y);

			// If xmin is less than zero and xmax greater than zero,
			// draw a line at x=0

			if (axis.getShowZerothLine() && axis.getLimits().getMin() < 0 && axis.getLimits().getMax() > 0) {
				int x = axes.toPlotX2(0);
				y = axes.getMargins().getTop();

				g2.drawLine(x, y, x, y + axes.getInternalSize().getH() - 1);
			}
		}
	}

	@Override
	public void drawTitle(Graphics2D g2, Axes axes, Axis axis) {
		if (axis.getTitle().getFontStyle().getVisible()) {
			g2.setFont(axis.getTitle().getFontStyle().getFont());
			g2.setColor(axis.getTitle().getFontStyle().getColor());

			int x = (axes.getInternalSize().getW()
					- g2.getFontMetrics().stringWidth(axes.getX1Axis().getTitle().getText())) / 2;

			int y = -axes.getMargins().getTop() + ModernWidget.getStringHeight(g2);

			g2.drawString(axis.getTitle().getText(), x, y);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.graphplot.figure.AxisLayerX#cache(org.graphplot.figure.Axes,
	 * org.graphplot.figure.Axis)
	 */
	@Override
	public void cache(Axes axes, Axis axis) {
		if (mHashId == null || !mHashId.equals(axes.hashId())) {
			mMinorTicks = new UniqueArrayList<Integer>(axis.getTicks().getMinorTicks().getTickCount());

			for (Tick t : axis.getTicks().getMinorTicks()) {
				mMinorTicks.add(axes.toPlotX1(t.getValue()));
			}

			mMajorTicks = new UniqueArrayList<Integer>(axis.getTicks().getMajorTicks().getTickCount());

			mMajorTickLabels = new UniqueArrayList<String>(axis.getTicks().getMajorTicks().getTickCount());

			for (int i = 0; i < axis.getTicks().getMajorTicks().getTickCount(); ++i) {
				// for (double y : axis.getTicks().getMajorTicks()) {
				double t = axis.getTicks().getMajorTicks().getTick(i);

				if (t != 0) {
					mMajorTicks.add(axes.toPlotX1(t));
					mMajorTickLabels.add(axis.getTicks().getMajorTicks().getLabel(i));
				}
			}

			mHashId = axes.hashId();
		}
	}
}
