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
package org.jebtk.graphplot.figure.heatmap.legacy;

import java.awt.Dimension;

import org.jebtk.core.geom.DoubleDim;
import org.jebtk.math.matrix.DataFrame;

/**
 * The class RowMatrixPlotElement.
 */
public abstract class RowMatrixPlotElement extends MatrixPlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	protected int mWidth;

	/**
	 * Instantiates a new row matrix plot element.
	 *
	 * @param matrix      the matrix
	 * @param width       the width
	 * @param aspectRatio the aspect ratio
	 */
	public RowMatrixPlotElement(DataFrame matrix, DoubleDim aspectRatio, int width) {
		super(matrix, aspectRatio);

		setWidth(width);
	}

	/**
	 * Sets the width.
	 *
	 * @param width the new width
	 */
	public void setWidth(int width) {
		mWidth = width;
	}

	@Override
	public void plotSize(Dimension d) {
		d.width += mWidth;
		d.height += mDrawingDim.mRows * mBlockSize.getH();
	}
}
