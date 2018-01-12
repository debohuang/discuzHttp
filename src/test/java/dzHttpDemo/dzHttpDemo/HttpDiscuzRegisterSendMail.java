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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.apache.http.util.EntityUtils;
import org.jsoup.helper.StringUtil;

import dzHttpDemo.dzHttpDemo.util.C3P0ConnentionProvider;

/**
 * A example that demonstrates how HttpClient APIs can be used to perform
 * form-based logon.
 */
public class HttpDiscuzRegisterSendMail {
	
	private static Connection connection = null;
	
	 private static BasicClientCookie setWeiSiteCookies(String name,String value){  
	        BasicClientCookie2 cookie = new BasicClientCookie2(name,value);  
	        cookie.setDomain("localhost");  
	        cookie.setPath("/"); 
	        Calendar cl = Calendar.getInstance();  
	        cl.add(Calendar.DAY_OF_MONTH, 1);
	        cookie.setExpiryDate(cl.getTime());    //设置超时时间
	        return cookie;  
	  }  
	
		public static String getMailAccount() {
			try {
				if (null == connection || connection.isClosed()) {
					connection = C3P0ConnentionProvider.getConnection();
				}
				//查询号码前一天每种类型的扣费记录
				String sql = "select * from mail_account m where m.`status`=0 and m.is_send=0 order by id  LIMIT 1";
				//调用sqlHelper()返回一个结果集  
				Map<String, Object> obj = new QueryRunner().query(
						connection, sql, new MapHandler());
				if(obj!=null && !obj.isEmpty()) {
					return obj.get("ACCOUNT").toString();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					DbUtils.close(connection);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return null;
			
		}
		
		public static void updateMailStatus(String a) {
			try {
				if (null == connection || connection.isClosed()) {
					connection = C3P0ConnentionProvider.getConnection();
				}
				//查询号码前一天每种类型的扣费记录
				String sql = "update mail_account set status=1 where account=?";
				new QueryRunner().update(connection,sql,new Object[]{a});
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					DbUtils.close(connection);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
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
        
//        String formhash=null;
//        String hash=null;
//        String referer=null;
//        String regsubmit=null;
        String mailAccount=getMailAccount();
        if(StringUtil.isBlank(mailAccount)) {
        	return;
        }
        System.out.println("mailAccount:"+mailAccount);
        
//    	String url=FetchMail.getMail("135622d@mail.ccyunbo.com","123456");
        
        
        try {
           //===============2
            HttpUriRequest login2 = RequestBuilder.post()
                    .setUri(new URI("http://www.hdpfans.com/member.php?mod=registerhdp&inajax=1"))
                    .addParameter("regsubmit", "yes")
                    .addParameter("formhash", "9ee8e308")
                    .addParameter("referer","http://www.hdpfans.com/member.php?mod=registerhdp" )
                    .addParameter("activationauth","")
                    .addParameter("hash", "")
                    .addParameter("hdpemail345",URLDecoder.decode(mailAccount, "UTF-8") )
                    .addParameter("handlekey", "sendregister")
                    .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
                    .setHeader("X-Requested-With", "XMLHttpRequest")
                    .setHeader("Referer", "http://www.hdpfans.com/member.php?mod=registerhdp")
                    .setHeader("Cache-Control", "max-age=0")
                    .setHeader("Origin", "http://www.hdpfans.com")
                    .setHeader("pgrade-Insecure-Requests", "1")
                    .setHeader("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundaryjFSr4IcIxBiQZRCE")
                    .setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .build();
            
            CloseableHttpResponse response4 = httpclient.execute(login2);
            try {
                HttpEntity entity = response4.getEntity();
                System.out.println("Login form post: " + response4.getStatusLine());
                String uString=EntityUtils.toString(entity,"utf-8");
                System.out.println("返回内容："+uString);
                
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

