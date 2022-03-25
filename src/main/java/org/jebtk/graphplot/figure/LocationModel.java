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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.modern.graphics.CanvasListener;

/**
 * Represents a series of objects stored at different z layers.
 *
 * @author Antony Holmes
 * @param <T> the generic type
 */
public class LocationModel<T extends ZLayer> extends LayerCanvasListener
		implements Iterable<GridLocation>, CanvasListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * The member name map.
	 */
	private Map<GridLocation, ZModel<T>> mLayoutMap = new HashMap<GridLocation, ZModel<T>>();

	/**
	 * Returns the object at a particular z level.
	 *
	 * @param l the l
	 * @return the at z
	 */
	public ZModel<T> getChild(GridLocation l) {
		if (!mLayoutMap.containsKey(l)) {
			mLayoutMap.put(l, new ZModel<T>());

			mLayoutMap.get(l).addCanvasListener(this);
		}

		return mLayoutMap.get(l);
	}

	/**
	 * Gets the at Z.
	 *
	 * @param z the z
	 * @return the at Z
	 */
	public T getAtZ(int z) {
		return getChild(GridLocation.CENTER).getChild(z);
	}

	/**
	 * Find all layers matching some text.
	 *
	 * @param text the text
	 * @return the list
	 */
	public List<T> find(String text) {
		List<T> ret = new ArrayList<T>();

		for (GridLocation l : mLayoutMap.keySet()) {
			ret.addAll(mLayoutMap.get(l).find(text));
		}

		return ret;
	}

	/**
	 * Removes the by name.
	 *
	 * @param name the name
	 */
	public void removeByName(String name) {
		for (GridLocation l : mLayoutMap.keySet()) {
			mLayoutMap.get(l).removeByName(name);
		}
	}

	/**
	 * Find by name.
	 *
	 * @param name the name
	 * @return the t
	 */
	public T findByName(String name) {
		T ret = null;

		for (GridLocation l : mLayoutMap.keySet()) {
			T layer = mLayoutMap.get(l).findByName(name);

			if (layer != null) {
				ret = layer;
				break;
			}
		}

		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<GridLocation> iterator() {
		return mLayoutMap.keySet().iterator();
	}

	/**
	 * Clear all.
	 */
	public void clearAll() {
		removeAll();

		fireCanvasChanged();
	}

	/**
	 * Removes the all.
	 */
	public void removeAll() {
		mLayoutMap.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.figure.LayerCanvasListener#
	 * canvasChanged(org.abh.lib.event.ChangeEvent)
	 */
	@Override
	public void canvasChanged(ChangeEvent e) {
		fireCanvasChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.figure.LayerCanvasListener#
	 * redrawCanvas(org.abh.lib.event.ChangeEvent)
	 */
	@Override
	public void redrawCanvas(ChangeEvent e) {
		fireCanvasRedraw();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.columbia.rdf.lib.bioinformatics.plot.figure.LayerCanvasListener#
	 * canvasScrolled(org.abh.lib.event.ChangeEvent)
	 */
	@Override
	public void canvasScrolled(ChangeEvent e) {
		fireCanvasScrolled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.graphplot.figure.LayerCanvasListener#canvasResized(org.abh.common.event
	 * .ChangeEvent)
	 */
	@Override
	public void canvasResized(ChangeEvent e) {
		fireCanvasResized();
	}
}
