package ru.myx.ae3.l2.pdf;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseString;

class PdfLayoutContainer extends PdfLayoutDefinition {
	@Override
	public BaseObject onExecute(final PdfTargetContext context, final BaseObject layout) {
		final BaseObject content = layout.baseGet( "content", BaseString.EMPTY );
		return content;
	}
}
