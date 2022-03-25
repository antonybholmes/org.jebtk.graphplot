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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jebtk.core.ColorUtils;
import org.jebtk.core.StringId;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.Io;
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.cluster.Cluster;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.MatrixGroup;

/**
 * A collection of series.
 *
 * @author Antony Holmes
 */
public class XYSeriesGroup extends Group<XYSeries> implements ChangeListener {
	/**
	 * The constant NEXT_ID.
	 */
	private static final StringId NEXT_ID = new StringId("XY Series Group");

	/** The Constant EMPTY_GROUP. */
	public static final XYSeriesGroup EMPTY_GROUP = new XYSeriesGroup();

	/**
	 * The member name.
	 */
	private String mName;

	/**
	 * Instantiates a new XY series group.
	 */
	public XYSeriesGroup() {
		this(NEXT_ID.getNextId());
	}

	/**
	 * Instantiates a new XY series group.
	 *
	 * @param name the name
	 */
	public XYSeriesGroup(String name) {
		mName = name;
	}

	public XYSeriesGroup(XYSeries s1, XYSeries... series) {
		this(NEXT_ID.getNextId(), s1, series);
	}

	public XYSeriesGroup(String name, XYSeries s1, XYSeries... series) {
		this(name);

		add(s1);

		for (XYSeries s : series) {
			add(s);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.lib.NameProperty#getName()
	 */
	@Override
	public String getName() {
		return mName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.series.Group#add(org.abh.
	 * lib.NameProperty)
	 */
	@Override
	public boolean add(XYSeries g) {
		g.addChangeListener(this);

		return super.add(g);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.columbia.rdf.lib.bioinformatics.plot.figure.series.Group#autoCreate()
	 */
	@Override
	public XYSeries autoCreate() {
		XYSeries group = new XYSeries();

		add(group);

		return group;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
	 */
	@Override
	public void changed(ChangeEvent e) {
		fireChanged();
	}

	/**
	 * Gets the x max.
	 *
	 * @param m           the m
	 * @param seriesGroup the series group
	 * @return the x max
	 */
	public static double getXMax(DataFrame m, XYSeriesGroup seriesGroup) {
		double ret = Double.MIN_VALUE;

		for (XYSeries p : seriesGroup) {
			double v = XYSeries.getXMax(m, p);

			if (v > ret) {
				ret = v;
			}
		}

		return ret;
	}

	/**
	 * Gets the y max.
	 *
	 * @param m          the m
	 * @param collection the collection
	 * @return the y max
	 */
	public static double getYMax(DataFrame m, XYSeriesGroup collection) {
		double ret = Double.MIN_VALUE;

		for (XYSeries p : collection) {
			double v = XYSeries.getYMax(m, p);

			if (v > ret) {
				ret = v;
			}
		}

		return ret;
	}

	/**
	 * Gets the x min.
	 *
	 * @param m          the m
	 * @param collection the collection
	 * @return the x min
	 */
	public static double getXMin(DataFrame m, XYSeriesGroup collection) {
		double ret = Double.MAX_VALUE;

		for (XYSeries p : collection) {
			double min = XYSeries.getXMin(m, p);

			if (min < ret) {
				ret = min;
			}
		}

		return ret;
	}

	/**
	 * Gets the y min.
	 *
	 * @param m          the m
	 * @param collection the collection
	 * @return the y min
	 */
	public static double getYMin(DataFrame m, XYSeriesGroup collection) {
		double ret = Double.MAX_VALUE;

		for (XYSeries p : collection) {
			double min = XYSeries.getYMin(m, p);

			if (min < ret) {
				ret = min;
			}
		}

		return ret;
	}

	/**
	 * Returns the sum of the y values in the group.
	 *
	 * @param m           the m
	 * @param seriesGroup the series group
	 * @return the y sum
	 */
	public static double getYSum(DataFrame m, XYSeriesGroup seriesGroup) {
		double ret = 0;

		for (XYSeries s : seriesGroup) {
			ret += XYSeries.getYSum(m, s);
		}

		return ret;
	}

	/*
	 * public static XlsxTableModel getExcelModel(XYSeriesGroup sc) throws
	 * IOException { // find the max rows int maxRows = 0;
	 * 
	 * for (XYSeries series : sc) { if (series.getPointCount() > maxRows) { maxRows
	 * = series.getPointCount(); } }
	 * 
	 * XSSFWorkbook workbook = new XSSFWorkbook();
	 * 
	 * Sheet sheet = workbook.createSheet();
	 * 
	 * // Keep track of how many rows we have created. int r = 0;
	 * 
	 * XSSFRow row = (XSSFRow)sheet.createRow(r); XSSFCell cell;
	 * 
	 * // The header for (int i = 0; i < sc.getCount(); ++i) { int c = i * 3;
	 * 
	 * cell = row.createCell(c); cell.setCellValue("x");
	 * 
	 * cell = row.createCell(c + 1); cell.setCellValue("y");
	 * 
	 * cell = row.createCell(c + 2); }
	 * 
	 * ++r;
	 * 
	 * for (int i = 0; i < maxRows; ++i) { row = (XSSFRow)sheet.createRow(r);
	 * 
	 * for (int j = 0; j < sc.getCount(); ++j) { XYSeries series = sc.get(j);
	 * 
	 * int c = j * 3;
	 * 
	 * if (i < series.getPointCount()) { XYPoint point = series.getPoint(i);
	 * 
	 * cell = row.createCell(c); cell.setCellValue(point.getX()); cell =
	 * row.createCell(c + 1); cell.setCellValue(point.getY()); } else { cell =
	 * row.createCell(c); cell = row.createCell(c + 1);
	 * 
	 * } }
	 * 
	 * ++r; }
	 * 
	 * XlsxTableModel newModel = new XlsxTableModel(workbook, true);
	 * 
	 * return newModel; }
	 */

	/**
	 * Arrange groups by cluster.
	 *
	 * @param m           the m
	 * @param groups      the groups
	 * @param rootCluster the root cluster
	 * @return the map
	 */
	public static Map<Integer, XYSeriesGroup> arrangeGroupsByCluster(DataFrame m, XYSeriesGroup groups,
			Cluster rootCluster) {
		if (groups == null || groups.getCount() == 0 || rootCluster == null) {
			return Collections.emptyMap();
		}

		// First get the normal order
		Map<Integer, XYSeriesGroup> orderedGroups = arrangeGroupsByIndex(m, groups);

		List<Integer> newOrder = new ArrayList<Integer>();

		Deque<Cluster> stack = new ArrayDeque<Cluster>();

		stack.push(rootCluster);

		while (stack.size() > 0) {
			Cluster cluster = stack.pop();

			if (cluster.isParent()) {
				Cluster c1 = cluster.getChild1();
				Cluster c2 = cluster.getChild2();

				// Determine which is closest
				stack.push(c2);
				stack.push(c1);
			} else {
				// add the indices in the order they appear in the
				// cluster tree
				newOrder.add(cluster.getId());
			}
		}

		// reorder the groups

		Map<Integer, XYSeriesGroup> ret = new HashMap<Integer, XYSeriesGroup>();

		for (int i = 0; i < newOrder.size(); ++i) {
			ret.put(i, orderedGroups.get(newOrder.get(i)));
		}

		return ret;
	}

	/**
	 * Arrange groups by index.
	 *
	 * @param m      the m
	 * @param groups the groups
	 * @return the map
	 */
	public static Map<Integer, XYSeriesGroup> arrangeGroupsByIndex(DataFrame m, XYSeriesGroup groups) {

		Map<Integer, XYSeriesGroup> orderedGroups = new HashMap<Integer, XYSeriesGroup>();

		for (XYSeries group : groups) {
			List<Integer> indices = XYSeries.findColumnIndices(m, group);

			for (int i : indices) {
				if (!orderedGroups.containsKey(i)) {
					orderedGroups.put(i, new XYSeriesGroup());
				}

				orderedGroups.get(i).add(group);
			}
		}

		return orderedGroups;
	}

	/**
	 * Returns an ordered list of lists where each list corresponds to a group and
	 * each list contains the indices of the columns that match the group.
	 *
	 * @param matrix the matrix
	 * @param groups the groups
	 * @return the list
	 */
	public static List<List<Integer>> findColumnIndices(DataFrame matrix, XYSeriesGroup groups) {
		System.err.println("find ind " + Arrays.toString(matrix.getColumnNames()));
		return findIndices(matrix.getColumnNames(), groups);
	}

	/**
	 * Find indices.
	 *
	 * @param ids    the ids
	 * @param groups the groups
	 * @return the list
	 */
	public static List<List<Integer>> findIndices(String[] ids, XYSeriesGroup groups) {
		List<List<Integer>> ret = new ArrayList<List<Integer>>();

		for (XYSeries group : groups) {
			ret.add(MatrixGroup.findIndices(ids, group));
		}

		return ret;
	}

	/**
	 * Order groups.
	 *
	 * @param groups the groups
	 * @return the XY series group
	 */
	public static XYSeriesGroup orderGroups(Map<Integer, XYSeriesGroup> groups) {
		if (groups == null || groups.size() == 0) {
			return null;
		}

		Set<XYSeries> used = new HashSet<XYSeries>();

		XYSeriesGroup ret = new XYSeriesGroup();

		List<Integer> orderedKeys = CollectionUtils.sort(groups.keySet());

		for (int key : orderedKeys) {
			List<XYSeries> temp = new ArrayList<XYSeries>();

			System.err.println("key " + key + " " + groups.get(key));

			XYSeriesGroup keyGroups = groups.get(key);

			if (keyGroups != null) {
				for (XYSeries group : keyGroups) {
					if (used.contains(group)) {
						continue;
					}

					temp.add(group);
					used.add(group);
				}

				// sort the groups by name
				Collections.sort(temp);

				for (XYSeries x : temp) {
					ret.add(x);
				}
			}
		}

		return ret;
	}

	/**
	 * Order groups.
	 *
	 * @param m      the m
	 * @param groups the groups
	 * @return the XY series group
	 */
	public static XYSeriesGroup orderGroups(DataFrame m, XYSeriesGroup groups) {
		Map<Integer, XYSeriesGroup> orderedGroups = arrangeGroupsByIndex(m, groups);

		XYSeriesGroup ret = orderGroups(orderedGroups);

		return ret;
	}

	/**
	 * Reads a group file into memory.
	 *
	 * @param file   the file
	 * @param matrix the matrix
	 * @return the XY series group
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static XYSeriesGroup createRowGroupsByName(Path file, DataFrame matrix) throws IOException {
		XYSeriesGroup groups = new XYSeriesGroup();

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
				List<String> searchTerms = CollectionUtils.subList(tokens, 2);

				XYSeries group = XYSeries.create(name, searchTerms, color);

				groups.add(group);

				/*
				 * for (String term : searchTerms) { List<Integer> indices =
				 * TextUtils.matches(matrix.getRowNames(), term);
				 * 
				 * for (int row : indices) { if (inUse.contains(row)) { continue; }
				 * 
				 * inUse.add(row);
				 * 
				 * group.add(row); } }
				 */
			}
		} finally {
			reader.close();
		}

		return groups;
	}
}
