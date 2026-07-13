package ru.myx.ae3.l2.pdf;

import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;

final class PdfElementParagraph extends Paragraph implements PdfElement {
	
	private final PdfElement	parent;
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4737394114135484938L;
	
	PdfElementParagraph(final PdfElement parent) {
		this.parent = parent;
	}
	
	//
	
	@Override
	public void addElement(final PdfTargetContext context, final Element element) {
		this.add( element );
	}
	
	@Override
	public PdfElement doFinishAndGetParent(final PdfTargetContext context) {
		this.parent.addElement( context, this );
		return this.parent;
	}
}
