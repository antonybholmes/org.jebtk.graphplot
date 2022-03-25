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
import java.util.List;

import org.jebtk.core.collections.DefaultHashMap;
import org.jebtk.core.collections.HashMapCreator;
import org.jebtk.core.collections.IterMap;
import org.jebtk.core.collections.UniqueArrayList;
import org.jebtk.core.sys.SysUtils;
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
public class ImageFillPlotLayer extends PlotClippedLayer {

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

	/** The m color map. */
	protected ColorMap mColorMap = null;

	/** The m M. */
	protected DataFrame mM;

	/** The m hash id. */
	protected String mHashId;

	public ImageFillPlotLayer() {
		setRasterMode(true);
	}

	@Override
	public String getType() {
		return "Image Fill Layer";
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

		// int h = Math.max(1, axes.toPlotY1(0) - axes.toPlotY1(1));

		cache(context, figure, subFigure, axes, plot, m);

		for (int i = 0; i < mY.size() - 1; ++i) {
			SysUtils.err().println("imgage i", i);

			int y1 = mY.get(i);
			int y2 = mY.get(i + 1);

			int h = y1 - y2;

			for (int j = 0; j < mX.size() - 1; ++j) {
				int x = mX.get(j);

				int w = Math.max(2, mX.get(j + 1) - x);

				Color c = mColorsMap.get(x).get(y1);

				g2.setColor(c);

				// SysUtils.err().println("imgage fill", x, y1, w, h, c);

				g2.fillRect(x, y2, w, h);
			}
		}
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
	protected void cache(DrawingContext context, Figure figure, SubFigure subFigure, Axes axes, Plot plot,
			DataFrame m) {

		System.err.println("wtf " + m.getShape());

		if (mHashId == null || !mHashId.equals(subFigure.hashId())) {
			mX = new UniqueArrayList<Integer>(m.getCols());
			mY = new UniqueArrayList<Integer>(m.getRows());

			for (int i = 0; i < m.getCols(); ++i) {
				int x = axes.toPlotX1(i);

				mX.add(x);

				System.err.println("img " + x + " " + i);
			}

			mX.add(axes.toPlotX1(m.getCols()));

			for (int i = 0; i < m.getRows(); ++i) {
				int y = axes.toPlotY1(m.getRows() - i - 1);

				mY.add(y);
			}

			mY.add(axes.toPlotY1(m.getRows()));
		}

		// Monitor for changes in the color plot and the normalization method
		// which will cause the matrix to change

		if (mColorMap == null || !plot.getColorMap().equals(mColorMap) || mM == null || !m.equals(mM) || mHashId == null
				|| !mHashId.equals(subFigure.hashId())) {

			mColorsMap.clear();

			for (int i = 0; i < m.getRows(); ++i) {
				int y = axes.toPlotY1(m.getRows() - i - 1);

				for (int j = 0; j < m.getCols(); ++j) {
					int x = axes.toPlotX1(j);

					if (mColorsMap.get(x).containsKey(y)) {
						continue;
					}

					double v = m.getValue(i, j);

					SysUtils.err().println("norm", i, j, v, plot.getNorm().norm(v));

					Color c = plot.getColorMap().getColor(plot.getNorm().norm(v));

					mColorsMap.get(x).put(y, c);
				}
			}
		}

		mColorMap = plot.getColorMap();
		mM = m;
		mHashId = subFigure.hashId();
	}
}
