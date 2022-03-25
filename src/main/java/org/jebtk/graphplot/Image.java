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
package org.jebtk.graphplot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.fop.svg.PDFTranscoder;
import org.apache.xmlgraphics.java2d.ps.EPSDocumentGraphics2D;
import org.jebtk.core.Props;
import org.jebtk.core.geom.IntDim;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.io.TmpService;
import org.jebtk.graphplot.plotbox.PlotBox;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.ImageUtils;
import org.jebtk.modern.graphics.ModernCanvas;
import org.jebtk.modern.window.ModernWindow;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.svg.SVGDocument;

/**
 * The class Image.
 */
public class Image {

	/**
	 * The constant DPI_300_CONVERSION.
	 */
	public static final Float DPI_300_CONVERSION = new Float(25.4f / 300f);

	/**
	 * The constant DPI_600_CONVERSION.
	 */
	public static final Float DPI_600_CONVERSION = new Float(25.4f / 600f);

	/**
	 * Instantiates a new image.
	 */
	private Image() {
		//
	}

	/**
	 * Sends the graphic output of a canvas through the svg renderer rather than
	 * on-screen.
	 *
	 * @param canvas the canvas
	 * @param file   the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void translateSvg(ModernCanvas canvas, Path file) throws IOException {

		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		SVGDocument doc = (SVGDocument) impl.createDocument(svgNS, "svg", null);

		SVGGraphics2D g2 = new SVGGraphics2D(doc);

		IntDim d = canvas.getAbsPreferredSize();

		try {
			g2.setFont(ModernPlotCanvas.PLOT_FONT);
			g2.setSVGCanvasSize(IntDim.toDimension(d));

			// Canvases should be using this method for correctly off setting
			// images. Since we don't want to do that for saving the SVG,
			// we call the method directory so that drawCanvasForeground is
			// bypassed to prevent the canvas

			// temp store the current view rectangle
			// IntRect tempRect = canvas.getViewRect();

			// Create a view rect big enough for the whole canvas
			// canvas.updateViewRectangle(canvas.getCanvasSize());

			// Create the drawing
			canvas.rasterCanvas(g2, DrawingContext.OUTPUT);

			// Writer out = new PathWriter(file);
			Writer out = FileUtils.newBufferedWriter(file); // new
															// OutputStreamWriter(new
															// PathOutputStream(file),
															// "UTF-8");

			try {
				g2.stream(out, true);
			} finally {
				out.close();
			}
		} finally {
			g2.dispose();
		}
	}

	public static void translateSvg(PlotBox canvas, Path file) throws IOException {

		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		SVGDocument doc = (SVGDocument) impl.createDocument(svgNS, "svg", null);

		SVGGraphics2D g2 = new SVGGraphics2D(doc);

		Dimension d = canvas.getPreferredSize();

		try {
			g2.setFont(ModernPlotCanvas.PLOT_FONT);
			g2.setSVGCanvasSize(d);

			System.err.println("Image size " + g2.getSVGCanvasSize());

			// Canvases should be using this method for correctly off setting
			// images. Since we don't want to do that for saving the SVG,
			// we call the method directory so that drawCanvasForeground is
			// bypassed to prevent the canvas

			// temp store the current view rectangle
			// IntRect tempRect = canvas.getViewRect();

			// Create a view rect big enough for the whole canvas
			// canvas.updateViewRectangle(canvas.getCanvasSize());

			// Create the drawing
			canvas.plot(g2, DrawingContext.OUTPUT, new Props());

			// Writer out = new PathWriter(file);
			Writer out = FileUtils.newBufferedWriter(file); // new
															// OutputStreamWriter(new
															// PathOutputStream(file),
															// "UTF-8");

			try {
				g2.stream(out, true);
			} finally {
				out.close();
			}
		} finally {
			g2.dispose();
		}
	}

	/**
	 * Translate png.
	 *
	 * @param canvas the canvas
	 * @param file   the file
	 * @throws IOException         Signals that an I/O exception has occurred.
	 * @throws TranscoderException the transcoder exception
	 */
	public static void translatePng(ModernCanvas canvas, Path file) throws IOException, TranscoderException {

		PNGTranscoder trans = new PNGTranscoder();

		trans.addTranscodingHint(ImageTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER, DPI_300_CONVERSION);

		translate(canvas, trans, file);
	}

	/**
	 * Translate pdf.
	 *
	 * @param canvas the canvas
	 * @param file   the file
	 * @throws IOException         Signals that an I/O exception has occurred.
	 * @throws TranscoderException the transcoder exception
	 */
	public static void translatePdf(ModernCanvas canvas, Path file) throws IOException, TranscoderException {
		Transcoder trans = new PDFTranscoder();

		trans.addTranscodingHint(ImageTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER, DPI_600_CONVERSION);

		translate(canvas, trans, file);
	}

