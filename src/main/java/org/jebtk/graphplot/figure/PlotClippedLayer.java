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

/**
 * Each layer of the graph can respond to changes.
 * 
 * @author Antony Holmes
 *
 */
public abstract class PlotClippedLayer extends PlotLayer {

	private static final PlotClip CLIP = new PlotClipRectY();

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	public PlotClippedLayer() {
		setClip(CLIP);
	}

	public PlotClippedLayer(String name) {
		super(name);

		setClip(CLIP);
	}

	@Override
	public String getType() {
		return "Plot Clipped Layer";
	}

	/*
	 * @Override public final void plotContext(Graphics2D g2, DrawingContext
	 * context, Figure figure, SubFigure subFigure, Axes axes, Plot plot, DataFrame
	 * m) { Graphics2D g2Temp = ImageUtils.clone(g2);
	 * 
	 * try { g2Temp.clipRect(0, 0, axes.getInternalSize().getW(),
	 * axes.getInternalSize().getH());
	 * 
	 * super.plotContext(g2Temp, context, figure, subFigure, axes, plot, m); }
	 * finally { g2Temp.dispose(); } }
	 */
}
