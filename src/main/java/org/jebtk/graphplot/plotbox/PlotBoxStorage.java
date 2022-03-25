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
package org.jebtk.graphplot.plotbox;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.event.ChangeListeners;
import org.jebtk.core.geom.IntPos2D;
import org.jebtk.graphplot.figure.GridLocation;

/**
 * Backend storage of plots for plot layouts. This allows plots to be optimally
 * stored for use with a plot layout.
 */
public abstract class PlotBoxStorage extends ChangeListeners implements Iterable<PlotBox>, ChangeListener {

	private static final long serialVersionUID = 1L;

	private static final Iterable<Integer> SINGLE_Z_LAYER = Collections.emptyList();

	private static final Iterable<IntPos2D> EMPTY_POSITIONS = Collections.emptyList();

	private Map<Integer, PlotBox> mIdMap = new HashMap<Integer, PlotBox>();
	private Map<String, PlotBox> mNameMap = new TreeMap<String, PlotBox>();

	public void add(PlotBox plot) {
		plot.addChangeListener(this);

		mIdMap.put(plot.getId(), plot);
		mNameMap.put(plot.getName(), plot);

		fireChanged();
	}

	public void add(PlotBox plot, int index) {
		add(plot);
	}
	
	public void add(PlotBox plot, int i, int j) {
		add(plot);
	}
	
	public void add(PlotBox plot, IntPos2D pos) {
		add(plot);
	}
	
	public void add(PlotBox plot, GridLocation l) {
		add(plot);
	}

	public PlotBox get(int z) {
		return null;
	}

	public PlotBox get(int i, int j) {
		return null;
	}

	public PlotBox get(IntPos2D pos) {
		return null;
	}
	
	public PlotBox get(GridLocation l) {
		return null;
	}

	public boolean remove(PlotBox plot) {
		return false;
	}
	
	public boolean remove(GridLocation l) {
		return false;
	}

	public boolean remove(int i) {
		return false;
	}
	
	public boolean remove(IntPos2D pos) {
		return remove(pos.x, pos.y);
	}
	
	public boolean remove(int i, int j) {
		return false;
	}

	public PlotBox getByName(String name) {
		return mNameMap.get(name);
	}

	/**
	 * Return a plot with a given id.
	 * 
	 * @param id
	 * @return
	 */
	public PlotBox getById(int id) {
		return mIdMap.get(id);
	}

	public int getChildCount() {
		return 0;
	}

	public Iterable<String> getNames() {
		return mNameMap.keySet();
	}

	/**
	 * Returns the next available z layer.
	 * 
	 * @return
	 */
	public int getUnusedZ() {
		return Integer.MIN_VALUE;
	}

	public Iterable<Integer> getZ() {
		return SINGLE_Z_LAYER;
	}

	public Iterable<IntPos2D> getPositions() {
		return EMPTY_POSITIONS;
	}

	@Override
	public Iterator<PlotBox> iterator() {
		return PlotBox.NO_CHILDREN.iterator();
	}

	/**
	 * Find a plotBox in the graph by name.
	 * 
	 * @param name
	 * @return
	 */
	public PlotBox findByName(String name) {
		String ls = name.toLowerCase();

		PlotBox ret = null;

		Deque<PlotBox> stack = new ArrayDeque<PlotBox>(100);

		for (PlotBox c : this) {
			stack.push(c);
		}

		while (!stack.isEmpty()) {
			PlotBox p = stack.pop();

			if (p.getName().toLowerCase().contains(ls)) {
				ret = p;
				break;
			}

			for (PlotBox c : p) {
				stack.push(c);
			}
		}

		return ret;
	}

	public PlotBox findByType(String type) {
		String ls = type.toLowerCase();

		PlotBox ret = null;

		Deque<PlotBox> stack = new ArrayDeque<PlotBox>(100);

		for (PlotBox c : this) {
			stack.push(c);
		}

		while (!stack.isEmpty()) {
			PlotBox p = stack.pop();

			if (p.getType().toLowerCase().contains(ls)) {
				ret = p;
				break;
			}

			for (PlotBox c : p) {
				stack.push(c);
			}
		}

		return ret;
	}

	public void clear() {
		mNameMap.clear();

		fireChanged();
	}

	public void removeByName(String name) {
		remove(mNameMap.get(name));
	}

	public <T extends PlotBox> void setChildren(Collection<T> plots) {
		clear();

		for (PlotBox c : plots) {
			add(c);
		}
	}

	@Override
	public void changed(ChangeEvent e) {
		fireChanged();
	}
}
