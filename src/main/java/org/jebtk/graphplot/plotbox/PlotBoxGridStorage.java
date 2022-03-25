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

import java.util.Iterator;

/**
 * Store plots on a grid
 */
public class PlotBoxGridStorage extends PlotBoxStorage {

	private static final long serialVersionUID = 1L;

	private final PlotBox[][] mLocations;

	public PlotBoxGridStorage(int rows, int columns) {
		mLocations = new PlotBox[rows][columns];
	}

//  @Override
//  public void add(PlotBox plot, Props params) {
//    int row = 0;
//    int col = 0;
//
//    if (params.length > 1) {
//      if (params[0] instanceof Integer) {
//        row = (int) params[0];
//      }
//
//      if (params[1] instanceof Integer) {
//        col = (int) params[1];
//      }
//    }
//
//    add(plot, row, col);
//  }

	@Override
	public void add(PlotBox plot, int row, int col) {
		mLocations[row][col] = plot;

		super.add(plot);
	}

	@Override
	public void add(PlotBox plot) {
		add(plot, 0, 0);
	}

	@Override
	public PlotBox get(int row, int col) {
		return mLocations[row][col];
	}

	@Override
	public Iterator<PlotBox> iterator() {
		return new PlotBoxGridIterator<PlotBox>(mLocations);
	}

	@Override
	public int getChildCount() {
		return mLocations.length * mLocations[0].length;
	}

	@Override
	public boolean remove(PlotBox plot) {
		boolean found = false;

		for (int i = 0; i < mLocations.length; ++i) {
			for (int j = 0; j < mLocations[0].length; ++j) {
				if (mLocations[i][j].equals(plot)) {
					remove(i, j);
					found = true;
					break;
				}
			}

			if (found) {
				break;
			}
		}

		if (found) {
			remove(0, 0);
		}

		return true;
	}

	@Override
	public boolean remove(int i, int j) {
		mLocations[i][j] = null;
		
		return true;
	}
}
