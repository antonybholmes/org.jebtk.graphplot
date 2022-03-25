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
import org.jebtk.core.collections.UniqueArrayList;
import org.jebtk.graphplot.figure.props.Tick;
import org.jebtk.graphplot.figure.props.TickMarkProps;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.ImageUtils;

/**
 * Draws an x axis on a plot
 * 
 * @author Antony Holmes
 *
 */
public class AxisLabelLayerX1 extends PlotLayer {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The m hash id. */
	protected String mHashId = null;

	/** The m minor ticks. */
	protected List<Integer> mMinorTicks;

	/** The m major ticks. */
	protected List<Integer> mMajorTicks;

	/** The m major tick labels. */
	protected List<String> mMajorTickLabels;

	@Override
	public String getType() {
		return "X1 Axis Labels";
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
		Axis axis = axes.getX1Axis();

		if (mHashId == null || !mHashId.equals(axes.hashId())) {
			// int offset = axes.getMargins().getLeft();

			mMinorTicks = new UniqueArrayList<Integer>(axis.getTicks().getMinorTicks().getTickCount());

			for (Tick x : axis.getTicks().getMinorTicks()) {
				// if (x != 0) {
				mMinorTicks.add(axes.toPlotX1(x.getValue()));
				// }
			}

			mMajorTicks = new UniqueArrayList<Integer>(axis.getTicks().getMajorTicks().getTickCount());

			mMajorTickLabels = new UniqueArrayList<String>(axis.getTicks().getMajorTicks().getTickCount());

			for (int i = 0; i < axis.getTicks().getMajorTicks().getTickCount(); ++i) {
				// for (double y : axis.getTicks().getMajorTicks()) {
				double x = axis.getTicks().getMajorTicks().getTick(i);

				// if (t != 0) {
				mMajorTicks.add(axes.toPlotX1(x));
				mMajorTickLabels.add(axis.getTicks().getMajorTicks().getLabel(i));
				// }
			}

			mHashId = axes.hashId();
		}

		TickMarkProps ticks = axis.getTicks().getMajorTicks();

		int x = 0;
		int y = 0;

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

					String mark = ModernWidget.getTruncatedText(g2, mMajorTickLabels.get(i),
							axes.getMargins().getBottom() - offset);

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

		// axis label

		if (axis.getTitle().getFontStyle().getVisible()) {
			g2.setFont(axis.getTitle().getFontStyle().getFont());
			g2.setColor(axis.getTitle().getFontStyle().getColor());

			x = axes.getMargins().getLeft() + (axes.getInternalSize().getW()
					- g2.getFontMetrics().stringWidth(axes.getX1Axis().getTitle().getText())) / 2;

			y = axes.getPreferredSize().height - g2.getFontMetrics().getDescent();

			g2.drawString(axis.getTitle().getText(), x, y);
		}
	}
}
