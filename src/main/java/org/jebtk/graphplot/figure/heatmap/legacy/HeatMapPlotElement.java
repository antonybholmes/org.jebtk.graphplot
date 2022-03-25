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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.jebtk.core.Mathematics;
import org.jebtk.core.Props;
import org.jebtk.core.geom.DoubleDim;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.ImageUtils;
import org.jebtk.modern.graphics.colormap.ColorMap;

/**
 * The class HeatMapPlotElement.
 */
public class HeatMapPlotElement extends MatrixPlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member color map.
	 */
	protected ColorMap mColorMap;

	/**
	 * The member border.
	 */
	private Color mBorder = null;

	/**
	 * The member outline.
	 */
	private Color mOutline = null;

	/** The m cell image cache. */
	private Map<Color, BufferedImage> mCellImageCache = new HashMap<Color, BufferedImage>();

	/** The m blank image. */
	private BufferedImage mBlankImage;

	private Color mGridRowColor;

	private Color mGridColColor;

	/**
	 * Instantiates a new heat map plot element.
	 *
	 * @param matrix      the matrix
	 * @param colorMap    the color map
	 * @param aspectRatio the aspect ratio
	 */
	public HeatMapPlotElement(DataFrame matrix, ColorMap colorMap, DoubleDim aspectRatio) {
		super(matrix, aspectRatio);

		setColorMap(colorMap);

		/// setAspectRatio(aspectRatio);

		setRasterMode(true);
	}

	/**
	 * Sets the color map.
	 *
	 * @param colorMap the new color map
	 */
	public void setColorMap(ColorMap colorMap) {
		mColorMap = colorMap;
	}

	/**
	 * Sets the grid color.
	 * 
	 * @param gridColor the grid color. The grid is not drawn if gridColor == null.
	 */
	public void setGridColor(Color gridColor) {
		setGridRowColor(gridColor);
		setGridColColor(gridColor);
	}

	public void setGridRowColor(Color gridColor) {
		mGridRowColor = gridColor;
	}

	public void setGridColColor(Color gridColor) {
		mGridColColor = gridColor;
	}

	/**
	 * Sets the border color.
	 *
	 * @param border the new border color
	 */
	public void setBorderColor(Color border) {
		mBorder = border;
	}

	/**
	 * Sets the outline color.
	 *
	 * @param outline the new outline color
	 */
	public void setOutlineColor(Color color) {
		mOutline = color;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.ModernPlotCanvas#plot(java.awt.
	 * Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext)
	 */
	@Override
	public void plotLayer(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {

		drawMatrix(g2, context);

		drawGrid(g2);

		drawOutline(g2);

		drawBorder(g2);

		/*
		 * if (image == null) { return; }
		 * 
		 * g2.drawImage(image, 0, 0, null);
		 */

		super.plotLayer(g2, offset, context, params);
	}

	/**
	 * Draw matrix.
	 *
	 * @param g2      the g2
	 * @param context the context
	 */
	protected void drawMatrix(Graphics2D g2, DrawingContext context) {
		int y = 0;

		// System.err.println("create matrix " + matrix.getRowCount() + " " +
		// matrix.getColumnCount());
		int w = mBlockSize.getW();
		int h = mBlockSize.getH();

		if (context == DrawingContext.UI) {
			for (int i = 0; i < mDrawingDim.mRows; ++i) {
				int x = 0;

				for (int j = 0; j < mDrawingDim.mCols; ++j) {
					double v = getValue(i, j);

					// System.err.println("hmm " + i + " " + j + " " + mMatrix.getText(i,
					// j) + " " + mMatrix.getValue(i, j) + " " + x + " " + y + " " + mBlockSize);

					if (Mathematics.isValidNumber(v)) {
						g2.drawImage(cacheCell(mColorMap.getColor(v)), x, y, null);
					} else {
						g2.drawImage(cacheBlankCell(), x, y, null);
					}

					x += w;
				}

				y += h;
			}
		} else {
			for (int i = 0; i < mDrawingDim.mRows; ++i) {
				int x = 0;

				for (int j = 0; j < mDrawingDim.mCols; ++j) {

					double v = getValue(i, j);

					if (Mathematics.isValidNumber(v)) {
						g2.setColor(mColorMap.getColor(v));
						g2.fillRect(x, y, w, h);
					} else {
						g2.setColor(ModernWidget.DARK_LINE_COLOR);
						g2.drawLine(x, y + h, x + w, y);
					}

					x += w;
				}

				y += h;
			}
		}
	}

	private double getValue(int i, int j) {
		if (mScaleYMode) {
			i = (i * mRatio.h) >> 16;
		}

		if (mScaleXMode) {
			j = (j * mRatio.w) >> 16;
		}

		return mMatrix.getValue(i, j);
	}

	/**
	 * Cache cell.
	 *
	 * @param color the color
	 * @return the buffered image
	 */
	protected BufferedImage cacheCell(Color color) {
		if (!mCellImageCache.containsKey(color)) {
			BufferedImage image = ImageUtils.createImage(mBlockSize);

			Graphics g = image.getGraphics();
			g.setColor(color);
			g.fillRect(0, 0, (int) mBlockSize.getW(), (int) mBlockSize.getH());

			mCellImageCache.put(color, image);
		}

		return mCellImageCache.get(color);
	}

	/**
	 * Cache blank cell.
	 *
	 * @return the buffered image
	 */
	private BufferedImage cacheBlankCell() {
		if (mBlankImage == null) {
			mBlankImage = ImageUtils.createImage(mBlockSize);

			Graphics g = mBlankImage.getGraphics();
			Graphics2D g2 = ImageUtils.createAATextGraphics(g);
			g2.setColor(ModernWidget.DARK_LINE_COLOR);
			g2.drawLine(0, (int) mBlockSize.getH(), (int) mBlockSize.getW(), 0);
		}

		return mBlankImage;
	}

	/**
	 * Draw grid.
	 *
	 * @param g2 the g2
	 */
	private void drawGrid(Graphics2D g2) {

		int w = getPreferredSize().width - 1;
		int h = getPreferredSize().height - 1;

		int w2 = mBlockSize.getW();
		int h2 = mBlockSize.getH();

		if (mGridRowColor != null) {

			g2.setColor(mGridRowColor);

			int y = 0;

			for (int i = 0; i <= mDrawingDim.mRows; ++i) {
				g2.drawLine(0, y, w, y);

				y += h2;
			}
		}

		if (mGridColColor != null) {
			g2.setColor(mGridColColor);

			int x = 0;

			for (int i = 0; i <= mDrawingDim.mCols; ++i) {
				g2.drawLine(x, 0, x, h);

				x += w2;
			}
		}
	}

	/**
	 * Draw outline.
	 *
	 * @param g2 the g2
	 */
	private void drawOutline(Graphics2D g2) {
		if (mOutline == null) {
			return;
		}

		g2.setColor(mOutline);

		int y = 0;

		int w = mBlockSize.getW();
		int h = mBlockSize.getH();

		for (int i = 0; i < mDrawingDim.mRows; ++i) {
			int x = 0;

			for (int j = 0; j < mDrawingDim.mCols; ++j) {
				g2.drawRect(x, y, w, h);

				x += w;
			}

			y += h;
		}
	}

	/**
	 * Draw border.
	 *
	 * @param g2 the g2
	 */
	private void drawBorder(Graphics2D g2) {
		if (mBorder == null) {
			return;
		}

		g2.setColor(mBorder);

		int w = getPreferredSize().width - 1;
		int h = getPreferredSize().height - 1;

		g2.drawRect(0, 0, w, h);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.common.ui.ui.graphics.ModernCanvas#getCanvasSize()
	 */
	@Override
	public void plotSize(Dimension d) {
		d.width += mDrawingDim.mCols * mBlockSize.getW() + 1;
		d.height += mDrawingDim.mRows * mBlockSize.getH() + 1; // mMatrix.getRowCount()
	}
}
