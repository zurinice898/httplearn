package com.ljw.httpclient;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ClientAuthentication {

	public static void main(String[] args) throws IOException {
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope("httpbin.org",80),new UsernamePasswordCredentials("user","passwd"));
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		try{
			HttpGet httpGet = new HttpGet("http://httpbin.org/basic-auth/user/passwd");
			System.out.println("Executing request " + httpGet.getRequestLine());
				CloseableHttpResponse response = httpClient.execute(httpGet);
			try {
				System.out.println("---------------------");
				System.out.println(response.getStatusLine());
				System.out.println(EntityUtils.toString(response.getEntity()));
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				response.close();
			}
		}finally {
			httpClient.close();
		}
	}
}
