package ru.myx.ae3.l2.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.TreeMap;

import com.lowagie.text.Anchor;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.i3.TargetInterface;
import ru.myx.ae3.l2.LayoutDefinition;
import ru.myx.ae3.l2.TargetContextAbstract;

/** @author myx */
public abstract class PdfTargetContext extends TargetContextAbstract<PdfTargetContext> {

	private final static Map<String, PdfLayoutDefinition> LAYOUTS;
	static {
		LAYOUTS = new TreeMap<>();
		PdfTargetContext.LAYOUTS.put("container", new PdfLayoutContainer());
		PdfTargetContext.LAYOUTS.put("grid", new PdfLayoutGrid());
		PdfTargetContext.LAYOUTS.put("image", new PdfLayoutImage());
		PdfTargetContext.LAYOUTS.put("link", new PdfLayoutLink());
		PdfTargetContext.LAYOUTS.put("numbered", new PdfLayoutNumbered());
		PdfTargetContext.LAYOUTS.put("sequence", new PdfLayoutSequence());
		PdfTargetContext.LAYOUTS.put("sequence-attachment-west", new PdfLayoutSequenceAttachmentWest());
		PdfTargetContext.LAYOUTS.put("string", new PdfLayoutString());
	}

	private static final String FNTPATH_SERIF = "ru/myx/ae3/l2/pdf/fonts/DejaVuSerifCondensed.ttf";

	/** @param output
	 * @param pass1
	 * @param pass2
	 * @return */
	public static PdfElementDocument createA4(final OutputStream output, final String pass1, final String pass2) {

		final float w = PageSize.A4.getWidth();
		final float h = PageSize.A4.getHeight();
		final PdfElementDocument document = new PdfElementDocument(
				new Rectangle(w, h), //
				30,
				30,
				20,
				25 // bottom
		);
		document.addCreationDate();
		try {
			// step 2:
			// we create a writer that listens to the document
			// and directs a PDF-stream to a file
			final PdfWriter writer = PdfWriter.getInstance(document, output);
			if (pass1 != null || pass2 != null) {
				writer.setEncryption(
						(pass1 == null
							? pass2
							: pass1).getBytes(),
						(pass2 == null
							? pass1
							: pass2).getBytes(),
						PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING,
						PdfWriter.STANDARD_ENCRYPTION_128);
			}
			// step 3: we open the document
			document.open();
		} catch (final DocumentException e) {
			throw new RuntimeException(e);
		}
		document.addCreator("ae3 layout engine");
		return document;
	}

	private final Font fontNORMAL;

	private final Font fontLINK;

	private final Font fontTITLE;

	private Map<String, Image> cacheImage;

	private String title = null;

	private PdfElementDocument document;

	PdfElement current = null;

	/** @param iface */
	public PdfTargetContext(final TargetInterface iface) {

		super(iface);
		{
			final BaseFont baseFont;
			try {
				baseFont = BaseFont.createFont(PdfTargetContext.FNTPATH_SERIF, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			} catch (final IOException e) {
				throw new RuntimeException(e);
			} catch (final DocumentException e) {
				throw new RuntimeException(e);
			}
			this.fontNORMAL = new Font(baseFont, Font.DEFAULTSIZE, Font.NORMAL);
			this.fontLINK = new Font(baseFont, Font.DEFAULTSIZE, Font.UNDERLINE);
			this.fontTITLE = new Font(baseFont, 3 * Font.DEFAULTSIZE, Font.NORMAL);

			this.fontLINK.setColor(0, 0, 127);
		}
	}

	Image cacheImageGet(final String key) {

		return this.cacheImage == null
			? null
			: this.cacheImage.get(key);
	}

	void cacheImagePut(final String key, final Image image) {

		if (this.cacheImage == null) {
			this.cacheImage = new TreeMap<>();
		}
		this.cacheImage.put(key, image);
	}

	/** @param document */
	@SuppressWarnings("static-method")
	protected void defaultDocumentDestroy(final PdfElementDocument document) {

		if (document.getPageNumber() == 0) {
			throw new IllegalStateException("The document has no content!");
		}
		document.close();
	}

	@Override
	public void doFinish() {

		if (this.title != null) {
			this.document.addTitle(this.title);
		}
		this.defaultDocumentDestroy(this.document);
		this.document = null;
		this.current = null;
	}

	/** Have to explicitly call it in constructor - java nature
	 *
	 * @param document */
	protected void doStart(final PdfElementDocument document) {

		this.document = document;
		this.current = this.document;
	}

	@Override
	public boolean dump(final String s) {

		return super.dump(System.identityHashCode(this.current) + " > " + s);
	}

	Font getFontNormal() {

		return this.fontNORMAL;
	}

	@Override
	protected LayoutDefinition<PdfTargetContext> getLayoutForContext(final String name) {

		return PdfTargetContext.LAYOUTS.get(name);
	}

	@Override
	public void onLeave(final PdfTargetContext target) {

		this.current = this.current.doFinishAndGetParent(this);
	}

	@Override
	public BaseObject onNest(final PdfTargetContext target, final BaseObject layout) {

		assert layout != null : "NULL java value";
		if (this.title == null) {
			{
				final String title = Base.getString(layout, "title", "").trim();
				if (title.length() > 0) {
					this.dump("got title: " + title);
					this.title = title;
				}
			}
			{
				final String subject = Base.getString(layout, "subject", "").trim();
				if (subject.length() > 0) {
					this.dump("got subject: " + subject);
					this.document.addSubject(subject);
				}
			}
			{
				final String author = Base.getString(layout, "author", "").trim();
				if (author.length() > 0) {
					this.dump("got author: " + author);
					this.document.addAuthor(author);
				}
			}
			{
				final String keywords = Base.getString(layout, "keywords", "").trim();
				if (keywords.length() > 0) {
					this.dump("got keywords: " + keywords);
					this.document.addKeywords(keywords);
				}
			}
			if (this.title != null) {
				try {
					switch (Base.getString(layout, "layout", "")) {
						case "document" :
							this.document.add(new Paragraph(this.title, this.fontTITLE));
							break;
						case "list" :
							this.document.add(new Paragraph(this.title, this.fontTITLE));
							break;
						case "view" :
							this.document.add(new Paragraph(this.title, this.fontTITLE));
							break;
						default :
							// do nothing special
					}
				} catch (final DocumentException e) {
					throw new RuntimeException(e);
				}
				// create new page as soon as we got title
				this.document.newPage();
			}
		}
		return super.onNest(target, layout);
	}

	BaseObject setImage(final Image image) {

		this.dump("setImage: " + image);
		this.current.addElement(this, image);
		return null;
	}

	BaseObject setLink(final String href, final String title) {

		this.dump("setLink: " + href + ", title:" + title);
		final Anchor anchor = new Anchor(title, this.fontLINK);
		anchor.setReference(href);
		this.current.addElement(this, new Phrase(anchor));
		return null;
	}

	BaseObject setSequence(final PdfElement block, final BaseArray array) {

		this.dump("setSequence: block=" + block + ", size=" + array.length());
		this.current = block;
		return super.enterSequence(
				block instanceof PdfContextHandler
					? (PdfContextHandler) block
					: this,
				array);
	}

	BaseObject setString(final String s) {

		this.dump("setString: " + s);
		final Phrase block = new Phrase(
				s.length() == 0
					? " "
					: s,
				this.fontNORMAL);
		this.current.addElement(this, block);
		return null;
	}
}
