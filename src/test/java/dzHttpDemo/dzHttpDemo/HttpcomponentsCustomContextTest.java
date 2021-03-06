package dzHttpDemo.dzHttpDemo;

import java.util.Date;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.apache.http.util.EntityUtils;

public class HttpcomponentsCustomContextTest {

    public final static void main(String[] args) throws Exception {
    	
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // Create a local instance of cookie store
            CookieStore cookieStore = new BasicCookieStore();
            
            // Create local HTTP context
            HttpClientContext localContext = HttpClientContext.create();
            // Bind custom cookie store to the local context
            localContext.setCookieStore(cookieStore);
            HttpGet httpget = new HttpGet("http://localhost:18086/forum.php");
            System.out.println("Executing request " + httpget.getRequestLine());

            // Pass local context as a parameter
            CloseableHttpResponse response = httpclient.execute(httpget, localContext);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                List<Cookie> cookies = cookieStore.getCookies();
                for (int i = 0; i < cookies.size(); i++) {
                    System.out.println("Local cookie: " + cookies.get(i));
                }
//                EntityUtils.consume(response.getEntity());
                System.out.println(EntityUtils.toString(response.getEntity(),"utf-8"));
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

}