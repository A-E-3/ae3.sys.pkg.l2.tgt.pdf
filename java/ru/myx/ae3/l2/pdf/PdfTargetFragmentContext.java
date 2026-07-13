package ru.myx.ae3.l2.pdf;

import java.io.IOException;

import ru.myx.ae3.serve.ServeRequest;

/**
 *
 * @author myx
 *
 */
public class PdfTargetFragmentContext extends PdfTargetContext {
	
	/**
	 * @param document
	 * @param query
	 * @param pass1
	 * @param pass2
	 * @throws IOException
	 */
	public PdfTargetFragmentContext(final PdfElementDocument document, final ServeRequest query, final String pass1, final String pass2) throws IOException {
		super(null);
		this.doStart(document);
	}

	@Override
	protected void defaultDocumentDestroy(final PdfElementDocument document) {
		
		// ignore
	}
}
