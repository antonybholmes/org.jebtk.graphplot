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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Set;

import org.jebtk.core.Props;
import org.jebtk.core.collections.ReverseIterator;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.graphplot.plotbox.PlotBox;
import org.jebtk.graphplot.plotbox.PlotBoxContainer;
import org.jebtk.graphplot.plotbox.PlotBoxLayout;
import org.jebtk.graphplot.plotbox.PlotBoxStorage;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.colormap.ColorMap;

/**
 * A Figure is a collection of axes layered on top of each other. In a simple
 * plot, there will be typically only be one set of axes.
 * 
 * @author Antony Holmes
 */
public abstract class PlotBoxGraph extends PlotBoxContainer { // LayoutLayer

	private static final long serialVersionUID = 1L;

	/**
	 * Keep track of the number of times this plot has been rendered since a change.
	 * We can use this to decide whether to refresh a cached portion of the graph
	 * tree. If there are no changes, there is no need to change the cache
	 */
	private int mDrawCounter = 0;

	/**
	 * Instantiates a new sub figure.
	 *
	 * @param id the id
	 */
	public PlotBoxGraph(String id, PlotBoxStorage storage, PlotBoxLayout layout) {
		super(id, storage, layout);

		init();
	}

	public PlotBoxGraph(String id, PlotBoxLayout layout) {
		super(id, layout);

		init();
	}

	private void init() {
		this.addChangeListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent e) {
				// reset
				mDrawCounter = 0;
			}
		});
	}

	@Override
	public PlotBox add(PlotBox plot, GridLocation l) {
		cacheCurrent(plot);

		return super.add(plot, l);
	}

	@Override
	public <T extends PlotBox> PlotBox setChildren(List<T> plots) {
		for (T plot : ReverseIterator.create(plots)) {
			if (cacheCurrent(plot)) {
				break;
			}
		}

		return super.setChildren(plots);
	}

	/**
	 * Should set the currently cached axes, sub figure etc when a new child is
	 * added. Should return true if an item was cached.
	 * 
	 * @param plot
	 * @return
	 */
	protected boolean cacheCurrent(PlotBox plot) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.graphplot.figure.Layer#setFont(java.awt.Font, java.awt.Color)
	 */
	@Override
	public void setFont(Set<PlotBox> used, Font font, Color color) {
		if (used.contains(this)) {
			return;
		}

		Deque<PlotBox> stack = new ArrayDeque<PlotBox>(100);

		stack.push(this);
		used.add(this);

		while (!stack.isEmpty()) {
			PlotBox p = stack.pop();

			p.setFont(used, font, color);

			for (PlotBox c : p) {
				stack.push(c);
			}
		}
	}

	@Override
	public void setStyle(Set<PlotBox> used, PlotStyle style, PlotStyle... styles) {
		if (used.contains(this)) {
			return;
		}

		Deque<PlotBox> stack = new ArrayDeque<PlotBox>(100);

		stack.push(this);
		used.add(this);

		while (!stack.isEmpty()) {
			PlotBox p = stack.pop();

			p.setStyle(used, style, styles);

			for (PlotBox c : p) {
				stack.push(c);
			}
		}
	}

	@Override
	public void addStyle(Set<PlotBox> used, PlotStyle style, PlotStyle... styles) {
		if (used.contains(this)) {
			return;
		}

		Deque<PlotBox> stack = new ArrayDeque<PlotBox>(100);

		stack.push(this);
		used.add(this);

		while (!stack.isEmpty()) {
			PlotBox p = stack.pop();

			p.addStyle(used, style, styles);

			for (PlotBox c : p) {
				stack.push(c);
			}
		}
	}

	/**
	 * Sets the style.
	 *
	 * @param name   the name
	 * @param styles the styles
	 */
	public void setStyle(String name, PlotStyle style, PlotStyle... styles) {
		for (PlotBox c : this) {
			c.setStyle(name, style, styles);
		}

		/*
		 * Deque<PlotBox> stack = new ArrayDeque<PlotBox>(100);
		 * 
		 * for (PlotBox c : this) { stack.push(c); }
		 * 
		 * while (!stack.isEmpty()) { PlotBox p = stack.pop();
		 * 
		 * p.setStyle(name, style, styles);
		 * 
		 * for (PlotBox c : p) { stack.push(c); } }
		 */
	}

	/**
	 * Adds the style.
	 *
	 * @param name   the name
	 * @param styles the styles
	 */
	public void addStyle(String name, PlotStyle style, PlotStyle... styles) {
		for (PlotBox c : this) {
			c.addStyle(name, style, styles);
		}

		/*
		 * Deque<PlotBox> stack = new ArrayDeque<PlotBox>(100); while (!stack.isEmpty())
		 * { PlotBox p = stack.pop();
		 * 
		 * p.addStyle(name, style, styles);
		 * 
		 * for (PlotBox c : p) { stack.push(c); } }
		 */
	}

	/**
	 * Sets the matrix.
	 *
	 * @param m the new matrix
	 */
	public void setMatrix(DataFrame m) {
		for (PlotBox c : this) {
			c.setMatrix(m);
		}

		/*
		 * Deque<PlotBox> stack = new ArrayDeque<PlotBox>(100);
		 * 
		 * for (PlotBox c : this) { stack.push(c); }
		 * 
		 * while (!stack.isEmpty()) { PlotBox p = stack.pop();
		 * 
		 * p.setMatrix(m);
		 * 
		 * for (PlotBox c : p) { stack.push(c); } }
		 */
	}

	/**
	 * Sets the color map.
	 *
	 * @param colorMap the new color map
	 */
	public void setColorMap(ColorMap colorMap) {
		for (PlotBox c : this) {
			c.setColorMap(colorMap);
		}

		/*
		 * Deque<PlotBox> stack = new ArrayDeque<PlotBox>(100);
		 * 
		 * for (PlotBox c : this) { stack.push(c); }
		 * 
		 * while (!stack.isEmpty()) { PlotBox p = stack.pop();
		 * 
		 * p.setColorMap(colorMap);
		 * 
		 * for (PlotBox c : p) { stack.push(c); } }
		 */
	}

	@Override
	public void setVisible(boolean visible) {
		for (PlotBox c : this) {
			c.setVisible(visible);
		}

		/*
		 * Deque<PlotBox> stack = new ArrayDeque<PlotBox>(100);
		 * 
		 * for (PlotBox c : this) { stack.push(c); }
		 * 
		 * while (!stack.isEmpty()) { PlotBox p = stack.pop();
		 * 
		 * p.setVisible(visible);
		 * 
		 * for (PlotBox c : p) { stack.push(c); } }
		 */
	}

	/**
	 * Returns true if the graph stack has been invalidated and should be
	 * updated/redraw. Most useful for elements that cache the look of a plot.
	 * 
	 * @return
	 */
	public boolean invalidated() {
		return mDrawCounter == 0;
	}

	public int getDrawCounter() {
		return mDrawCounter;
	}

	@Override
	public void plot(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
		super.plot(g2, offset, context, params);

		++mDrawCounter;
	}
}
