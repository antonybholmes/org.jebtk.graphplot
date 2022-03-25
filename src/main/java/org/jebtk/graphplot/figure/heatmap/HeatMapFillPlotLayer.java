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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jebtk.core.Mathematics;
import org.jebtk.core.collections.DefaultHashMap;
import org.jebtk.core.collections.HashMapCreator;
import org.jebtk.core.collections.IterMap;
import org.jebtk.core.collections.UniqueArrayList;
import org.jebtk.graphplot.Image;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.Plot;
import org.jebtk.graphplot.figure.PlotClippedLayer;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.colormap.ColorMap;

/**
 * The class HeatMapFillPlotLayer.
 */
public class HeatMapFillPlotLayer extends PlotClippedLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** The m X. */
	protected List<Integer> mX = null;

	/** The m Y. */
	protected List<Integer> mY = null;

	/** The m colors map. */
	protected IterMap<Integer, IterMap<Integer, Color>> mColorsMap = DefaultHashMap
			.create(new HashMapCreator<Integer, Color>());

	/** The m color tile map. */
	protected Map<Color, BufferedImage> mColorTileMap = new HashMap<Color, BufferedImage>();

	/** The m color map. */
	protected ColorMap mColorMap = null;

	/** The m M. */
	protected DataFrame mM;

	/** The m hash id. */
	protected String mHashId;

	@Override
	public String getType() {
		return "Heat Map Fill Layer";
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

		int w = Math.max(1, axes.toPlotX1(1) - axes.toPlotX1(0));
		int h = Math.max(1, axes.toPlotY1(0) - axes.toPlotY1(1));

		int x1 = 0; // axes.toPlotX1(0);
		int y1 = 0; // axes.toPlotY1(m.getRowCount());

		cache(context, figure, subFigure, axes, plot, m, x1, y1, w, h);

		Graphics2D g2Temp = (Graphics2D) g2.create();
		g2Temp.translate(x1, y1);

		if (context == DrawingContext.UI) {
			for (int y : mY) {
				for (int x : mX) {
					g2Temp.drawImage(mColorTileMap.get(mColorsMap.get(x).get(y)), x, y, null);
				}
			}
		} else {
			for (int y : mY) {
				for (int x : mX) {
					g2Temp.setColor(mColorsMap.get(x).get(y));

					g2Temp.fillRect(x, y, w, h);
				}
			}
		}

		g2Temp.dispose();
	}

	/**
	 * Cache.
	 *
	 * @param context   the context
	 * @param subFigure the sub figure
	 * @param axes      the axes
	 * @param plot      the plot
	 * @param m         the m
	 * @param x1        the x 1
	 * @param y1        the y 1
	 * @param w         the w
	 * @param h         the h
	 */
	protected void cache(DrawingContext context, Figure figure, SubFigure subFigure, Axes axes, Plot plot, DataFrame m,
			int x1, int y1, int w, int h) {

		if (mHashId == null || !mHashId.equals(subFigure.hashId())) {
			mX = new UniqueArrayList<Integer>(m.getCols());
			mY = new UniqueArrayList<Integer>(m.getRows());

			for (int i = 0; i < m.getCols(); ++i) {
				int x = axes.toPlotX1(i) - x1;

				mX.add(x);
			}

			for (int i = 0; i < m.getRows(); ++i) {
				int y = axes.toPlotY1(m.getRows() - i) - y1;

				mY.add(y);
			}
		}

		// Monitor for changes in the color plot and the normalization method
		// which will cause the matrix to change

		if (mColorMap == null || !plot.getColorMap().equals(mColorMap) || mM == null || !m.equals(mM) || mHashId == null
				|| !mHashId.equals(subFigure.hashId())) {

			mColorsMap.clear();
			mColorTileMap.clear();

			for (int i = 0; i < m.getRows(); ++i) {
				int y = axes.toPlotY1(m.getRows() - i) - y1;

				for (int j = 0; j < m.getCols(); ++j) {
					int x = axes.toPlotX1(j) - x1;

					if (mColorsMap.get(x).containsKey(y)) {
						continue;
					}

					double v = m.getValue(i, j);

					Color c;

					if (Mathematics.isValidNumber(v)) {
						c = plot.getColorMap().getColor(plot.getNorm().norm(v));
					} else {
						c = Color.WHITE;
					}

					mColorsMap.get(x).put(y, c);

					if (context == DrawingContext.UI) {
						if (!mColorTileMap.containsKey(c)) {
							BufferedImage img = Image.createBuffIm(w, h);

							Graphics2D g2Temp = (Graphics2D) img.createGraphics();
							g2Temp.setColor(c);
							g2Temp.fillRect(0, 0, img.getWidth(), img.getHeight());
							g2Temp.dispose();

							mColorTileMap.put(c, img);
						}
					}
				}
			}
		}

		mColorMap = plot.getColorMap();
		mM = m;
		mHashId = subFigure.hashId();
	}
}
