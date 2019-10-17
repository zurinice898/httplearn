package com.ljw.httpclient;

import jdk.net.SocketFlow;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLException;
import java.io.*;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpClient_第一章 {

	{
		HttpContext httpContext;
		HttpConnection httpConnection;
		HttpHost httpHost;
		HttpRoute httpRoute;
		RequestConfig requestConfig;
	}
	public static void requestInterceper() throws IOException {
		CloseableHttpClient httpClient = HttpClients.custom().addInterceptorLast(
				new HttpRequestInterceptor() {
					@Override
					public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
						AtomicInteger count = (AtomicInteger) context.getAttribute("count");
						request.addHeader("Count",Integer.toString(count.getAndIncrement()));

					}
				}
		).build();

		AtomicInteger count = new AtomicInteger(1);
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setAttribute("count", count);
		HttpGet httpGet = new HttpGet("http://localhost/");
		for(int i = 0; i < 10; i++){
			CloseableHttpResponse response = httpClient.execute(httpGet,localContext);
			try{
				HttpEntity entity = response.getEntity();
			}finally{
				response.close();
			}
		}

	}

	public static void retryHandler1(){
		HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {

				if(executionCount >= 5){
					return false;
				}

				if(exception instanceof InterruptedIOException){
					return false;
				}

				if(exception instanceof UnknownHostException){
					return false;
				}

				if(exception instanceof ConnectTimeoutException){
					return false;
				}

				if(exception instanceof SSLException){
					return false;
				}

				HttpClientContext clientContext = HttpClientContext.adapt(context);
				HttpRequest request = clientContext.getRequest();
				boolean idempotent = !(request instanceof  HttpEntityEnclosingRequest);
				if(idempotent){
					return true;
				}
				return false;
			}
		};

		CloseableHttpClient httpClient = HttpClients.custom().setRetryHandler(requestRetryHandler).build();
	}
}
