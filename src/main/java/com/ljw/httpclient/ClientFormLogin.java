package com.ljw.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ClientFormLogin {
	public static void main(String[] args) {
		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

		try{
			HttpGet httpGet = new HttpGet("https://someportal/");
			CloseableHttpResponse response1= httpClient.execute(httpGet);
			HttpEntity entity = response1.getEntity();
			System.out.println("Login form get: " + response1.getStatusLine());
			EntityUtils.consume(entity);

			System.out.println("Initial set of cookies:");
			List<Cookie> cookies = cookieStore.getCookies();
			if (cookies.isEmpty()) {
				System.out.println("None");
			} else {
				for (int i = 0; i < cookies.size(); i++) {
					System.out.println("- " + cookies.get(i).toString());
				}
			}
			response1.close();


			HttpUriRequest login = RequestBuilder.post().setUri(new URI("https://someportal/")).addParameter("IDToken1","username").addParameter("IDtokent2","password").build();
			CloseableHttpResponse response2 = httpClient.execute(login);
			try{
				HttpEntity entity2 = response2.getEntity();

				System.out.println("Login form get: " + response2.getStatusLine());
				EntityUtils.consume(entity);

				System.out.println("Post logon cookies:");
				List<Cookie> cookies2 = cookieStore.getCookies();
				if (cookies2.isEmpty()) {
					System.out.println("None");
				} else {
					for (int i = 0; i < cookies2.size(); i++) {
						System.out.println("- " + cookies2.get(i).toString());
					}
				}
			}finally {
				response2.close();
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} finally {

		}


	}
}
