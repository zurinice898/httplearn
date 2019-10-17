package com.ljw.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.entity.*;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class StringEntityTest {

	{
		StringEntity stringEntity;
		ByteArrayEntity byteArrayEntity;
		InputStreamEntity inputStreamEntity;
		FileEntity fileEntity;
	}

	public static void main(String[] args) throws IOException {
		HttpEntity myEntity = new StringEntity("important message", ContentType.create("text/plain","UTF-8"));



		System.out.println(myEntity.getContentType());
		System.out.println(myEntity.getContentLength());
		System.out.println(myEntity.getContent());
		System.out.println(EntityUtils.toString(myEntity));
		System.out.println(EntityUtils.toByteArray(myEntity).length);

		myEntity = new BufferedHttpEntity(myEntity);
		EntityUtils.consume(myEntity);

	}

}
