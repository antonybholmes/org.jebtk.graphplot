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
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Represents a 2D Cartesian graph. This class draws basic axes and titles but
 * should be subclassed to provide specific plot functionality.
 * 
 * @author Antony Holmes
 *
 */
public class AxisLabelLayerY2 extends PlotLayer {

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
		return "Y2 Axis Labels";
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
	public void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes,
			Plot plot, DataFrame m) {
		Axis axis = axes.getY2Axis();

		// Cache positions if not already done so
		if (mHashId == null || !mHashId.equals(subFigure.hashId())) {
			mMinorTicks = new UniqueArrayList<Integer>(axis.getTicks().getMinorTicks().getTickCount());

			for (Tick t : axis.getTicks().getMinorTicks()) {
				mMinorTicks.add(axes.toPlotY2(t.getValue()));
			}

			mMajorTicks = new UniqueArrayList<Integer>(axis.getTicks().getMajorTicks().getTickCount());

			mMajorTickLabels = new UniqueArrayList<String>(axis.getTicks().getMajorTicks().getTickCount());

			for (int i = 0; i < axis.getTicks().getMajorTicks().getTickCount(); ++i) {
				// for (double y : axis.getTicks().getMajorTicks()) {
				double y = axis.getTicks().getMajorTicks().getTick(i);

				// if (y != 0) {
				mMajorTicks.add(axes.toPlotY2(y));
				mMajorTickLabels.add(axis.getTicks().getMajorTicks().getLabel(i));
				// }
			}
		}

		int x = 0;

		//
		// The y labels
		//

		drawLabels(g2, axis, mMajorTicks, mMajorTickLabels, x);

		// labels

		// System.err.println("say what " + axis.getName() + " " +
		// axis.getTitle().getFontStyle().getVisible());

		drawTitle(g2, axes, axis,
				axes.getPreferredSize().width - g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent());

		mHashId = subFigure.hashId();
	}

	/**
	 * Draw labels.
	 *
	 * @param g2               the g2
	 * @param axis             the axis
	 * @param majorTicks       the major ticks
	 * @param majorTicksLabels the major ticks labels
	 * @param x                the x
	 */
	private static void drawLabels(Graphics2D g2, Axis axis, List<Integer> majorTicks, List<String> majorTicksLabels,
			int x) {
		if (axis.getTicks().getMajorTicks().getFontStyle().getVisible()) {
			g2.setColor(axis.getTicks().getMajorTicks().getFontStyle().getColor());
			g2.setFont(axis.getTicks().getMajorTicks().getFontStyle().getFont());

			int xOffset = x + axis.getTicks().getMajorTicks().getTickSize()
					+ axis.getTicks().getMajorTicks().getTickSpacing();
			int yOffset = (g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent()) / 2;

			for (int i = 0; i < majorTicks.size(); ++i) {
				int y = majorTicks.get(i) + yOffset;

				String mark = majorTicksLabels.get(i);

				g2.drawString(mark, xOffset, y);
			}
		}
	}

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
				g2Temp.rotate(AxisLayerY.Y_ROTATION);
				g2Temp.setColor(axis.getTitle().getFontStyle().getColor());
				g2Temp.setFont(axis.getTitle().getFontStyle().getFont());
				g2Temp.drawString(axis.getTitle().getText(), 0, 0);
			} finally {
				g2Temp.dispose();
			}
		}
	}
}
