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

import org.jebtk.core.Props;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.graphics.icons.ModernVectorIcon;
import org.jebtk.modern.theme.ThemeService;

/**
 * The class PeakStyleIcon.
 */
public class PeakStyleIcon extends ModernVectorIcon {

	/**
	 * The constant LINE_COLOR.
	 */
	protected static final Color LINE_COLOR = ThemeService.getInstance().getColors().getTheme(4);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.abh.common.ui.ui.icons.ModernIcon#drawForeground(java.awt.Graphics2D,
	 * java.awt.Rectangle)
	 */
	@Override
	public void drawIcon(Graphics2D g2, int x, int y, int w, int h, Props params) {
		g2.setColor(Color.WHITE);
		g2.fillRect(x, y, w, h);

		g2.setColor(ModernWidget.LINE_COLOR);
		g2.drawRect(x, y, w - 1, h - 1);
	}

}
