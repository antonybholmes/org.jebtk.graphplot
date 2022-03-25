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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jebtk.core.IdProperty;
import org.jebtk.core.IntId;
import org.jebtk.core.NameGetter;
import org.jebtk.core.Props;
import org.jebtk.core.UidProperty;
import org.jebtk.core.event.ChangeListeners;
import org.jebtk.core.geom.IntPos2D;
import org.jebtk.core.sys.SysUtils;
import org.jebtk.core.text.Join;
import org.jebtk.core.text.TextUtils;
import org.jebtk.graphplot.figure.GridLocation;
import org.jebtk.graphplot.figure.PlotHashProperty;
import org.jebtk.graphplot.figure.PlotStyle;
import org.jebtk.graphplot.figure.props.MarginProps;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.AAModes;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.ImageUtils;
import org.jebtk.modern.graphics.colormap.ColorMap;

/**
 * The class PlotBox.
 */
public abstract class PlotBox extends ChangeListeners
		implements Iterable<PlotBox>, IdProperty, NameGetter, PlotHashProperty, UidProperty {

	private static final long serialVersionUID = 1L;

	public static final Iterable<PlotBox> NO_CHILDREN = Collections.emptyList();

	private String mName;
	private String mPlotName;

	protected AAModes mAAModes = new AAModes();

	protected boolean mRasterMode = false;

	protected boolean mClipMode = false;

	private BufferedImage mBufferedImage;

	private PlotBox mParent = null;

	private boolean mVisible = true;

	private static final IntId NEXT_ID = new IntId();

	private int mId;

	public PlotBox() {
		mId = NEXT_ID.getNextId();

		mPlotName = createId(getType(), mId);

		setName(mPlotName);
	}

	public PlotBox(String name) {
		this();

		setName(name);
	}

	/**
	 * Set how the component uses anti-aliasing.
	 * 
	 * @return
	 */
	public AAModes getAAModes() {
		return mAAModes;
	}

	@Override
	public int getId() {
		return mId;
	}

	/**
	 * Returns the plot name such as Figure 1. This is immutable and set once when
	 * the object is created. {@code getName()} will return the same value as
	 * {@code getPlotName()} unless {@code setName()} is called.
	 * 
	 * @return
	 */
	public String getPlotName() {
		return mPlotName;
	}

	/**
	 * Allows the plot to have a secondary name that can be more human readable
	 * instead of Sub Figure 1 etc. This is designed so that complex layouts can
	 * have easier to understand names
	 * 
	 * @param name
	 */
	public void setName(String name) {
		mName = name;

		fireChanged();
	}

	/**
	 * Returns a more human readable name for the plot.
	 * 
	 */
	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String getUid() {
		return getUid(this);
	}

	/**
	 * Generate an id from the plot box using the graph from the root to this leaf
	 * to create a unique id.
	 * 
	 * @param plotBox
	 * @return
	 */
	private static String getUid(PlotBox plotBox) {
		List<String> stack = new ArrayList<String>(10);

		PlotBox p = plotBox;

		while (true) {
			stack.add(TextUtils.squareBrackets(p.getName()));

			p = p.getParent();

			if (p == null) {
				break;
			}
		}

		// Print in order from root to leaf.
		Collections.reverse(stack);

		return Join.onColon().values(stack).toString();
	}

	public void setParent(PlotBox parent) {
		mParent = parent;
	}

	public PlotBox getParent() {
		return mParent;
	}

	public void setStorage(PlotBoxStorage s) {

	}

	public PlotBoxStorage getStorage() {
		return null;
	}

	public void setLayout(PlotBoxLayout layout) {
		// Do nothing
	}

	public PlotBoxLayout getPlotBoxLayout() {
		return null;
	}

	public MarginProps getMargins() {
		return MarginProps.DEFAULT_MARGIN;
	}

	/**
	 * Sets the left margin.
	 *
	 * @param margin the new left margin
	 */
	public void setLeftMargin(int margin) {

	}

	/**
	 * Sets the bottom margin.
	 *
	 * @param margin the new bottom margin
	 */
	public void setBottomMargin(int margin) {

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
		updateMargins(margins);

		fireChanged();
	}

	/**
	 * Update margins.
	 *
	 * @param margins the margins
	 * @return
	 */
	public boolean updateMargins(MarginProps margins) {
		return false;
	}

	/**
	 * Sets whether the layer should be cached as an image rather than being
	 * rendered with primitives.
	 * 
	 * @param on
	 */
	public void setRasterMode(boolean on) {
		mRasterMode = on;

		fireChanged();
	}

	/**
	 * Sets whether the layer should clip on the boundaries.
	 * 
	 * @param on
	 */
	public void setClipMode(boolean on) {
		mClipMode = on;

		fireChanged();
	}

	public final void plot(Graphics2D g2, DrawingContext context, Props params) {
	  
	  
		if (getVisible()) {
		  System.err.println("plbo");
			plot(g2, new Dimension(0, 0), context, params);
		}
	}

	public void plot(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
	  System.err.println("plbo2");
		plotContext(g2, offset, context, params);
	}

	public void plotContext(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
		if (context == DrawingContext.UI) {
			plotScreen(g2, offset, context, params);
		} else {
			plotLayer(g2, offset, context, params);
		}
	}

	/**
	 * Aa plot.
	 *
	 * @param g2        the g 2
	 * @param offset,   context the offset, context
	 * @param subFigure the sub figure
	 * @param axes      the axes
	 * @param plot      the plot
	 * @param m         the m
	 */
	public void plotScreen(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
	  System.err.println("plot sc " + mRasterMode);
	  
		if (mRasterMode) {
			plotRaster(g2, offset, context, params);
		} else {
			plotAA(g2, offset, context, params);
		}
	}

	/**
	 * Optionally plot using anti-aliasing.
	 * 
	 * @param g2
	 * @param offset
	 * @param context
	 * @param params
	 */
	public void plotAA(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {

		if (getAAModes().size() > 0) {
			// Anti-alias by default
			Graphics2D g2Temp = ImageUtils.createAAGraphics(g2, getAAModes());

			try {
				plotLayer(g2Temp, offset, context, params);
			} finally {
				g2Temp.dispose();
			}
		} else {
			plotLayer(g2, offset, context, params);
		}
	}

	/**
	 * Cache plot.
	 *
	 * @param g2        the g 2
	 * @param offset,   context the offset, context
	 * @param subFigure the sub figure
	 * @param axes      the axes
	 * @param plot      the plot
	 * @param m         the m
	 */
	public void plotRaster(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
		// Create an image version of the canvas and draw that to spped
		// up operations
		if (mBufferedImage == null) {
			// The canvas need only be the size of the available display

			Dimension s = getPreferredSize();

			// Make it one pixel bigger to account for borders being drawn
			mBufferedImage = ImageUtils.createImage(s.width + 1, s.height + 1);

			Graphics2D g2Temp = ImageUtils.createGraphics(mBufferedImage);

			try {
				plotAA(g2Temp, new Dimension(), context, params);
			} finally {
				g2Temp.dispose();
			}
		}

		g2.drawImage(mBufferedImage, 0, 0, null);

		plotSize(offset);
	}

	/**
	 * Draw plot.
	 *
	 * @param g2        the g 2
	 * @param offset,   context the offset, context
	 * @param subFigure the sub figure
	 * @param axes      the axes
	 * @param plot      the plot
	 * @param m         the m
	 */
	public void plotLayer(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
		plotSize(offset);
	}

	public PlotBox addChild(PlotBox plot) {
		return this;
	}

	public PlotBox addChild(PlotBox plot, int index) {
		return this;
	}
	
	public PlotBox addChild(PlotBox plot, int i, int j) {
		return this;
	}

	public PlotBox addChild(PlotBox plot, IntPos2D pos) {
		return this;
	}

	public PlotBox getChild(int index) {
		return null;
	}

	public PlotBox getChild(int i, int j) {
		return null;
	}

	public PlotBox getChild(IntPos2D pos) {
		return null;
	}
	
	public PlotBox getChild(GridLocation l) {
		return null;
	}

	public boolean remove(PlotBox plot) {
		return false;
	}

	public boolean remove(int index) {
		return false;
	}

	public boolean remove(IntPos2D pos) {
		return false;
	}

	public PlotBox getByName(String name) {
		return null;
	}

	public PlotBox getChildById(int id) {
		return null;
	}

	public <T extends PlotBox> PlotBox setChildren(List<T> plots) {
		for (T plot : plots) {
			plot.setParent(this);
		}

		return this;
	}

	public Iterable<IntPos2D> getPositions() {
		return null;
	}

	public Iterable<GridLocation> getLocations() {
		return GridLocation.LOCATIONS_LIST;
	}

	public String getType() {
		return "Plot Box";
	}

	public int getChildCount() {
		return 0;
	}

	/**
	 * Returns the next available z layer.
	 * 
	 * @return
	 */
	public int getUnusedZ() {
		return -1;
	}

	public Iterable<Integer> getZ() {
		return null;
	}

	public void clear() {

	}

	@Override
	public Iterator<PlotBox> iterator() {
		return NO_CHILDREN.iterator();
	}

	/**
	 * Create a unique hash string for this plot box.
	 * 
	 * @return
	 */
	@Override
	public abstract String hashId();

	public final void setFont(Font font, Color color) {
		Set<PlotBox> used = new HashSet<PlotBox>();

		setFont(used, font, color);
	}

	public void setFont(Set<PlotBox> used, Font font, Color color) {

	}

	public void setStyle(String name, PlotStyle style, PlotStyle... styles) {
		// TODO Auto-generated method stub
	}

	/**
	 * Sets the style.
	 *
	 * @param style the new style
	 */
	public final void setStyle(PlotStyle style, PlotStyle... styles) {
		Set<PlotBox> used = new HashSet<PlotBox>();

		setStyle(used, style, styles);
	}

	public void setStyle(Set<PlotBox> used, PlotStyle style, PlotStyle... styles) {

	}

	public void addStyle(String name, PlotStyle style, PlotStyle... styles) {
		// TODO Auto-generated method stub
	}

	public final void addStyle(PlotStyle style, PlotStyle... styles) {
		Set<PlotBox> used = new HashSet<PlotBox>();

		addStyle(used, style, styles);
	}

	public void addStyle(Set<PlotBox> used, PlotStyle style, PlotStyle... styles) {

	}

	public void setMatrix(DataFrame m) {
		// TODO Auto-generated method stub
	}

	public void setColorMap(ColorMap colorMap) {
		// TODO Auto-generated method stub
	}

	public void setVisible(boolean visible) {
		mVisible = visible;

		fireChanged();
	}

	public boolean getVisible() {
		return mVisible;
	}

	/**
	 * Find a plotBox in the graph by name.
	 * 
	 * @param name
	 * @return
	 */
	public PlotBox findByName(String name) {
		return findByName(this, name);
	}

	public static PlotBox findByName(PlotBox plot, String name) {
		String ls = name.toLowerCase();

		Deque<PlotBox> stack = new ArrayDeque<PlotBox>(100);

		stack.push(plot);

		while (!stack.isEmpty()) {
			PlotBox p = stack.pop();

			// System.err.println("Find " + p.getName());

			if (p.getName().toLowerCase().contains(ls)) {
				return p;
			}

			// Explore its children
			for (PlotBox c : p) {
				stack.push(c);
			}
		}

		// If this fails, try searching by plot name.
		return findByPlotName(plot, name);
	}

	public PlotBox findByPlotName(String name) {
		return findByPlotName(this, name);
	}

	public static PlotBox findByPlotName(PlotBox plot, String name) {
		String ls = name.toLowerCase();

		Deque<PlotBox> stack = new ArrayDeque<PlotBox>(100);

		stack.push(plot);

		while (!stack.isEmpty()) {
			PlotBox p = stack.pop();

			if (p.getPlotName().toLowerCase().contains(ls)) {
				return p;
			}

			for (PlotBox c : p) {
				stack.push(c);
			}
		}

		return null;
	}

	public PlotBox findByType(String type) {
		return findByType(this, type);
	}

	public static PlotBox findByType(PlotBox plot, String type) {
		String ls = type.toLowerCase();

		PlotBox ret = null;

		Deque<PlotBox> stack = new ArrayDeque<PlotBox>(100);

		stack.push(plot);

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

	public List<PlotBox> getByType(String type) {
		return getByType(this, type);
	}

	public static List<PlotBox> getByType(PlotBox plot, String type) {
		String ls = type.toLowerCase();

		List<PlotBox> ret = new ArrayList<PlotBox>(100);

		Deque<PlotBox> stack = new ArrayDeque<PlotBox>(100);

		stack.push(plot);

		while (!stack.isEmpty()) {
			PlotBox p = stack.pop();

			if (p.getType().toLowerCase().contains(ls)) {
				ret.add(p);
				break;
			}

			for (PlotBox c : p) {
				stack.push(c);
			}
		}

		return ret;
	}

	/**
	 * Apply a function to all a plot box and all children matching a given name
	 * (case insensitive).
	 * 
	 * @param name
	 * @param f
	 */
	public void applyByName(ApplyFunc f, String name, String... names) {
		applyByName(this, f, name, names);
	}

	/**
	 * Apply a function to all plot boxes matching a given name (case insensitive).
	 * 
	 * @param plot
	 * @param name
	 * @param f
	 */
	public static void applyByName(PlotBox plot, ApplyFunc f, String name, String... names) {

		List<String> ls = new ArrayList<String>(1 + names.length);

		ls.add(name.toLowerCase());

		if (names.length > 0) {
			for (String n : TextUtils.toLowerCase(names)) {
				ls.add(n);
			}
		}

		Deque<PlotBox> stack = new ArrayDeque<PlotBox>(100);

		stack.push(plot);

		while (!stack.isEmpty()) {
			PlotBox p = stack.pop();

			String n = p.getName().toLowerCase();

			for (String s : ls) {
				if (n.contains(s)) {
					f.apply(p);
					break;
				}
			}

			for (PlotBox c : p) {
				stack.push(c);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.common.ui.ui.graphics.ModernCanvas#getCanvasSize()
	 */
	public Dimension getPreferredSize() {
		Dimension dim = new Dimension(0, 0);

		if (getVisible()) {
			plotSize(dim);
		}

		return dim;
	}

	/**
	 * Should update dimension to add the size of this plotbox.
	 * 
	 * @param d
	 */
	public void plotSize(Dimension d) {
		// Do nothing
	}

	public void removeByName(String name) {

	}

	// public String getUid() {
	// return mUuid;
	// }

	/*
	 * @Override public int compareTo(PlotBox plot) { return
	 * mName.compareTo(plot.mName); }
	 * 
	 * @Override public boolean equals(Object o) { if (o instanceof PlotBox) {
	 * return compareTo((PlotBox)o) == 0; } else { return false; } }
	 * 
	 * @Override public String toString() { return mName; }
	 */

	/**
	 * Produce a stack trace for the plot box.
	 * 
	 * @param plot
	 */
	public static void toStack(PlotBox plot) {
		System.err.println("--------");
		System.err.println(plot.getName());
		System.err.println("--------");

		Deque<PlotBox> stack = new ArrayDeque<PlotBox>(100);
		Deque<Integer> levelStack = new ArrayDeque<Integer>(100);

		for (PlotBox c : plot) {
			stack.push(c);
			levelStack.push(1);
		}

		while (!stack.isEmpty()) {
			PlotBox p = stack.pop();
			int level = levelStack.pop();

			SysUtils.err().println(level, p.getName());

			int nlevel = level + 1;

			for (PlotBox c : p) {
				SysUtils.err().println(c + " my child");
				stack.push(c);
				levelStack.push(nlevel);
			}
		}

		System.err.println("========");
	}

	public static void childStack(PlotBox plot) {
		System.err.println("--------");
		System.err.println(plot.getName() + plot + " " + plot.getStorage());
		System.err.println("--------");

		for (PlotBox c : plot) {
			SysUtils.err().println(c.getName() + " " + c);
		}

		System.err.println("========");
	}

	/**
	 * Create a plot name.
	 * 
	 * @param type
	 * @param id
	 * @return
	 */
	public static String createId(String type, int id) {
		return type + " " + id;
	}

	public static boolean checkName(String name, PlotBox plot) {
		String ln = name.toLowerCase();

		return plot.getName().toLowerCase().equals(ln) || plot.getPlotName().toLowerCase().equals(ln);
	}
}
