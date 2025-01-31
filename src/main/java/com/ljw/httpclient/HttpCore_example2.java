package com.ljw.httpclient;

import org.apache.http.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.pool.BasicConnFactory;
import org.apache.http.impl.pool.BasicConnPool;
import org.apache.http.impl.pool.BasicPoolEntry;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.*;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Future;

public class HttpCore_example2 {
	public static void main(String[] args) throws Exception {
		final HttpProcessor httpproc = HttpProcessorBuilder.create()
				.add(new RequestContent())
				.add(new RequestTargetHost())
				.add(new RequestConnControl())
				.add(new RequestUserAgent("Test/1.1"))
				.add(new RequestExpectContinue(true)).build();

		final HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

		final BasicConnPool pool = new BasicConnPool(new BasicConnFactory());
		pool.setDefaultMaxPerRoute(2);
		pool.setMaxTotal(2);

		HttpHost[] targets = {
				new HttpHost("www.google.com", 80),
				new HttpHost("www.yahoo.com", 80),
				new HttpHost("www.apache.com", 80)
		};

		class WorkerThread extends Thread {

			private final HttpHost target;

			WorkerThread(final HttpHost target) {
				super();
				this.target = target;
			}

			@Override
			public void run() {
				ConnectionReuseStrategy connStrategy = DefaultConnectionReuseStrategy.INSTANCE;
				try {
					Future<BasicPoolEntry> future = pool.lease(this.target, null);

					boolean reusable = false;
					BasicPoolEntry entry = future.get();
					try {
						HttpClientConnection conn = entry.getConnection();
						HttpCoreContext coreContext = HttpCoreContext.create();
						coreContext.setTargetHost(this.target);

						BasicHttpRequest request = new BasicHttpRequest("GET", "/");
						System.out.println(">> Request URI: " + request.getRequestLine().getUri());

						httpexecutor.preProcess(request, httpproc, coreContext);
						HttpResponse response = httpexecutor.execute(request, conn, coreContext);
						httpexecutor.postProcess(response, httpproc, coreContext);

						System.out.println("<< Response: " + response.getStatusLine());
						System.out.println(EntityUtils.toString(response.getEntity()));

						reusable = connStrategy.keepAlive(response, coreContext);
					} catch (IOException ex) {
						throw ex;
					} catch (HttpException ex) {
						throw ex;
					} finally {
						if (reusable) {
							System.out.println("Connection kept alive...");
						}
						pool.release(entry, reusable);
					}
				} catch (Exception ex) {
					System.out.println("Request to " + this.target + " failed: " + ex.getMessage());
				}
			}

		};

		WorkerThread[] workers = new WorkerThread[targets.length];
		for (int i = 0; i < workers.length; i++) {
			workers[i] = new WorkerThread(targets[i]);
		}
		for (int i = 0; i < workers.length; i++) {
			workers[i].start();
		}
		for (int i = 0; i < workers.length; i++) {
			workers[i].join();
		}
	}

}
