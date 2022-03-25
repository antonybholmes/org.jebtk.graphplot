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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jebtk.core.geom.IntDim;

/**
 * The class EmptyPlotBox.
 */
public class PlotBoxEmpty extends PlotBox {

	private IntDim mSize;

	public PlotBoxEmpty(int w, int h) {
		this(new IntDim(w, h));
	}

	public PlotBoxEmpty(Dimension d) {
		this(IntDim.create(d));
	}

	public PlotBoxEmpty(IntDim size) {
		mSize = size;
	}

	@Override
	public String getType() {
		return "Plot Box Empty";
	}

	@Override
	public void plotSize(Dimension d) {
		d.width = mSize.getW();
		d.height = mSize.getH();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final List<PlotBox> NO_PLOTS = Collections.emptyList();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<PlotBox> iterator() {
		return NO_PLOTS.iterator();
	}

	@Override
	public String hashId() {
		// TODO Auto-generated method stub
		return getName() + mSize;
	}
}
