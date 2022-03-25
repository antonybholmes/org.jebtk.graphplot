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
import java.awt.geom.GeneralPath;

import org.jebtk.core.Props;

/**
 * The class JoinedFilledStyleIcon.
 */
public class JoinedFilledStyleIcon extends PeakStyleIcon {

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

		GeneralPath path = new GeneralPath();

		path.moveTo(x, y + h);
		path.lineTo(x + w / 2, y);
		path.lineTo(x + w, y + h);
		path.closePath();

		g2.setColor(LINE_COLOR);

		g2.fill(path);

		g2.drawLine(x, y + h, x + w / 2, y);
		g2.drawLine(x + w / 2, y, x + w, y + h);
	}

}
