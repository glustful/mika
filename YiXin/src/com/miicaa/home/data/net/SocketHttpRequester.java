package com.miicaa.home.data.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.CookieStore;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.miicaa.common.base.FormFile;
import com.miicaa.home.data.old.UserAccount;
import com.miicaa.home.data.storage.LocalPath;


public class SocketHttpRequester {
	/**
	 
	 * 
	 * @param path
	 *            上传路径
	 * @param params
	 *            请求参数 key为参数名,value为参数值
	 * @param file
	 *            上传文件
	 */
	
	static String TAG = "SocketHttpRequester";
	
	public  String post(String path, Map<String, String> params,
			FormFile[] files,OnProgressListener listen,OnCompleteListener complete) {
		BufferedReader reader = null;
		OutputStream outStream = null;
		Socket socket = null;
		try{
		path = UserAccount.mSeverHost + path;
		
		String tmp = "***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************";
		final String BOUNDARY = "---------------------------7da2137580612"; // 数据分隔线
		final String endline = "--" + BOUNDARY + "--\r\n";// 数据结束标志
	
		int fileDataLength = 0;
		double localFileDataLength = 0;
		// 得到文件类型数据的总长度
		for(FormFile uploadFile :files){
			StringBuilder fileExplain = new StringBuilder();
			fileExplain.append("--");
			fileExplain.append(BOUNDARY);
			fileExplain.append("\r\n");
			fileExplain.append("Content-Disposition: form-data;name=\""
					+ uploadFile.getParameterName() + "\";filename=\""
					+ uploadFile.getFilname() + "\"\r\n");
			fileExplain.append("Content-Type: " + uploadFile.getContentType()
					+ "\r\n\r\n");
			
			fileDataLength += fileExplain.length();
			if (uploadFile.getInStream() != null) {
				fileDataLength += uploadFile.getFile().length();
				localFileDataLength += uploadFile.getFile().length();
			}
			fileDataLength += "\r\n".length();
			
			
		}
		StringBuilder textEntity = new StringBuilder();
		textEntity.append("--");
		textEntity.append(BOUNDARY);
		textEntity.append("\r\n");
		textEntity.append("Content-Disposition: form-data; name=\""
				+ "tmp1" + "\"\r\n\r\n");
		textEntity.append(tmp);
		textEntity.append("\r\n");
		for (Map.Entry<String, String> entry : params.entrySet()) {// 构造文本类型参数的实体数据
			textEntity.append("--");
			textEntity.append(BOUNDARY);
			textEntity.append("\r\n");
			textEntity.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"\r\n\r\n");
			textEntity.append(entry.getValue());
			textEntity.append("\r\n");
		}
		textEntity.append("--");
		textEntity.append(BOUNDARY);
		textEntity.append("\r\n");
		textEntity.append("Content-Disposition: form-data; name=\""
				+ "tmp2" + "\"\r\n\r\n");
		textEntity.append(tmp);
		textEntity.append("\r\n");
		// 计算传输给服务器的实体数据总长度
		int dataLength = textEntity.toString().getBytes().length
				+ fileDataLength + endline.getBytes().length;

		URL url = new URL(path);
		
		int port = url.getProtocol().equals("http")?80:443;
		socket = createSocket(url.getProtocol());
		if(socket == null){
			complete.onFailed();
			return null;
		}
		socket.connect(new InetSocketAddress(InetAddress.getByName(url.getHost()), port));
		Log.d(TAG, "InetAddress.getByName(url.getHost()):"+InetAddress.getByName(url.getHost()).toString());
//		socket.connect(new InetSocketAddress(Inet4Address.getByName(UserAccount.mServerIp), port));
		socket.setSoTimeout(10000);
		outStream = socket.getOutputStream();
		
		// 下面完成HTTP请求头的发送
		String requestmethod = "POST " + url.getPath() + " HTTP/1.1\r\n";
		outStream.write(requestmethod.getBytes());
		String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
		outStream.write(accept.getBytes());
		String language = "Accept-Language: zh-CN\r\n";
		outStream.write(language.getBytes());
		String contenttype = "Content-Type: multipart/form-data; boundary="
				+ BOUNDARY + "\r\n";
		outStream.write(contenttype.getBytes());
		String contentlength = "Content-Length: " + dataLength + "\r\n";
		outStream.write(contentlength.getBytes());
		String alive = "Connection: Keep-Alive\r\n";
		outStream.write(alive.getBytes());
		String host = "Host: " + url.getHost() + ":" + port + "\r\n";
		outStream.write(host.getBytes());
		setCookie(outStream);
		// 写完HTTP请求头后根据HTTP协议再写一个回车换行
		outStream.write("\r\n".getBytes());
		// 把所有文本类型的实体数据发送出来
		outStream.write(textEntity.toString().getBytes());
		long total = 0;
		// 把所有文件类型的实体数据发送出来
		for(FormFile uploadFile :files){
			StringBuilder fileEntity = new StringBuilder();
			fileEntity.append("--");
			fileEntity.append(BOUNDARY);
			fileEntity.append("\r\n");
			fileEntity.append("Content-Disposition: form-data;name=\""
					+ uploadFile.getParameterName() + "\";filename=\"");
			outStream.write(fileEntity.toString().getBytes());
			outStream.write(uploadFile.getFilname().getBytes(Charset.forName("ISO-8859-1")));
			
			String temp = "\"\r\n" + "Content-Type: " + uploadFile.getContentType()
					+ "\r\n\r\n";
			outStream.write(temp.getBytes());
			if (uploadFile.getInStream() != null) {

				byte[] buffer = new byte[1024*200];

				int len = 0;
				
				while ((len = uploadFile.getInStream().read(buffer, 0, 1024*200)) != -1) {
					total += len;
					if(listen != null){
						listen.onProgress((int) (total/localFileDataLength*100));
					}
					outStream.write(buffer, 0, len);
					
					
				}

				uploadFile.getInStream().close();
			}
			outStream.write("\r\n".getBytes());
		}
		
		// 下面发送数据结束标志，表示数据已经结束
		outStream.write(endline.getBytes());
		outStream.flush();
		
		//socket.shutdownOutput();//文档上说明只关闭output,实测input已被关闭，获取不到，因此检测不到流结束，只能以超时来判断
		
		
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()
				, "gbk"));
		
		String line = reader.readLine();
		
			line = reader.readLine();
		while (line != null) {// 读取web服务器返回的数据，判断请求码是否为200，如果不是200，代表请求失败
			System.out.println("line="+new String(line.getBytes("gbk"),"utf8"));
			
			if(line.contains("\"succeed\":true")){
				complete.onSuccess(new String(line.getBytes("gbk"),"utf8"));
				break;
			}
			if(line.contains("\"succeed\":false")){
				complete.onFailed();
				break;
			}
			if(line.contains("</html>")){
				complete.onFailed();
				break;
			}
			line = reader.readLine();
			
		}
		//complete.onFailed();
		
		}catch(Exception e){
			complete.onFailed();
		}finally{
			
		
		
		try {
			if(outStream != null)
			outStream.close();
			if(reader != null)
			reader.close();
			if(socket != null)
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("exceptio="+e.getMessage());
		}
		
		
		}
		
		return null;
	}
	
	private static void setCookie(OutputStream outStream) throws IOException {
		CookieStore mCookie = RequstManager.getInstance().getCookie();
		if(mCookie == null){
			 File cookieFile = new File(LocalPath.intance().cacheBasePath + "co");
	            String res = "";
	            try {
	                FileInputStream fin = new FileInputStream(cookieFile);

	                int length = fin.available();

	                byte[] buffer = new byte[length];
	                fin.read(buffer);

	                res = EncodingUtils.getString(buffer, "UTF-8");
	                fin.close();
	                if (res != null && !"".equals(res.trim())) {
	                	
	                    BasicCookieStore basicCookieStore = new BasicCookieStore();
	                    JSONArray array = new JSONArray(res);
	                    for (int i = 0; i < array.length(); i++) {
	                        JSONObject o = array.getJSONObject(i);
	                        String name = o.optString("name");
	                        String value = o.optString("value");
	                        if (name != null && value != null) {
	                        	String str = "Cookie: ";
	                            BasicClientCookie basicClientCookie = new BasicClientCookie(name, value);
	                            str += name+"="+value+";";
	                            basicClientCookie.setComment(o.optString("comment"));
	                            basicClientCookie.setDomain(o.optString("domain"));
	                            basicClientCookie.setPath(o.optString("path"));
	                            basicClientCookie.setVersion(o.optInt("version"));
	                            basicClientCookie.setSecure(o.optBoolean("secure"));
	                            basicCookieStore.addCookie(basicClientCookie);
	                            str += "comment="+o.optString("comment")+";";
	            				str += "domain="+o.optString("domain")+";";
	            				str += "path="+o.optString("path")+";";
	            				str += "version="+o.optInt("version")+";";
	            				str += "secure="+o.optBoolean("secure")+"\r\n";
	            				outStream.write(str.getBytes());
	            				
	                        }
	                       
	                    }

	                    RequstManager.getInstance().setCookie(basicCookieStore);
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
		}else{
			List<Cookie> cookies = mCookie.getCookies();
			for(Cookie cok:cookies){
				String str = "Cookie: "+cok.getName()+"=" +cok.getValue()+";";
				str += "comment="+cok.getComment()+";";
				str += "domain="+cok.getDomain()+";";
				str += "path="+cok.getPath()+";";
				str += "version="+cok.getVersion()+";";
				str += "secure="+cok.isSecure()+"\r\n";
				outStream.write(str.getBytes());
				
			}
		}
		
	}
	
	private  Socket createSocket(String protocol) {
        try {
        	if(protocol.equals("http"))
        		return new Socket();
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            return sf.createSocket();

        } catch (Exception e) {
            return null;
        }
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

	public interface OnProgressListener{
		void onProgress(int progress);
	}
	
	public interface OnCompleteListener{
		void onSuccess(String json);
		void onFailed();
	}
	
}
