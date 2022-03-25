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
package org.jebtk.graphplot.icons;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jebtk.core.ColorUtils;
import org.jebtk.core.Props;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.graphics.icons.ModernVectorIcon;

/**
 * Simple pane icon.
 * 
 * @author Antony Holmes
 *
 */
public class HeatMap32VectorIcon extends ModernVectorIcon {

	/**
	 * The constant WIDTH.
	 */
	protected static final int WIDTH = 3;

	/**
	 * The constant COLOR_1.
	 */
	public static final Color COLOR_1 = ColorUtils.decodeHtmlColor("#ff8080");

	/**
	 * The constant COLOR_2.
	 */
	public static final Color COLOR_2 = ColorUtils.decodeHtmlColor("#87de87");// ColorUtils.decodeHtmlColor("#ffd5d5");

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.abh.common.ui.ui.icons.ModernIcon#drawForeground(java.awt.Graphics2D,
	 * java.awt.Rectangle)
	 */
	@Override
	public void drawIcon(Graphics2D g2, int x, int y, int w, int h, Props params) {
		int x1 = x + 4;
		int y1 = y + 4;

		g2.setColor(ModernWidget.BACKGROUND_COLOR);

		g2.fillRect(x + 2, y + 2, 27, 27);

		g2.setColor(ModernWidget.LINE_COLOR);

		g2.drawRect(x + 2, y + 2, 27, 27);

		int i = 0;

		for (int r = 0; r < 8; ++r) {
			x1 = x + 4;

			for (int c = 0; c < 4; ++c) {
				if (i % 2 == 0) {
					g2.setColor(COLOR_1);
				} else {
					g2.setColor(COLOR_2);
				}

				// g2.setColor(COLOR_2);

				g2.fillRect(x1, y1, 2 * WIDTH, WIDTH);

				x1 += 2 * WIDTH;

				++i;
			}

			++i;

			y1 += WIDTH;
		}
	}
}
