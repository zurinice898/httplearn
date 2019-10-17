package com.ljw.httpclient;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.SetCookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class HttpClient_第三章 {

	{
		Cookie cookie;
		SetCookie setCookie;
		ClientCookie clientCookie;
		CookieSpec cookieSpec;
		CookieStore cookieStore;
	}

	public void cookie1(){
		BasicClientCookie cookie = new BasicClientCookie("name","value");
		cookie.setDomain(".mycompany.com");
		cookie.setPath("/");
		cookie.setAttribute(ClientCookie.PATH_ATTR,"/");
		cookie.setAttribute(ClientCookie.DOMAIN_ATTR, ".mycompany.com");
	}

	public void cookiepolicy(){
		RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.DEFAULT).build();
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
		RequestConfig localConfig = RequestConfig.copy(globalConfig).setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
		HttpGet httpGet = new HttpGet("/");
		httpGet.setConfig(localConfig);

	}

	public void cookieStre1(){
		CookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie cookie = new BasicClientCookie("name","value");
		cookie.setDomain(".mycompany.com");
		cookie.setPath("/");
		cookieStore.addCookie(cookie);
		CloseableHttpClient httpClient =HttpClients.custom().setDefaultCookieStore(cookieStore).build();
	}




}
