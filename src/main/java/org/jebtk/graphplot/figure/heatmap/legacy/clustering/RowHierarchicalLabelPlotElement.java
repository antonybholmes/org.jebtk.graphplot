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
package org.jebtk.graphplot.figure.heatmap.legacy.clustering;

import java.util.ArrayDeque;
import java.util.Deque;

import org.jebtk.core.geom.DoubleDim;
import org.jebtk.core.text.Formatter;
import org.jebtk.graphplot.figure.heatmap.legacy.RowLabelProps;
import org.jebtk.graphplot.figure.heatmap.legacy.RowLabelsPlotElement;
import org.jebtk.math.cluster.Cluster;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.Matrix;

/**
 * The class RowHierarchicalLabelPlotElement.
 */
public class RowHierarchicalLabelPlotElement extends RowLabelsPlotElement {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new row hierarchical label plot element.
	 *
	 * @param matrix      the matrix
	 * @param rootCluster the root cluster
	 * @param Props       the properties
	 * @param aspectRatio the aspect ratio
	 * @param charWidth   the char width
	 */
	public RowHierarchicalLabelPlotElement(DataFrame matrix, Cluster rootCluster, RowLabelProps properties,
			DoubleDim aspectRatio, int charWidth) {
		super(matrix, properties, aspectRatio, charWidth);

		Deque<Cluster> stack = new ArrayDeque<Cluster>();

		stack.push(rootCluster);

		String[][] labels = new String[matrix.getRows()][properties.showAnnotations.getVisibleCount()];

		while (stack.size() > 0) {
			Cluster cluster = stack.pop();

			if (cluster.isParent()) {
				Cluster c1 = cluster.getChild1();
				Cluster c2 = cluster.getChild2();

				stack.push(c2);
				stack.push(c1);
			} else {
				/*
				 * StringBuilder text = new StringBuilder(matrix.getRowName(cluster.getId()));
				 * 
				 * List<String> extra = new ArrayList<String>();
				 * 
				 * for (int i = 0; i < matrix.getIndex().getNames().size(); ++i) { String name =
				 * matrix.getIndex().getNames().get(i);
				 * 
				 * if (properties.showAnnotations != null &&
				 * properties.showAnnotations.containsKey(name) &&
				 * properties.showAnnotations.get(name)) {
				 * extra.add(matrix.getIndex().getText(name, cluster.getId())); } }
				 * 
				 * text.append(" ").append(TextUtils.join(extra, "; "));
				 * 
				 * labels.add(text.toString());
				 */

				// labels.add(getLabel(matrix, cluster.getId(), properties));

				int r = cluster.getId();

				int c = 0;

				for (int i = 0; i < matrix.getIndex().getNames().size(); ++i) {
					String name = matrix.getIndex().getNames().get(i);

					if (properties.showAnnotations.isVisible(name)) {

						double v = matrix.getIndex().getValue(name, r);

						if (Matrix.isValidMatrixNum(v)) {
							labels[r][c] = Formatter.number().format(v);
						} else {
							labels[r][c] = matrix.getIndex().getText(name, r);
						}

						++c;
					}
				}
			}
		}

		setLabels(labels);
	}
}
