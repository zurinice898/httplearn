package com.ljw.httpclient;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.ProxyClient;
import org.apache.http.protocol.HTTP;

import java.io.*;
import java.net.Socket;

/**
 * 通过代理和目标服务器的http隧道，来向服务器传任何数据。
 */
public class ProxyTunnelDemo {
	public final static void main(String[] args) throws Exception {

		ProxyClient proxyClient = new ProxyClient();
		HttpHost target = new HttpHost("www.yahoo.com", 80);
		HttpHost proxy = new HttpHost("localhost", 8888);
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("user", "pwd");
		Socket socket = proxyClient.tunnel(proxy, target, credentials);
		try {
			Writer out = new OutputStreamWriter(socket.getOutputStream(), HTTP.DEF_CONTENT_CHARSET);
			out.write("GET / HTTP/1.1\r\n");
			out.write("Host: " + target.toHostString() + "\r\n");
			out.write("Agent: whatever\r\n");
			out.write("Connection: close\r\n");
			out.write("\r\n");
			out.flush();
			BufferedReader in = new BufferedReader(
					new InputStreamReader(socket.getInputStream(), HTTP.DEF_CONTENT_CHARSET));
			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
		} finally {
			socket.close();
		}
	}
}
