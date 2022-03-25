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

import org.jebtk.core.model.ItemModel;

/**
 * The class Graph2dStyleModel.
 */
public class Graph2dStyleModel extends ItemModel<PlotStyle> {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.common.model.ItemModel#get()
	 */
	@Override
	public PlotStyle get() {
		PlotStyle style = super.get();

		if (style == null) {
			set(PlotStyle.JOINED_SMOOTH);

			style = super.get();
		}

		return style;
	}
}
