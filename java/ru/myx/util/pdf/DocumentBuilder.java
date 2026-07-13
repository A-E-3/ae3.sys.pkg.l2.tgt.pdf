package ru.myx.util.pdf;

import java.io.IOException;
import java.io.StringReader;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferCollector;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.l2.pdf.PdfElementDocument;
import ru.myx.ae3.l2.pdf.PdfTargetFragmentContext;
import ru.myx.ae3.serve.ServeRequest;
import ru.myx.ae3.serve.SimpleServeRequest;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.FontMapper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * @author myx
 * 
 */
public class DocumentBuilder {
	/**
	 * 
	 */
	protected final FontMapper			fontMapper;
	
	private final float					w, h;
	
	private final PdfElementDocument	document;
	
	private int							topPadding	= 0;
	
	private final PdfWriter				writer;
	
	private final TransferCollector		collector;
	
	/**
	 * 
	 * @param title
	 * @param pageSize
	 * @param marginTop
	 * @param marginRight
	 * @param marginBottom
	 * @param marginLeft
	 * @throws Exception
	 */
	public DocumentBuilder(final String title,
			final Rectangle pageSize,
			final int marginTop,
			final int marginRight,
			final int marginBottom,
			final int marginLeft) throws Exception {
		this.fontMapper = new DefaultFontMapper();
		this.w = pageSize.getWidth();
		this.h = pageSize.getHeight();
		this.document = new PdfElementDocument( pageSize,
		// margins
				marginLeft, // left
				marginRight, // right
				marginTop, // top
				marginBottom // bottom
		);
		this.topPadding = (int) Math.ceil( this.document.topMargin() );
		
		this.collector = Transfer.createCollector();
		this.writer = PdfWriter.getInstance( this.document, this.collector.getOutputStream() );
		this.writer.setViewerPreferences( PdfWriter.PageLayoutSinglePage | PdfWriter.PageModeUseOutlines );
		this.document.open();
		this.document.addCreationDate();
		this.document.addTitle( title );
		this.document.addCreator( "ae3 pdf component" );
	}
	
	/**
	 * 
	 * @param htmlText
	 * @throws IOException
	 */
	public void appendHtml(final String htmlText) throws IOException {
		final HTMLWorker htmlWorker = new HTMLWorker( this.document );
		htmlWorker.getStyleSheet().loadStyle( "body", "face", "Courier" );
		htmlWorker.getStyleSheet().loadStyle( "body", "font-family", "Courier" );
		htmlWorker.getStyleSheet().loadStyle( "body", "size", "10pt" );
		htmlWorker.getStyleSheet().loadStyle( "body", "font-size", "10" );
		htmlWorker.getStyleSheet().loadStyle( "th", "size", "8" );
		htmlWorker.getStyleSheet().loadStyle( "td", "size", "8" );
		htmlWorker.getStyleSheet().loadStyle( "td", "font-size", "8" );
		htmlWorker.parse( new StringReader( htmlText ) );
		htmlWorker.close();
	}
	
	/**
	 * 
	 * @param layout
	 * @throws Exception
	 */
	public void appendLayout(final BaseObject layout) throws Exception {
		final ServeRequest query = new SimpleServeRequest();
		query.setResourceIdentifier( "/document.pdf" );
		new PdfTargetFragmentContext( this.document, query, null, null ).transform( layout );
	}
	
	/**
	 * 
	 * @param xhtmlText
	 * @throws IOException
	 */
	public void appendXhtml(final String xhtmlText) throws IOException {
		// XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
		final HTMLWorker htmlWorker = new HTMLWorker( this.document );
		// worker.parseXHtml(pdfWriter, document, new StringReader(str));
		htmlWorker.parse( new StringReader( xhtmlText ) );
		htmlWorker.close();
	}
	
	/**
	 * 
	 * @throws DocumentException
	 */
	public void flushPage() throws DocumentException {
		this.topPadding = (int) Math.ceil( this.document.topMargin() );
		this.document.newPage();
	}
	
	/**
	 * 
	 * @return
	 */
	public TransferCopier toBinary() {
		this.document.close();
		this.writer.close();
		return this.collector.toBinary();
	}
}
