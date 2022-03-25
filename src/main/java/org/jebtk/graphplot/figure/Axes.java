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
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jebtk.core.IntId;
import org.jebtk.core.Props;
import org.jebtk.core.StringId;
import org.jebtk.core.collections.DefaultHashMap;
import org.jebtk.core.collections.HashMapCreator;
import org.jebtk.core.collections.IterMap;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.geom.DoublePos2D;
import org.jebtk.core.geom.IntDim;
import org.jebtk.core.geom.IntPos2D;
import org.jebtk.core.stream.ListReduceFunction;
import org.jebtk.core.stream.Stream;
import org.jebtk.core.text.Join;
import org.jebtk.graphplot.figure.props.LegendProps;
import org.jebtk.graphplot.figure.props.StyleProps;
import org.jebtk.graphplot.figure.props.TitleProps;
import org.jebtk.graphplot.figure.series.XYPoint;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.graphplot.plotbox.PlotBox;
import org.jebtk.graphplot.plotbox.PlotBoxZLayout;
import org.jebtk.graphplot.plotbox.PlotBoxZStorage;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.MatrixGroup;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Represents a 2D Cartesian graph. This class draws basic axes and titles but
 * should be subclassed to provide specific plot functionality.
 * 
 * @author Antony Holmes
 *
 */
public class Axes extends PlotBoxGraph {
	private static abstract class CachePoints {
		protected Axes mAxes;

		protected IterMap<Double, IterMap<Double, Point>> mXYMap = DefaultHashMap
				.create(new HashMapCreator<Double, Point>());

		public CachePoints(Axes axes) {
			mAxes = axes;
		}

		public Point toPlotXY(double x, double y) {
			if (!mXYMap.get(x).containsKey(y)) {
				mXYMap.get(x).put(y, new Point(toPlotX(x), toPlotY(y)));
			}

			return mXYMap.get(x).get(y);
		}

		protected abstract int toPlotX(double x);

		protected abstract int toPlotY(double x);

		public void clear() {
			mXYMap.clear();
		}
	}

	private static class CachePointsX1Y1 extends CachePoints {
		public CachePointsX1Y1(Axes axes) {
			super(axes);
		}

		@Override
		protected int toPlotX(double x) {
			return mAxes.toPlotX1(x);
		}

		@Override
		protected int toPlotY(double x) {
			return mAxes.toPlotY1(x);
		}
	}

	private static class CachePointsX1Y2 extends CachePointsX1Y1 {
		public CachePointsX1Y2(Axes axes) {
			super(axes);
		}

		@Override
		protected int toPlotY(double x) {
			return mAxes.toPlotY2(x);
		}
	}

	private static class CachePointsX2Y1 extends CachePoints {
		public CachePointsX2Y1(Axes axes) {
			super(axes);
		}

		@Override
		protected int toPlotX(double x) {
			return mAxes.toPlotX2(x);
		}

		@Override
		protected int toPlotY(double x) {
			return mAxes.toPlotY1(x);
		}
	}

	private static class CachePointsX2Y2 extends CachePointsX2Y1 {
		public CachePointsX2Y2(Axes axes) {
			super(axes);
		}

		@Override
		protected int toPlotY(double x) {
			return mAxes.toPlotY2(x);
		}
	}

	private class AxesCachePoints {
		private CachePoints mX1Y1Map;
		private CachePoints mX1Y2Map;
		private CachePoints mX2Y1Map;
		private CachePoints mX2Y2Map;

		public AxesCachePoints(Axes axes) {
			mX1Y1Map = new CachePointsX1Y1(axes);
			mX1Y2Map = new CachePointsX1Y2(axes);
			mX2Y1Map = new CachePointsX2Y1(axes);
			mX2Y2Map = new CachePointsX2Y2(axes);
		}

		public Point toPlotXY(double x, XAxisType xAxis, double y, YAxisType yAxis) {
			if (xAxis == XAxisType.X1) {
				// X1

				if (yAxis == YAxisType.Y1) {
					return mX1Y1Map.toPlotXY(x, y);
				} else {
					return mX1Y2Map.toPlotXY(x, y);
				}
			} else {
				// X2
				if (yAxis == YAxisType.Y1) {
					return mX2Y1Map.toPlotXY(x, y);
				} else {
					return mX2Y2Map.toPlotXY(x, y);
				}
			}
		}

