package com.googlecode.openbox.http.page;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.openbox.common.IOUtils;
import com.googlecode.openbox.common.UtilsAPI;
import com.googlecode.openbox.http.HttpClientException;
import com.googlecode.openbox.http.httpbuilder.HttpBuilder;
import com.googlecode.openbox.http.httpbuilder.HttpBuilder.Response;

public final class LinkClicker {
	private static final Logger logger = LogManager.getLogger();

	private LinkClicker parent;
	private CloseableHttpClient httpClient;
	private String link;
	private Response response;
	private boolean clicked;

	private LinkClicker(CloseableHttpClient httpClient, String link) {
		this.parent = null;
		this.httpClient = httpClient;
		this.link = link;
		this.clicked = false;
	}

	private LinkClicker(LinkClicker parent, String link) {
		this.parent = parent;
		this.httpClient = parent.getHttpClient();
		this.link = link;
		this.clicked = false;
	}

	public static LinkClicker newInstance(CloseableHttpClient httpClient,
			String link) {
		return new LinkClicker(httpClient, link);
	}

	public static LinkClicker newInstance(LinkClicker parent, String link) {
		return new LinkClicker(parent, link);
	}

	public LinkClicker click() {

		try {
			clicked = true;
			response = HttpBuilder
					.create(getHttpClient())
					.setUrl(link)
					.setMethod(HttpGet.METHOD_NAME)
					.setRequestConfig(
							RequestConfig
									.custom()
									.setCookieSpec(
											CookieSpecs.BROWSER_COMPATIBILITY)
									.build()).execute();
			return this;
		} catch (Exception e) {
			throw HttpClientException.create("click page link [" + link
					+ "] error !!!", e);
		}
	}

	public String download(String localFolder) {
		HttpGet getRequest = new HttpGet(link);
		CloseableHttpResponse response = null;
		InputStream downloadStream = null;
		try {
			getRequest.setConfig(RequestConfig.custom()
					.setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY).build());
			response = HttpClients.createDefault().execute(getRequest);
			downloadStream = response.getEntity().getContent();
			String location = localFolder + IOUtils.PATH_SPLIT
					+ UtilsAPI.getLastPath(link);
			IOUtils.createFile(location, downloadStream);
			return location;
		} catch (Exception e) {
			String msg = "download webex meeting client error !";
			logger.error(msg, e);
			throw HttpClientException.create(msg, e);
		} finally {
			if (null != getRequest) {
				getRequest.releaseConnection();
			}
			if (null != response) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != downloadStream) {
				try {
					downloadStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public LinkClicker getNextLinkClicker(LinkClickerParser parser) {
		return parser.parserNext(this);
	}

	public void checkIfClicked() {
		if (!isClicked()) {
			throw HttpClientException
					.create("The PageUrlClicker hasn't be clicked method yet , please call PageUrlClicker.click() !!!");
		}
	}

	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	public LinkClicker getParent() {
		return parent;
	}

	public LinkClicker getRootParent() {
		LinkClicker root = this;
		while (root.getParent() != null) {
			root = root.getParent();
		}
		return root;
	}

	public void setParent(LinkClicker parent) {
		this.parent = parent;
	}

	public int getStatusCode() {
		checkIfClicked();
		return response.getStatusLine().getStatusCode();
	}

	public String getResponseContent() {
		checkIfClicked();
		return response.getContent();
	}

	public String getLink() {
		return link;
	}

	public boolean isClicked() {
		return clicked;
	}

	public boolean isGotoRetireTipPage(String tipMsg) {
		checkIfClicked();
		return response.getContent().indexOf(tipMsg) != -1;
	}

}
