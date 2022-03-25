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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.List;

import org.jebtk.core.IntId;
import org.jebtk.core.Props;
import org.jebtk.core.StringId;
import org.jebtk.core.stream.ListReduceFunction;
import org.jebtk.core.stream.Stream;
import org.jebtk.graphplot.plotbox.PlotBox;
import org.jebtk.graphplot.plotbox.PlotBoxCompassGridLayout;
import org.jebtk.graphplot.plotbox.PlotBoxCompassGridStorage;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * A Figure is a collection of axes layered on top of each other. In a simple
 * plot, there will be typically only be one set of axes.
 * 
 * @author Antony Holmes
 */
public class SubFigure extends PlotBoxGraph { // LayoutLayer

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	private IntId mNextSubFigureId = new IntId();

	/** The m next axes id. */
	private IntId mNextAxesId = new IntId();

	private IntId mNextPlotId = new IntId();

	/** The m vert alignment. */
	private FigureVertAlignment mVertAlignment = FigureVertAlignment.TOP;

	private Axes mCurrentAxes;

	private SubFigure mCurrentSubFigure;

	private static final StringId NEXT_ID = new StringId("Sub Figure");

	public SubFigure() {
		this(NEXT_ID.getNextId());
	}

	/**
	 * Instantiates a new sub figure.
	 *
	 * @param id the id
	 */
	public SubFigure(String id) {
		super(id, new PlotBoxCompassGridStorage(), new PlotBoxCompassGridLayout());
	}

	@Override
	protected boolean cacheCurrent(PlotBox plot) {
		if (plot instanceof SubFigure) {
			mCurrentSubFigure = (SubFigure) plot;
			return true;
		} else if (plot instanceof Axes) {
			mCurrentAxes = (Axes) plot;
			return true;
		} else {
			return false;
		}
	}

	public Axes newAxes() {
		return newAxes(GridLocation.CENTER);
	}

	/**
	 * Return a new axes object with the compass grid layout enabled.
	 * 
	 * @return
	 */
	public Axes newGridAxes() {
		Axes ret = newAxes();
		ret.setLayout(new PlotBoxCompassGridLayout());
		ret.setStorage(new PlotBoxCompassGridStorage());

		return ret;
	}

	/**
	 * New axes.
	 *
	 * @param l the l
	 * @return the axes
	 */
	public Axes newAxes(GridLocation l) {
		return newAxes(createId(LayerType.AXES, mNextAxesId.getNextId()), l);
	}

	public Axes newAxes(String name, GridLocation l) {
	  System.err.println("sub new axes");
	  
		mCurrentAxes = new Axes(name);

		add(mCurrentAxes, l);

		return mCurrentAxes;
	}

	public Axes getAxes(String name) {
		return getAxes(name, GridLocation.CENTER);
	}

	public Axes getAxes(String name, GridLocation l) {
		PlotBox c = getChild(l);

		if (c == null || !(c instanceof Axes) || !checkName(name, c)) {
			c = newAxes(l);
		}

		return (Axes) c;
	}

	public Axes getAxes(int name) {
		return getAxes(name, GridLocation.CENTER);
	}

	public Axes getAxes(int id, GridLocation l) {
		PlotBox c = getChild(l);

		if (c == null || !(c instanceof Axes) || c.getId() != id) {
			c = newAxes(l);
		}

		return (Axes) c;
	}

	public Axes currentAxes() {
	  System.err.println("current axes");
		return currentAxes(GridLocation.CENTER);
	}

	public Axes currentAxes(GridLocation l) {
		if (mCurrentAxes == null) {
			newAxes(l);
		}

		return mCurrentAxes;
	}

	/**
	 * New sub figure.
	 *
	 * @return the sub figure
	 */
	public SubFigure newSubFigure() {
		return newSubFigure(GridLocation.CENTER);
	}

	/**
	 * New sub figure.
	 *
	 * @param l the l
	 * @return the sub figure
	 */
	public SubFigure newSubFigure(GridLocation l) {
		mCurrentSubFigure = new SubFigure(createId(LayerType.SUBFIGURE, mNextSubFigureId.getNextId()));

		add(mCurrentSubFigure, l);

		return mCurrentSubFigure;
	}

	public SubFigure currentSubFigure() {
		return currentSubFigure(GridLocation.CENTER);
	}

	public SubFigure currentSubFigure(GridLocation l) {
		if (mCurrentSubFigure == null) {
			newSubFigure(l);
		}

		return mCurrentSubFigure;
	}

	public Plot newPlot() {
		return newPlot(GridLocation.CENTER);
	}

	public Plot newPlot(GridLocation l) {
		Plot plot = new Plot(createId(LayerType.PLOT, mNextPlotId.getNextId()));

		addChild(plot);

		return plot;
	}

	public void addPlot(Plot plot) {
		addPlot(plot, GridLocation.CENTER);
	}

	public void addPlot(Plot plot, GridLocation l) {
		add(plot, l);
	}

	// public void addChild(Axes axes, GridLocation l) {
	// addChild(axes, l);
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.graphplot.figure.MovableLayer#getType()
	 */
	@Override
	public String getType() {
		return LayerType.SUBFIGURE;
	}

	/**
	 * Gets the sub figure.
	 *
	 * @param id the id
	 * @return the sub figure
	 */
	public SubFigure getSubFigure(int id) {
		return getSubFigure(id, GridLocation.CENTER);
	}

	/**
	 * Gets the sub figure.
	 *
	 * @param id the id
	 * @param l  the l
	 * @return the sub figure
	 */
	public SubFigure getSubFigure(int id, GridLocation l) {
		PlotBox c = getChild(l);

		if (c == null || !(c instanceof SubFigure) || c.getId() != id) {
			c = newSubFigure(l);
		}

		return (SubFigure) c;
	}

	/**
	 * Sets the vert alignment.
	 *
	 * @param alignment the new vert alignment
	 */
	public void setVertAlignment(FigureVertAlignment alignment) {
		mVertAlignment = alignment;

		fireChanged();
	}

	/**
	 * Gets the vert alignment.
	 *
	 * @return the vert alignment
	 */
	public FigureVertAlignment getVertAlignment() {
		return mVertAlignment;
	}

	public Iterable<Axes> getAllAxes() {
		return Stream.of(this).reduce(new ListReduceFunction<PlotBox, Axes>() {

			@Override
			public void apply(PlotBox plot, List<Axes> values) {
				if (plot instanceof Axes) {
					values.add((Axes) plot);
				}
			}
		});
	}

	@Override
	public void plot(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
		super.plot(g2, offset, context, params.set("subfig", this));
	}

	public static String createSubFigureId(int id) {
		return createId(LayerType.SUBFIGURE, id);
	}

	public static SubFigure createSubFigure() {
		return createSubFigure(NEXT_ID.getNextId());
	}

	public static SubFigure createSubFigure(String name) {
		return new SubFigure(name);
	}
}