		public Point toPlotX1Y1(double x, double y) {
			return mX1Y1Map.toPlotXY(x, y); // toPlotXY(x, XAxisType.X1, y,
											// YAxisType.Y1);
		}

		public void clear() {
			mX1Y1Map.clear();
			mX1Y2Map.clear();
			mX2Y1Map.clear();
			mX2Y2Map.clear();
		}
	}

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The x1 axis.
	 */
	private Axis mX1Axis = new Axis("X Axis 1");

	/**
	 * The x2 axis (top).
	 */
	private Axis mX2Axis = new Axis("X Axis 2");

	/**
	 * The y1 axis.
	 */
	private Axis mY1Axis = new Axis("Y Axis 1");

	/**
	 * The y2 axis.
	 */
	private Axis mY2Axis = new Axis("Y Axis 2");

	/**
	 * The legend.
	 */
	private LegendProps mLegend = new LegendProps();

	/**
	 * The style.
	 */
	private StyleProps mStyle = new StyleProps();

	/**
	 * The title.
	 */
	private TitleProps mTitle = new TitleProps();

	/** The m next axes id. */
	protected final IntId mNextAxesId = new IntId();

	protected final IntId mNextPlotId = new IntId();

	/** The m x1 axis trans. */
	private AxisTranslation mX1AxisTrans;

	/** The m y1 axis trans. */
	private AxisTranslation mY1AxisTrans;

	/** The m x2 axis trans. */
	private AxisTranslation mX2AxisTrans;

	/** The m y2 axis trans. */
	private AxisTranslation mY2AxisTrans;

	public static final IntDim DEFAULT_SIZE = new IntDim(800, 400);

	public static final int RESERVED_Z_GRID = -1000;
	public static final int RESERVED_Z_X_AXIS_1 = 1000;
	public static final int RESERVED_Z_X_AXIS_2 = 2000;
	public static final int RESERVED_Z_Y_AXIS_1 = 3000;
	public static final int RESERVED_Z_Y_AXIS_2 = 4000;
	public static final int RESERVED_Z_LEGEND = 5000;
	public static final int RESERVED_Z_TITLE = 6000;

	private IntDim mInternalSize = DEFAULT_SIZE;

	private Plot mCurrentPlot;

	private AxesCachePoints mPointsCache;

	private static final StringId NEXT_ID = new StringId("Axes");

