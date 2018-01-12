package dzHttpDemo.dzHttpDemo.apache.httpclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpClientTest {
	
    public static void main(String[] args) {
        String url = "http://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient/4.5.4";//论坛的登陆页面
        String url2="http://discuzdemo.c88.53dns.com/post.php?infloat=yes&action=newthread&fid=2&extra=&topicsubmit=yes&inajax=1";//论坛的发贴页面
        HttpClient httpClient = new HttpClient();
        //httpClient.getHostConfiguration().setProxy("222.247.62.195", 8080);
        httpClient.getParams().setCookiePolicy(
                CookiePolicy.BROWSER_COMPATIBILITY);
        PostMethod postMethod = new PostMethod(url);
        PostMethod postMethod2 = new PostMethod(url2);
//        NameValuePair[] data = {
//                new NameValuePair("username", "123"),
//                new NameValuePair("referer",
//                        "http://discuzdemo.c88.53dns.com/index.php"),
//                new NameValuePair("password", "123"),
//                new NameValuePair("loginfield", "username"),
//                new NameValuePair("questionid", "0"),
//                new NameValuePair("formhash", "fc922ca7") };
//        postMethod.setRequestHeader("Referer",
//                "http://discuzdemo.c88.53dns.com/index.php");
//        postMethod.setRequestHeader("Host", "discuzdemo.c88.53dns.com");
        // postMethod.setRequestHeader("Connection", "keep-alive");
        // postMethod.setRequestHeader("Cookie", "jbu_oldtopics=D123D;
        // jbu_fid2=1249912623; smile=1D1; jbu_onlineusernum=2;
        // jbu_sid=amveZM");
        postMethod
                .setRequestHeader(
                        "User-Agent",
                        "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2");
        postMethod
                .setRequestHeader("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        // postMethod.setRequestHeader("Accept-Encoding", "gzip,deflate");
        // postMethod.setRequestHeader("Accept-Language", "zh-cn");
        // postMethod.setRequestHeader("Accept-Charset",
        // "GB2312,utf-8;q=0.7,*;q=0.7");
//        postMethod.setRequestBody(data);
        try {
            httpClient.executeMethod(postMethod);
            StringBuffer response = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    postMethod.getResponseBodyAsStream(), "gb2312"));//以gb2312编码方式打印从服务器端返回的请求
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append(
                        System.getProperty("line.separator"));
            }
            reader.close();
            Header header = postMethod.getResponseHeader("Set-Cookie");
            Cookie[] cookies=httpClient.getState().getCookies();//取出登陆成功后，服务器返回的cookies信息，里面保存了服务器端给的“临时证”
            String tmpcookies="";
            for(Cookie c:cookies){
                tmpcookies=tmpcookies+c.toString()+";";
                System.out.println(c);
            }
            System.out.println("*************"+tmpcookies);
            System.out.println(header.getValue());
            System.out.println(response);
            NameValuePair[] data2 = {
                    new NameValuePair("subject", "测试自动发贴"),
                    new NameValuePair("message",
                            "能否发贴成功呢？测试一下就知道了"),
                    new NameValuePair("updateswfattach", "0"),
                    new NameValuePair("wysiwyg", "0"),
                    new NameValuePair("checkbox", "0"),
                    new NameValuePair("handlekey", "newthread"),
                    new NameValuePair("formhash", "885493ec") };
            postMethod2.setRequestHeader("cookie",tmpcookies);//将“临时证明”放入下一次的发贴请求操作中
            postMethod2.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "gbk");//因为发贴时候有中文，设置一下请求编码
            postMethod2.setRequestHeader("Referer",
                    "http://discuzdemo.c88.53dns.com/forumdisplay.php?fid=4");
            postMethod2.setRequestHeader("Host", "discuzdemo.c88.53dns.com");
            // postMethod.setRequestHeader("Connection", "keep-alive");
            // postMethod.setRequestHeader("Cookie", "jbu_oldtopics=D123D;
            // jbu_fid2=1249912623; smile=1D1; jbu_onlineusernum=2;
            // jbu_sid=amveZM");
            postMethod2
                    .setRequestHeader(
                            "User-Agent",
                            "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2");
            postMethod2
                    .setRequestHeader("Accept",
                            "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");//以上操作是模拟浏览器的操作，使用服务器混淆

            postMethod2.setRequestBody(data2);
            httpClient.executeMethod(postMethod2);
            StringBuffer response1 = new StringBuffer();
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(
                    postMethod2.getResponseBodyAsStream(), "gb2312"));
            String line1;
            while ((line1 = reader1.readLine()) != null) {
                response1.append(line1).append(
                        System.getProperty("line.separator"));
            }
            reader1.close();
            System.out.println(response1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            // TODO: handle exception
        } finally {
            postMethod.releaseConnection();
//            postMethod2.releaseConnection();
        }

    }

}
