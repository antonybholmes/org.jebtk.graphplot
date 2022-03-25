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
package org.jebtk.graphplot.figure;

import java.util.HashMap;
import java.util.Map;

import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.math.matrix.DataFrame;

/**
 * Stores unique xy coordinates associated with a matrix globally so that
 * multiple plots sharing a matrix can also share unique coordinates.
 * 
 * @author Antony Holmes
 *
 */
public class UniqueXYService {

	/**
	 * The instance.
	 */
	private static UniqueXYService INSTANCE = new UniqueXYService();

	/**
	 * Gets the single instance of UniqueXYService.
	 *
	 * @return single instance of UniqueXYService
	 */
	public static UniqueXYService getInstance() {
		return INSTANCE;
	}

	/**
	 * The member unique map.
	 */
	private Map<String, UniqueXY> mUniqueMap = new HashMap<String, UniqueXY>();

	/**
	 * Instantiates a new unique xy service.
	 */
	private UniqueXYService() {
		// Do nothing
	}

	/**
	 * Gets the.
	 *
	 * @param figure   the figure
	 * @param axes     the axes
	 * @param m        the m
	 * @param series   the series
	 * @param zeroEnds the zero ends
	 * @return the unique xy
	 */
	public UniqueXY get(Figure figure, SubFigure subFigure, Axes axes, DataFrame m, XYSeries series, boolean zeroEnds) {
		String id = getId(figure, subFigure, axes, m, series, zeroEnds);

		if (!mUniqueMap.containsKey(id)) {
			mUniqueMap.put(id, new UniqueXY(m, series, axes, zeroEnds));
		}

		// System.err.println("Cached unique " + id);

		return mUniqueMap.get(id);
	}

	/**
	 * Gets the id.
	 *
	 * @param figure   the figure
	 * @param axes     the axes
	 * @param m        the m
	 * @param series   the series
	 * @param zeroEnds the zero ends
	 * @return the id
	 */
	private static String getId(Figure figure, SubFigure subFigure, Axes axes, DataFrame m, XYSeries series,
			boolean zeroEnds) {
		return m.hashCode() + Boolean.toString(zeroEnds);
	}
}
