package com.ljw.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class ClientWithResponseHandler {
	public static void main(String[] args) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try{
			HttpGet httpGet = new HttpGet("http://httpbin.org/");
			System.out.println("Executing request " + httpGet.getRequestLine());
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				@Override
				public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if(status >= 200 && status < 300){
						HttpEntity entity = response.getEntity();
						return entity != null ? entity.toString() : null;
					}else{
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}
			};
			String responsBody = httpClient.execute(httpGet, responseHandler);
			System.out.println("------------------------------------------");
			System.out.println(responsBody);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			httpClient.close();
		}
	}
}
