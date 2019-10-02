package com.ljw.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;

public class ClientConnectionRelease {
	public static void main(String[] args) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try{
			HttpGet httpGet = new HttpGet("http://httpbin.org/get");

			System.out.println("Executing request " + httpGet.getRequestLine());
			CloseableHttpResponse response = httpClient.execute(httpGet);
			try{
				System.out.println("----------------------------------");
				System.out.println(response.getStatusLine());
				HttpEntity entity = response.getEntity();
				if(entity != null){
					InputStream  inStream = entity.getContent();
					try{
						inStream.read();
					}catch (IOException ex){
						throw ex;
					}finally {
						inStream.close();
					}
				}
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
