package ru.myx.util.pdf;

import com.lowagie.text.PageSize;

/**
 * 
 * @author myx
 *
 */
public class PdfAPI {
	
	/**
	 * 
	 */
	public static final int	MARGIN_TOP		= 20;
	
	/**
	 * 
	 */
	public static final int	MARGIN_RIGHT	= 30;
	
	/**
	 * 
	 */
	public static final int	MARGIN_BOTTOM	= 17;
	
	/**
	 * 
	 */
	public static final int	MARGIN_LEFT		= 30;
	
	
	/**
	 * 
	 * @param title
	 * @return
	 * @throws Exception
	 */
	public static DocumentBuilder createA4(
			final String title) throws Exception {
	
		return new DocumentBuilder( title, PageSize.A4, 20, 30, 17, 30 );
	}
}
