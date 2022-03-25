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

import java.awt.Color;

import org.jebtk.core.Cycle;
import org.jebtk.modern.graphics.colormap.ColorMap;

/**
 * Color list to cycle through to choose how things are colored.
 * 
 * @author Antony Holmes
 *
 */
public class ColorCycle extends Cycle<Color> {

	/**
	 * Instantiates a new color cycle.
	 */
	public ColorCycle() {
		// add(ColorUtils.decodeHtmlColor("#ff5555"));
		// add(ColorUtils.decodeHtmlColor("#00aa44"));
		// add(ColorUtils.decodeHtmlColor("#5f8dd3"));

		add(Color.RED);
		add(ColorMap.GREEN);
		add(ColorMap.BLUE);
		add(ColorMap.ORANGE);
		add(ColorMap.PURPLE);
		add(ColorMap.YELLOW);
		add(ColorMap.PINK);
		add(Color.GRAY);
		add(Color.BLACK);
	}
}
