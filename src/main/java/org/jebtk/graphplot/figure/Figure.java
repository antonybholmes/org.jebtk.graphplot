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

import org.jebtk.core.Function;
import org.jebtk.core.IntId;
import org.jebtk.core.Props;
import org.jebtk.core.StringId;
import org.jebtk.core.stream.Stream;
import org.jebtk.graphplot.plotbox.PlotBox;
import org.jebtk.graphplot.plotbox.PlotBoxDimStorage;
import org.jebtk.graphplot.plotbox.PlotBoxLayout;
import org.jebtk.graphplot.plotbox.PlotBoxRowLayout;
import org.jebtk.graphplot.plotbox.PlotBoxRowsLayout;
import org.jebtk.graphplot.plotbox.PlotBoxStorage;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Allows multiple plots to be placed on a grid and drawn together.
 *
 * @author Antony Holmes
 */
public class Figure extends PlotBoxGraph {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	private static final StringId NEXT_ID = new StringId("Figure");

	/** The m next sub figure id. */
	private IntId mNextSubFigureId = new IntId();

	private SubFigure mCurrentSubFigure;

	/**
	 * Instantiates a new figure grid canvas.
	 *
	 * @param name the name
	 */
	public Figure(String name) {
		this(name, new PlotBoxDimStorage(), new PlotBoxRowsLayout(2));
	}

	public Figure(String name, PlotBoxStorage storage, PlotBoxLayout layout) {
		super(name, storage, layout);
	}

	public Figure(String name, PlotBoxLayout layout) {
		super(name, layout);
	}

	/**
	 * Instantiates a new figure.
	 *
	 * @param figure the figure
	 */
	public Figure(String name, SubFigure figure) {
		this(name);

		addChild(figure);
	}

	public Figure addSubFigure(SubFigure subFigure) {
		addChild(subFigure);

		return this;
	}

	@Override
	protected boolean cacheCurrent(PlotBox plot) {
		if (plot instanceof SubFigure) {
			mCurrentSubFigure = (SubFigure) plot;

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Creates the new axes.
	 *
	 * @return the axes
	 */
	public SubFigure newSubFigure() {
		mCurrentSubFigure = new SubFigure(createId(LayerType.SUBFIGURE, mNextSubFigureId.getNextId()));

		addChild(mCurrentSubFigure);

		return mCurrentSubFigure;
	}

	/**
	 * Gets the sub figure by name.
	 *
	 * @param name the name
	 * @return the axes
	 */
	public SubFigure getSubFigure(String name) {
		return (SubFigure) getByName(name);
	}

	/**
	 * Returns the current sub figure. If there is no current sub figure, one will
	 * be created.
	 * 
	 * @return The current sub figure.
	 */
	public SubFigure currentSubFigure() {
		if (mCurrentSubFigure == null) {
			newSubFigure();
		}

		return mCurrentSubFigure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.graphplot.figure.ZLayer#getType()
	 */
	@Override
	public String getType() {
		return LayerType.FIGURE;
	}

	public Iterable<SubFigure> getSubFigures() {
		return getSubFigures(this);
	}

	public static Iterable<SubFigure> getSubFigures(Figure figure) {
		return Stream.of(figure).map(new Function<PlotBox, SubFigure>() {

			@Override
			public SubFigure apply(PlotBox item) {
				return (SubFigure) item;
			}
		});
	}

	@Override
	public void plot(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
		super.plot(g2, offset, context, params.set("fig", this));
	}

	public static Figure createFigure() {
		return createFigure(NEXT_ID.getNextId());
	}

	public static Figure createFigure(String name) {
		return new Figure(name);
	}

	public static Figure createRowFigure() {
		return createRowFigure(NEXT_ID.getNextId());
	}

	public static Figure createRowFigure(String name) {
		return new Figure(name, new PlotBoxRowLayout());
	}
}
