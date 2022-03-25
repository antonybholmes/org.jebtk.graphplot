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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.List;

import org.jebtk.core.Function;
import org.jebtk.core.Props;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.geom.IntPos2D;
import org.jebtk.core.stream.Stream;
import org.jebtk.core.text.Join;
import org.jebtk.graphplot.figure.GridLocation;
import org.jebtk.graphplot.figure.props.MarginProps;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.ImageUtils;

/**
 * The class PlotBox.
 */
public class PlotBoxContainer extends PlotBox implements ChangeListener {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	private MarginProps mMargins = MarginProps.DEFAULT_MARGIN;

	private PlotBoxLayout mLayout = null;

	private PlotBoxStorage mStorage;

	/**
	 * Determines whether to pass on changeevents when part of a plot is updated.
	 */
	private boolean mFireEvents = true;

	public PlotBoxContainer(String id) {
		this(id, new PlotBoxDimStorage(), new PlotBoxColumnLayout());
	}

	public PlotBoxContainer() {
		this(new PlotBoxDimStorage(), new PlotBoxColumnLayout());
	}

	public PlotBoxContainer(String id, PlotBoxLayout layout) {
		this(id, new PlotBoxDimStorage(), layout);
	}

	public PlotBoxContainer(PlotBoxLayout layout) {
		this(new PlotBoxDimStorage(), layout);
	}

	public PlotBoxContainer(String id, PlotBoxStorage storage, PlotBoxLayout layout) {
		super(id);
		setStorage(storage);
		setLayout(layout);
	}

	public PlotBoxContainer(PlotBoxStorage storage, PlotBoxLayout layout) {
		setLayout(storage, layout);
	}
	
	public PlotBox add(PlotBox plot, GridLocation l) {
	  System.err.println("add cont " + plot + " " + l + " " + mStorage);
		mStorage.add(plot, l);
		
		return plot;
	}

	@Override
	public String getType() {
		return "Plot Box Container";
	}

	public void setStorage(PlotBoxStorage s) {
		mStorage = s;
		s.addChangeListener(this);
	}

	public PlotBoxStorage getStorage() {
		return mStorage;
	}

	public void setLayout(PlotBoxLayout layout) {
		mLayout = layout;
	}

	public void setLayout(PlotBoxStorage storage, PlotBoxLayout layout) {
		setStorage(storage);
		setLayout(layout);
	}

	/**
	 * Set the layout to use z layers.
	 * 
	 * @return
	 */
	public PlotBoxContainer setZLayout() {
		setLayout(new PlotBoxZStorage(), new PlotBoxZLayout());

		return this;
	}

	public PlotBoxLayout getPlotBoxLayout() {
		return mLayout;
	}

	public MarginProps getMargins() {
		return mMargins;
	}

	/**
	 * Sets the left margin.
	 *
	 * @param margin the new left margin
	 */
	public void setLeftMargin(int margin) {
		setMargins(mMargins.getTop(), margin, mMargins.getBottom(), mMargins.getRight());
	}

	/**
	 * Sets the bottom margin.
	 *
	 * @param margin the new bottom margin
	 */
	public void setBottomMargin(int margin) {
		setMargins(mMargins.getTop(), mMargins.getLeft(), margin, mMargins.getRight());
	}

	public void setTopMargin(int margin) {
		setMargins(margin, mMargins.getLeft(), mMargins.getBottom(), mMargins.getRight());
	}

	/**
	 * Sets the margins.
	 *
	 * @param s the new margins
	 */
	public void setMargins(int s) {
		setMargins(s, s, s, s);
	}

	/**
	 * Sets the margins.
	 *
	 * @param t the top margin.
	 * @param l the left margin.
	 * @param b the bottom margin.
	 * @param r the right margin.
	 */
	public void setMargins(int t, int l, int b, int r) {
		setMargins(new MarginProps(t, l, b, r));
	}

	/**
	 * Sets the margins.
	 *
	 * @param margins the new margins
	 */
	public void setMargins(MarginProps margins) {
		if (updateMargins(margins)) {
			fireChanged();
		}
	}

	/**
	 * Update margins.
	 *
	 * @param margins the margins
	 * @return
	 */
	@Override
	public boolean updateMargins(MarginProps margins) {
		if (!margins.equals(mMargins)) {
			mMargins = margins;
			return true;
		} else {
			return false;
		}
	}

