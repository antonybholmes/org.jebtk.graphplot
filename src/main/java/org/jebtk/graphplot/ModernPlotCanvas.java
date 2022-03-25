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
package org.jebtk.graphplot;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;

import org.jebtk.core.Props;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.ImageUtils;
import org.jebtk.modern.theme.ThemeService;
import org.jebtk.modern.zoom.ZoomCanvas;

/**
 * A panel designed for drawing operations/graphics that is scroll aware, the
 * graphics context is translated to match the scroll parameters. This removes
 * the need to move the actual panel around in the scroll pane.
 * 
 * @author Antony Holmes
 *
 */
public abstract class ModernPlotCanvas extends ZoomCanvas {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The constant PLOT_FONT.
	 */
	public static final Font PLOT_FONT = ThemeService.loadFont("plot.fonts.text");

	/**
	 * The constant PLOT_BOLD_FONT.
	 */
	public static final Font PLOT_BOLD_FONT = ThemeService.loadFont("plot.fonts.bold-text");

	/**
	 * Instantiates a new modern plot canvas.
	 */
	public ModernPlotCanvas() {
		setup();
	}

	/**
	 * Instantiates a new modern plot canvas.
	 *
	 * @param size the size
	 */
	public ModernPlotCanvas(Dimension size) {
		super(size);

		setup();
	}

	/**
	 * Setup.
	 */
	private void setup() {
		setFont(PLOT_FONT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.abh.common.ui.ui.graphics.ModernCanvas#drawCanvasForeground(java.awt.
	 * Graphics2D)
	 */
	@Override
	public final void drawCanvas(Graphics2D g2, DrawingContext context) {

		Graphics2D g2Temp = ImageUtils.clone(g2);

		try {
			g2Temp.setFont(getFont());

			plot(g2Temp, context, new Props());
		} finally {
			g2Temp.dispose();
		}
	}

	/**
	 * Plot.
	 *
	 * @param g2      the g2
	 * @param context the context
	 */
	public abstract void plot(Graphics2D g2, DrawingContext context, Props params);
}
