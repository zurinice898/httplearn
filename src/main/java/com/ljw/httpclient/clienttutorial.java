package com.ljw.httpclient;

import org.apache.http.*;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicHttpResponse;

public class clienttutorial {

	public static void main(String[] args) {
		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK,"ok");
		response.addHeader("Set_Cookie","c1=a;path=/;domain=location");
		response.addHeader("Set_Cookie","c2=b;path=\"/\",c3=c;domain=localhost");
		HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator("Set_Cookie"));
		while(it.hasNext()){
			HeaderElement element = it.nextElement();
			System.out.println(element.getName() + " = " + element.getValue());
			NameValuePair[] params = element.getParameters();
			for(int i = 0; i< params.length;i++){
				System.out.println(params[i].getName() + ": " + params[i].getValue());
			}

		}
	}
}