	public void addMargin(Dimension dim) {
		dim.width += mMargins.getLineMargin();
		dim.height += mMargins.getPageMargin();
	}

	/**
	 * Update parameter dim to the current plot size. This is to allow for recursive
	 * updating of the size without generating dimension objects.
	 *
	 * @param plotBox the plot box
	 * @param dim     the dim
	 * @return the plot size recursive
	 */
	@Override
	public void plotSize(Dimension dim) {
		mLayout.plotSize(this, dim);

		addMargin(dim);
	}

	/**
	 * Draw recursive.
	 *
	 * @param g2      the g2
	 * @param plotBox the plot box
	 * @param offset  the offset
	 * @param context the context
	 */
	@Override
	public void plotLayer(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
	  
	  System.err.println("sdfsdf layer");
	  
		Graphics2D g2Temp = ImageUtils.clone(g2);

		try {
			// Translate to account for padding.
			g2Temp.translate(mMargins.getLeft(), mMargins.getTop());

			mLayout.plot(g2Temp, this, offset, context, params);
		} finally {
			g2Temp.dispose();
		}

	}

	@Override
	public <T extends PlotBox> PlotBox setChildren(List<T> plots) {
		mStorage.setChildren(plots);

		return super.setChildren(plots);
	}

	@Override
	public PlotBox addChild(PlotBox plot) {
		mStorage.add(plot);
		
		return this;
	}

	@Override
	public PlotBox addChild(PlotBox plot, int i) {
		mStorage.add(plot, i);
		
		return this;
	}
	
	@Override
	public PlotBox addChild(PlotBox plot, int i, int j) {
		mStorage.add(plot, i, j);
		
		return this;
	}
	
	@Override
  public PlotBox getChild(int i) {
    return mStorage.get(i);
  }
	
	@Override
  public PlotBox getChild(int i, int j) {
    return mStorage.get(i, j);
  }
	
	@Override
  public PlotBox getChild(GridLocation l) {
    return mStorage.get(l);
  }
	
	@Override
	public PlotBox getByName(String name) {
		return mStorage.getByName(name);
	}

	@Override
	public PlotBox getChildById(int id) {
		return mStorage.getById(id);
	}

	@Override
	public Iterable<IntPos2D> getPositions() {
		return mStorage.getPositions();
	}

	@Override
	public Iterable<GridLocation> getLocations() {
		return GridLocation.LOCATIONS_LIST;
	}

	public Iterable<String> getNames() {
		return mStorage.getNames();
	}

	@Override
	public int getChildCount() {
		return mStorage.getChildCount();
	}

	/**
	 * Returns the next available z layer.
	 * 
	 * @return
	 */
	@Override
	public int getUnusedZ() {
		return mStorage.getUnusedZ();
	}

	@Override
	public Iterable<Integer> getZ() {
		return mStorage.getZ();
	}

	public void clear() {
		mStorage.clear();
	}

	@Override
	public Iterator<PlotBox> iterator() {
		return mStorage.iterator();
	}

	/**
	 * Create a unique hash string for this plot box.
	 * 
	 * @return
	 */
	@Override
	public String hashId() {
		return Join.onColon().values(getName(), getMargins(), getPreferredSize()).toString();
	}

	@Override
	public void removeByName(String name) {
		mStorage.removeByName(name);
	}

	@Override
	public boolean remove(PlotBox plot) {
		return mStorage.remove(plot);
	}

	/**
	 * Return the names of the children
	 * 
	 * @param figure
	 * @return
	 */
	public static Iterable<String> getNames(PlotBox figure) {
		return Stream.of(figure).map(new Function<PlotBox, String>() {
			@Override
			public String apply(PlotBox item) {
				return item.getName();
			}
		});
	}

	@Override
	public void changed(ChangeEvent e) {
		fireChanged();
	}

	@Override
	public void fireChanged() {
		if (mFireEvents) {
			super.fireChanged();
		}
	}

	/**
	 * Control whether events are fired or not. Useful if applying multiple Props at
	 * once with triggering multiple events. If set to true, will trigger a
	 * changeevent.
	 * 
	 * @param fireEvents Whether to forward plot change events up the hierarchy.
	 */
	public void setFireEvents(boolean fireEvents) {
		mFireEvents = fireEvents;

		if (fireEvents) {
			fireChanged();
		}
	}
}
