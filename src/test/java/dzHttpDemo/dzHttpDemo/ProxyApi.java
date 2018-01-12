package dzHttpDemo.dzHttpDemo;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.helper.StringUtil;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.google.gson.Gson;

public class ProxyApi {
	
	private static Log logger = LogFactory.getLog(ProxyApi.class);
	
	public static String getProxyIp(){
		BasicCookieStore cookieStore = new BasicCookieStore();
//      HttpHost proxy=new HttpHost("127.0.0.1", 28080);
      
      RequestConfig defaultRequestConfig = RequestConfig.custom()
              .setCookieSpec(CookieSpecs.DEFAULT)
              .setExpectContinueEnabled(true)
              .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
              .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
              .build();
      
      CloseableHttpClient httpclient = HttpClients.custom()
              .setDefaultCookieStore(cookieStore)
              .setDefaultRequestConfig(defaultRequestConfig)
//              .setProxy(proxy)
              .build();
      
      HttpClientContext localContext = HttpClientContext.create();
      localContext.setCookieStore(cookieStore);
      
      String ip=null;
  	
  	try {
			//===============1
  		
  		
			HttpGet httpget = new HttpGet("http://pvt.daxiangdaili.com/ip/?tid=556003049493198&num=1&ports=53281,8080&delay=1&filter=on");
			httpget.setConfig(defaultRequestConfig);
			httpget.setHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
			CloseableHttpResponse response1 = httpclient.execute(httpget, localContext);
			try {
				HttpEntity entity = response1.getEntity();
              String uString=EntityUtils.toString(entity,"utf-8");
              System.out.println("proxy返回ip："+uString);
              logger.info("proxy返回ip："+uString);
              ip=uString;
              
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					Thread.sleep(5000);
					getProxyIp();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} finally {
				try {
					response1.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			
//			ip="115.213.226.52:38261";
			if(ip!=null){
				String[] ips=ip.split(":");
				HttpHost proxy=new HttpHost(ips[0], Integer.parseInt(ips[1]));
				 CloseableHttpClient httpclient1 = HttpClients.custom()
			                .setDefaultCookieStore(cookieStore)
			                .setDefaultRequestConfig(defaultRequestConfig)
			                .setConnectionTimeToLive(2000, TimeUnit.MILLISECONDS)
			                .setProxy(proxy)
			                .build();
				 
//				HttpGet httpget1 = new HttpGet("https://api.waimai.baidu.com/");
				HttpGet httpget1 = new HttpGet("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json");
				httpget1.setConfig(defaultRequestConfig);
				httpget1.setHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
				CloseableHttpResponse response2 = httpclient1.execute(httpget1, localContext);
				
				try {
					HttpEntity entity = response2.getEntity();
	                String uString=EntityUtils.toString(entity,"utf-8");
	                System.out.println("proxy check返回："+uString);
	                logger.info("proxy check返回："+uString);
	                if(!StringUtil.isBlank(uString)){
	                	Gson gson=new Gson();
	                	
	                	Map map=null;
	                	
	                	try {
							map = gson.fromJson(uString, Map.class);
						} catch (Exception e) {
							try {
	            				Thread.sleep(5000);
	            				getProxyIp();
	            			} catch (InterruptedException e1) {
	            				// TODO Auto-generated catch block
	            				e1.printStackTrace();
	            			}
							
						}
	                	
						if(map!=null){
	                		return ip;
	                	}else{
	                		try {
	            				Thread.sleep(5000);
	            				getProxyIp();
	            			} catch (InterruptedException e1) {
	            				// TODO Auto-generated catch block
	            				e1.printStackTrace();
	            			}
	                	}
	                	
	                }
	                
	                
				} finally {
					response2.close();
				} 
				
			}
  		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				Thread.sleep(5000);
				getProxyIp();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}finally {
          try {
			httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
  		
  		return ip;
  		
	}
	
	
	public static void main(String[] args) throws Exception {
//		System.out.println("------start-----");
//		String ip=getProxyIp();
//		String ip="106.81.230.213:8118";
//		System.out.println(ip);
		
		   System.setProperty ( "webdriver.gecko.driver" , "E:/software/geckodriver-v0.19.1-win64/geckodriver.exe" );
		   
//		   String[] ips=ip.split(":");
//		   String proxyIp = ips[0];  
//		   int proxyPort = Integer.parseInt(ips[1]);  
//		   proxy.setHttpProxy(proxyIp+":"+proxyPort);
		   
//		   Proxy proxy=new Proxy(); 
//		   proxy.setHttpProxy(ip);
		   FirefoxOptions options = new FirefoxOptions();  
//		   options.setProxy(proxy);
		   // 使用代理  
		   WebDriver webDriver = new FirefoxDriver(options);
		   
		   String u="http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json";
		   webDriver.get(u);
		   
		   
		
		System.out.println("------start-----");
	}
	
	
}
