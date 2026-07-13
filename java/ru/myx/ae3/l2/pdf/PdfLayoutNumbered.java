package ru.myx.ae3.l2.pdf;

import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseObject;

class PdfLayoutNumbered extends PdfLayoutDefinition {
	@Override
	public BaseObject onExecute(final PdfTargetContext context, final BaseObject layout) {
		final BaseArray array = layout.baseGet( "elements", BaseObject.UNDEFINED ).baseArray();
		if (array == null) {
			return null;
		}
		final PdfElementNumbered block = new PdfElementNumbered( context.current );
		return context.setSequence( block, array );
	}
}
