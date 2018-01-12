package dzHttpDemo.dzHttpDemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpViewTest2 {

	public static void postUrl(String url) throws IOException{
		
//		HttpHost proxy=new HttpHost("127.0.0.1", 28080);
		
//117.65.40.218:44311
//117.65.46.249:44311
//117.65.47.77:44311
//117.65.45.250:44311
//117.65.43.3:44311
//117.65.44.79:44311
//117.65.47.229:44311
//117.65.40.202:44311
//117.65.41.242:44311
//117.65.47.163:44311
//180.118.94.239:44311
		
		CloseableHttpClient httpclient = HttpClients.custom()
//	                .setProxy(proxy)
	                .build();
		
    	HttpGet httpget = new HttpGet(url);
        httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
        CloseableHttpResponse response1 = httpclient.execute(httpget);
        try {
//            HttpEntity entity = response1.getEntity();
//            System.out.println("Login form get: " + response1.getStatusLine());
//	                System.out.println("Initial set of cookies:");
//	                List<Cookie> cookies = cookieStore.getCookies();
//	                if (cookies.isEmpty()) {
//	                    System.out.println("None");
//	                } else {
//	                    for (int i = 0; i < cookies.size(); i++) {
//	                        System.out.println("- " + cookies.get(i).toString());
//	                    }
//	                }
        } finally {
            response1.close();
        }
	}
	
	public static void main(String[] args) throws IOException {
		String url="http://www.hdpfans.com/forum.php?mod=viewthread&tid=794770&fromuid=3784430";
		for(int i=0;i<10;i++){
			postUrl(url);
			System.out.println(i);
		}
	}
	
}
