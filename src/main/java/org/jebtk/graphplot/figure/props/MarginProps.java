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

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.event.ChangeListeners;
import org.jebtk.core.text.TextUtils;

/**
 * Describes the margins on a plot.
 * 
 * @author Antony Holmes
 *
 */
public class MarginProps extends ChangeListeners implements Comparable<MarginProps> {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member top.
	 */
	private int mTop = -1;

	/**
	 * The member left.
	 */
	private int mLeft = -1;

	/**
	 * The member bottom.
	 */
	private int mBottom = -1;

	/**
	 * The member right.
	 */
	private int mRight = -1;

	/** The m text. */
	private String mText = TextUtils.EMPTY_STRING;

	/** The Constant DEFAULT_SIZE. */
	public static final int DEFAULT_SIZE = 100; // 100;

	/** The Constant DEFAULT_MARGIN. */
	public static final MarginProps DEFAULT_MARGIN = new MarginProps(0);

	/**
	 * Instantiates a new margin properties.
	 */
	public MarginProps() {
		this(0);
	}

	/**
	 * Instantiates a new margin properties.
	 *
	 * @param margin the margin
	 */
	public MarginProps(int margin) {
		this(margin, margin, margin, margin);
	}

	/**
	 * Instantiates a new margin properties.
	 *
	 * @param top    the top
	 * @param left   the left
	 * @param bottom the bottom
	 * @param right  the right
	 */
	public MarginProps(int top, int left, int bottom, int right) {
		addChangeListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent e) {
				mText = TextUtils
						.squareBrackets(TextUtils.join(TextUtils.COMMA_DELIMITER, mTop, mLeft, mBottom, mRight));
			}
		});

		setMargins(top, left, bottom, right);
	}

	/**
	 * Instantiates a new margin properties.
	 *
	 * @param margins the margins
	 */
	public MarginProps(MarginProps margins) {
		setMargins(margins);
	}

	/**
	 * Sets the margins.
	 *
	 * @param margins the new margins
	 */
	private void setMargins(MarginProps margins) {
		setMargins(margins.mTop, margins.mLeft, margins.mBottom, margins.mRight);
	}

	// private void updateMargins(MarginProps margins) {
	// updateMargins(margins.getTop(), margins.getLeft(), margins.getBottom(),
	// margins.getRight());
	// }

	// private void setMargins(int m) {
	// setMargins(m, m, m, m);
	// }

	// private void updateMargins(int m) {
	// updateMargins(m, m, m, m);
	// }

	/**
	 * Sets the plot margins.
	 *
	 * @param top    the top
	 * @param left   the left
	 * @param bottom the bottom
	 * @param right  the right
	 */
	private void setMargins(int top, int left, int bottom, int right) {
		boolean fire = updateMargins(top, left, bottom, right);

		if (fire) {
			fireChanged();
		}
	}

	/**
	 * Update margins.
	 *
	 * @param top    the top
	 * @param left   the left
	 * @param bottom the bottom
	 * @param right  the right
	 * @return true, if successful
	 */
	private boolean updateMargins(int top, int left, int bottom, int right) {
		boolean fire = mTop != top || mLeft != left || mBottom != bottom || mRight != right;

		if (fire) {
			mTop = top;
			mLeft = left;
			mBottom = bottom;
			mRight = right;
		}

		return fire;
	}

	/**
	 * Returns the height of the plot.
	 *
	 * @return the top
	 */
	public int getTop() {
		return mTop;
	}

	/**
	 * Gets the left.
	 *
	 * @return the left
	 */
	public int getLeft() {
		return mLeft;
	}

	/**
	 * Gets the bottom.
	 *
	 * @return the bottom
	 */
	public int getBottom() {
		return mBottom;
	}

	/**
	 * Gets the right.
	 *
	 * @return the right
	 */
	public int getRight() {
		return mRight;
	}

	/**
	 * Return the sum of the line margins (left + right).
	 * 
	 * @return
	 */
	public int getLineMargin() {
		return mLeft + mRight;
	}

	/**
	 * Return the sum of the page margins (top + bottom).
	 * 
	 * @return
	 */
	public int getPageMargin() {
		return mTop + mBottom;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return mText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof MarginProps) {
			return compareTo((MarginProps) o) == 0;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(MarginProps m) {
		return (mTop - m.mTop) + (mLeft - m.mLeft) + (mBottom - m.mBottom) + (mRight - m.mRight);
	}

}
