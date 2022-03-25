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

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.modern.graphics.CanvasListener;
import org.jebtk.modern.graphics.CanvasListeners;
import org.jebtk.modern.graphics.CanvasMouseEvent;
import org.jebtk.modern.graphics.CanvasMouseEventProducer;
import org.jebtk.modern.graphics.CanvasMouseListener;
import org.jebtk.modern.graphics.CanvasMouseListeners;

/**
 * The listener interface for receiving layerCanvas events. The class that is
 * interested in processing a layerCanvas event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addLayerCanvasListener<code> method. When the layerCanvas
 * event occurs, that object's appropriate method is invoked.
 *
 * @see LayerCanvasEvent
 */
public class LayerCanvasListener extends CanvasListeners
		implements CanvasListener, CanvasMouseListener, CanvasMouseEventProducer {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The member canvas listeners.
	 */
	private CanvasListeners mCanvasListeners = new CanvasListeners();

	/**
	 * The member canvas mouse listeners.
	 */
	private CanvasMouseListeners mCanvasMouseListeners = new CanvasMouseListeners();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.abh.common.ui.ui.graphics.ModernCanvasMouseListener#canvasMouseClicked(
	 * org.abh.common.ui.ui.graphics.CanvasMouseEvent)
	 */
	@Override
	public void canvasMouseClicked(CanvasMouseEvent e) {
		mCanvasMouseListeners.fireCanvasMouseClicked(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.abh.common.ui.ui.graphics.ModernCanvasMouseListener#canvasMouseEntered(
	 * org.abh.common.ui.ui.graphics.CanvasMouseEvent)
	 */
	@Override
	public void canvasMouseEntered(CanvasMouseEvent e) {
		mCanvasMouseListeners.fireCanvasMouseEntered(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.abh.common.ui.ui.graphics.ModernCanvasMouseListener#canvasMouseExited(
	 * org.abh.common.ui.ui.graphics.CanvasMouseEvent)
	 */
	@Override
	public void canvasMouseExited(CanvasMouseEvent e) {
		mCanvasMouseListeners.fireCanvasMouseExited(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.abh.common.ui.ui.graphics.ModernCanvasMouseListener#canvasMousePressed(
	 * org.abh.common.ui.ui.graphics.CanvasMouseEvent)
	 */
	@Override
	public void canvasMousePressed(CanvasMouseEvent e) {
		mCanvasMouseListeners.fireCanvasMousePressed(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.abh.common.ui.ui.graphics.ModernCanvasMouseListener#canvasMouseReleased
	 * (org.abh.common.ui.ui.graphics.CanvasMouseEvent)
	 */
	@Override
	public void canvasMouseReleased(CanvasMouseEvent e) {
		mCanvasMouseListeners.fireCanvasMouseReleased(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.abh.common.ui.ui.graphics.ModernCanvasMouseListener#canvasMouseDragged(
	 * org.abh.common.ui.ui.graphics.CanvasMouseEvent)
	 */
	@Override
	public void canvasMouseDragged(CanvasMouseEvent e) {
		mCanvasMouseListeners.fireCanvasMouseDragged(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.abh.common.ui.ui.graphics.ModernCanvasMouseListener#canvasMouseMoved(
	 * org.abh.common.ui.ui.graphics.CanvasMouseEvent)
	 */
	@Override
	public void canvasMouseMoved(CanvasMouseEvent e) {
		mCanvasMouseListeners.fireCanvasMouseMoved(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.abh.common.ui.ui.graphics.ModernCanvasListener#canvasChanged(org.abh.
	 * lib.event.ChangeEvent)
	 */
	@Override
	public void canvasChanged(ChangeEvent e) {
		mCanvasListeners.fireCanvasChanged(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.abh.common.ui.ui.graphics.ModernCanvasListener#redrawCanvas(org.abh.lib
	 * .event.ChangeEvent)
	 */
	@Override
	public void redrawCanvas(ChangeEvent e) {
		mCanvasListeners.fireCanvasRedraw(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.abh.common.ui.ui.graphics.ModernCanvasListener#canvasScrolled(org.abh.
	 * lib.event.ChangeEvent)
	 */
	@Override
	public void canvasScrolled(ChangeEvent e) {
		mCanvasListeners.fireCanvasScrolled(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.common.ui.graphics.ModernCanvasListener#canvasResized(org.abh.
	 * common.event.ChangeEvent)
	 */
	@Override
	public void canvasResized(ChangeEvent e) {
		mCanvasListeners.fireCanvasResized(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.common.ui.ui.graphics.ModernCanvasMouseEventProducer#
	 * addCanvasMouseListener(org.abh.common.ui.ui.graphics.
	 * ModernCanvasMouseListener)
	 */
	@Override
	public void addCanvasMouseListener(CanvasMouseListener l) {
		mCanvasMouseListeners.addCanvasMouseListener(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.common.ui.ui.graphics.ModernCanvasMouseEventProducer#
	 * removeCanvasMouseListener(org.abh.common.ui.ui.graphics.
	 * ModernCanvasMouseListener)
	 */
	@Override
	public void removeCanvasMouseListener(CanvasMouseListener l) {
		mCanvasMouseListeners.removeCanvasMouseListener(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.common.ui.ui.graphics.ModernCanvasMouseEventProducer#
	 * fireCanvasMouseMoved(org.abh.common.ui.ui.graphics.CanvasMouseEvent)
	 */
	@Override
	public void fireCanvasMouseMoved(CanvasMouseEvent e) {
		mCanvasMouseListeners.fireCanvasMouseMoved(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.common.ui.ui.graphics.ModernCanvasMouseEventProducer#
	 * fireCanvasMouseDragged(org.abh.common.ui.ui.graphics.CanvasMouseEvent)
	 */
	@Override
	public void fireCanvasMouseDragged(CanvasMouseEvent e) {
		mCanvasMouseListeners.fireCanvasMouseDragged(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.common.ui.ui.graphics.ModernCanvasMouseEventProducer#
	 * fireCanvasMouseClicked(org.abh.common.ui.ui.graphics.CanvasMouseEvent)
	 */
	@Override
	public void fireCanvasMouseClicked(CanvasMouseEvent e) {
		mCanvasMouseListeners.fireCanvasMouseClicked(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.common.ui.ui.graphics.ModernCanvasMouseEventProducer#
	 * fireCanvasMousePressed(org.abh.common.ui.ui.graphics.CanvasMouseEvent)
	 */
	@Override
	public void fireCanvasMousePressed(CanvasMouseEvent e) {
		mCanvasMouseListeners.fireCanvasMousePressed(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.common.ui.ui.graphics.ModernCanvasMouseEventProducer#
	 * fireCanvasMouseReleased(org.abh.common.ui.ui.graphics.CanvasMouseEvent)
	 */
	@Override
	public void fireCanvasMouseReleased(CanvasMouseEvent e) {
		mCanvasMouseListeners.fireCanvasMouseReleased(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.common.ui.ui.graphics.ModernCanvasMouseEventProducer#
	 * fireCanvasMouseEntered(org.abh.common.ui.ui.graphics.CanvasMouseEvent)
	 */
	@Override
	public void fireCanvasMouseEntered(CanvasMouseEvent e) {
		mCanvasMouseListeners.fireCanvasMouseEntered(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.abh.common.ui.ui.graphics.ModernCanvasMouseEventProducer#
	 * fireCanvasMouseExited(org.abh.common.ui.ui.graphics.CanvasMouseEvent)
	 */
	@Override
	public void fireCanvasMouseExited(CanvasMouseEvent e) {
		mCanvasMouseListeners.fireCanvasMouseExited(e);
	}
}
