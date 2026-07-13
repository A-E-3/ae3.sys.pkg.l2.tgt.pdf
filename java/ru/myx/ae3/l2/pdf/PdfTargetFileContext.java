package ru.myx.ae3.l2.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author myx
 *
 */
public class PdfTargetFileContext extends PdfTargetContext {
	
	private final File output;

	/**
	 * @param output
	 * @param pass1
	 * @param pass2
	 * @throws IOException
	 */
	public PdfTargetFileContext(final File output, final String pass1, final String pass2) throws IOException {
		super(null);
		assert output != null : "Output is NULL";
		this.doStart(PdfTargetContext.createA4(new FileOutputStream(output), pass1, pass2));
		this.output = output;
	}

	@Override
	public void doFinish() {
		
		super.doFinish();
		/**
		 * all done - file should be created created
		 */
		assert this.output.exists() : "Doesn't exist: " + this.output.getAbsolutePath();
	}
}
