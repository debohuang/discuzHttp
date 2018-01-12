package dzHttpDemo.dzHttpDemo;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

public class Test {

	public static void main(String[] args) throws IOException {
//		String uString="<input type=\"hidden\" name=\"formhash\" value=\"c6eadb3d\" />\r\n" + 
//				"<input type=\"hidden\" name=\"referer\" value=\"http://localhost:18086/./forum.php\" />\r\n" + 
//				"<input type=\"hidden\" name=\"auth\" value=\"59daEXQfYJdLGsOK1yZ/xYwuvFSP06GIZJ8ojhMio+QuTaq0JkMVSwOOKavX\" />";
//		
//		String a=uString.substring(uString.indexOf("formhash")+17, uString.indexOf("formhash")+25);
//		String b=uString.substring(uString.indexOf("auth")+13, uString.indexOf("auth")+13+60);
//		System.out.println(a);
//		System.out.println(b);
//		
		
//		 if(java.awt.Desktop.isDesktopSupported()){
//	            try{
//	                //创建一个URI实例,注意不是URL
//	                java.net.URI uri=java.net.URI.create("http://www.jb51.net");
//	                //获取当前系统桌面扩展
//	                java.awt.Desktop dp=java.awt.Desktop.getDesktop();
//	                //判断系统桌面是否支持要执行的功能
//	                if(dp.isSupported(java.awt.Desktop.Action.BROWSE)){
//	                    //获取系统默认浏览器打开链接
//	                    dp.browse(uri);
//	                    
//	                }
//	            }catch(java.lang.NullPointerException e){
//	                //此为uri为空时抛出异常
//	            }catch(java.io.IOException e){
//	                //此为无法获取系统默认浏览器
//	            }
//	        }
		
		
//		String url="http://localhost:18086/misc.php?mod=seccode&action=update&idhash=cSAgV0666&0.5601373082706770&modid=member::logging";
		
//		String url="http://www.hdpfans.com/forum.php?mod=forumdisplay&fid=46&filter=author&orderby=dateline";
//		CloseableHttpClient httpclient = HttpClients.createDefault();
//		HttpGet httpget = new HttpGet(url);
////		httpget.setHeader("Content-Type","image/png");
//        CloseableHttpResponse response = httpclient.execute(httpget);
//        try {
//            // 获取响应实体
//            HttpEntity entity = response.getEntity();
//            // 打印响应状态
//            if (entity != null) {
////            	String aaString=EntityUtils.toString(entity);
////                Document document = Jsoup.parse(aaString);   
////                String xi1=document.getElementsByClass("xi1").text();
//                
//                String aaString=EntityUtils.toString(entity);
//                Document document = Jsoup.parse(aaString);   
//                Elements elements=document.getElementsByClass("xi1");
//                
//                //formhash
////                String v=document.select("input[name=formhash]").val();
////                System.out.println(v);
//            }
//        }catch (Exception e) {
//        	e.printStackTrace();
//		} finally {
//            response.close();
//        }
			
//		String uString="{\"body\":{\"errno\":20111,\"error\":\"invalid timestamp\",\"data\":\"\"},\"cmd\":\"resp.\",\"sign\":\"239A575791A88B22EB8F32A701735F31\",\"source\":null,\"ticket\":\"87E6DFB0-379D-80EF-9A74-C8D96F224699\",\"timestamp\":1515327585,\"version\":null}";
//		Gson gson=new Gson();
//    	Map map=gson.fromJson(uString, Map.class);
//		System.out.println(map.get("body").toString());

//		String mailAccount="54644try@mail.ccyunbo.com";
//		mailAccount=mailAccount.substring(0, mailAccount.indexOf("@"));
//		System.out.println(mailAccount);
		
		
    	BasicCookieStore cookieStore = new BasicCookieStore();
//        HttpHost proxy=new HttpHost("127.0.0.1", 28080);
        
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.DEFAULT)
                .setExpectContinueEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();
        
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .setDefaultRequestConfig(defaultRequestConfig)
//                .setProxy(proxy)
                .build();
		
        HttpClientContext localContext = HttpClientContext.create();
        localContext.setCookieStore(cookieStore);
        
		String url="http://www.hdpfans.com/forum.php?mod=forumdisplay&fid=46&filter=author&orderby=dateline";
    	HttpGet httpget = new HttpGet(url);
        httpget.setConfig(defaultRequestConfig);
        httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
        CloseableHttpResponse response1 = httpclient.execute(httpget,localContext);
        try {
            HttpEntity entity = response1.getEntity();

            System.out.println("Login form get: " + response1.getStatusLine());
//            EntityUtils.consume(entity);
          String aaString=EntityUtils.toString(entity);
          Document document = Jsoup.parse(aaString);   
          Elements elements=document.getElementsByClass("xi1");
          if(elements !=null && !elements.isEmpty()) {
        	  for(Element element:elements) {
        		  String node=element.text();
        		  if(!"New".equals(node)) {
        			  int s=element.parent().parent().parent().getElementsByClass("by").get(1).getElementsByTag("em").size();
        			  if(s>0) {
        				  String u=element.parent().parent().parent().getElementsByClass("by").get(1).getElementsByTag("em").get(0).getElementsByTag("a").get(0).attr("href");
        			  }
        		  }
        	  }
          }
//            
          
//            System.out.println("Initial set of cookies:");
//            List<Cookie> cookies = cookieStore.getCookies();
//            if (cookies.isEmpty()) {
//                System.out.println("None");
//            } else {
//                for (int i = 0; i < cookies.size(); i++) {
//                    System.out.println("- " + cookies.get(i).toString());
//                }
//            }
        } finally {
            response1.close();
        }
		
		
		
	}

}
