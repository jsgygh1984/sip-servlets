package org.mobicents.slee.resource.xcapclient.handler;

import java.net.URI;

import org.apache.http.Header;
import org.apache.http.auth.Credentials;
import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptor;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.mobicents.xcap.client.XcapResponse;

/**
 * Handles an async put if ETag matches none request, using byte array content.
 * 
 * @author emmartins
 * 
 */
public class AsyncPutIfNoneMatchByteArrayContentHandler extends
		AbstractAsyncHandler {

	protected String mimetype;
	protected byte[] content;
	protected String eTag;

	public AsyncPutIfNoneMatchByteArrayContentHandler(
			XCAPClientResourceAdaptor ra,
			XCAPResourceAdaptorActivityHandle handle, URI uri, String eTag,
			String mimetype, byte[] content, Header[] additionalRequestHeaders,
			Credentials credentials) {
		super(ra, handle, uri, additionalRequestHeaders, credentials);
		this.mimetype = mimetype;
		this.content = content;
		this.eTag = eTag;
	}

	@Override
	protected XcapResponse doRequest() throws Exception {
		return ra.getClient().putIfNoneMatch(uri, eTag, mimetype, content,
				additionalRequestHeaders, credentials);
	}

}