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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.modern.graphics.CanvasListener;

/**
 * Represents a series of objects stored at different z layers.
 *
 * @author Antony Holmes
 * @param <T> the generic type
 */
public class ZModel<T extends ZLayer> extends LayerCanvasListener implements Iterable<Integer>, CanvasListener {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The constant LOWER_RESERVED_Z.
	 */
	public static final int LOWER_RESERVED_Z = -1000;

	/**
	 * The constant UPPER_RESERVED_Z.
	 */
	public static final int UPPER_RESERVED_Z = 1000;

	/**
	 * The member name map.
	 */
	private Map<String, T> mNameMap = new TreeMap<String, T>();

	/** The m layer map. */
	private Map<Integer, T> mLayerMap = new TreeMap<Integer, T>();

	/** The m current. */
	private T mCurrent = null;

	/**
	 * Returns the next available z between 1 and 999 inclusive. Z values outside
	 * this range are reserved and should not be used by plot layer.
	 *
	 * @return the next unused plot z
	 */
	public int getUnusedZ() {
		for (int i = 1; i < UPPER_RESERVED_Z; ++i) {
			if (!mLayerMap.containsKey(i)) {
				return i;
			}
		}

		return 1;
	}

	/**
	 * Put z.
	 *
	 * @param <X>    the generic type
	 * @param layers the layers
	 */
	public <X extends Collection<T>> void addChild(X layers) {
		for (T layer : layers) {
			addChild(layer);
		}
	}

	/**
	 * Adds the.
	 *
	 * @param <X>    the generic type
	 * @param zModel the z model
	 */
	public <X extends ZModel<T>> void add(X zModel) {
		for (int z : zModel) {
			addChild(zModel.getChild(z));
		}
	}

	/**
	 * Add a layer without triggering a change event.
	 *
	 * @param layer the layer
	 */
	public void addChild(T layer) {
		addChild(layer, getUnusedZ());
	}

	/**
	 * Put z.
	 *
	 * @param layer the layer
	 * @param z     the z
	 */
	public void addChild(T layer, int z) {
		// the layer listens for events passed to it
		addCanvasMouseListener(layer);

		layer.addCanvasListener(this);

		mNameMap.put(layer.getName(), layer);
		mLayerMap.put(z, layer);

		mCurrent = layer;

		fireCanvasChanged();
	}

	/**
	 * Removes the.
	 *
	 * @param name the name
	 */
	public void remove(String name) {
		for (int z : mLayerMap.keySet()) {
			if (mLayerMap.get(z).getName().equals(name)) {
				mNameMap.remove(name);
				mLayerMap.remove(z);

				break;
			}
		}

		if (mCurrent != null && mCurrent.getName().equals(name)) {
			mCurrent = null;
		}

		fireCanvasChanged();
	}

	/**
	 * Gets the current layer.
	 *
	 * @return the current layer
	 */
	public T getCurrentLayer() {
		return mCurrent;
	}

	/**
	 * Gets the layer count.
	 *
	 * @return the layer count
	 */
	public int getLayerCount() {
		return mLayerMap.size();
	}

	/**
	 * Returns the Z layer ids in use.
	 *
	 * @return the z layers
	 */
	public List<Integer> getZLayers() {
		return Collections.unmodifiableList(CollectionUtils.toList(mLayerMap.keySet()));
	}

	/**
	 * Returns the object at a particular z level.
	 *
	 * @param z the z
	 * @return the at z
	 */
	public T getChild(int z) {
		return mLayerMap.get(z);
	}

	/**
	 * Gets the layer.
	 *
	 * @param name the name
	 * @return the layer
	 */
	public T getChild(String name) {
		return mNameMap.get(name);
	}

	/**
	 * Contains.
	 *
	 * @param name the name
	 * @return true, if successful
	 */
	public boolean contains(String name) {
		return mNameMap.containsKey(name);
	}

