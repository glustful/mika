package com.yxst.epic.yixin.rest;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

public class EasyHttpClient extends DefaultHttpClient {

	@Override
	protected ClientConnectionManager createClientConnectionManager() {
		// return super.createClientConnectionManager();
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(),
				443));
//		schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(),
//				8443));
		ClientConnectionManager connManager = new ThreadSafeClientConnManager(
				getParams(), schemeRegistry);
		return connManager;
	}

//	SchemeRegistry schemeRegistry = new SchemeRegistry();  
//	schemeRegistry.register(new Scheme("https",  
//	                    new EasySSLSocketFactory(), 443));  
//	schemeRegistry.register(new Scheme("https",  
//	                    new EasySSLSocketFactory(), 8443));  
//	ClientConnectionManager connManager = new ThreadSafeClientConnManager(params, schemeRegistry);  
//	HttpClient httpClient = new DefaultHttpClient(connManager, params);
}
