package org.jebtk.graphplot.figure;

import java.awt.Graphics2D;

import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.ImageUtils;

public class PlotClipRect implements PlotClip {
	@Override
	public Graphics2D clip(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes,
			Plot plot) {
		Graphics2D g2Temp = ImageUtils.clone(g2);

		g2Temp.clipRect(0, 0, axes.getInternalSize().getW(), axes.getInternalSize().getH());

		return g2Temp;
	}
}
