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
 * The Interface PlotHashProperty.
 */
public interface PlotHashProperty {

	/**
	 * Should return a string describing the Props of the plot object. If the plot
	 * object is updated (e.g. axis limits change) then it should change to reflect
	 * this. Comparison of of plot hashes should offer a fast way to determine if a
	 * plot object has changed since it was last encountered.
	 *
	 * @return the string
	 */
	public String hashId();
}
