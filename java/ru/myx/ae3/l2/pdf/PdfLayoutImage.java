package ru.myx.ae3.l2.pdf;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.TransferCopier;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;

class PdfLayoutImage extends PdfLayoutDefinition {
	@Override
	public BaseObject onExecute(final PdfTargetContext context, final BaseObject layout) {
		/**
		 * check awt image
		 */
		{
			final Object imageObject = Base.getJava( layout, "image", null );
			if (imageObject != null) {
				if (imageObject instanceof java.awt.Image) {
					try {
						final Image image = Image.getInstance( (java.awt.Image) imageObject, null, false );
						context.setImage( image );
					} catch (final BadElementException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}
				if (imageObject instanceof Image) {
					context.setImage( (Image) imageObject );
					return null;
				}
				return null;
			}
		}
		/**
		 * check for binary data
		 */
		{
			final Object binaryObject = Base.getJava( layout, "binary", null );
			if (binaryObject != null) {
				try {
					final Image image = Image.getInstance( ((TransferCopier) binaryObject).nextDirectArray() );
					context.setImage( image );
				} catch (final BadElementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		}
		/**
		 * check for a reference
		 */
		{
			final String hrefString = Base.getString( layout, "href", "" ).trim();
			if (hrefString.length() > 0) {
				Image image = context.cacheImageGet( "href:" + hrefString );
				if (image == null) {
					try {
						final URL url = new URL( hrefString );
						image = Image.getInstance( url );
						// could clog the memory while building a document with
						// many images, need to store binaries, that could be
						// files (not in RAM)
						context.cacheImagePut( "href:" + hrefString, image );
					} catch (final BadElementException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (final IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				context.setImage( image );
				return null;
			}
		}
		context.dump( "unhandled image type - passing through" );
		return layout;
	}
}
