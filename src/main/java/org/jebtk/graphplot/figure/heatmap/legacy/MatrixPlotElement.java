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

import org.jebtk.core.geom.DoubleDim;
import org.jebtk.core.geom.IntDim;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.graphplot.PlotElement;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.MatrixDim;

/**
 * Plot element that handles data from a matrix.
 * 
 * @author Antony Holmes
 *
 */
public abstract class MatrixPlotElement extends PlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The constant BLOCK_SIZE.
	 */
	public static final int BLOCK_SIZE = SettingsService.getInstance().getInt("graphplot.plot.block-size");

	public static IntDim DEFAULT_BLOCK = new IntDim(BLOCK_SIZE, BLOCK_SIZE);

	protected IntDim mBlockSize = DEFAULT_BLOCK;

	/**
	 * The member matrix.
	 */
	protected DataFrame mMatrix;

	protected boolean mScaleYMode = false;

	protected MatrixDim mDrawingDim;

	protected IntDim mRatio;

	protected boolean mScaleXMode = false;

	protected DoubleDim mAspectRatio;

	// protected Matrix mIM;

	/**
	 * Instantiates a new matrix plot element.
	 *
	 * @param matrix      the matrix
	 * @param aspectRatio the aspect ratio
	 */
	public MatrixPlotElement(DataFrame matrix, DoubleDim aspectRatio) {
		super("matrix");

		mMatrix = matrix;

		// mIM = matrix.getInnerMatrix();

		setAspectRatio(aspectRatio);
	}

	/**
	 * Gets the matrix.
	 *
	 * @return the matrix
	 */
	public DataFrame getMatrix() {
		return mMatrix;
	}

	public void setAspectRatio(DoubleDim dim) {
		mAspectRatio = dim;

		setBlockSize(dim);
	}

	public void setBlockSize(DoubleDim dim) {
		int rows;
		int cols;

		int yRatio;

		if (dim.getH() < 1) {
			mScaleYMode = true;

			int r = getMatrix().getRows();

			rows = (int) Math.max(1, r * dim.getH());

			yRatio = (int) ((r << 16) / rows) + 1;
		} else {
			rows = getMatrix().getRows();
			yRatio = 1;
		}

		int xRatio;

		if (dim.getW() < 1) {
			mScaleXMode = true;

			int c = getMatrix().getCols();
			cols = (int) Math.max(1, c * dim.getW());

			xRatio = (int) ((c << 16) / cols) + 1;
		} else {
			cols = getMatrix().getCols();
			xRatio = 1;
		}

		mRatio = new IntDim(xRatio, yRatio);
		mDrawingDim = new MatrixDim(rows, cols);
		mBlockSize = new IntDim((int) Math.max(1, dim.getW()), (int) Math.max(1, dim.getH()));
	}

	/*
	 * public void setCanvasSize(Dimension size) { blockSize.width = Math.max(1,
	 * (int)(size.width / matrix.getColumnCount())); blockSize.height = Math.max(1,
	 * (int)(size.height / matrix.getRowCount()));
	 * 
	 * super.setCanvasSize(size); }
	 */
}
