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
package org.jebtk.graphplot.figure.props;

import java.awt.Color;
import java.awt.Font;

import org.jebtk.graphplot.ModernPlotCanvas;
import org.jebtk.modern.font.FontService;

/**
 * Describes the font for a plot object.
 * 
 * @author Antony Holmes
 *
 */
public class FontProps extends ColorProps {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member font.
	 */
	private Font mFont = ModernPlotCanvas.PLOT_FONT;

	/**
	 * Instantiates a new font properties.
	 */
	public FontProps() {
		super(Color.BLACK);
	}

	/**
	 * Copy.
	 *
	 * @param Props the font
	 */
	public void copy(FontProps properties) {
		setFont(properties.mFont);

		super.copy(properties);
	}

	/**
	 * Sets the font size.
	 *
	 * @param size the new font size
	 */
	public void setFontSize(int size) {
		setFont(FontService.getInstance().loadFont(mFont.getFamily(), mFont.getStyle(), size));
	}

	/**
	 * Sets the family.
	 *
	 * @param family the new family
	 */
	public void setFamily(String family) {
		setFont(FontService.getInstance().loadFont(family, mFont.getStyle(), mFont.getSize()));
	}

	/**
	 * Sets the bold.
	 */
	public void setBold() {
		setFont(FontService.getInstance().loadFont(mFont.getFamily(), Font.BOLD, mFont.getSize()));
	}

	/**
	 * Sets the italic.
	 */
	public void setItalic() {
		setFont(FontService.getInstance().loadFont(mFont.getFamily(), Font.ITALIC, mFont.getSize()));
	}

	/**
	 * Sets the bold italic.
	 */
	public void setBoldItalic() {
		setFont(FontService.getInstance().loadFont(mFont.getFamily(), Font.BOLD | Font.ITALIC, mFont.getSize()));
	}

	/**
	 * Sets the plain.
	 */
	public void setPlain() {
		setFont(FontService.getInstance().loadFont(mFont.getFamily(), Font.PLAIN, mFont.getSize()));
	}

	/**
	 * Sets the font family.
	 * 
	 * @param font The font family.
	 */
	public void setFont(Font font) {
		updateFont(font);

		fireChanged();
	}

	/**
	 * Sets the font.
	 *
	 * @param font  the font
	 * @param color the color
	 */
	public void setFont(Font font, Color color) {
		updateFont(font);
		updateColor(color);

		fireChanged();
	}

	/**
	 * Update font.
	 *
	 * @param font the font
	 */
	public void updateFont(Font font) {
		mFont = font;
	}

	/**
	 * Returns the font family.
	 * 
	 * @return The font family.
	 */
	public Font getFont() {
		return mFont;
	}

}
