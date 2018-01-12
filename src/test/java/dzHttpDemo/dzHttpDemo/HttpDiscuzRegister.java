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
import java.net.URLDecoder;
import java.nio.charset.Charset;
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
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dzHttpDemo.dzHttpDemo.util.FetchMail;

public class HttpDiscuzRegister {
	
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
        
        
        HttpClientContext localContext = HttpClientContext.create();
        localContext.setCookieStore(cookieStore);
        
        String formhash=null;
        String hash=null;
        String referer=null;
        String regsubmit=null;
        String mailAccount=null;
        
    	String url=FetchMail.getMail("025K46@mail.ccyunbo.com","123456");
        
        try {
        	//===============1
        	HttpGet httpget = new HttpGet(url);
            httpget.setConfig(defaultRequestConfig);
            httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            CloseableHttpResponse response1 = httpclient.execute(httpget,localContext);
            try {
                HttpEntity entity = response1.getEntity();

                System.out.println("Login form get: " + response1.getStatusLine());
//                EntityUtils.consume(entity);
              String uString=EntityUtils.toString(entity,"utf-8");
              System.out.println("注册返回auth："+uString);
              Document document = Jsoup.parse(uString);   
			  formhash = document.getElementById("layer_reg").getElementsByAttributeValue("name", "formhash").val();
			  hash = document.getElementById("layer_reg").getElementsByAttributeValue("name", "hash").val();
			  referer = document.getElementById("layer_reg").getElementsByAttributeValue("name", "referer").val();
			  regsubmit = document.getElementById("layer_reg").getElementsByAttributeValue("name", "regsubmit").val();
//			  mailAccount = document.getElementById("hdpemail345").val();
              
			  
			  Elements elements=document.getElementById("reginfo_a").getElementsByTag("input");
			  for(Element element:elements) {
				  System.out.println("aa"+element.attr("name"));
			  }
			  
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
//            
//            List params = new ArrayList();
//            URLEncodedUtils.format(params, "UTF-8");
            
//          String host="http://localhost:18086";
            String host="http://localhost:18086";
            
//            //===============2
            HttpUriRequest login2 = RequestBuilder.post()
                    .setUri(new URI(host+"/member.php?mod=registerhdp&inajax=1"))
                    .addParameter("regsubmit", regsubmit)
                    .addParameter("formhash", formhash)
                    .addParameter("referer",referer)
                    .addParameter("activationauth","")
                    .addParameter("hash", hash)
                    .addParameter("hdpname564", mailAccount.substring(0, mailAccount.indexOf("@")))
                    .addParameter("hdppwd877", "a123456789")
                    .addParameter("hdppwd1145", "a123456789")
                    .addParameter("hdpemail345", mailAccount)
                    .addParameter("realname", "张生")
                    .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
//                    .setHeader("X-Requested-With", "XMLHttpRequest")
                    .setHeader("Referer", host)
                    .setHeader("Cache-Control", "max-age=0")
                    .setHeader("Origin", host)
                    .setHeader("pgrade-Insecure-Requests", "1")
                    .setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .setCharset(Charset.forName("UTF-8"))
                    .build();

//            CloseableHttpResponse response4 = httpclient.execute(login2);
//            try {
//                HttpEntity entity = response4.getEntity();
//                System.out.println("Login form post: " + response4.getStatusLine());
////                EntityUtils.consume(entity);
////                System.out.println(EntityUtils.toString(entity,"utf-8"));
//                String uString=EntityUtils.toString(entity,"utf-8");
//                System.out.println("登录返回auth："+uString);
//                
//                System.out.println("Post logon cookies:");
//                List<Cookie> cookies = cookieStore.getCookies();
//                if (cookies.isEmpty()) {
//                    System.out.println("None");
//                } else {
//                    for (int i = 0; i < cookies.size(); i++) {
//                        System.out.println("- " + cookies.get(i).toString());
//                    }
//                }
//            } finally {
//            	response4.close();
//            }
            
        } finally {
            httpclient.close();
        }
    }
}

