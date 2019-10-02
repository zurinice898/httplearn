package com.ljw.httpclient;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;

public class ClientChunkEncodedPost {
	public static void main(String[] args) throws IOException {
		if(args.length != 1){
			System.out.println("File path not given");
			System.exit(1);
		}

		CloseableHttpClient httpClient = HttpClients.createDefault();
		try{
			HttpPost httpPost = new HttpPost("http://httpbin.org/post");
			File file = new File(args[0]);
			InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(file),-1, ContentType.APPLICATION_OCTET_STREAM);
			reqEntity.setChunked(true);

			httpPost.setEntity(reqEntity);
			System.out.println("Executing request: " + httpPost.getRequestLine());
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try{
				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				System.out.println(EntityUtils.toString(response.getEntity()));
			}finally {
				response.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			httpClient.close();
		}
	}
}