	public static void translatePdf(PlotBox canvas, Path file) throws IOException, TranscoderException {
		Transcoder trans = new PDFTranscoder();

		trans.addTranscodingHint(ImageTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER, DPI_600_CONVERSION);

		translate(canvas, trans, file);
	}

	/**
	 * Translate.
	 *
	 * @param canvas     the canvas
	 * @param transcoder the transcoder
	 * @param file       the file
	 * @throws IOException         Signals that an I/O exception has occurred.
	 * @throws TranscoderException the transcoder exception
	 */
	public static void translate(ModernCanvas canvas, Transcoder transcoder, Path file)
			throws IOException, TranscoderException {

		Path svgPath = TmpService.getInstance().newTmpFile("svg");

		translateSvg(canvas, svgPath);

		// Transcode the file.
		String svgURI = svgPath.toUri().toURL().toString();

		TranscoderInput input = new TranscoderInput(svgURI);
		OutputStream ostream = FileUtils.newOutputStream(file); // new
																// PathOutputStream(file);
		TranscoderOutput output = new TranscoderOutput(ostream);

		try {
			transcoder.transcode(input, output);
		} finally {
			// Flush and close the output.
			ostream.flush();
			ostream.close();
		}
	}

	public static void translate(PlotBox canvas, Transcoder transcoder, Path file)
			throws IOException, TranscoderException {

		Path svgPath = TmpService.getInstance().newTmpFile("svg");

		translateSvg(canvas, svgPath);

		// Transcode the file.
		String svgURI = svgPath.toUri().toURL().toString();

		TranscoderInput input = new TranscoderInput(svgURI);
		OutputStream ostream = FileUtils.newOutputStream(file); // new
																// PathOutputStream(file);
		TranscoderOutput output = new TranscoderOutput(ostream);

		try {
			transcoder.transcode(input, output);
		} finally {
			// Flush and close the output.
			ostream.flush();
			ostream.close();
		}
	}

	/**
	 * Write png.
	 *
	 * @param canvas the canvas
	 * @param file   the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writePng(ModernCanvas canvas, Path file) throws IOException {
		write(canvas, file, "png");
	}

	public static void writePng(PlotBox canvas, Path file) throws IOException {
		write(canvas, file, "png");
	}

	/**
	 * Write jpg.
	 *
	 * @param canvas the canvas
	 * @param file   the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeJpg(ModernCanvas canvas, Path file) throws IOException {
		write(canvas, file, "jpg");
	}

	public static void writeJpg(PlotBox canvas, Path file) throws IOException {
		write(canvas, file, "jpg");
	}

	/**
	 * Write.
	 *
	 * @param canvas the canvas
	 * @param file   the file
	 * @param type   the type
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void write(ModernCanvas canvas, Path file, String type) throws IOException {
		IntDim d = canvas.getAbsPreferredSize();

		BufferedImage bufferedImage;

		System.err.println("png " + canvas.getPreferredSize() + " " + canvas.getAbsPreferredSize());

		if (type.equals("png")) {
			bufferedImage = new BufferedImage(d.getW(), d.getH(), BufferedImage.TYPE_INT_ARGB);
		} else {
			bufferedImage = new BufferedImage(d.getW(), d.getH(), BufferedImage.TYPE_INT_RGB);
		}

		Graphics2D g2 = ImageUtils.createAATextGraphics(bufferedImage.createGraphics());

		// temp store the current view rectangle
		// IntRect tempRect = canvas.getViewRect();

		System.err.println("Image size: " + canvas.getCanvasSize());

		try {
			g2.setFont(ModernPlotCanvas.PLOT_FONT);

			if (!type.equals("png")) {
				g2.setBackground(Color.WHITE);
				g2.setColor(g2.getBackground());
				g2.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
			}

			// Create a view rect big enough for the whole canvas
			// canvas.updateViewRectangle(canvas.getCanvasSize());

			canvas.rasterCanvas(g2, DrawingContext.OUTPUT);
		} finally {
			g2.dispose();
		}

		// canvas.updateViewRectangle(tempRect);

		ImageIO.write(bufferedImage, type, file.toFile());
	}

	public static void write(PlotBox canvas, Path file, String type) throws IOException {
		Dimension d = canvas.getPreferredSize();

		BufferedImage bufferedImage;

		if (type.equals("png")) {
			bufferedImage = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
		} else {
			bufferedImage = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
		}

		Graphics2D g2 = ImageUtils.createAATextGraphics(bufferedImage.createGraphics());

		// temp store the current view rectangle
		// IntRect tempRect = canvas.getViewRect();

		try {
			g2.setFont(ModernPlotCanvas.PLOT_FONT);

			if (!type.equals("png")) {
				g2.setBackground(Color.WHITE);
				g2.setColor(g2.getBackground());
				g2.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
			}

			// Create a view rect big enough for the whole canvas
			// canvas.updateViewRectangle(canvas.getCanvasSize());

			canvas.plot(g2, DrawingContext.OUTPUT, new Props());
		} finally {
			g2.dispose();
		}

		// canvas.updateViewRectangle(tempRect);

		ImageIO.write(bufferedImage, type, file.toFile());
	}

	/**
	 * Export a plot to disk using a GUI.
	 *
	 * @param parent the parent
	 * @param canvas the canvas
	 * @param pwd    the working directory
	 * @throws IOException         Signals that an I/O exception has occurred.
	 * @throws TranscoderException the transcoder exception
	 */
	public static void export(ModernWindow parent, ModernCanvas canvas, Path pwd)
			throws IOException, TranscoderException {
		Path file = saveFile(parent, pwd);

		if (file == null) {
			return;
		}

		if (FileUtils.exists(file)) {
			ModernDialogStatus status = ModernMessageDialog.createFileReplaceDialog(parent, file);

			if (status == ModernDialogStatus.CANCEL) {
				export(parent, canvas, pwd);

				return;
			}
		}

		write(parent, canvas, file);
	}

