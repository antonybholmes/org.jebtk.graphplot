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

import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.ImageUtils;

/**
 * Each layer of the graph can respond to changes.
 * 
 * @author Antony Holmes
 *
 */
public abstract class AxesClippedLayer extends AxesLayer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	public AxesClippedLayer() {
		// Do nothing
	}

	public AxesClippedLayer(String name) {
		super(name);
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
	public final void drawPlot(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes) {
		Graphics2D g2Temp = ImageUtils.clone(g2);

		try {
			g2Temp.clipRect(0, 0, axes.getInternalSize().getW(), axes.getInternalSize().getH());

			plotLayer(g2Temp, context, figure, subFigure, axes);
		} finally {
			g2Temp.dispose();
		}
	}

	/**
	 * Plot clipped.
	 *
	 * @param g2      the g2
	 * @param context the context
	 * @param figure  the figure
	 * @param axes    the axes
	 */
	public abstract void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure,
			Axes axes);
}
