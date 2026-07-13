package ru.myx.ae3.l2.pdf;

import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

final class PdfElementGrid extends PdfPTable implements PdfElement {
	
	private final PdfElement	parent;
	
	PdfElementGrid(final int columns, final boolean border, final PdfElement parent) {
		super( columns );
		this.setWidthPercentage( 100 );
		this.setSplitLate( false );
		this.setExtendLastRow( false );
		this.getDefaultCell().setBorder( border
				? Rectangle.BOX
				: Rectangle.NO_BORDER );
		this.parent = parent;
	}
	
	@Override
	public void addElement(final PdfTargetContext context, final Element element) {
		final PdfPCell cell = new PdfPCell( this.getDefaultCell() );
		cell.addElement( element );
		this.addCell( cell );
	}
	
	@Override
	public PdfElement doFinishAndGetParent(final PdfTargetContext context) {
		this.completeRow();
		this.parent.addElement( context, this );
		// this.parent.addElement( context, new Phrase() );
		return this.parent;
	}
}
