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

import org.jebtk.core.collections.UniqueArrayList;
import org.jebtk.graphplot.figure.props.Tick;
import org.jebtk.graphplot.figure.props.TickMarkProps;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Represents a 2D Cartesian graph. This class draws basic axes and titles but
 * should be subclassed to provide specific plot functionality.
 * 
 * @author Antony Holmes
 *
 */
public class AxisLayerY1 extends AxisLayerY {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** The m hash id. */
	private String mHashId = null;

	/** The m minor ticks. */
	private List<Integer> mMinorTicks;

	/** The m major ticks. */
	private List<Integer> mMajorTicks;

	/** The m major tick labels. */
	private UniqueArrayList<String> mMajorTickLabels;

	@Override
	public String getType() {
		return "Y1 Axis";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.figure.AxesLayer#plot(java.awt.
	 * Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Figure,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Axes)
	 */
	@Override
	public void drawPlot(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes) {
		Axis axis = axes.getY1Axis();

		if (!axis.getVisible()) {
			return;
		}

		// Cache positions if not already done so
		if (mHashId == null || !mHashId.equals(axes.hashId())) {
			mMinorTicks = new UniqueArrayList<Integer>(axis.getTicks().getMinorTicks().getTickCount());

			for (Tick t : axis.getTicks().getMinorTicks()) {
				// if (y != 0) {
				mMinorTicks.add(axes.toPlotY1(t.getValue()));
				// }
			}

			mMajorTicks = new UniqueArrayList<Integer>(axis.getTicks().getMajorTicks().getTickCount());

			mMajorTickLabels = new UniqueArrayList<String>(axis.getTicks().getMajorTicks().getTickCount());

			for (int i = 0; i < axis.getTicks().getMajorTicks().getTickCount(); ++i) {
				// for (double y : axis.getTicks().getMajorTicks()) {
				double y = axis.getTicks().getMajorTicks().getTick(i);

				// System.err.println("y1 " + y + " " + axes.toPlotY1(y));

				// if (y != 0) {
				mMajorTicks.add(axes.toPlotY1(y));
				mMajorTickLabels.add(axis.getTicks().getMajorTicks().getLabel(i));
				// }
			}
		}

		//
		// y axis
		//

		int x = 0; // axes.getMargins().getLeft();

		// the line

		drawLine(g2, axes, axis, x);

		//
		// Tick lines
		//

		drawTicks(g2, axes, axis.getTicks().getMinorTicks(), mMinorTicks, axis.getTicks().getDrawInside(), x, context);

		drawTicks(g2, axes, axis.getTicks().getMajorTicks(), mMajorTicks, axis.getTicks().getDrawInside(), x, context);

		//
		// The y labels
		//

		drawLabels(g2, axis, mMajorTicks, mMajorTickLabels, x);

		// labels

		// System.err.println("say what " + axis.getName() + " " +
		// axes.getMargins());

		drawTitle(g2, axes, axis,
				g2.getFontMetrics().getAscent() + g2.getFontMetrics().getDescent() - axes.getMargins().getLeft());

		mHashId = subFigure.hashId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.graphplot.figure.AxisLayerY#drawLabels(java.awt.Graphics2D,
	 * org.graphplot.figure.Axis, java.util.List, java.util.List, int)
	 */
	@Override
	protected void drawLabels(Graphics2D g2, Axis axis, List<Integer> majorTicks, List<String> majorTicksLabels,
			int x) {
		if (axis.getTicks().getMajorTicks().getFontStyle().getVisible()) {
			g2.setColor(axis.getTicks().getMajorTicks().getFontStyle().getColor());
			g2.setFont(axis.getTicks().getMajorTicks().getFontStyle().getFont());

			int xOffset = x - axis.getTicks().getMajorTicks().getTickSize()
					- axis.getTicks().getMajorTicks().getTickSpacing();
			int yOffset = (g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent()) / 2;

			for (int i = 0; i < majorTicks.size(); ++i) {
				int y = majorTicks.get(i) + yOffset;

				String mark = majorTicksLabels.get(i);

				x = xOffset - g2.getFontMetrics().stringWidth(mark);

				g2.drawString(mark, x, y);
			}
		}
	}

	/**
	 * Draw ticks.
	 *
	 * @param g2         the g2
	 * @param axes       the axes
	 * @param ticks      the ticks
	 * @param marks      the marks
	 * @param drawInside the draw inside
	 * @param x          the x
	 * @param context    the context
	 */
	protected static void drawTicks(Graphics2D g2, Axes axes, TickMarkProps ticks, List<Integer> marks,
			boolean drawInside, int x, DrawingContext context) {

		if (ticks.getLineStyle().getVisible()) {
			if (!drawInside) {
				x -= (ticks.getTickSize() - 1);
			}

			/*
			 * if (context == DrawingContext.SCREEN) { BufferedImage im =
			 * Image.createTransBuffIm(ticks.getTickSize(), 1);
			 * 
			 * Graphics2D g2Temp = (Graphics2D)im.createGraphics();
			 * g2Temp.setColor(ticks.getLineStyle().getColor());
			 * g2Temp.setStroke(ticks.getLineStyle().getStroke());
			 * 
			 * g2Temp.drawLine(0, 0, ticks.getTickSize(), 0); g2Temp.dispose();
			 * 
			 * for (int y : marks) { g2.drawImage(im, x, y, null); } } else { // Render to
			 * file g2.setColor(ticks.getLineStyle().getColor());
			 * g2.setStroke(ticks.getLineStyle().getStroke());
			 * 
			 * int x2 = x + ticks.getTickSize();
			 * 
			 * for (int y : marks) { g2.drawLine(x, y, x2, y); } }
			 */

			g2.setColor(ticks.getLineStyle().getColor());
			g2.setStroke(ticks.getLineStyle().getStroke());

			int x2 = x + ticks.getTickSize() - 1;

			for (int y : marks) {
				g2.drawLine(x, y, x2, y);
			}
		}
	}
}
