package com.ljw.httpclient;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;


public class ClientCustomContext {

	public static void main(String[] args) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try{
			CookieStore cookieStore = new BasicCookieStore();
			HttpClientContext localContext = HttpClientContext.create();
			localContext.setCookieStore(cookieStore);
			HttpGet httpGet = new HttpGet("http://httpbin.org/cookies");
			System.out.println("Executing request " + httpGet.getRequestLine());
			CloseableHttpResponse response = httpClient.execute(httpGet,localContext);
			try{
				System.out.println("=============================");
				System.out.println(response.getStatusLine());
				List<Cookie> cookies = cookieStore.getCookies();
				for(int i = 0; i < cookies.size(); i++){
					System.out.println("Local cookie: " + cookies.get(i));
				}
				EntityUtils.consume(response.getEntity());
			}finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			httpClient.close();
		}
	}
}
