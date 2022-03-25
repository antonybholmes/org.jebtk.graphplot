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
package org.jebtk.graphplot.figure.props;

import java.awt.Color;

import org.jebtk.core.ColorUtils;
import org.jebtk.core.settings.SettingsService;

/**
 * Controls how grid lines appear on plots.
 *
 * @author Antony Holmes
 */
public class GridProps extends LineProps {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The constant GRID_COLOR.
	 */
	public static final Color GRID_COLOR = ColorUtils
			.decodeHtmlColor(SettingsService.getInstance().getString("plot.grid.color"));

	/**
	 * Instantiates a new grid properties.
	 */
	public GridProps() {
		// setStroke(StrokeStyle.DASHED, 1);
		setColor(GRID_COLOR);

		// Default to not being visible.
		setVisible(false);
	}
}
