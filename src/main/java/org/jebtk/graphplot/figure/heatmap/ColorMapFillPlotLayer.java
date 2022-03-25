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
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jebtk.core.collections.UniqueArrayList;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.Plot;
import org.jebtk.graphplot.figure.PlotClippedLayer;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The class HeatMapFillPlotLayer.
 */
public class ColorMapFillPlotLayer extends PlotClippedLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** The m X. */
	private List<Integer> mX = null;

	/** The m colors map. */
	private Map<Integer, Color> mColorsMap = new HashMap<Integer, Color>();

	private int mWidth;

	/**
	 * Instantiates a new heat map fill plot layer.
	 *
	 * @param name the name
	 */
	public ColorMapFillPlotLayer(int height) {
		mWidth = height;
	}

	@Override
	public String getType() {
		return "Color Map Layer";
	}

	@Override
	public void plotSize(Dimension d) {
		d.height = mWidth;
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

		int x1 = 0; // axes.getMargins().getLeft();
		int y1 = 0; // axes.getMargins().getTop();
		int h = axes.getInternalSize().getH();

		int s = cache(context, figure, subFigure, axes, plot, x1);

		for (int x : mX) {
			g2.setColor(mColorsMap.get(x));

			g2.fillRect(x1 + x, y1, s, h);
		}
	}

	/**
	 * Cache.
	 *
	 * @param context   the context
	 * @param subFigure the sub figure
	 * @param axes      the axes
	 * @param plot      the plot
	 * @param x1        the x 1
	 * @return the int
	 */
	protected int cache(DrawingContext context, Figure figure, SubFigure subFigure, Axes axes, Plot plot, int x1) {
		int w = axes.getInternalSize().getW();

		double wd = w;

		int s = 1; // (int)Math.ceil(wd / (plot.getColorMap().getColorCount() - 1));

		mX = new UniqueArrayList<Integer>(w);

		int p = 0;

		while (p < wd) {
			mX.add(p);

			p += s;
		}

		mColorsMap.clear();

		for (int x : mX) {
			int index = (int) (x / wd * plot.getColorMap().getColorCount());

			Color c = plot.getColorMap().getColorByIndex(index);

			mColorsMap.put(x, c);
		}

		return (int) Math.round(s);
	}
}
