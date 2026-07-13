package ru.myx.ae3.l2.pdf;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;

class PdfLayoutString extends PdfLayoutDefinition {
	@Override
	public BaseObject onExecute(final PdfTargetContext context, final BaseObject layout) {
		final String string = Base.getString( layout, "value", " no data " );
		final int length = string.length();
		if (length > 0 && string.charAt( 0 ) == '\t') {
			final StringBuilder builder = new StringBuilder( length + 32 );
			int i = 0;
			for (; i < length && string.charAt( i ) == '\t'; ++i) {
				builder.append( "    " );
			}
			if (i < length) {
				builder.append( string.substring( i ) );
			}
			return context.setString( builder.toString() );
		}
		return context.setString( string );
	}
}
