package ru.myx.ae3.l2.pdf;

import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseObject;

import com.lowagie.text.Rectangle;

class PdfLayoutSequence extends PdfLayoutDefinition {
	@Override
	public BaseObject onExecute(final PdfTargetContext context, final BaseObject layout) {
		final BaseArray array = layout.baseGet( "elements", BaseObject.UNDEFINED ).baseArray();
		if (array == null) {
			return null;
		}
		final PdfElementSequence block = new PdfElementSequence( context.current );
		block.getDefaultCell().setBorder( Rectangle.NO_BORDER );
		return context.setSequence( block, array );
	}
}
