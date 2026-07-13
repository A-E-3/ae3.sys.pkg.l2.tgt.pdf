package ru.myx.ae3.l2.pdf;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;

class PdfLayoutLink extends PdfLayoutDefinition {
	@Override
	public BaseObject onExecute(final PdfTargetContext context, final BaseObject layout) {
		final String url = Base.getString( layout, "href", "" ).trim();
		if (url.length() == 0) {
			return layout;
		}
		final String title = Base.getString( layout, "title", url );
		return context.setLink( url, title );
	}
}
