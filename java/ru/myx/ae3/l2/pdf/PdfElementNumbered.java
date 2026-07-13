package ru.myx.ae3.l2.pdf;

import com.lowagie.text.Element;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;

final class PdfElementNumbered extends List implements PdfElement {
	
	private final PdfElement		parent;
	
	private PdfElementNumberedTable	table	= null;
	
	PdfElementNumbered(final PdfElement parent) {
		super( true );
		this.setNumbered( true );
		this.setFirst( 1 );
		this.setAutoindent( true );
		this.setPostSymbol( ". " );
		this.parent = parent;
	}
	
	@Override
	public void addElement(final PdfTargetContext context, final Element element) {
		if (this.table != null) {
			this.table.addElement( context, element );
		} else //
		if (element instanceof Phrase) {
			this.add( new ListItem( (Phrase) element ) );
		} else //
		if (element instanceof PdfPTable) {
			this.table = new PdfElementNumberedTable( this.parent );
			for (final Object o : this.getItems()) {
				this.table.addElement( context, (Element) o );
			}
			this.table.addElement( context, element );
		} else {
			this.add( element );
		}
	}
	
	@Override
	public PdfElement doFinishAndGetParent(final PdfTargetContext context) {
		if (this.table != null) {
			return this.table.doFinishAndGetParent( context );
		}
		this.normalizeIndentation();
		this.parent.addElement( context, this );
		return this.parent;
	}
}
