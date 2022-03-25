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
import org.jebtk.modern.graphics.icons.ModernVectorIcon;

/**
 * Download arrow vector icon.
 * 
 * @author Antony Holmes
 *
 */
public class Legend32VectorIcon extends ModernVectorIcon {

	/**
	 * The constant WIDTH.
	 */
	private static final int WIDTH = 30;

	/**
	 * The constant SIZE.
	 */
	private static final int SIZE = 7;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.abh.common.ui.ui.icons.ModernIcon#drawForeground(java.awt.Graphics2D,
	 * java.awt.Rectangle)
	 */
	@Override
	public void drawIcon(Graphics2D g2, int x, int y, int w, int h, Props params) {
		x = x + (w - WIDTH) / 2;
		y = y + (h - WIDTH) / 2;

		g2.setColor(ColorUtils.decodeHtmlColor("#ff5555"));
		g2.fillRect(x, y, SIZE, SIZE);
		g2.setColor(Color.BLACK);
		g2.drawLine(x + SIZE + 5, y + SIZE / 2, x + SIZE + 15, y + SIZE / 2);

		g2.setColor(ColorUtils.decodeHtmlColor("#37c871"));
		g2.fillRect(x, y + SIZE + SIZE / 2, SIZE, SIZE);
		g2.setColor(Color.BLACK);
		g2.drawLine(x + SIZE + 5, y + 2 * SIZE, x + SIZE + 15, y + 2 * SIZE);

		g2.setColor(ColorUtils.decodeHtmlColor("#80b3ff"));
		g2.fillRect(x, y + 3 * SIZE, SIZE, SIZE);
		g2.setColor(Color.BLACK);
		g2.drawLine(x + SIZE + 5, y + 3 * SIZE + SIZE / 2, x + SIZE + 15, y + 3 * SIZE + SIZE / 2);
	}
}
