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
package org.jebtk.graphplot.plotbox;

import java.awt.Dimension;

import org.jebtk.core.geom.IntDim;

/**
 * The class EmptyPlotBox.
 */
public class PlotBoxEmptyLayout extends PlotBoxLayout {

	private IntDim mDim;

	/**
	 * Instantiates a new empty plot box.
	 *
	 * @param dimension the dimension
	 */
	public PlotBoxEmptyLayout(IntDim dim) {
		mDim = dim;
	}

	public PlotBoxEmptyLayout(Dimension dim) {
		this(IntDim.create(dim));
	}

	/**
	 * Instantiates a new empty plot box.
	 *
	 * @param width  the width
	 * @param height the height
	 */
	public PlotBoxEmptyLayout(int width, int height) {
		this(new IntDim(width, height));
	}

	@Override
	public void plotSize(PlotBox plot, Dimension dim) {
		dim.width += mDim.getW();
		dim.height += mDim.getH();
	}
}
