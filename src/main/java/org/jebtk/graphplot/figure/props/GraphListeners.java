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
import org.jebtk.core.event.EventProducer;

/**
 * Provides the ability to register and unregister ModernClickEventListeners for
 * controls and provides standard functions to interface with {
 * EventListenerList by taking care of casting etc.
 *
 * @author Antony Holmes
 *
 */
public class GraphListeners extends EventProducer<GraphListener> implements GraphEventProducer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.figure.properties.
	 * GraphEventProducer#addGraphListener(edu.columbia.rdf.lib.bioinformatics.
	 * plot.figure.properties.GraphListener)
	 */
	public synchronized void addGraphListener(GraphListener l) {
		mListeners.add(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.figure.properties.
	 * GraphEventProducer#removeGraphListener(edu.columbia.rdf.lib.bioinformatics.
	 * plot.figure.properties.GraphListener)
	 */
	public synchronized void removeGraphListener(GraphListener l) {
		mListeners.remove(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.figure.properties.
	 * GraphEventProducer#fireGraphChanged(org.abh.lib.event.ChangeEvent)
	 */
	public synchronized void fireGraphChanged(ChangeEvent e) {
		for (GraphListener l : mListeners) {
			l.graphChanged(e);
		}
	}

	/**
	 * Fire graph changed.
	 */
	public void fireGraphChanged() {
		fireGraphChanged(new ChangeEvent(this));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.figure.properties.
	 * GraphEventProducer#fireLayoutChanged(org.abh.lib.event.ChangeEvent)
	 */
	public synchronized void fireLayoutChanged(ChangeEvent e) {
		for (GraphListener l : mListeners) {
			l.layoutChanged(e);
		}
	}

	/**
	 * Called when the plot dimensions change.
	 */
	public void fireLayoutChanged() {
		fireLayoutChanged(new ChangeEvent(this));
	}
}
