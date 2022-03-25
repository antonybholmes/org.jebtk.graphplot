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
package org.jebtk.graphplot.figure.heatmap;

/**
 * The enum ColorStandardization.
 */
public class ColorNormalization {

	public static final ColorNormalization NONE = new ColorNormalization(ColorNormalizationType.NONE);

	public static final ColorNormalization ZSCORE_MATRIX = new ColorNormalization(ColorNormalizationType.ZSCORE_MATRIX);

	public static final ColorNormalization ZSCORE_ROW = new ColorNormalization(ColorNormalizationType.ZSCORE_ROW);

	public static final ColorNormalization ZSCORE_COLUMN = new ColorNormalization(ColorNormalizationType.ZSCORE_COLUMN);

	public static final ColorNormalization NORMALIZE = new ColorNormalization(ColorNormalizationType.NORMALIZE);

	private ColorNormalizationType mType;

	private double mMin = -1;
	private double mMax = -1;

	public ColorNormalization(ColorNormalizationType type) {
		this(type, Double.NaN);
	}

	public ColorNormalization(ColorNormalizationType type, double min) {
		this(type, min, min);
	}

	public ColorNormalization(ColorNormalizationType type, double min, double max) {
		mType = type;
		mMin = min;
		mMax = max;
	}

	public ColorNormalizationType getType() {
		return mType;
	}

	public double getMin() {
		return mMin;
	}

	public double getMax() {
		return mMax;
	}
}
