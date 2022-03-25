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
package org.jebtk.graphplot.figure.props;

import org.jebtk.core.event.ChangeEvent;

/**
 * For classes that generate ChangeEvents.
 *
 * @author Antony Holmes
 */
public interface GraphEventProducer {

	/**
	 * Add a change listener.
	 *
	 * @param l the l
	 */
	public void addGraphListener(GraphListener l);

	/**
	 * Remove a change listener.
	 *
	 * @param l the l
	 */
	public void removeGraphListener(GraphListener l);

	/**
	 * Fire to all listeners that a change has occurred.
	 *
	 * @param e the e
	 */
	public void fireGraphChanged(ChangeEvent e);

	/**
	 * Fire layout changed.
	 *
	 * @param e the e
	 */
	public void fireLayoutChanged(ChangeEvent e);
}
