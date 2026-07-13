/**
 * 
 */
package ru.myx.ae3.l2.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;

/**
 * 
 * @author myx
 * 
 */
public final class PdfElementDocument extends Document implements PdfElement {
	/**
	 * 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 */
	public PdfElementDocument(final Rectangle arg0,
			final float arg1,
			final float arg2,
			final float arg3,
			final float arg4) {
		super( arg0, arg1, arg2, arg3, arg4 );
	}
	
	@Override
	public void addElement(final PdfTargetContext context, final Element element) {
		try {
			this.add( element );
		} catch (final DocumentException e) {
			throw new RuntimeException( e );
		}
	}
	
	@Override
	public PdfElement doFinishAndGetParent(final PdfTargetContext context) {
		return null;
	}
	
}
