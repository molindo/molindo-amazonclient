/**
 * Copyright 2010 Molindo GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.molindo.amazonclient;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import com.amazon.webservices.awsecommerceservice.AWSECommerceService;
import com.amazon.webservices.awsecommerceservice.AWSECommerceServicePortType;

public class AmazonClient {
	private AmazonClient() {
	}

	public static AWSECommerceService newService(String awsSecret) {
		if (awsSecret == null) {
			throw new IllegalStateException("awsSecretKey not configured");
		}

		final AWSECommerceService svc = new AWSECommerceService();
		svc.setHandlerResolver(new AWSHandlerResolver(awsSecret));

		return svc;
	}

	public static void setEndpoint(AWSECommerceServicePortType port, AmazonProductAdvertisingLocale locale, boolean useHttps) {
		// set endpoint depending on locale
		final String endpoint = useHttps ? locale.getSecureEndpoint() : locale.getEndpoint();

		if (endpoint == null) {
			throw new IllegalArgumentException("can't create endpoit for locale " + locale
					+ " (useHttps=" + useHttps + ")");
		}

		final BindingProvider bp = (BindingProvider) port;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
	}

	public static void enableCompression(AWSECommerceServicePortType port) {
		Map<String, Object> ctxt = ((BindingProvider) port).getRequestContext();

		Map<String, List<String>> httpHeaders = new HashMap<String, List<String>>();

		@SuppressWarnings("unchecked")
		Map<String, List<String>> currentHeaders = (Map<String, List<String>>) ctxt
				.get(MessageContext.HTTP_REQUEST_HEADERS);
		if (currentHeaders != null) {
			httpHeaders.putAll(currentHeaders);
		}

		httpHeaders.put("Accept-Encoding", Collections.singletonList("gzip"));

		ctxt.put(MessageContext.HTTP_REQUEST_HEADERS, httpHeaders);
	}
}
