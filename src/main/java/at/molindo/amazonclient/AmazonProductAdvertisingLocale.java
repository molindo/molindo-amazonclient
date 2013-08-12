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

import com.amazon.webservices.awsecommerceservice.AWSECommerceService;
import com.amazon.webservices.awsecommerceservice.AWSECommerceServicePortType;

/**
 * <a href=
 * "http://docs.amazonwebservices.com/AWSECommerceService/2011-08-01/DG/SOAPEndpoints.html"
 * >SOAP Endpoints</a> seems to be outdated. Using <a href=
 * "http://docs.amazonwebservices.com/AWSECommerceService/2011-08-01/DG/AnatomyOfaRESTRequest.html"
 * >REST Endpoints</a> instead
 * 
 * @author stf
 * 
 */
public enum AmazonProductAdvertisingLocale {
	CA("https://ecs.amazonaws.ca/onca/soap") {

		@Override
		protected AWSECommerceServicePortType newPort(AWSECommerceService service) {
			return service.getAWSECommerceServicePortCA();
		}
	},
	CN("https://webservices.amazon.cn/onca/soap") {

		@Override
		protected AWSECommerceServicePortType newPort(AWSECommerceService service) {
			return service.getAWSECommerceServicePortCN();
		}
	},
	DE("https://ecs.amazonaws.de/onca/soap") {

		@Override
		protected AWSECommerceServicePortType newPort(AWSECommerceService service) {
			return service.getAWSECommerceServicePortDE();
		}
	},
	ES("https://webservices.amazon.es/onca/soap") {
		@Override
		protected AWSECommerceServicePortType newPort(AWSECommerceService service) {
			return service.getAWSECommerceServicePortES();
		}
	},
	FR("https://ecs.amazonaws.fr/onca/soap") {

		@Override
		protected AWSECommerceServicePortType newPort(AWSECommerceService service) {
			return service.getAWSECommerceServicePortFR();
		}
	},
	IT("https://webservices.amazon.it/onca/soap") {

		@Override
		protected AWSECommerceServicePortType newPort(AWSECommerceService service) {
			return service.getAWSECommerceServicePortIT();
		}
	},
	JP("https://ecs.amazonaws.jp/onca/soap") {

		@Override
		protected AWSECommerceServicePortType newPort(AWSECommerceService service) {
			return service.getAWSECommerceServicePortJP();
		}
	},
	UK("https://ecs.amazonaws.co.uk/onca/soap") {

		@Override
		protected AWSECommerceServicePortType newPort(AWSECommerceService service) {
			return service.getAWSECommerceServicePortUK();
		}
	},
	US("https://webservices.amazon.com/onca/soap") {

		@Override
		protected AWSECommerceServicePortType newPort(AWSECommerceService service) {
			return service.getAWSECommerceServicePortUS();
		}
	};

	private final String _endpoint;
	private final String _secureEndpoint;

	private AmazonProductAdvertisingLocale(String secureEndpoint) {
		this(null, secureEndpoint);
	}

	private AmazonProductAdvertisingLocale(String endpoint, String secureEndpoint) {
		_endpoint = endpoint;
		_secureEndpoint = secureEndpoint;
	}

	public String getEndpoint() {
		return _endpoint;
	}

	public String getSecureEndpoint() {
		return _secureEndpoint;
	}

	public String getEndpoint(boolean useHttps) {
		return useHttps ? getSecureEndpoint() : getEndpoint();
	}

	public AWSECommerceServicePortType getPort(AWSECommerceService service, boolean useHttps) {
		AWSECommerceServicePortType port = getPort(service);
		AmazonClient.setEndpoint(port, this, useHttps);
		return port;
	}

	public AWSECommerceServicePortType getPort(AWSECommerceService service) {
		AWSECommerceServicePortType port = newPort(service);
		AmazonClient.enableCompression(port);
		return port;
	}

	protected abstract AWSECommerceServicePortType newPort(AWSECommerceService service);
}
