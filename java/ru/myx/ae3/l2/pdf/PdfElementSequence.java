package ru.myx.ae3.l2.pdf;

import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

final class PdfElementSequence extends PdfPTable implements PdfElement {
	
	private final PdfElement	parent;
	
	PdfElementSequence(final PdfElement parent) {
		super( 1 );
		this.setSplitLate( false );
		this.setWidthPercentage( 100 );
		this.setExtendLastRow( false );
		this.parent = parent;
	}
	
	//
	
	@Override
	public void addElement(final PdfTargetContext context, final Element element) {
		final PdfPCell cell = new PdfPCell( this.getDefaultCell() );
		cell.addElement( element );
		this.addCell( cell );
	}
	
	@Override
	public PdfElement doFinishAndGetParent(final PdfTargetContext context) {
		this.parent.addElement( context, this );
		return this.parent;
	}
}
