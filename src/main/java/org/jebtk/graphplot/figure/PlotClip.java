package org.jebtk.graphplot.figure;

import java.awt.Graphics2D;

import org.jebtk.modern.graphics.DrawingContext;

public interface PlotClip {
	public Graphics2D clip(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes,
			Plot plot);

}