	/**
	 * Find all layers matching some text.
	 *
	 * @param text the text
	 * @return the list
	 */
	public List<T> find(String text) {
		String ls = text.toLowerCase();

		List<T> ret = new ArrayList<T>();

		for (int z : mLayerMap.keySet()) {
			T layer = mLayerMap.get(z);

			if (layer.getName().toLowerCase().contains(ls)) {
				ret.add(layer);
			}
		}

		return ret;
	}

	/**
	 * Gets the layer names.
	 *
	 * @return the names
	 */
	public Set<String> getNames() {
		return CollectionUtils.toSet(mNameMap.keySet());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Integer> iterator() {
		return mLayerMap.keySet().iterator();
	}

	/**
	 * Clear.
	 */
	public void clearUnreservedLayers() {
		removeUnreservedLayers();

		fireCanvasChanged();
	}

	/**
	 * Removes the unreserved layers.
	 */
	public void removeUnreservedLayers() {
		List<Integer> ids = new ArrayList<Integer>(mLayerMap.size());

		for (int i : mLayerMap.keySet()) {
			if (i > LOWER_RESERVED_Z && i < UPPER_RESERVED_Z) {
				ids.add(i);
			}
		}

		for (int i : ids) {
			mLayerMap.remove(i);
		}
	}

	/**
	 * Clear lower reserved layers.
	 */
	public void clearLowerReservedLayers() {
		removeLowerReservedLayers();

		fireCanvasChanged();
	}

	/**
	 * Removes the lower reserved layers.
	 */
	public void removeLowerReservedLayers() {
		List<Integer> ids = new ArrayList<Integer>();

		for (int i : mLayerMap.keySet()) {
			if (i <= LOWER_RESERVED_Z) {
				ids.add(i);
			}
		}

		for (int i : ids) {
			mLayerMap.remove(i);
		}
	}

	/**
	 * Clear upper reserved layers.
	 */
	public void clearUpperReservedLayers() {
		removeUpperReservedLayers();

		fireCanvasChanged();
	}

	/**
	 * Removes the upper reserved layers.
	 */
	public void removeUpperReservedLayers() {
		List<Integer> ids = new ArrayList<Integer>();

		for (int i : mLayerMap.keySet()) {
			if (i >= UPPER_RESERVED_Z) {
				ids.add(i);
			}
		}

		for (int i : ids) {
			mLayerMap.remove(i);
		}
	}

	/**
	 * Clear all.
	 */
	public void clear() {
		removeAll();

		fireCanvasChanged();
	}

	/**
	 * Removes the all.
	 */
	public void removeAll() {
		mNameMap.clear();
		mLayerMap.clear();
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

	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size() {
		return mLayerMap.size();
	}

	/**
	 * Remove layers by name.
	 *
	 * @param name the name
	 */
	public void removeByName(String name) {
		String ls = name.toLowerCase();

		int rmZ = Integer.MIN_VALUE;

		for (int z : mLayerMap.keySet()) {
			T layer = mLayerMap.get(z);

			if (layer.getName().toLowerCase().contains(ls)) {
				rmZ = z;
				break;
			}
		}

		if (rmZ != Integer.MIN_VALUE) {
			mNameMap.remove(mLayerMap.get(rmZ).getName());
			mLayerMap.remove(rmZ);
		}
	}

	/**
	 * Find by name.
	 *
	 * @param name the name
	 * @return the t
	 */
	public T findByName(String name) {
		String ls = name.toLowerCase();

		T ret = null;

		for (int z : mLayerMap.keySet()) {
			T layer = mLayerMap.get(z);

			if (layer.getName().toLowerCase().contains(ls)) {
				ret = layer;
				break;
			}
		}

		return ret;
	}

	/**
	 * Finds all layers of a given type and returns them by their z order.
	 *
	 * @param type the type
	 * @return the by type
	 */
	public List<T> getByType(String type) {
		List<T> ret = new ArrayList<T>();

		for (int z : this) {
			T layer = mLayerMap.get(z);

			if (layer.getType().equals(type)) {
				ret.add(layer);
			}
		}

		return ret;
	}
}
