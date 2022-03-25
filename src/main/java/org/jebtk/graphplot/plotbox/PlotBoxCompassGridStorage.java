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
import java.util.Map;
import java.util.TreeMap;

import org.jebtk.graphplot.figure.GridLocation;

/**
 * The class PlotBox.
 */
public class PlotBoxCompassGridStorage extends PlotBoxStorage {

	private static final long serialVersionUID = 1L;

	public static final GridLocation[][] ROWS = { { GridLocation.NW, GridLocation.N, GridLocation.NE },
			{ GridLocation.W, GridLocation.CENTER, GridLocation.E },
			{ GridLocation.SW, GridLocation.S, GridLocation.SE } };
	
	public static final GridLocation[] ALL_LOCS = {GridLocation.NW, GridLocation.N, GridLocation.NE,
      GridLocation.W, GridLocation.CENTER, GridLocation.E,
      GridLocation.SW, GridLocation.S, GridLocation.SE};

	private Map<GridLocation, PlotBox> mMap = new TreeMap<GridLocation, PlotBox>();

	@Override
	public void add(PlotBox plot, GridLocation l) {
	  System.err.println("where " + l + " " + plot);
	  
		mMap.put(l, plot);

		super.add(plot);
	}

	@Override
	public PlotBox get(GridLocation l) {
		return mMap.get(l);
	}
	
	@Override
  public PlotBox get(int i) {
    return get(ALL_LOCS[i]);
  }
	
	@Override
  public PlotBox get(int i, int j) {
    return get(ROWS[i][j]);
  }
	
	@Override
	public Iterator<PlotBox> iterator() {
		return mMap.values().iterator();
	}

	@Override
	public int getChildCount() {
		return mMap.size();
	}

	@Override
	public boolean remove(PlotBox plot) {
		GridLocation rl = GridLocation.CENTER;

		boolean found = false;

		for (GridLocation l : mMap.keySet()) {
			if (mMap.get(l).equals(plot)) {
				rl = l;
				found = true;
				break;
			}
		}

		if (found) {
			remove(rl);
		}

		return true;
	}

	@Override
	public boolean remove(GridLocation l) {
		mMap.remove(l);
		
		return true;
	}

//	private static GridLocation parseLocation(Object param, Props params) {
//		GridLocation l = GridLocation.CENTER;
//
//		if (param instanceof GridLocation) {
//			l = (GridLocation) param;
//		} else {
//			if (params.length > 0) {
//				if (param instanceof Integer && params[0] instanceof Integer) {
//					l = ROWS[(int) param][(int) params[0]];
//				}
//			}
//		}
//
//		return l;
//	}
}
