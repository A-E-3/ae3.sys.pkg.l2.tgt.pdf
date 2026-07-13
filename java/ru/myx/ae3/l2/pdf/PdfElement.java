package ru.myx.ae3.l2.pdf;

import com.lowagie.text.Element;

interface PdfElement {
	public void addElement(final PdfTargetContext context, final Element element);
	
	public PdfElement doFinishAndGetParent(final PdfTargetContext context);
}
