package ru.myx.ae3.l2.pdf;

import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;

final class PdfElementNumberedTable extends PdfPTable implements PdfElement {
	
	private final PdfElement	parent;
	
	private int					index	= 0;
	
	PdfElementNumberedTable(final PdfElement parent) {
		super( new float[] { 0.05f, 0.95f } );
		this.getDefaultCell().setBorder( 0 );
		this.setSplitLate( false );
		this.setExtendLastRow( false );
		this.setWidthPercentage( 100 );
		this.parent = parent;
	}
	
	@Override
	public void addElement(final PdfTargetContext context, final Element element) {
		this.index++;
		{
			this.addCell( new Phrase( this.index + ".", context.getFontNormal() ) );
		}
		if (element instanceof Phrase) {
			this.addCell( (Phrase) element );
		} else //
		if (element instanceof PdfPTable) {
			this.addCell( (PdfPTable) element );
		} else {
			this.addCell( new Phrase( "bad type: (" + element.getClass().getName() + ") " + element ) );
		}
	}
	
	@Override
	public PdfElement doFinishAndGetParent(final PdfTargetContext context) {
		this.calculateWidths();
		this.parent.addElement( context, this );
		return this.parent;
	}
}
