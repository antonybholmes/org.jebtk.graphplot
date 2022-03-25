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

import java.awt.Graphics2D;

import org.jebtk.core.Props;

/**
 * The class BarsStyleIcon.
 */
public class BarsStyleIcon extends PeakStyleIcon {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.ui.plot.icons.PeakStyleIcon#
	 * drawForeground(java.awt.Graphics2D, java.awt.Rectangle)
	 */
	@Override
	public void drawIcon(Graphics2D g2, int x, int y, int w, int h, Props params) {
		super.drawIcon(g2, x, y, w, h, params);

		x = 5;
		y = 5;
		w = w - 2 * x;
		h = h - 2 * y;
		int y2 = y + h;

		int bw = w / 4;

		g2.setColor(LINE_COLOR);

		g2.fillRect(x, y2 - 5, bw, 5);
		g2.fillRect(x + bw, y2 - h, bw, h);
		g2.fillRect(x + 2 * bw, y2 - h / 2, bw, h / 2);
		g2.fillRect(x + 3 * bw, y2 - h * 3 / 4, bw, h * 3 / 4);
	}

}
