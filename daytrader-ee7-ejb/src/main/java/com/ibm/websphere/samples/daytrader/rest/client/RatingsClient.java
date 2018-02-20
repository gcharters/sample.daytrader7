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

	private final String protocol;
	private final String hostname;
	private final int port;
	private final String path;

	protected String buildURL(String protocol, String host, int port, String path) {
		try {
			URI uri = new URI(protocol, null, host, port, path, null, null);
			return uri.toString();
		} catch (Exception e) {
			System.out.println("URISyntaxException");
			return null;
		}	}
	
	private RatingsClient(Builder builder) {
		this.protocol = builder.protocol;
		this.hostname = builder.hostname;
		this.port = builder.port;
		this.path = builder.path;
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
		String url = buildURL(protocol, hostname, port, path + "/" + symbol);
		Invocation.Builder builder = buildClientBuilder(url);
		return getRatingHelper(builder);
	}



	public static class Builder {

		private String protocol = "http";
		private String hostname = "localhost";
		private int port = 9080;
		private String path = "/";

		public Builder protocol(String protocol) {
			this.protocol = protocol;
			return this;
		}
		
		public Builder hostname(String hostname) {
			this.hostname = hostname;
			return this;
		}

		public Builder port(int port) {
			this.port = port;
			return this;
		}

		public Builder path(String path) {
			this.path = path;
			return this;
		}

		public RatingsClient build() {
			return new RatingsClient(this);
		}

	}
}
