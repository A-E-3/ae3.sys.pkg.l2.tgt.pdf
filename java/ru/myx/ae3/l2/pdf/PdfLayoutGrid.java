package ru.myx.ae3.l2.pdf;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseObject;

class PdfLayoutGrid extends PdfLayoutDefinition {
	@Override
	public BaseObject onExecute(final PdfTargetContext context, final BaseObject layout) {
		final long width = Base.getLong( layout, "width", 0 );
		if (width <= 0 || width > 256) {
			return null;
		}
		final BaseArray array = layout.baseGet( "elements", BaseObject.UNDEFINED ).baseArray();
		if (array == null) {
			return null;
		}
		final PdfElementGrid block = new PdfElementGrid( (int) width,
				Base.getBoolean( layout, "border", false ),
				context.current );
		return context.setSequence( block, array );
	}
}
