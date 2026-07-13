package ru.myx.ae3.l2.pdf;

import java.io.IOException;

import ru.myx.ae3.answer.Reply;
import ru.myx.ae3.answer.ReplyAnswer;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferCollector;
import ru.myx.ae3.serve.ServeRequest;
import ru.myx.sapi.FileSAPI;

/**
 *
 * @author myx
 * 
 */
public class PdfTargetReplyContext extends PdfTargetContext {

	private final ServeRequest query;

	private final TransferCollector output;

	/**
	 * @param query
	 * @param pass1
	 * @param pass2
	 * @throws IOException
	 */
	public PdfTargetReplyContext(final ServeRequest query, final String pass1, final String pass2) throws IOException {
		super(null);
		this.query = query;
		this.output = Transfer.createCollector();
		assert this.output != null : "Output is NULL";
		this.doStart(PdfTargetContext.createA4(this.output.getOutputStream(), pass1, pass2));
	}

	@Override
	public void doFinish() {

		super.doFinish();
		/**
		 * all done - collector should be closed and ready
		 */
		try {
			final String name = FileSAPI.getFileName(this.query.getResourceIdentifier());
			final ReplyAnswer reply = Reply.binary(
					"L2-PDF", //
					this.query,
					/**
					 * collector is supposed to be closed
					 */
					this.output.toCloneFactory(),
					name.endsWith(".pdf")
						? name
						: name + ".pdf");
			this.query.getResponseTarget().apply(reply);
		} catch (final RuntimeException e) {
			throw e;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
}
