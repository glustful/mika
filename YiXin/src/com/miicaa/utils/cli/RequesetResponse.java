package com.miicaa.utils.cli;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class RequesetResponse {
	private static final int mRevBufferSize = 1024 * 2;
	public static CookieStore mCookieStore;
	
	String urlStr = "";
	public HashMap<String,String> paramMaps;
	DefaultHttpClient httpClient;
	CallMothed mMothed;
	Boolean stopRunning = false;
	DoFunction mFunction ;
	HttpContext mHttpContext;
	HttpResponse httpResponse;
	
	public RequesetResponse(){
		mMothed = CallMothed.ePost;
		mFunction = DoFunction.eGeneral;
		mHttpContext = new BasicHttpContext();
	}
	

	class SSLSocketFactoryEx extends SSLSocketFactory {

        SSLContext sslContext = SSLContext.getInstance("TLS");

        public SSLSocketFactoryEx(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain, String authType)
                        throws java.security.cert.CertificateException {

                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain, String authType)
                        throws java.security.cert.CertificateException {

                }
            };

            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port,
                                   boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port,
                    autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
	
	public RequesetResponse setHttpUrl(String url){
		urlStr = url;
		return this;
	}
	public RequesetResponse setParam(String key,String value){
		paramMaps.put(key, value);
		return this;
	}
	
	public RequesetResponse setMothed(CallMothed mothed){
		mMothed = mothed;
		return this;
		
	}
	
	public void planHttpGet(){
		 
		if(paramMaps != null && paramMaps.size()>0){
			 urlStr += "?";
	            for (Map.Entry<String, String> entry : paramMaps.entrySet()) {
	                String key = entry.getKey();
	                String value = entry.getValue();
	                if (value instanceof String) {
	                    urlStr += key + "=" + value;
	                    urlStr += "&";
	                }
	            }
	            urlStr = urlStr.substring(0, urlStr.length() - 1);//ȥ������&�ַ�
		}
		try{
		 HttpGet get = new HttpGet(urlStr);
         get.setHeader("x-requested-with", "XMLHttpRequest");
         httpResponse = httpClient.execute(get, mHttpContext);
         int responseCode = httpResponse.getStatusLine().getStatusCode();
         if(responseCode >= 200 && responseCode < 400){
        	 
         }
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	//post��ʽ����
	public void planHttpPost(){
		
	}
	//�����file�Ļ�
	
	//��ʼ��HttpClient
	private DefaultHttpClient getHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams httpParams = new BasicHttpParams();
            HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setUseExpectContinue(httpParams, true);

            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schReg.register(new Scheme("https", sf, 443));
            ClientConnectionManager conManager = new ThreadSafeClientConnManager(httpParams, schReg);
            HttpConnectionParams.setConnectionTimeout(httpParams, 30 * 1000);
            HttpConnectionParams.setSoTimeout(httpParams, 30 * 1000); //�˴��ĵ�λ�Ǻ���,30��
            HttpConnectionParams.setSocketBufferSize(httpParams, mRevBufferSize * 2);
            DefaultHttpClient httpClient = new DefaultHttpClient(conManager, httpParams);
            return httpClient;
        } catch (Exception e) {
            return null;
        }
    }
	
	public enum CallMothed{
		ePost,
		eGet,
		eFile;
	}
	public enum DoFunction{
		eFileUp,
		eFileDown,
		eGeneral;
	}
	//�ر�����
	private void cancel(){
		stopRunning = false;
		httpClient.getConnectionManager().shutdown();
	}
	
	

}
