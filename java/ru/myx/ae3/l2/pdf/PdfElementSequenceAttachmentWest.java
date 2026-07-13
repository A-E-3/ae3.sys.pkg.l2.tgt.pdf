package ru.myx.ae3.l2.pdf;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;

import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

final class PdfElementSequenceAttachmentWest extends PdfPTable implements PdfContextHandler, PdfElement {
	
	private final PdfElement	parent;
	
	PdfElementSequenceAttachmentWest(final PdfElement parent) {
		super( 2 );
		this.setWidthPercentage( 100 );
		this.parent = parent;
	}
	
	//
	
	@Override
	public void addElement(final PdfTargetContext context, final Element element) {
		final PdfPCell cell = element instanceof Phrase
				? new PdfPCell( (Phrase) element )
				: element instanceof PdfPTable
						? new PdfPCell( (PdfPTable) element )
						: new PdfPCell( new Phrase( "bad type: (" + element.getClass().getName() + ") " + element ) );
		cell.setBorder( 0 );
		this.addCell( cell );
	}
	
	@Override
	public PdfElement doFinishAndGetParent(final PdfTargetContext context) {
		this.calculateWidths();
		this.parent.addElement( context, this );
		return this.parent;
	}
	
	@Override
	public BaseObject onEnter(final PdfTargetContext target, final BaseObject layout) {
		return null;
	}
	
	@Override
	public void onLeave(final PdfTargetContext target) {
		target.onLeave( target );
	}
	
	@Override
	public BaseObject onNest(final PdfTargetContext target, final BaseObject layout) {
		if ("replacement".equals( Base.getString( layout, "layout", "" ) )) {
			final String string = Base.getString( layout, "attachment", "" );
			final PdfPCell cell = new PdfPCell( new Phrase( string, target.getFontNormal() ) );
			cell.setBorder( 0 );
			this.addCell( cell );
		}
		return layout;
	}
}
