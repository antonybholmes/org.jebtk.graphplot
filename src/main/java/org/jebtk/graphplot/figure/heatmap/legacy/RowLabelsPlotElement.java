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
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jebtk.core.Mathematics;
import org.jebtk.core.Props;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.geom.DoubleDim;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.core.text.Formatter;
import org.jebtk.core.text.Join;
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.matrix.CellType;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.Matrix;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * The class RowLabelsPlotElement.
 */
public class RowLabelsPlotElement extends RowMatrixPlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	// private static String[][] mAnnotations;

	/**
	 * The member labels.
	 */
	// private List<String> mLabels;

	/**
	 * The member title.
	 */
	// private String mTitle = null;

	/**
	 * The member properties.
	 */
	private RowLabelProps mProps;

	/** The m labels. */
	private String[][] mLabels;

	/** The m widths. */
	private int[] mWidths;

	/** The m char width. */
	private int mCharWidth;

	/** The m titles. */
	private String[] mTitles;

	/** The Constant FIELD_GAP. */
	// Pixel gap between fields
	private static final int FIELD_GAP = SettingsService.getInstance().getInt("graphplot.plot.field-gap");

	/**
	 * Instantiates a new row labels plot element.
	 *
	 * @param matrix      the matrix
	 * @param Props       the properties
	 * @param aspectRatio the aspect ratio
	 * @param charWidth   the char width
	 */
	public RowLabelsPlotElement(DataFrame matrix, RowLabelProps properties, DoubleDim aspectRatio, int charWidth) {
		super(matrix, aspectRatio, -1);

		// mTitle = title;
		mProps = properties;
		mCharWidth = charWidth;

		// List<String> labels = new ArrayList<String>(matrix.getRowCount());

		String[][] labels = new String[matrix.getRows()][properties.showAnnotations.getVisibleCount()];

		List<String> types = CollectionUtils.replicate("string", properties.showAnnotations.getVisibleCount());

		List<String> names = matrix.getIndex().getNames();
		// List<String> lnames = TextUtils.toLowerCase(names);

		Map<String, String> lnames = TextUtils.toLowerCaseMap(names);

		for (int r = 0; r < matrix.getRows(); ++r) {
			int c = 0;

			for (int i = 0; i < names.size(); ++i) {
				String name = names.get(i);

				if (properties.showAnnotations.isVisible(name)) {
					Matrix annMatrix = matrix.getIndex().getAnnotation(name);

					// double v = annMatrix.getValue(r, 0);
					// matrix.getIndex().getAnnotationValue(name, r);

					// System.err.println("i " + i + " " + name + " " + r + " " +
					// matrix.getIndex().getText(name, r) + " " + annMatrix.getCellType(0, r));

					// System.err.println("sdfsdfsdf " + annMatrix.getClass() + " " +
					// annMatrix.getType());

					if (annMatrix.getCellType(0, r) == CellType.NUMBER) { // Matrix.isValidMatrixNum(v)) {

						double v = annMatrix.getValue(0, r);

						types.set(c, "number");

						if (Mathematics.isInt(v)) {
							String ln = lnames.get(name);

							int vi = (int) v;

							if (ln.contains("entrez") || ln.contains("id")) {
								// items that appear to be ids, should not
								// be formatted with commas or periods.
								labels[r][c] = Integer.toString(vi);
							} else {
								labels[r][c] = Formatter.number().format(vi);
							}
						} else {
							labels[r][c] = Formatter.number().format(v);
						}
					} else {
						labels[r][c] = matrix.getIndex().getText(name, r);
					}

					++c;
				}
			}

			/*
			 * List<String> annotations = new ArrayList<String>();
			 * 
			 * if (properties.showAnnotations.get(matrix.getIndex().getNames().get(0))) {
			 * text.append(matrix.getRowName(r)); }
			 * 
			 * 
			 * 
			 * for (int i = 1; i < matrix.getIndex().getNames().size(); ++i) { String name =
			 * matrix.getIndex().getNames().get(i);
			 * 
			 * if (properties.showAnnotations.get(name)) {
			 * extra.add(matrix.getIndex().getText(name, i)); } }
			 * 
			 * if (extra.size() > 0) {
			 * text.append(" (").append(TextUtils.commaJoin(extra)).append(")"); }
			 */

			/*
			 * for (int i = 0; i < matrix.getIndex().getNames().size(); ++i) { String name =
			 * matrix.getIndex().getNames().get(i);
			 * 
			 * if (properties.showAnnotations != null &&
			 * properties.showAnnotations.containsKey(name) &&
			 * properties.showAnnotations.get(name)) {
			 * 
			 * double v = matrix.getIndex().getAnnotationValue(name, r);
			 * 
			 * if (Matrix.isValidMatrixNum(v)) {
			 * annotations.add(Formatter.number().format(v)); } else {
			 * annotations.add(matrix.getIndex().getText(name, r)); } } }
			 * 
			 * labels.add(Join.on(", ").values(annotations).toString());
			 */

			// labels.add(getLabel(matrix, r, properties));
		}

		setLabels(labels);
	}

	/**
	 * Sets the labels.
	 *
	 * @param labels the new labels
	 */
	public void setLabels(String[][] labels) {
		mLabels = labels;

		mTitles = new String[mProps.showAnnotations.getVisibleCount()];

		int c = 0;

		for (int i = 0; i < mMatrix.getIndex().getNames().size(); ++i) {
			String name = mMatrix.getIndex().getNames().get(i);

			if (mProps.showAnnotations.isVisible(name)) {
				mTitles[c] = name;

				++c;
			}
		}

		// Find the max width of each column
		mWidths = Mathematics.zerosIntArray(mLabels[0].length);

		for (int i = 0; i < mWidths.length; ++i) {
			mWidths[i] = mTitles[i].length();

			for (int j = 0; j < mLabels.length; ++j) {
				mWidths[i] = Math.max(mWidths[i], mLabels[j][i].length());
			}
		}

		int width = 0;

		for (int w : mWidths) {
			width += w;
		}

		width += FIELD_GAP * (mWidths.length - 1);

		setWidth(mCharWidth * width);
	}

	/**
	 * Gets the label.
	 *
	 * @param matrix the matrix
	 * @param row    the row
	 * @param Props  the properties
	 * @return the label
	 */
	public static String getLabel(DataFrame matrix, int row, RowLabelProps properties) {
		List<String> annotations = new ArrayList<String>();

		for (int i = 0; i < matrix.getIndex().getNames().size(); ++i) {
			String name = matrix.getIndex().getNames().get(i);

			if (properties.showAnnotations.isVisible(name)) {

				double v = matrix.getIndex().getValue(name, row);

				if (Matrix.isValidMatrixNum(v)) {
					annotations.add(Formatter.number().format(v));
				} else {
					annotations.add(matrix.getIndex().getText(name, row));
				}
			}
		}

		return Join.on(", ").values(annotations).toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.ModernPlotCanvas#plot(java.awt.
	 * Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext)
	 */
	@Override
	public void plot(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
		drawLabels(g2);

		super.plot(g2, offset, context, params);
	}

	/**
	 * Draw labels.
	 *
	 * @param g2 the g2
	 */
	private void drawLabels(Graphics2D g2) {
		g2.setColor(mProps.color);

		int x = 0;
		int y = (int) -mBlockSize.getH();

		for (int i = 0; i < mTitles.length; ++i) {
			String title = mTitles[i];

			int d = mCharWidth * mWidths[i];

			int x1 = x + (d - ModernWidget.getStringWidth(g2, title)) / 2;

			g2.drawString(title, x1, y);

			x += d + FIELD_GAP;
		}

		// if (mTitle != null) {
		// g2.drawString(mTitle, (getPreferredSize().width -
		// g2.getFontMetrics().stringWidth(mTitle)), -mBlockSize.getH());
		// }

		int h = mBlockSize.getH(); // mBlockSize.getH();

		y = (int) ((h + g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent()) / 2);

		for (int r = 0; r < mLabels.length; ++r) {
			x = 0;

			for (int i = 0; i < mWidths.length; ++i) {
				int d = mCharWidth * mWidths[i];

				String label = mLabels[r][i];

				int x1 = x + (d - ModernWidget.getStringWidth(g2, label)) / 2;

				// if (mTypes.get(i).equals("number")) {
				// x1 = x+ d - ModernWidget.getStringWidth(g2, label);
				// }

				g2.drawString(label, x1, y);

				x += d + FIELD_GAP;
			}

			y += h;
		}

		/*
		 * for (String label : mLabels) { g2.drawString(label, 0, y);
		 * 
		 * y += mBlockSize.getH(); }
		 */
	}
}
