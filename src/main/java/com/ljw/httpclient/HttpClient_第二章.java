package com.ljw.httpclient;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.*;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpClient_第二章 {

	{
		RouteInfo routeInfo;
		HttpRoute httpRoute;
		HttpClientConnectionManager httpClientConnectionManager;
	}

	public static void connectionManager() throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException {
		HttpClientContext context = HttpClientContext.create();
		HttpClientConnectionManager connMrg = new BasicHttpClientConnectionManager();
		HttpRoute route = new HttpRoute(new HttpHost("localhost", 80));
		ConnectionRequest connRequest = connMrg.requestConnection(route,null);
		HttpClientConnection conn = connRequest.get(10, TimeUnit.SECONDS);
		try{
			if(!conn.isOpen()){
				connMrg.connect(conn,route,1000,context);
				connMrg.routeComplete(conn,route,context);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			connMrg.releaseConnection(conn,null,1,TimeUnit.MINUTES);
		}
	}

	public static void poolHttpClient1(){
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(200);
		cm.setDefaultMaxPerRoute(20);
		HttpHost localhost = new HttpHost("localhost",80);
		cm.setMaxPerRoute(new HttpRoute(localhost),50);

		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
	}

	public static void MultithreadRequestExe(){
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
		String[] urisToGet = {
				"http://www.domain1.com/",
				"http://www.domain2.com/",
				"http://www.domain3.com/",
				"http://www.domain4.com/"
		};

		GetThread[] threads = new GetThread[urisToGet.length];
		for(int i=0; i < threads.length; i++){
			HttpGet httpGet = new HttpGet(urisToGet[i]);
			threads[i] = new GetThread(httpClient, httpGet);
		}

		for(int j = 0; j < threads.length; j++){
			threads[j].start();
		}

		for(int j = 0; j < threads.length; j++){
			threads[j].join();
		}
	}

	static class GetThread extends Thread{
		private final CloseableHttpClient httpClient;
		private final HttpContext context;
		private final HttpGet httpget;

		public GetThread(CloseableHttpClient httpClient, HttpGet httpget) {
			this.httpClient = httpClient;
			this.context = HttpClientContext.create();
			this.httpget = httpget;
		}

		@Override
		public void run() {
			try{
				CloseableHttpResponse response = httpClient.execute(httpget,context);
				try{
					HttpEntity entity = response.getEntity();
				}finally {
					response.close();
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public static class IdleConnectionMonitorThead extends  Thread{
		private final  HttpClientConnectionManager connMgr;
		private volatile boolean shutdown;

		public IdleConnectionMonitorThead(HttpClientConnectionManager connMgr) {
			this.connMgr = connMgr;
		}

		@Override
		public void run() {
			try{
				while(!shutdown){
					synchronized (this){
						wait(5000);
						connMgr.closeExpiredConnections();

						connMgr.closeIdleConnections(30,TimeUnit.SECONDS);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public void  shutdown(){
			shutdown = true;
			synchronized (this){
				notifyAll();
			}
		}
	}

	public static void KeepAliveStrategy(){
		ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {
			@Override
			public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
				HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(
						HTTP.CONN_KEEP_ALIVE
				));
				while(it.hasNext()){
					HeaderElement he = it.nextElement();
					String param = he.getName();
					String value = he.getValue();
					if(value != null && param.equalsIgnoreCase("timeout")){
						try{
							return Long.parseLong(value) * 1000;
						}catch(NumberFormatException ignore){

						}
					}
				}

				HttpHost target = (HttpHost) context.getAttribute(HttpClientContext.HTTP_TARGET_HOST);
				if("www.naugthy-server.com".equalsIgnoreCase(target.getHostName())){
					return 5 * 1000;
				}else{
					return 30 * 1000;
				}
			}

		};

		CloseableHttpClient httpClient = HttpClients.custom().setKeepAliveStrategy(myStrategy).build();
	}


}
