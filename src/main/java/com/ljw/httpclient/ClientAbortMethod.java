package com.ljw.httpclient;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class ClientAbortMethod {
	public static void main(String[] args) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try{
			HttpGet httpGet = new HttpGet("http://httpbin.org/get");
			System.out.println("Executing request " + httpGet.getRequestLine());
			try {
				CloseableHttpResponse response = httpClient.execute(httpGet);
				try{
					System.out.println("-----------------");
					System.out.println(response.getStatusLine());
					httpGet.abort();
				}finally {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}finally {
			httpClient.close();
		}
	}
}
