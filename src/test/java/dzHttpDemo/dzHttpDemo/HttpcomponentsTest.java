package dzHttpDemo.dzHttpDemo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpcomponentsTest {
	
    private static PoolingHttpClientConnectionManager connectionManager = null;  
    private static HttpClientBuilder httpBulder = null;  
    private static RequestConfig requestConfig = null;  
    private static CookieStore cookieStore = new BasicCookieStore();  
    
    private static int MAXCONNECTION = 10;  
   
    private static int DEFAULTMAXCONNECTION = 5;  
   
    private static String IP = "cnivi.com.cn";  
    private static int PORT = 80;  
   
    static {  
    	HttpHost proxy=new HttpHost("127.0.0.1", 8888);
        //设置http的状态参数  
        requestConfig = RequestConfig.custom()  
                .setSocketTimeout(5000)  
                .setConnectTimeout(5000)  
                .setConnectionRequestTimeout(5000)
                .setProxy(proxy)
                .build();  
   
        HttpHost target = new HttpHost(IP, PORT);  
        connectionManager = new PoolingHttpClientConnectionManager();  
        connectionManager.setMaxTotal(MAXCONNECTION);  
        connectionManager.setDefaultMaxPerRoute(DEFAULTMAXCONNECTION);  
        connectionManager.setMaxPerRoute(new HttpRoute(target), 20);  
        httpBulder = HttpClients.custom();  
        httpBulder.setConnectionManager(connectionManager);
        httpBulder.setDefaultCookieStore(cookieStore); 
    }  
   
    public static CloseableHttpClient getConnection() {  
        CloseableHttpClient httpClient = httpBulder.build();  
        httpClient = httpBulder.build();  
        return httpClient;  
    }  
    
    public static HttpUriRequest getRequestMethod(Map<String, String> map, String url, String method) {  
        List<NameValuePair> params = new ArrayList<NameValuePair>();  
        Set<Map.Entry<String, String>> entrySet = map.entrySet();  
        for (Map.Entry<String, String> e : entrySet) {  
            String name = e.getKey();  
            String value = e.getValue();  
            NameValuePair pair = new BasicNameValuePair(name, value);  
            params.add(pair);  
        }  
        HttpUriRequest reqMethod = null;  
        if ("post".equals(method)) {  
            reqMethod = RequestBuilder.post().setUri(url)  
                    .addParameters(params.toArray(new BasicNameValuePair[params.size()]))  
                    .setConfig(requestConfig).build();  
        } else if ("get".equals(method)) {  
            reqMethod = RequestBuilder.get().setUri(url)  
                    .addParameters(params.toArray(new BasicNameValuePair[params.size()]))  
                    .setConfig(requestConfig).build();  
        }  
        return reqMethod;  
    }  
   
    public static void main(String args[]) throws IOException {  
        Map<String, String> map = new HashMap<String, String>();  
        map.put("account", "");  
        map.put("password", "");  
   
        HttpClient client = getConnection();  
        HttpUriRequest post = getRequestMethod(map, "http://localhost:18086/forum.php", "get"); 
        
//        BasicClientCookie cookie = new BasicClientCookie("name", "zhaoke");   
//        cookie.setVersion(0);    
//        cookie.setDomain("/pms/");   //设置范围  
//        cookie.setPath("/");   
//        cookieStore.addCookie(cookie);
        
        HttpResponse response = client.execute(post);  
        
        if (response.getStatusLine().getStatusCode() == 200) {  
            HttpEntity entity = response.getEntity();  
            String message = EntityUtils.toString(entity, "utf-8");  
//            System.out.println(message);  
            List<Cookie> cookies = cookieStore.getCookies();  
            for (int i = 0; i < cookies.size(); i++) {  
                System.out.println("Local cookie: " + cookies.get(i));  
            } 
        } else {  
            System.out.println("请求失败");  
        }  
    }  

}
