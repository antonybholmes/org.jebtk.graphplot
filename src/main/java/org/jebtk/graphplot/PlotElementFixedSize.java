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

import org.jebtk.core.geom.IntDim;

/**
 * Draws a gradient color bar to represent the range of a color map.
 * 
 * @author Antony Holmes
 *
 */
public class PlotElementFixedSize extends PlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	private IntDim mSize;

	public PlotElementFixedSize(String name, IntDim size) {
		super(name);

		mSize = size;
	}

	@Override
	public void plotSize(Dimension d) {
		d.width += mSize.getW();
		d.height += mSize.getH();
	}
}
