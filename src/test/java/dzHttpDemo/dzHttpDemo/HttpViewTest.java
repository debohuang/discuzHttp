package dzHttpDemo.dzHttpDemo;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpViewTest {

	public static void postUrl(String url){
	        HttpClient httpClient = new HttpClient(); 
	        httpClient.getParams().setCookiePolicy( CookiePolicy.BROWSER_COMPATIBILITY); 
	        PostMethod postMethod = new PostMethod(url); 
	        
//	        NameValuePair[] data = { 
//	                new NameValuePair("username", "123"), 
//	                new NameValuePair("referer", 
//	                        "http://discuzdemo.c88.53dns.com/index.php"), 
//	                new NameValuePair("password", "123"), 
//	                new NameValuePair("loginfield", "username"), 
//	                new NameValuePair("questionid", "0"), 
//	                new NameValuePair("formhash", "fc922ca7") }; 
//	        postMethod.setRequestHeader("Referer", 
//	                "http://discuzdemo.c88.53dns.com/index.php"); 
//	        postMethod.setRequestHeader("Host", "discuzdemo.c88.53dns.com"); 
	        // postMethod.setRequestHeader("Connection", "keep-alive"); 
	        // postMethod.setRequestHeader("Cookie", "jbu_oldtopics=D123D; 
	        // jbu_fid2=1249912623; smile=1D1; jbu_onlineusernum=2; 
	        // jbu_sid=amveZM"); 
	        postMethod.setRequestHeader( 
	                        "User-Agent", 
	                        "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2"); 
	        postMethod.setRequestHeader("Accept", 
	                        "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); 
	        // postMethod.setRequestHeader("Accept-Encoding", "gzip,deflate"); 
	        // postMethod.setRequestHeader("Accept-Language", "zh-cn"); 
	        // postMethod.setRequestHeader("Accept-Charset", 
	        // "GB2312,utf-8;q=0.7,*;q=0.7"); 
//	        postMethod.setRequestBody(); 
	        try { 
	            httpClient.executeMethod(postMethod); 
	            StringBuffer response = new StringBuffer(); 
	            BufferedReader reader = new BufferedReader(new InputStreamReader( 
	                    postMethod.getResponseBodyAsStream(), "gb2312"));//��gb2312���뷽ʽ��ӡ�ӷ������˷��ص����� 
	            String line; 
	            while ((line = reader.readLine()) != null) { 
	                response.append(line).append( 
	                        System.getProperty("line.separator")); 
	            } 
	            reader.close(); 
	            Header header = postMethod.getResponseHeader("Set-Cookie"); 
	            Cookie[] cookies=httpClient.getState().getCookies();//ȡ����½�ɹ��󣬷��������ص�cookies��Ϣ�����汣���˷������˸��ġ���ʱ֤�� 
	            String tmpcookies=""; 
	            for(Cookie c:cookies){ 
	                tmpcookies=tmpcookies+c.toString()+";"; 
	                System.out.println(c); 
	            } 
//	            System.out.println(tmpcookies); 
//	            System.out.println(header.getValue()); 
//	            System.out.println(response); 
	        } catch (Exception e) { 
	            System.out.println(e.getMessage()); 
	            // TODO: handle exception 
	        } finally { 
	            postMethod.releaseConnection(); 
	        } 
		
	}
	
	
	public static void main(String[] args) {
		String url="http://bbs.rednet.cn/thread-47421199-1-1.html";
		for(int i=0;i<20;i++){
			postUrl(url);
			System.out.println(i);
		}
	}
	
}
