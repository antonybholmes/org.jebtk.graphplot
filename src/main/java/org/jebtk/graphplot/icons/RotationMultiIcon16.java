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

import org.jebtk.modern.graphics.icons.ArrowDownVectorIcon;
import org.jebtk.modern.graphics.icons.ArrowLeftVectorIcon;
import org.jebtk.modern.graphics.icons.ArrowRightVectorIcon;
import org.jebtk.modern.graphics.icons.ArrowUpVectorIcon;
import org.jebtk.modern.graphics.icons.MultiIcon;
import org.jebtk.modern.graphics.icons.Raster16Icon;

/**
 * The class RotationMultiIcon16.
 */
public class RotationMultiIcon16 extends MultiIcon {

	/**
	 * Instantiates a new rotation multi icon16.
	 */
	public RotationMultiIcon16() {
		addIcon(new Raster16Icon(new ArrowRightVectorIcon()));
		addIcon(new Raster16Icon(new ArrowDownVectorIcon()));
		addIcon(new Raster16Icon(new ArrowLeftVectorIcon()));
		addIcon(new Raster16Icon(new ArrowUpVectorIcon()));
	}
}
