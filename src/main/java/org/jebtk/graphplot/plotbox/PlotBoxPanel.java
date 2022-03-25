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
package org.jebtk.graphplot.plotbox;

import java.awt.Dimension;
import java.awt.Graphics2D;

import org.jebtk.core.Props;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.graphplot.ModernPlotCanvas;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The class PlotBox.
 */
public class PlotBoxPanel extends ModernPlotCanvas implements ChangeListener {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	private PlotBox mPlot;

	private Dimension mS;

	/**
	 * Instantiates a new plot box.
	 *
	 * @param renderer the renderer
	 */
	public PlotBoxPanel(PlotBox plot) {
		mPlot = plot;
		mPlot.addChangeListener(this);

		refresh();
	}

	@Override
	public void plot(Graphics2D g2, DrawingContext context, Props params) {
	  System.err.println("plotbox draw");
		mPlot.plot(g2, context, params);
	}

	// @Override
	// public Dimension getPreferredSize() {
	// return mPlot.getPreferredSize();
	// }

	public PlotBoxStorage getStorage() {
		return mPlot.getStorage();
	}

	private void refresh() {
		Dimension s = mPlot.getPreferredSize();

		if (mS == null || !s.equals(mS)) {
			setCanvasSize(s);

			mS = s;
		} else {
			fireCanvasRedraw();
		}
	}

	@Override
	public void changed(ChangeEvent e) {
		refresh();
	}
}
