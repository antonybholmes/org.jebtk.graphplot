package org.jebtk.graphplot.figure;

import java.awt.Graphics2D;

import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.ImageUtils;

/**
 * Clip along all boundaries except for positive Y
 * 
 * @author antony
 *
 */
public class PlotClipRectY implements PlotClip {
	@Override
	public Graphics2D clip(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes,
			Plot plot) {
		Graphics2D g2Temp = ImageUtils.clone(g2);

		int offset = figure.getMargins().getTop() + subFigure.getMargins().getTop() + axes.getMargins().getTop();

		g2Temp.clipRect(0, -offset, axes.getInternalSize().getW(), axes.getInternalSize().getH() + offset);

		return g2Temp;
	}
}
