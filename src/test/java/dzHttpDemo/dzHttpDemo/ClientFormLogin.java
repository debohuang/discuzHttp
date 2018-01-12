package dzHttpDemo.dzHttpDemo;

/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

import java.net.URI;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * A example that demonstrates how HttpClient APIs can be used to perform
 * form-based logon.
 */
public class ClientFormLogin {
	
	 private static BasicClientCookie setWeiSiteCookies(String name,String value){  
	        BasicClientCookie2 cookie = new BasicClientCookie2(name,value);  
	        cookie.setDomain("localhost");  
	        cookie.setPath("/"); 
	        Calendar cl = Calendar.getInstance();  
	        cl.add(Calendar.DAY_OF_MONTH, 1);
	        cookie.setExpiryDate(cl.getTime());    //设置超时时间
	        return cookie;  
	    }  
	
    public static void main(String[] args) throws Exception {
    	BasicCookieStore cookieStore = new BasicCookieStore();
        HttpHost proxy=new HttpHost("127.0.0.1", 28080);
        String guid="111872281.24241379522887984.1514443013685.699";
        int a=new Random().nextInt(10);
        guid=guid+a;
        
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.DEFAULT)
                .setExpectContinueEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();
        
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .setDefaultRequestConfig(defaultRequestConfig)
                .setProxy(proxy)
                .build();
        
//        BasicClientCookie cookie = setWeiSiteCookies("__guid",guid); 
//        BasicClientCookie cookie2 = setWeiSiteCookies("AJSTAT_ok_times","2" ); 
//        BasicClientCookie cookie3 = setWeiSiteCookies("KToo_2132_sendmail","1" ); 
//        BasicClientCookie cookie4 = setWeiSiteCookies("KToo_2132_seccode","5.11fc774468e6b6a5db" ); 
//        BasicClientCookie cookie5 = setWeiSiteCookies("monitor_count","65" ); 
//        cookieStore.addCookie(cookie);
//        cookieStore.addCookie(cookie2);
//        cookieStore.addCookie(cookie3);
//        cookieStore.addCookie(cookie4);
//        cookieStore.addCookie(cookie5);
        
        
        HttpClientContext localContext = HttpClientContext.create();
        localContext.setCookieStore(cookieStore);
        
        try {
            
        	
        	//===============1
        	HttpGet httpget = new HttpGet("http://localhost:18086/forum.php");
            httpget.setConfig(defaultRequestConfig);
            httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            CloseableHttpResponse response1 = httpclient.execute(httpget,localContext);
            try {
                HttpEntity entity = response1.getEntity();

                System.out.println("Login form get: " + response1.getStatusLine());
//                EntityUtils.consume(entity);

//                System.out.println("Initial set of cookies:");
//                List<Cookie> cookies = cookieStore.getCookies();
//                if (cookies.isEmpty()) {
//                    System.out.println("None");
//                } else {
//                    for (int i = 0; i < cookies.size(); i++) {
//                        System.out.println("- " + cookies.get(i).toString());
//                    }
//                }
            } finally {
                response1.close();
            }
          
            //===============2
            HttpUriRequest login = RequestBuilder.post()
                    .setUri(new URI("http://localhost:18086/member.php?mod=logging&action=login&loginsubmit=yes&infloat=yes&lssubmit=yes&inajax=1"))
                    .addParameter("fastloginfield", "username")
                    .addParameter("username", "test123")
                    .addParameter("password", "123456")
                    .addParameter("quickforward", "yes")
                    .addParameter("handlekey", "ls")
                    .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
                    .setHeader("Referer", "http://localhost:18086/forum.php")
                    .setHeader("Cache-Control", "max-age=0")
                    .setHeader("Origin", "http://localhost:18086")
                    .setHeader("pgrade-Insecure-Requests", "1")
                    .setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .build();
            
            CloseableHttpResponse response2 = httpclient.execute(login);
            String login_auth_url="http://localhost:18086/";
            try {
                HttpEntity entity = response2.getEntity();
                System.out.println("Login form post: " + response2.getStatusLine());
//                EntityUtils.consume(entity);
//                System.out.println(EntityUtils.toString(entity,"utf-8"));
                String uString=EntityUtils.toString(entity,"utf-8");
                System.out.println("登录返回auth："+uString);
                if(uString!=null && uString.length()>0) {
                	login_auth_url=login_auth_url+uString.substring(uString.indexOf("member.php"), uString.indexOf("forum.php")+9)
                	+"&infloat=yes&handlekey=login&inajax=1&ajaxtarget=fwin_content_login";
                }
                
                System.out.println("Post logon cookies:");
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("- " + cookies.get(i).toString());
                    }
                }
            } finally {
                response2.close();
            }
            
          //===============3
            String codeUrl="http://localhost:18086/misc.php?mod=seccode&action=update&idhash=";
            String loginhash="";
            String idhash="";
            String formhash="";
            String auth="";
                HttpGet httpget1 = new HttpGet(login_auth_url);
                httpget1.setConfig(defaultRequestConfig);
                httpget1.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
                CloseableHttpResponse response3 = httpclient.execute(httpget1,localContext);
                try {
                    HttpEntity entity = response3.getEntity();

                    System.out.println("Login form get: " + response3.getStatusLine());
                    String uString=EntityUtils.toString(entity,"utf-8");
                    System.out.println("验证码返回："+uString);
                    if(uString!=null && uString.length()>0) {
                    	codeUrl=codeUrl+uString.substring(uString.indexOf("seccode_")+8, uString.indexOf("seccode_")+8+9)
                    	+"&0.5601373082706770&modid=member::logging";
                    	idhash=uString.substring(uString.indexOf("seccode_")+8, uString.indexOf("seccode_")+8+9);
                    	loginhash=uString.substring(uString.indexOf("loginhash=")+10, uString.indexOf("loginhash")+10+5);
                    	formhash=uString.substring(uString.indexOf("formhash")+17, uString.indexOf("formhash")+25);
                    	auth=uString.substring(uString.indexOf("auth")+13, uString.indexOf("auth")+13+60);
                    
                    }
                    
                    
                } finally {
                	response3.close();
                }

            System.out.println("验证码URL:"+codeUrl);
