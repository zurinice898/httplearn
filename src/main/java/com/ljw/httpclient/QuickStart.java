package com.ljw.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.testserver.HttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class QuickStart {

	{
		URIBuilder URIBuilder;
		BasicHttpResponse basicHttpResponse;
	}

	public static void main(String[] args) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet("http://httpbin.org/get");
			CloseableHttpResponse response1 = httpClient.execute(httpGet);
			try{
				System.out.println(response1.getStatusLine());
				HttpEntity entity1 = response1.getEntity();
				EntityUtils.consume(entity1);
			}finally {
				response1.close();
			}

			HttpPost httpPost = new HttpPost("http://httpbin.org/post");
			List<NameValuePair> nvps = new ArrayList<>();
			nvps.add(new BasicNameValuePair("username","vip"));
			nvps.add(new BasicNameValuePair("password","secret"));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			CloseableHttpResponse response2 = httpClient.execute(httpPost);
			try{
				System.out.println(response2.getStatusLine());
				HttpEntity entity2 = response2.getEntity();
				EntityUtils.consume(entity2);
			}finally {
				response2.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			httpClient.close();
		}

	}
}
