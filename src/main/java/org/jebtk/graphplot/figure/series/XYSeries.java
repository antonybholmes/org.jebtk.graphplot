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
package org.jebtk.graphplot.figure.series;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jebtk.core.ColorUtils;
import org.jebtk.core.StringId;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.geom.DoublePos2D;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.Io;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonParser;
import org.jebtk.core.text.RegexUtils;
import org.jebtk.core.text.TextUtils;
import org.jebtk.graphplot.ColorCycle;
import org.jebtk.graphplot.figure.props.FontProps;
import org.jebtk.graphplot.figure.props.StyleProps;
import org.jebtk.graphplot.icons.ShapeStyle;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.MatrixGroup;
import org.jebtk.math.statistics.Statistics;

/**
 * A set of data points to plot as well as how they should be plotted.
 * 
 * @author Antony Holmes
 *
 */
public class XYSeries extends MatrixGroup implements ChangeListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The color cycle. */
	private static ColorCycle COLOR_CYCLE = new ColorCycle();

	/**
	 * The member style.
	 */
	private StyleProps mStyle = new StyleProps();

	/** The m marker style. */
	private StyleProps mMarkerStyle = new StyleProps();

	/**
	 * The member font properties.
	 */
	private FontProps mFontProps = new FontProps();

	/**
	 * The member shape.
	 */
	private Marker mMarker = new MarkerSquare();

	/** The m M. */
	private DataFrame mM = null;

	/**
	 * The constant NEXT_ID.
	 */
	private static final StringId NEXT_ID = new StringId("XY Series");

	/**
	 * Instantiates a new XY series.
	 */
	public XYSeries() {
		this(COLOR_CYCLE.next());
	}

	/**
	 * Instantiates a new XY series.
	 *
	 * @param name the name
	 */
	public XYSeries(String name) {
		this(name, COLOR_CYCLE.next());
	}

	/**
	 * Instantiates a new XY series.
	 *
	 * @param color the color
	 */
	public XYSeries(Color color) {
		this(NEXT_ID.getNextId(), color);
	}

	/**
	 * Instantiates a new XY series.
	 *
	 * @param group the group
	 */
	// public XYSeries(String name) {
	// this(name, COLOR_CYCLE.next());
	// }

	/**
	 * Instantiates a new XY series.
	 *
	 * @param group the group
	 */
	public XYSeries(MatrixGroup group) {
		this(group.getName(), group.getColor());

		for (Pattern r : group) {
			addRegex(r);
		}
	}

	public XYSeries(MatrixGroup group, Color color) {
		this(group);

		setColor(color);
	}

	/**
	 * Instantiates a new XY series.
	 *
	 * @param name  the name
	 * @param color the color
	 */
	public XYSeries(String name, Color color) {
		this(name, color, ShapeStyle.SQUARE);
	}

	/**
	 * Instantiates a new XY series.
	 *
	 * @param name  the name
	 * @param color the color
	 * @param shape the shape
	 */
	public XYSeries(String name, Color color, ShapeStyle shape) {
		super(name, color);

		mFontProps.addChangeListener(this);
		mMarkerStyle.addChangeListener(this);
		mStyle.addChangeListener(this);

		/*
		 * mStyle.addChangeListener(new ChangeListener() {
		 * 
		 * @Override public void changed(ChangeEvent e) { mMarkerStyle.copy(mStyle);
		 * }});
		 */

		addRegex(name);

		setMarker(shape);

		setColor(color);
	}

	/**
	 * Instantiates a new XY series.
	 *
	 * @param name    the name
	 * @param regexes the regexes
	 * @param color   the color
	 */
	public XYSeries(String name, Collection<Pattern> regexes, Color color) {
		this(name, regexes, color, ShapeStyle.SQUARE);
	}

	/**
	 * Instantiates a new XY series.
	 *
	 * @param name          the name
	 * @param regexes       the regexes
	 * @param caseSensitive the case sensitive
	 * @param color         the color
	 */
	public XYSeries(String name, Collection<Pattern> regexes, boolean caseSensitive, Color color) {
		this(name, regexes, caseSensitive, color, ShapeStyle.SQUARE);
	}

	/**
	 * Instantiates a new XY series.
	 *
	 * @param name    the name
	 * @param regexes the regexes
	 * @param color   the color
	 * @param shape   the shape
	 */
	public XYSeries(String name, Collection<Pattern> regexes, Color color, ShapeStyle shape) {
		this(name, regexes, false, color, shape);
	}

	/**
	 * Instantiates a new XY series.
	 *
	 * @param name          the name
	 * @param regexes       the regexes
	 * @param caseSensitive the case sensitive
	 * @param color         the color
	 * @param shape         the shape
	 */
	public XYSeries(String name, Collection<Pattern> regexes, boolean caseSensitive, Color color, ShapeStyle shape) {
		super(name, regexes, caseSensitive, color);

		setMarker(shape);
		setColor(color);
	}

	/**
	 * Gets the font style.
	 *
	 * @return the font style
	 */
	public FontProps getFontStyle() {
		return mFontProps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.lib.math.matrix.MatrixGroup#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}

	/**
	 * Gets the matrix.
	 *
	 * @return the matrix
	 */
	public DataFrame getMatrix() {
		return mM;
	}

	/**
	 * Sets the matrix.
	 *
	 * @param m the new matrix
	 */
	public void setMatrix(DataFrame m) {
		if (m != null) {
			mM = m;

			fireChanged();
		}
	}

	/**
	 * Set the width of the displayed bar as a percentage of data width.
	 *
	 * @param color the new color
	 */
	// public void setBarWidth(double barWidth) {
	// mBarWidth = Mathematics.bound(barWidth, 0, 1);
	//
	// mListeners.fireChanged();
	// }

	/**
	 * Returns a multiplier between 0 and 1 to scale bars by.
	 * 
	 * @return
	 */
	// public double getBarWidth() {
	// return mBarWidth;
	// }

	@Override
	public void setColor(Color color) {
		super.setColor(color);

		mStyle.getLineStyle().updateColor(color);
		mStyle.getFillStyle().updateColor(ColorUtils.getTransparentColor80(color));
		mStyle.getLineStyle().updateVisible(false); // setColor(color);

		mMarkerStyle.getLineStyle().updateColor(color);
		mMarkerStyle.getFillStyle().updateColor(color);

		fireChanged();
	}

	/**
	 * Set the point shape and trigger a change event.
	 *
	 * @param marker the new marker
	 */
	public void setMarker(ShapeStyle marker) {
		changeMarker(marker);

		mMarkerStyle.setVisible(true);
	}

	/**
	 * Change the shape without triggering a change event.
	 *
	 * @param marker the shape
	 */
	public void changeMarker(ShapeStyle marker) {
		mMarker = ShapeStyle.getShape(marker);

		mMarker.addChangeListener(this);
	}

	/**
	 * Returns the marker shape for generating data points.
	 *
	 * @return the shape
	 */
	public Marker getMarker() {
		return mMarker;
	}

	/**
	 * Gets the style.
	 *
	 * @return the style
	 */
	public StyleProps getStyle() {
		return mStyle;
	}

	/**
	 * Gets the marker style.
	 *
	 * @return the marker style
	 */
	public StyleProps getMarkerStyle() {
		return mMarkerStyle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
	 */
	@Override
	public final void changed(ChangeEvent e) {
		fireChanged(new ChangeEvent(this));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.lib.math.matrix.MatrixGroup#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof XYSeries)) {
			return false;
		}

		return compareTo((XYSeries) o) == 0;
	}

	/**
	 * Values.
	 *
	 * @param m      the m
	 * @param series the series
	 * @return the list
	 */
	public static List<Double> values(DataFrame m, XYSeries series) {
		return xValues(m, series);
	}

	/**
	 * X values.
	 *
	 * @param m      the m
	 * @param series the series
	 * @return the list
	 */
	public static List<Double> xValues(DataFrame m, XYSeries series) {
		List<Double> values = new ArrayList<Double>();

		List<Integer> columns = MatrixGroup.findColumnIndices(m, series);

		int c = columns.get(0);

		for (int i = 0; i < m.getRows(); ++i) {
			values.add(m.getValue(i, c));
		}

		return values;
	}

	/**
	 * Y values.
	 *
	 * @param m      the m
	 * @param series the series
	 * @return the list
	 */
	public static List<Double> yValues(DataFrame m, XYSeries series) {
		List<Double> values = new ArrayList<Double>();

		List<Integer> columns = MatrixGroup.findColumnIndices(m, series);

		int c = columns.get(1);

		for (int i = 0; i < m.getRows(); ++i) {
			values.add(m.getValue(i, c));
		}

		return values;
	}

	/**
	 * Returns the max x in a series.
	 *
	 * @param m      the m
	 * @param series the series
	 * @return the x max
	 */
	public static double getXMax(DataFrame m, XYSeries series) {
		double ret = Double.MIN_VALUE;

		List<Integer> columns = MatrixGroup.findColumnIndices(m, series);

		for (int i = 0; i < m.getRows(); ++i) {
			for (int c : columns) {
				double v = m.getValue(i, c);

				if (v > ret) {
					ret = v;
				}

				break;
			}
		}

		return ret;
	}

	/**
	 * Gets the y max.
	 *
	 * @param m      the m
	 * @param series the series
	 * @return the y max
	 */
	public static double getYMax(DataFrame m, XYSeries series) {
		double ret = Double.MIN_VALUE;

		List<Integer> columns = MatrixGroup.findColumnIndices(m, series);

		for (int i = 0; i < m.getRows(); ++i) {
			double v = m.getValue(i, columns.get(1));

			if (v > ret) {
				ret = v;
			}
		}

		return ret;
	}

	/**
	 * Returns the sum of the y values in the series.
	 *
	 * @param m      the m
	 * @param series the series
	 * @return the y sum
	 */
	public static double getYSum(DataFrame m, XYSeries series) {
		double ret = 0;

		List<Integer> columns = MatrixGroup.findColumnIndices(m, series);

		for (int i = 0; i < m.getRows(); ++i) {
			double v = m.getValue(i, columns.get(1));

			ret += v;
		}

		return ret;
	}

	/**
	 * Gets the x min.
	 *
	 * @param m      the m
	 * @param series the series
	 * @return the x min
	 */
	public static double getXMin(DataFrame m, XYSeries series) {
		double ret = Double.MAX_VALUE;

		List<Integer> columns = MatrixGroup.findColumnIndices(m, series);

		for (int i = 0; i < m.getRows(); ++i) {
			double v = m.getValue(i, columns.get(0));

			ret = Math.min(ret, v);
		}

		return ret;
	}

	/**
	 * Gets the y min.
	 *
	 * @param m      the m
	 * @param series the series
	 * @return the y min
	 */
	public static double getYMin(DataFrame m, XYSeries series) {
		double ret = Double.MAX_VALUE;

		List<Integer> columns = MatrixGroup.findColumnIndices(m, series);

		for (int i = 0; i < m.getRows(); ++i) {
			double v = m.getValue(i, columns.get(1));

			ret = Math.min(ret, v);
		}

		return ret;
	}

	/**
	 * Mean.
	 *
	 * @param m      the m
	 * @param series the series
	 * @return the double
	 */
	public static double mean(DataFrame m, XYSeries series) {
		List<Integer> columns = MatrixGroup.findColumnIndices(m, series);

		int c = columns.get(0);

		double[] v = new double[m.getRows()];

		for (int i = 0; i < m.getRows(); ++i) {
			v[i] = m.getValue(i, c);
		}

		return Statistics.mean(v);
	}

	/**
	 * Sd.
	 *
	 * @param m      the m
	 * @param series the series
	 * @return the double
	 */
	public static double sd(DataFrame m, XYSeries series) {
		List<Integer> columns = MatrixGroup.findColumnIndices(m, series);

		int c = columns.get(0);

		double[] v = new double[m.getRows()];

		for (int i = 0; i < m.getRows(); ++i) {
			v[i] = m.getValue(i, c);
		}

		return Statistics.popStdDev(v);
	}

	/**
	 * Get the XY points of a series (assuming two columns, one for x values and one
	 * for y values). If the series does not map to at least two columns null will
	 * be returned. If the series maps to more than two columns, the first will be
	 * considered to be x and the second y, all other colums will be ignored.
	 *
	 * @param m      the m
	 * @param series the series
	 * @return the XY points
	 */
	public static List<DoublePos2D> getXYPoints(DataFrame m, XYSeries series) {
		List<Integer> columns = MatrixGroup.findColumnIndices(m, series);

		if (columns.size() < 2) {
			return Collections.emptyList();
		}

		List<DoublePos2D> points = new ArrayList<DoublePos2D>();

		for (int i = 0; i < m.getRows(); ++i) {
			points.add(new DoublePos2D(m.getValue(i, columns.get(0)), m.getValue(i, columns.get(1))));
		}

		return points;
	}

	/**
	 * Creates the xy series.
	 *
	 * @return the XY series
	 */
	public static XYSeries createXYSeries() {
		return createXYSeries(COLOR_CYCLE.next());
	}

	/**
	 * Creates the XY series.
	 *
	 * @param name the name
	 * @return the XY series
	 */
	public static XYSeries createXYSeries(String name) {
		return createXYSeries(name, COLOR_CYCLE.next());
	}

	/**
	 * Creates the XY series.
	 *
	 * @param name  the name
	 * @param color the color
	 * @return the XY series
	 */
	public static XYSeries createXYSeries(String name, Color color) {
		XYSeries series = new XYSeries(name, color);

		series.addRegex("x", "y");

		return series;
	}

	/**
	 * Creates the XY series.
	 *
	 * @param color the color
	 * @return the XY series
	 */
	public static XYSeries createXYSeries(Color color) {
		XYSeries series = new XYSeries(color);

		series.addRegex("x", "y");

		return series;
	}

	public static XYSeries createXYSeries(MatrixGroup series, Color color) {
		return new XYSeries(series, color);
	}

	/**
	 * Load series.
	 *
	 * @param file   the file
	 * @param matrix the matrix
	 * @return the list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static List<XYSeries> loadSeries(Path file, DataFrame matrix) throws IOException {
		if (file == null) {
			return Collections.emptyList();
		}

		String ext = PathUtils.getFileExt(file);

		if (ext.equals("mgrp2")) {
			return loadSeries2(file, matrix);
		} else {
			return loadSeries1(file, matrix);
		}

	}

	/**
	 * Reads a group file into memory.
	 *
	 * @param file   the file
	 * @param matrix the matrix
	 * @return the list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static List<XYSeries> loadSeries1(Path file, DataFrame matrix) throws IOException {
		List<XYSeries> groups = new ArrayList<XYSeries>();

		String line;
		List<String> tokens;

		BufferedReader reader = FileUtils.newBufferedReader(file);

		// skip header
		reader.readLine();

		try {
			while ((line = reader.readLine()) != null) {
				if (Io.isEmptyLine(line)) {
					continue;
				}

				tokens = TextUtils.fastSplit(line, TextUtils.TAB_DELIMITER);

				// Labels

				String name = tokens.get(0);
				Color color = ColorUtils.decodeHtmlColor(tokens.get(1));
				List<String> searchTerms = TextUtils.removeEmptyElements(CollectionUtils.subList(tokens, 2));

				XYSeries group = create(name, searchTerms, color);

				/*
				 * for (String term : searchTerms) { System.err.println(name + " terms " +
				 * term);
				 * 
				 * List<Integer> indices = TextUtils.matches(matrix.getColumnNames(), term);
				 * 
				 * for (int row : indices) { if (inUse.contains(row)) { continue; }
				 * 
				 * //inUse.add(row);
				 * 
				 * group.add(row); } }
				 */

				groups.add(group);
			}
		} finally {
			reader.close();
		}

		return groups;
	}

	/**
	 * Creates the.
	 *
	 * @param name    the name
	 * @param regexes the regexes
	 * @param color   the color
	 * @return the XY series
	 */
	public static XYSeries create(String name, List<String> regexes, Color color) {
		return new XYSeries(name, TextUtils.compile(regexes), color);
	}

	/**
	 * Load series2.
	 *
	 * @param file   the file
	 * @param matrix the matrix
	 * @return the list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static List<XYSeries> loadSeries2(Path file, DataFrame matrix) throws IOException {
		if (file == null || !PathUtils.getFileExt(file).equals("mgrp2")) {
			return Collections.emptyList();
		}

		List<XYSeries> groups = new ArrayList<XYSeries>();

		String line;
		List<String> tokens;

		Map<String, XYSeries> groupMap = new HashMap<String, XYSeries>();

		BufferedReader reader = FileUtils.newBufferedReader(file);

		// skip header
		reader.readLine();

		try {
			while ((line = reader.readLine()) != null) {
				if (Io.isEmptyLine(line)) {
					continue;
				}

				tokens = TextUtils.fastSplit(line, TextUtils.TAB_DELIMITER);

				// Labels

				String name = tokens.get(0);
				Color color = ColorUtils.decodeHtmlColor(tokens.get(1));
				String sample = tokens.get(2);

				XYSeries group;

				if (groupMap.containsKey(name)) {
					group = groupMap.get(name);
				} else {
					group = new XYSeries(name, color);

					groupMap.put(name, group);
					groups.add(group);
				}

				group.addRegex(sample);
			}
		} finally {
			reader.close();
		}

		return groups;
	}

	public static List<XYSeries> loadJson(Path file) throws IOException {
		Json json = new JsonParser().parse(file);

		List<XYSeries> ret = new ArrayList<XYSeries>();

		for (Json group : json) {
			String name = group.getString("name");
			Color color = group.getColor("color");

			boolean caseSensitive = false;

			if (group.containsKey("case-sensitive")) {
				caseSensitive = group.getBool("case-sensitive");
			}

			List<String> regexes = new ArrayList<String>();

			for (Json search : group.get("searches")) {
				regexes.add(search.getString());
			}

			System.err.println("grpj " + name + " " + regexes);

			ret.add(new XYSeries(name, RegexUtils.compile(regexes, caseSensitive), caseSensitive, color));
		}

		return ret;
	}
}