//            if(java.awt.Desktop.isDesktopSupported()){  //在浏览器上打开验证码链接
//	            try{
//	                //创建一个URI实例,注意不是URL
//	                java.net.URI uri=java.net.URI.create(codeUrl);
//	                //获取当前系统桌面扩展
//	                java.awt.Desktop dp=java.awt.Desktop.getDesktop();
//	                //判断系统桌面是否支持要执行的功能
//	                if(dp.isSupported(java.awt.Desktop.Action.BROWSE)){
//	                    //获取系统默认浏览器打开链接
//	                    dp.browse(uri);
//	                }
//	            }catch(java.lang.NullPointerException e){
//	                //此为uri为空时抛出异常
//	            }catch(java.io.IOException e){
//	                //此为无法获取系统默认浏览器
//	            }
//	        }
            
            System.out.println("请输入KToo_2132_seccode：");
	        Scanner scannerSeccode=new Scanner(System.in);  
	        String KToo_2132_seccode=scannerSeccode.nextLine(); 
	        BasicClientCookie cookie = setWeiSiteCookies("__guid",guid); 
	        BasicClientCookie cookie2 = setWeiSiteCookies("AJSTAT_ok_times","2" ); 
	        BasicClientCookie cookie3 = setWeiSiteCookies("KToo_2132_sendmail","1" ); 
	        BasicClientCookie cookie4 = setWeiSiteCookies("KToo_2132_seccode",KToo_2132_seccode ); 
	        BasicClientCookie cookie5 = setWeiSiteCookies("monitor_count","65" ); 
	        cookieStore.addCookie(cookie);
	        cookieStore.addCookie(cookie2);
	        cookieStore.addCookie(cookie3);
	        cookieStore.addCookie(cookie4);
	        cookieStore.addCookie(cookie5);
            

	        HttpGet httpgetcode = new HttpGet(codeUrl);
	        httpgetcode.setConfig(defaultRequestConfig);
	        httpgetcode.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
	        CloseableHttpResponse responsehttpgetcode = httpclient.execute(httpgetcode,localContext);
	        try {
	            HttpEntity entity = responsehttpgetcode.getEntity();
	
	            System.out.println("Login form get: " + responsehttpgetcode.getStatusLine());
	            String uString=EntityUtils.toString(entity,"utf-8");
	            System.out.println("验证码返回："+uString);
	            
	        } finally {
	        	responsehttpgetcode.close();
	        }
	        
	        System.out.println("请输入验证码：");
	        Scanner scanner=new Scanner(System.in);  
	        String str=scanner.nextLine(); 
            
            //===============2
            String login2_url="http://localhost:18086/member.php?mod=logging&action=login&loginsubmit=yes&handlekey=login&inajax=1&loginhash="+loginhash;
            HttpUriRequest login2 = RequestBuilder.post()
                    .setUri(new URI(login2_url))
//                    .addParameter("loginhash", loginhash)
                    .addParameter("formhash", formhash)
                    .addParameter("referer", "http%3A%2F%2Flocalhost%3A18086%2F.%2Fforum.php")
                    .addParameter("auth", auth)
                    .addParameter("seccodehash", idhash)
                    .addParameter("seccodemodid", "member%3A%3Alogging")
                    .addParameter("seccodeverify", str.trim())
                    .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
//                    .setHeader("X-Requested-With", "XMLHttpRequest")
                    .setHeader("Referer", "http%3A%2F%2Flocalhost%3A18086%2F.%2Fforum.php")
                    .setHeader("Cache-Control", "max-age=0")
                    .setHeader("Origin", "http://localhost:18086")
                    .setHeader("pgrade-Insecure-Requests", "1")
                    .setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .build();
            
            CloseableHttpResponse response4 = httpclient.execute(login2);
            try {
                HttpEntity entity = response4.getEntity();
                System.out.println("Login form post: " + response4.getStatusLine());
//                EntityUtils.consume(entity);
//                System.out.println(EntityUtils.toString(entity,"utf-8"));
                String uString=EntityUtils.toString(entity,"utf-8");
                System.out.println("登录返回auth："+uString);
                
                System.out.println("Post logon cookies:");
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.isEmpty()) {
                    System.out.println("None");
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("- " + cookies.get(i).toString());
                    }
                }
            } finally {
            	response4.close();
            }
            
            
            
        } finally {
            httpclient.close();
        }
    }
}

