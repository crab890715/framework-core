package com.beacon.wechat.app.comm.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;


public class HttpUtils {
	private static Logger log = Logger.getLogger(HttpUtils.class);  
    
    
 
    public static String post(String url, Map<String, String> params, String charset){  
    	CloseableHttpClient httpclient = HttpClients.createDefault();
    	CloseableHttpResponse response = null;
    	try {
    		URIBuilder builder = new URIBuilder(url);
    		for(String key : params.keySet()){
    			builder.addParameter(key, params.get(key));
    		}
			URI uri = builder.build();
			HttpGet httpget = new HttpGet(uri);
			response = httpclient.execute(httpget); 
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				HttpEntity entity = response.getEntity();
				return IOUtils.toString(entity.getContent(), charset);
			}else{
				log.error("响应状态码 = " + response.getStatusLine().getStatusCode()); 
			}
		} catch (URISyntaxException e) {
			log.error(e);
		} catch (ClientProtocolException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}finally {
			if(response!=null){
				try {
					response.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
        return null;  
    }  
       
    public static String get(String url, Map<String, String> params, String charset){  
    	CloseableHttpClient httpclient = HttpClients.createDefault();
    	CloseableHttpResponse response = null;
    	try {
    		URIBuilder builder = new URIBuilder(url);
    		for(String key : params.keySet()){
    			builder.addParameter(key, params.get(key));
    		}
			URI uri = builder.build();
			HttpPost httppost = new HttpPost(uri);
			response = httpclient.execute(httppost); 
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				HttpEntity entity = response.getEntity();
				return IOUtils.toString(entity.getContent(), charset);
			}else{
				log.error("响应状态码 = " + response.getStatusLine().getStatusCode()); 
			}
		} catch (URISyntaxException e) {
			log.error(e);
		} catch (ClientProtocolException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}finally {
			if(response!=null){
				try {
					response.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
    	return null;  
    }     
}
