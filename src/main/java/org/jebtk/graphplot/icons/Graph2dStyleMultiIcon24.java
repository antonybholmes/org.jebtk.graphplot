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

import org.jebtk.modern.graphics.icons.MultiIcon;
import org.jebtk.modern.graphics.icons.Raster24Icon;

/**
 * The class Graph2dStyleMultiIcon24.
 */
public class Graph2dStyleMultiIcon24 extends MultiIcon {

	/**
	 * Instantiates a new graph2d style multi icon24.
	 */
	public Graph2dStyleMultiIcon24() {
		// addIcon(new Raster24Icon(new JoinedFilledStyleIcon()));
		addIcon(new Raster24Icon(new JoinedStyleIcon()));
		addIcon(new Raster24Icon(new JoinedSmoothStyleIcon()));
		addIcon(new Raster24Icon(new JoinedFilledTransStyleIcon()));
		addIcon(new Raster24Icon(new JoinedSmoothFilledStyleIcon()));

		addIcon(new Raster24Icon(new LinesStyleIcon()));
		addIcon(new Raster24Icon(new BarsStyleIcon()));
		addIcon(new Raster24Icon(new ScatterStyleIcon()));

		addIcon(new Raster24Icon(new HeatMap32VectorIcon()));
	}
}
