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
package org.jebtk.graphplot.figure.series;

import java.util.Map;
import java.util.TreeMap;

import org.jebtk.core.NameGetter;
import org.jebtk.core.collections.UniqueArrayList;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeEventProducer;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.event.ChangeListeners;

/**
 * A generic group object for creating a data group hierarchy.
 *
 * @author Antony Holmes
 * @param <T> the generic type
 */
public abstract class Group<T extends NameGetter> extends UniqueArrayList<T>
		implements NameGetter, ChangeEventProducer {

	// private List<T> mGroups = new UniqueList<T>();

	/**
	 * The member group map.
	 */
	protected Map<String, T> mGroupMap = new TreeMap<String, T>();

	/**
	 * The member current.
	 */
	private T mCurrent;

	/**
	 * The member listeners.
	 */
	private ChangeListeners mListeners = new ChangeListeners();

	/**
	 * Adds the.
	 *
	 * @param groups the groups
	 */
	public void add(Group<T> groups) {

		for (T g : groups) {
			add(g);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.lib.UniqueList#add(java.lang.Object)
	 */
	@Override
	public boolean add(T g) {
		super.add(g);
		mGroupMap.put(g.getName(), g);
		mCurrent = g;

		fireChanged();

		return true;
	}

	/**
	 * Auto create and name a new group.
	 *
	 * @return the t
	 */
	public abstract T autoCreate();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ArrayList#clear()
	 */
	public void clear() {
		mGroupMap.clear();
	}

	/**
	 * Return the current object.
	 *
	 * @return the current
	 */
	public T getCurrent() {
		if (mCurrent != null) {
			return mCurrent;
		}

		mCurrent = autoCreate();

		return mCurrent;
	}

	/**
	 * Gets the.
	 *
	 * @param name the name
	 * @return the t
	 */
	public T get(String name) {
		mCurrent = mGroupMap.get(name);

		return mCurrent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ArrayList#get(int)
	 */
	public T get(int i) {
		mCurrent = super.get(i);

		return mCurrent;
	}

	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public int getCount() {
		return size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.abh.lib.event.ChangeEventProducer#addChangeListener(org.abh.lib.event.
	 * ChangeListener)
	 */
	@Override
	public void addChangeListener(ChangeListener l) {
		mListeners.addChangeListener(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.lib.event.ChangeEventProducer#removeChangeListener(org.abh.lib.
	 * event.ChangeListener)
	 */
	@Override
	public void removeChangeListener(ChangeListener l) {
		mListeners.removeChangeListener(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.lib.event.ChangeEventProducer#fireChanged(org.abh.lib.event.
	 * ChangeEvent)
	 */
	@Override
	public void fireChanged(ChangeEvent e) {
		mListeners.fireChanged(e);
	}

	/**
	 * Fire changed.
	 */
	public void fireChanged() {
		fireChanged(new ChangeEvent(this));
	}
}