	/**
	 * The class GraphEvents.
	 */
	private class GraphEvents implements ChangeListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
		 */
		@Override
		public void changed(ChangeEvent e) {
			refresh(); // redraw();
		}
	}

	private class ForwardEvents implements ChangeListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
		 */
		@Override
		public void changed(ChangeEvent e) {
			fireChanged();
		}
	}

	/**
	 * Instantiates a new axes.
	 *
	 * @param name the name
	 */
	public Axes(String name) {
		super(name, new PlotBoxZStorage(), new PlotBoxZLayout());

		mPointsCache = new AxesCachePoints(this);

		mX1AxisTrans = new AxisTranslationX1(this);
		mY1AxisTrans = new AxisTranslationY1(this);
		mX2AxisTrans = new AxisTranslationX2(this);
		mY2AxisTrans = new AxisTranslationY2(this);

		GraphEvents ge = new GraphEvents();
		ForwardEvents fe = new ForwardEvents();

		mTitle.addChangeListener(fe);
		mLegend.addChangeListener(fe);
		mStyle.addChangeListener(fe);

		mX1Axis.addChangeListener(ge);
		mX2Axis.addChangeListener(ge);
		mY1Axis.addChangeListener(ge);
		mY2Axis.addChangeListener(ge);

		// Default to not drawing outlines
		// mStyle.getLineStyle().setColor(Color.LIGHT_GRAY);
		mStyle.getLineStyle().setVisible(false);
		mStyle.getFillStyle().setVisible(false);

		mX1Axis.getTitle().setText("X");
		mX1Axis.getTitle().getFontStyle().setVisible(false);

		// Default x2 axis to being invisible
		mX2Axis.getTitle().setText("Y");
		mX2Axis.setVisible(false);

		mY1Axis.getTitle().setText("Y");
		mY1Axis.getTitle().getFontStyle().setVisible(false);

		// Default y2 axis to being invisible
		mY2Axis.getTitle().setText("Y");
		mY2Axis.setVisible(false);

		addChild(new Grid2dLayer(), RESERVED_Z_GRID);
		addChild(new AxisLayerX1(), RESERVED_Z_X_AXIS_1);
		addChild(new AxisLayerX2(), RESERVED_Z_X_AXIS_2);
		addChild(new AxisLayerY1(), RESERVED_Z_Y_AXIS_1);
		addChild(new AxisLayerY2(), RESERVED_Z_Y_AXIS_2);

		// DO NOT NEED
		// mAxesLayers.setZ(new LabelPlotLayer(), 4000);

		addChild(new LegendLayer(), RESERVED_Z_LEGEND);
		addChild(new AxesTitleLayer(), RESERVED_Z_TITLE);
	}

	@Override
	protected boolean cacheCurrent(PlotBox plot) {
		if (plot instanceof Plot) {
			mCurrentPlot = (Plot) plot;
			return true;
		} else {
			return false;
		}
	}

	public void setInternalHeight(int h) {
		setInternalSize(getInternalSize().getW(), h);
	}

	public Axes setInternalSize(int w, int h) {
		return setInternalSize(new IntDim(w, h));
	}

	public Axes setInternalSize(Dimension d) {
		return setInternalSize(IntDim.create(d));
	}

	public Axes setInternalSize(IntDim d) {
		if (!d.equals(mInternalSize)) {
			mInternalSize = d;

			refresh();

			fireChanged();
		}

		return this;
	}

	public IntDim getInternalSize() {
		return mInternalSize;
	}

	@Override
	public void plotSize(Dimension dim) {
		dim.width = mInternalSize.getW();
		dim.height = mInternalSize.getH();

		addMargin(dim);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.graphplot.figure.PlotLocationGrid#getType()
	 */
	@Override
	public String getType() {
		return LayerType.AXES;
	}

	/**
	 * New axes.
	 *
	 * @param l the l
	 * @return the axes
	 */
	public Plot newPlot() {
		return newPlot(createId(LayerType.PLOT, mNextPlotId.getNextId()));
	}

	public Plot newPlot(String name) {
		mCurrentPlot = new Plot(name);

		addChild(mCurrentPlot);

		return mCurrentPlot;
	}

	public Plot newPlot(GridLocation l) {
		return newPlot();
	}

	/**
	 * Gets the axes.
	 *
	 * @param name the name
	 * @param l    the l
	 * @return the axes
	 */
	public Plot getPlot(String name) {
		PlotBox c = getByName(name);

		if (c == null || !(c instanceof Plot) || !c.getName().equals(name)) {
			c = newPlot(name);

			addChild(c);
		}

		return (Plot) c;
	}

	public Plot getPlot(int id) {
		PlotBox c = getChildById(id);

		if (c != null) {
			return (Plot) c;
		} else {
			return null;
		}
	}

	/**
	 * Gets the current axes.
	 *
	 * @param l the l
	 * @return the current axes
	 */
	public Plot currentPlot() {
		if (mCurrentPlot == null) {
			newPlot();
		}

		return mCurrentPlot;
	}

	/**
	 * Returns the layer model to control what is displayed on the plot and in what
	 * order.
	 *
	 * @return the axes layer z model
	 */
	// public AxesLayerZModel getAxesLayerZModel() {
	// return mAxesLayers;
	// }

	/**
	 * Sets the axis limits auto round.
	 */
	public void setAxisLimitsAutoRound() {
		setXAxisLimitAutoRound();
		setY1AxisLimitAutoRound();

		// setY2AxisLimitAutoRound();
	}

	/**
	 * Sets the x axis limit auto round.
	 */
	public void setXAxisLimitAutoRound() {
		double min = getXMin(this);

		if (min > 0) {
			min = 0;
		}

		double max = getXMax(this);

		mX1Axis.setLimitsAutoRound(min, max);
	}

	/**
	 * Sets the y1 axis limit auto round.
	 */
	public void setY1AxisLimitAutoRound() {
		double min = getY1Min(this);

		if (min > 0) {
			min = 0;
		}

		double max = getY1Max(this);

		mY1Axis.setLimitsAutoRound(min, max);
	}

	/**
	 * Sets the y2 axis limit auto round.
	 */
	public void setY2AxisLimitAutoRound() {
		double min = getY2Min(this);

		if (min > 0) {
			min = 0;
		}

		double max = getY2Max(this);

		mY2Axis.setLimitsAutoRound(min, max);
	}

	/**
	 * Gets the x axis.
	 *
	 * @return the x axis
	 */
	public Axis getX1Axis() {
		return mX1Axis;
	}

	/**
	 * Gets the x1 axis trans.
	 *
	 * @return the x1 axis trans
	 */
	public AxisTranslation getX1AxisTrans() {
		return mX1AxisTrans;
	}

	/**
	 * Gets the x2 axis.
	 *
	 * @return the x2 axis
	 */
	public Axis getX2Axis() {
		return mX2Axis;
	}

	/**
	 * Gets the x2 axis trans.
	 *
	 * @return the x2 axis trans
	 */
	public AxisTranslation getX2AxisTrans() {
		return mX2AxisTrans;
	}

	/**
	 * Gets the y1 axis.
	 *
	 * @return the y1 axis
	 */
	public Axis getY1Axis() {
		return mY1Axis;
	}

	/**
	 * Gets the y1 axis trans.
	 *
	 * @return the y1 axis trans
	 */
	public AxisTranslation getY1AxisTrans() {
		return mY1AxisTrans;
	}

	/**
	 * Gets the y2 axis.
	 *
	 * @return the y2 axis
	 */
	public Axis getY2Axis() {
		return mY2Axis;
	}

	/**
	 * Gets the y2 axis trans.
	 *
	 * @return the y2 axis trans
	 */
	public AxisTranslation getY2AxisTrans() {
		return mY2AxisTrans;
	}

	/**
	 * Gets the legend.
	 *
	 * @return the legend
	 */
	public LegendProps getLegend() {
		return mLegend;
	}

	/**
	 * Gets the style.
	 *
	 * @return the style
	 */
	public StyleProps getStyle() {
		return mStyle;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public TitleProps getTitle() {
		return mTitle;
	}

	// public FillProps getFill() {
	// return mFill;
	// }

	/**
	 * Returns true if the point is within the bounds of the graph.
	 *
	 * @param point the point
	 * @return true, if successful
	 */
	public boolean withinXY1Bounds(XYPoint point) {
		return mX1Axis.withinBounds(point.getX()) && mY1Axis.withinBounds(point.getY());
	}

	/*
	 * @Override public void plot(Graphics2D g2, DrawingContext context, SubFigure
	 * subFigure) { Graphics2D g2Temp = ImageUtils.clone(g2);
	 * 
	 * try { g2Temp.translate(getMargins().getLeft(), getMargins().getTop());
	 * 
	 * ZModel<MovableLayer> layers = mLocations.getChild(GridLocation.CENTER);
	 * 
	 * for (int z : layers) { MovableLayer c = layers.getChild(z);
	 * 
	 * if (c.getVisible()) { //SysUtils.err().println("axes", getName(),
	 * c.getName(), c.getVisible());
	 * 
	 * c.plot(g2Temp, context, subFigure, this); } } } finally { g2Temp.dispose(); }
	 * }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.graphplot.figure.Layer#setFont(java.awt.Font, java.awt.Color)
	 */
	@Override
	public void setFont(Set<PlotBox> used, Font font, Color color) {
		mX1Axis.setFont(font, color);
		mX2Axis.setFont(font, color);
		mY1Axis.setFont(font, color);
		mY2Axis.setFont(font, color);

		getTitle().getFontStyle().setFont(font, color);

		// mZ1Axis.setFont(font, color);
		// mZ1Axis.setFont(font, color);
	}

	public void setAxisVisible(boolean visible) {
		mX1Axis.setVisible(visible);
		mX2Axis.setVisible(visible);
		mY1Axis.setVisible(visible);
		mY2Axis.setVisible(visible);
	}

	public void refresh() {
		// Dimension s = getPreferredSize();
		// mX1AxisTrans.update(mXOffset, s.width);
		// mX2AxisTrans.update(mXOffset, s.width);
		// mY1AxisTrans.update(mYOffset, s.height);
		// mY2AxisTrans.update(mYOffset, s.height);

		mPointsCache.clear();

		// fireCanvasRedraw();

		fireChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.graphplot.figure.MovableLayer#hashId()
	 */
	@Override
	public String hashId() {
		return Join.onDash().values(getPreferredSize(), getX1Axis().getLimits().getMin(),
				getX1Axis().getLimits().getMax(), getY1Axis().getLimits().getMin(), getY1Axis().getLimits().getMax())
				.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Axes) {
			return ((Axes) o).hashId().equals(hashId());
		} else {
			return false;
		}
	}

	/**
	 * To plot x1.
	 *
	 * @param x the x
	 * @return the int
	 */
	public int toPlotX1(double x) {
		return mX1AxisTrans.toPlot(x);
	}

	/**
	 * To plot y1.
	 *
	 * @param y the y
	 * @return the int
	 */
	public int toPlotY1(double y) {
		return mY1AxisTrans.toPlot(y);
	}

	/**
	 * To plot x2.
	 *
	 * @param x the x
	 * @return the int
	 */
	public int toPlotX2(double x) {
		return mX2AxisTrans.toPlot(x);
	}

	/**
	 * To plot y2.
	 *
	 * @param y the y
	 * @return the int
	 */
	public int toPlotY2(double y) {
		return mY2AxisTrans.toPlot(y);
	}

	/**
	 * To plot x y1.
	 *
	 * @param m the m
	 * @param s the s
	 * @return the list
	 */
	public List<Point> toPlotX1Y1(DataFrame m, XYSeries s) {
		int n = m.getRows();

		List<Integer> columns = MatrixGroup.findColumnIndices(m, s);

		List<Point> points = new ArrayList<Point>(n);

		for (int i = 0; i < n; ++i) {
			points.add(toPlotX1Y1(m.getValue(i, columns.get(0)), m.getValue(i, columns.get(1))));
		}

		return points;
	}

	/**
	 * Converts a point from a series into the plot coordinates so it can be drawn.
	 *
	 * @param p the p
	 * @return the point
	 */
	public Point toPlotX1Y1(XYPoint p) {
		return toPlotX1Y1(p.getX(), p.getY());
	}

	/**
	 * To plot x y1.
	 *
	 * @param p the p
	 * @return the point
	 */
	public Point toPlotX1Y1(DoublePos2D p) {
		return toPlotX1Y1(p.getX(), p.getY());
	}

	/**
	 * To plot x y1.
	 *
	 * @param p the p
	 * @return the point
	 */
	public Point toPlotX1Y1(IntPos2D p) {
		return toPlotX1Y1(p.getX(), p.getY());
	}

	/**
	 * To plot x y1.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the point
	 */
	public Point toPlotX1Y1(double x, double y) {
		return mPointsCache.toPlotX1Y1(x, y); // toPlotXY(x, XAxisType.X1, y,
												// YAxisType.Y1);
	}

	public Point toPlotXY(double x, XAxisType xAxis, double y, YAxisType yAxis) {
		return mPointsCache.toPlotXY(x, xAxis, y, yAxis);
	}

	public AxesCachePoints getPointsCache() {
		return mPointsCache;
	}

	//
	// Static Methods
	//

	/**
	 * Gets the x max.
	 *
	 * @param axes the axes
	 * @return the x max
	 */
	public static double getXMax(Axes axes) {
		double ret = Double.MIN_VALUE;

		for (PlotBox c : axes) {
			if (c.getType().equals(LayerType.PLOT)) {
				Plot plot = (Plot) c;

				double m = Plot.getXMax(plot);

				if (m > ret) {
					ret = m;
				}
			}
		}

		return ret;
	}

	/**
	 * Gets the y1 max.
	 *
	 * @param axes the axes
	 * @return the y1 max
	 */
	public static double getY1Max(Axes axes) {
		double ret = Double.MIN_VALUE;

		for (PlotBox c : axes) {
			if (c.getType().equals(LayerType.PLOT)) {
				Plot plot = (Plot) c;

				double m = Plot.getY1Max(plot);

				if (m > ret) {
					ret = m;
				}
			}
		}

		return ret;
	}

	/**
	 * Gets the y2 max.
	 *
	 * @param axes the axes
	 * @return the y2 max
	 */
	public static double getY2Max(Axes axes) {
		double ret = Double.MIN_VALUE;

		for (PlotBox c : axes) {
			if (c.getType().equals(LayerType.PLOT)) {
				Plot plot = (Plot) c;

				double m = Plot.getY2Max(plot);

				if (m > ret) {
					ret = m;
				}
			}
		}

		return ret;
	}

	/**
	 * Gets the x min.
	 *
	 * @param axes the axes
	 * @return the x min
	 */
	public static double getXMin(Axes axes) {
		double ret = Double.MAX_VALUE;

		for (PlotBox c : axes) {
			if (c.getType().equals(LayerType.PLOT)) {
				Plot plot = (Plot) c;

				double m = Plot.getXMin(plot);

				if (m < ret) {
					ret = m;
				}
			}
		}

		return ret;
	}

	/**
	 * Gets the y1 min.
	 *
	 * @param axes the axes
	 * @return the y1 min
	 */
	public static double getY1Min(Axes axes) {
		double ret = Double.MAX_VALUE;

		for (PlotBox c : axes) {
			if (c.getType().equals(LayerType.PLOT)) {
				Plot plot = (Plot) c;

				double m = Plot.getY1Min(plot);

				if (m < ret) {
					ret = m;
				}
			}
		}

		return ret;
	}

	/**
	 * Gets the y2 min.
	 *
	 * @param axes the axes
	 * @return the y2 min
	 */
	public static double getY2Min(Axes axes) {
		double ret = Double.MAX_VALUE;

		for (PlotBox c : axes) {
			if (c.getType().equals(LayerType.PLOT)) {
				Plot plot = (Plot) c;

				double m = Plot.getY2Min(plot);

				if (m < ret) {
					ret = m;
				}
			}
		}

		return ret;
	}

	/**
	 * Disable all features.
	 *
	 * @param axes the axes
	 */
	public static void disableAllFeatures(Axes axes) {
		enableAllFeatures(axes, false);
	}

	/**
	 * Enable all features.
	 *
	 * @param axes the axes
	 */
	public static void enableAllFeatures(Axes axes) {
		enableAllFeatures(axes, true);
	}

	/**
	 * Enable all features.
	 *
	 * @param axes   the axes
	 * @param enable the enable
	 */
	public static void enableAllFeatures(Axes axes, boolean enable) {
		axes.getTitle().getFontStyle().setVisible(enable);

		axes.getStyle().setVisible(enable);

		Axis.enableAllFeatures(axes.getX1Axis(), enable);
		Axis.enableAllFeatures(axes.getX2Axis(), enable);
		Axis.enableAllFeatures(axes.getY1Axis(), enable);
		Axis.enableAllFeatures(axes.getY2Axis(), enable);
		// Axis.enableAllFeatures(axes.getZAxis(), enable);
	}

	public Iterable<Plot> getPlots() {
		return Stream.of(this).reduce(new ListReduceFunction<PlotBox, Plot>() {

			@Override
			public void apply(PlotBox plot, List<Plot> values) {
				if (plot instanceof Plot) {
					values.add((Plot) plot);
				}
			}
		});
	}

	@Override
	public void plot(Graphics2D g2, Dimension offset, DrawingContext context, Props params) {
		super.plot(g2, offset, context, params.set("axes", this));
	}

	/**
	 * Create a new axes object.
	 * 
	 * @return
	 */
	public static Axes createAxes() {
		return createAxes(NEXT_ID.getNextId());
	}

	public static Axes createAxes(String name) {
		return new Axes(name);
	}
}
