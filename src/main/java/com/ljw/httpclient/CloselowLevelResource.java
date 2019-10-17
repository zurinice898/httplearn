package com.ljw.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;

public class CloselowLevelResource {
	public static void main(String[] args) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://localhost/");
		CloseableHttpResponse response = httpClient.execute(httpGet);
		try{
			HttpEntity entity = response.getEntity();
			if(entity != null){
				InputStream inputStream = entity.getContent();
				try{

				}finally {
					inputStream.close();
				}
			}
		}finally {
			response.close();;
		}
	}
}
