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

/**
 * Translate between graph space and pixel space in the Y axis. Since Java uses
 * inverted coordinates for y (0 is top right) we must invert and correct for
 * this since on a 2D cartesian graph y, y increases from the bottom up.
 * 
 * @author Antony Holmes
 *
 */
public abstract class AxisTranslationX extends AxisTranslation {

	/**
	 * Instantiates a new axis translation y.
	 *
	 * @param axis the axis
	 */
	public AxisTranslationX(Axes axes, Axis axis) {
		super(axes, axis);
	}

	@Override
	public int getPixels() {
		return getAxes().getInternalSize().getW();
	}

}
