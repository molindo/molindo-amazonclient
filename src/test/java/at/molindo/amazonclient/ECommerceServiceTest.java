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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.webservices.awsecommerceservice.AWSECommerceServicePortType;
import com.amazon.webservices.awsecommerceservice.Item;
import com.amazon.webservices.awsecommerceservice.ItemSearch;
import com.amazon.webservices.awsecommerceservice.ItemSearchRequest;
import com.amazon.webservices.awsecommerceservice.ItemSearchResponse;
import com.amazon.webservices.awsecommerceservice.Items;

public class ECommerceServiceTest {

	private static final String SECRET_PROPERTY = "at.molindo.amazonclient.secret";

	private static final Logger log = LoggerFactory.getLogger(ECommerceServiceTest.class);

	private static final String SECRET = System.getProperty(SECRET_PROPERTY);

	@Before
	public void assertSecret() {
		Assert.assertNotNull("amazon secret missing (set system property " + SECRET_PROPERTY + ")", SECRET);
	}

	@Test
	public void getBuyInfo() throws Exception {
		AWSECommerceServicePortType port = newAWSECommerceServicePort(AmazonProductAdvertisingLocale.DE, true);

		final ItemSearchResponse itemSearchResponse = port.itemSearch(newItemSearch("The Beatles"));

		if (itemSearchResponse.getItems().size() == 0) {
			fail("no items in response");
		}

		for (final Items items : itemSearchResponse.getItems()) {
			for (final Item item : items.getItem()) {
				assertNotNull(item.getItemAttributes().getTitle());

				if (item.getOfferSummary() != null
						&& item.getOfferSummary().getLowestNewPrice() != null) {
					// expect prices in EUR, we're supposed to be in DE locale
					assertEquals("EUR", item.getOfferSummary().getLowestNewPrice()
							.getCurrencyCode());
				}
			}

		}
	}

	@Test
	public void testEndpoints() {
		for (AmazonProductAdvertisingLocale l : AmazonProductAdvertisingLocale.values()) {
			for (boolean useHttps : new boolean[] { true, false }) {

				if (l.getEndpoint(useHttps) == null) {
					// skip unsupported
					continue;
				}

				AWSECommerceServicePortType port = newAWSECommerceServicePort(l, useHttps);

				try {
					assertNotNull(port.itemSearch(newItemSearch("Led Zeppelin")));
					log.info("endpoint working: " + l + " (useHttps=" + useHttps + ")");
				} catch (RuntimeException e) {
					fail("endpoint NOT working: " + l + " (useHttps=" + useHttps + ") ["
							+ e.getMessage() + "]");
					throw e;
				}
			}
		}
	}

	private AWSECommerceServicePortType newAWSECommerceServicePort(AmazonProductAdvertisingLocale locale, boolean useHttps) {
		return locale.getPort(AmazonClient.newService(SECRET), useHttps);
	}

	private ItemSearch newItemSearch(final String artist) {
		final ItemSearchRequest itemSearchRequest = new ItemSearchRequest();
		itemSearchRequest.setArtist(artist);
		itemSearchRequest.setSearchIndex("Music");
		itemSearchRequest.getResponseGroup().add("Small,Images,Tracks,Offers");

		final ItemSearch itemSearch = new ItemSearch();
		itemSearch.getRequest().add(itemSearchRequest);
		itemSearch.setAWSAccessKeyId("1Y27MNKB3WCJ6MBG4QG2");
		itemSearch.setAssociateTag("poptippcom-21");
		return itemSearch;
	}

}