	/**
	 * Create a file save dialog specifically for saving a canvas as an image.
	 *
	 * @param parent the parent
	 * @param pwd    the pwd
	 * @return the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Path saveFile(ModernWindow parent, Path pwd) throws IOException {
		return ImageDialog.save(parent).getImage().getFile(pwd);
	}

	/**
	 * Write.
	 *
	 * @param parent the parent
	 * @param canvas the canvas
	 * @param file   the file
	 * @throws IOException         Signals that an I/O exception has occurred.
	 * @throws TranscoderException the transcoder exception
	 */
	public static void write(ModernWindow parent, ModernCanvas canvas, Path file)
			throws IOException, TranscoderException {
		write(canvas, file);

		ModernMessageDialog.createFileSavedDialog(parent, parent.getAppInfo().getName(), file);
	}

	/**
	 * Write the canvas to file based on its extension.
	 *
	 * @param canvas the canvas
	 * @param file   the file
	 * @throws IOException         Signals that an I/O exception has occurred.
	 * @throws TranscoderException the transcoder exception
	 */
	public static void write(ModernCanvas canvas, Path file) throws IOException, TranscoderException {
		String ext = PathUtils.getFileExt(file);

		if (ext.equals("pdf")) {
			translatePdf(canvas, file);
		} else if (ext.equals("png")) {
			writePng(canvas, file);
		} else if (ext.equals("jpg")) {
			writeJpg(canvas, file);
		} else {
			translateSvg(canvas, file);
		}
	}

	public static void write(PlotBox canvas, Path file) throws IOException, TranscoderException {
		String ext = PathUtils.getFileExt(file);

		if (ext.equals("pdf")) {
			translatePdf(canvas, file);
		} else if (ext.equals("png")) {
			writePng(canvas, file);
		} else if (ext.equals("jpg")) {
			writeJpg(canvas, file);
		} else {
			translateSvg(canvas, file);
		}
	}

	/**
	 * Write eps.
	 *
	 * @param canvas the canvas
	 * @param file   the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeEps(ModernCanvas canvas, Path file) throws IOException {
		OutputStream out = FileUtils.newOutputStream(file);

		EPSDocumentGraphics2D g2 = new EPSDocumentGraphics2D(false);
		g2.setGraphicContext(new org.apache.xmlgraphics.java2d.GraphicContext());

		try {
			g2.setupDocument(out, canvas.getPreferredSize().width, canvas.getPreferredSize().height);

			g2.setFont(ModernPlotCanvas.PLOT_FONT);

			canvas.rasterCanvas(g2, DrawingContext.OUTPUT);

			g2.finish();
		} finally {
			out.close();
		}
	}

	/**
	 * Creates the trans buff im.
	 *
	 * @param w the w
	 * @param h the h
	 * @return the buffered image
	 */
	public static BufferedImage createTransBuffIm(int w, int h) {
		return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	}

	/**
	 * Creates the buff im.
	 *
	 * @param w the w
	 * @param h the h
	 * @return the buffered image
	 */
	public static BufferedImage createBuffIm(int w, int h) {
		return new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	}
}