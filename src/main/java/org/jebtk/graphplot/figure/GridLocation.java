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

import java.util.Arrays;
import java.util.List;

/**
 * The Enum GridLocation.
 */
public enum GridLocation implements Comparable<GridLocation> {

	/** The nw. */
	NW,

	/** The n. */
	N,

	/** The ne. */
	NE,

	/** The e. */
	E,

	/** The se. */
	SE,

	/** The s. */
	S,

	/** The sw. */
	SW,

	/** The w. */
	W,

	/** The center. */
	CENTER;

	/**
	 * The order which the locations should be iterated over when searching for
	 * layers etc.
	 */
	public static final GridLocation[] LOCATIONS = { N, NE, E, SE, S, SW, W, NW, CENTER };

	public static final List<GridLocation> LOCATIONS_LIST = Arrays.asList(LOCATIONS);
}
