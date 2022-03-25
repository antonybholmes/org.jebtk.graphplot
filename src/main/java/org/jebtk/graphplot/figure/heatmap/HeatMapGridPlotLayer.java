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
package org.jebtk.graphplot.figure.heatmap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.jebtk.graphplot.Image;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.Plot;
import org.jebtk.graphplot.figure.PlotClippedLayer;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The class HeatMapOutlinePlotLayer.
 */
public class HeatMapGridPlotLayer extends PlotClippedLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** The m X. */
	private Map<Integer, Integer> mX = new HashMap<Integer, Integer>();

	/** The m Y 1. */
	private Map<Integer, Integer> mY1 = new HashMap<Integer, Integer>();

	/** The m Y 2. */
	private Map<Integer, Integer> mY2 = new HashMap<Integer, Integer>();

	/** The m hash id. */
	private String mHashId = null;

	@Override
	public String getType() {
		return "Heat Map Grid Layer";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.PlotClippedLayer#plotLayer(
	 * java.awt.Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Figure,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Axes,
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.Plot,
	 * org.abh.lib.math.matrix.DataFrame)
	 */
	@Override
	public void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes,
			Plot plot, DataFrame m) {

		int x1 = axes.getMargins().getLeft();
		int x2 = x1 + axes.getInternalSize().getW() - 1;

		int y1 = axes.getMargins().getTop();
		int y2 = y1 + axes.getInternalSize().getH() - 1;

		if (mHashId == null || !mHashId.equals(subFigure.hashId())) {
			mX.clear(); // = new UniqueArrayList<Integer>(m.getColumnCount());
			// = new UniqueArrayList<Integer>(m.getRowCount());

			for (int i = 0; i < m.getCols(); ++i) {
				mX.put(axes.toPlotX1(i), i);
			}

			mY1.clear();

			for (int i = 0; i < m.getRows(); ++i) {
				mY1.put(axes.toPlotY1(m.getRows() - i), i);
			}

			mY2.clear();

			for (int i = 0; i < m.getRows(); ++i) {
				mY2.put(axes.toPlotY2(m.getRows() - i), i);
			}
		}

		if (context == DrawingContext.UI) {
			BufferedImage xImg = Image.createTransBuffIm(x2 - x1 + 1, 1);
			BufferedImage yImg = Image.createTransBuffIm(1, y2 - y1 + 1);

			Graphics2D g2Temp = (Graphics2D) yImg.createGraphics();
			g2Temp.setColor(axes.getX1Axis().getGrid().getColor());
			g2Temp.drawLine(0, 0, 0, yImg.getHeight());
			g2Temp.dispose();

			g2Temp = (Graphics2D) xImg.createGraphics();
			g2Temp.setColor(axes.getY1Axis().getGrid().getColor());
			g2Temp.drawLine(0, 0, xImg.getWidth(), 0);
			g2Temp.dispose();

			if (axes.getX1Axis().getGrid().getVisible()) {
				for (int x : mX.keySet()) {
					g2.drawImage(yImg, x, y1, null);
				}
			}

			if (axes.getY1Axis().getGrid().getVisible()) {
				for (int y : mY1.keySet()) {
					g2.drawImage(xImg, x1, y, null);
				}
			}

			if (axes.getY2Axis().getGrid().getVisible()) {
				for (int y : mY2.keySet()) {
					g2.drawImage(xImg, x1, y, null);
				}
			}
		} else {
			if (axes.getX1Axis().getGrid().getVisible()) {
				g2.setColor(axes.getX1Axis().getGrid().getColor());

				for (int x : mX.keySet()) {
					g2.drawLine(x, y1, x, y2);
				}
			}

			if (axes.getY1Axis().getGrid().getVisible()) {
				g2.setColor(axes.getY1Axis().getGrid().getColor());

				for (int y : mY1.keySet()) {
					g2.drawLine(x1, y, x2, y);
				}
			}

			if (axes.getY2Axis().getGrid().getVisible()) {
				g2.setColor(axes.getY2Axis().getGrid().getColor());

				for (int y : mY2.keySet()) {
					g2.drawLine(x1, y, x2, y);
				}
			}
		}

		mHashId = subFigure.hashId();
	}
}
