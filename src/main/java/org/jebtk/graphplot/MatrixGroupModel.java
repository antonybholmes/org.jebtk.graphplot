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
package org.jebtk.graphplot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jebtk.core.event.ChangeListeners;
import org.jebtk.math.matrix.MatrixGroup;

/**
 * The class MatrixGroupModel.
 */
public class MatrixGroupModel extends ChangeListeners implements Iterable<MatrixGroup> {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member groups.
	 */
	private List<MatrixGroup> mGroups = new ArrayList<MatrixGroup>();

	/**
	 * Sets the groups.
	 *
	 * @param groups the new groups
	 */
	public void setGroups(List<MatrixGroup> groups) {
		if (groups == null) {
			return;
		}

		mGroups = new ArrayList<MatrixGroup>(groups);

		fireChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<MatrixGroup> iterator() {
		return mGroups.iterator();
	}
}