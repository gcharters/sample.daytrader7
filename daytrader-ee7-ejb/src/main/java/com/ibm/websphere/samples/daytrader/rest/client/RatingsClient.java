package com.ibm.websphere.samples.daytrader.rest.client;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class RatingsClient {

	private final String ratingsURL;
	
	private RatingsClient(Builder builder) {
		this.ratingsURL = builder.ratingsURL;
	}

	// Method that creates the client builder
	protected Invocation.Builder buildClientBuilder(String urlString) {
		try {
			Client client = ClientBuilder.newClient();
			Invocation.Builder builder = client.target(urlString).request();
			return builder.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
		} catch (Exception e) {
			System.out.println("ClientBuilderException");
			return null;
		}
	}

	// Helper method that processes the request
	protected Rating getRatingHelper(Invocation.Builder builder) {
		try {
			Response response = builder.get();
			if (response.getStatus() == Status.OK.getStatusCode()) {
				return response.readEntity(Rating.class);
			} else {
				System.out.println("Response Status is not OK.");
			}
		} catch (RuntimeException e) {
			System.out.println("Runtime exception: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception thrown while invoking the request.");
		}
		return null;
	}
	
	public Rating getRating(String symbol) {
		Invocation.Builder builder = buildClientBuilder(ratingsURL + "/" + symbol);
		return getRatingHelper(builder);
	}



	public static class Builder {

		private String ratingsURL = "http://localhost:9080/ratingsservice/rating/";

		public Builder url(String url) {
			this.ratingsURL = url;
			return this;
		}

		public RatingsClient build() {
			return new RatingsClient(this);
		}

	}
}
