package dzHttpDemo.dzHttpDemo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
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
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import dzHttpDemo.dzHttpDemo.util.C3P0ConnentionProvider;

public class DiscuzReply {
	private static Log logger = LogFactory.getLog(DiscuzReply.class);
    private static Connection connection = null;
	private static List<String> linkList=null;
    
	/**
	 * 获取当天有效账号
	 * @return
	 */
	public static String getMailAccount() {
		try {
			if (null == connection || connection.isClosed()) {
				connection = C3P0ConnentionProvider.getConnection();
			}
			//查询号码前一天每种类型的扣费记录
			String sql = "select * from mail_account m where m.`status`=3 and to_days(m.fb_time) = to_days(now()) order by id  LIMIT 1";
			
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
	
	/**
	 * 获取当天链接
	 * @return
	 * @throws IOException
	 */
	public static List<String> getLink() throws IOException {
		BasicCookieStore cookieStore = new BasicCookieStore();
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
        
        List<String> list=new ArrayList<String>();
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
        				  System.out.println("新链接："+u);
        				  list.add(u);
        			  }
        		  }
        	  }
          }
          
          
        } finally {
            response1.close();
        }
        return list;
        
	}
	
	public static void main(String[] args) throws NumberFormatException, InterruptedException, ParseException, IOException {

		  //WebDriver是一个接口，每一种浏览器都有一个实现类（多态）

		   
//		   String proxyIp = "localhost";  
//		   int proxyPort = 28080;  
//		   Proxy proxy=new Proxy(); 
//		   proxy.setHttpProxy(proxyIp+":"+proxyPort);
//		   FirefoxOptions options = new FirefoxOptions();  
//		   options.setProxy(proxy);
		   // 使用代理  
//		   WebDriver webDriver = new FirefoxDriver(options);
		
		   String account=getMailAccount();
		   if(StringUtil.isBlank(account)) {
			   return;
		   }
		   
		   linkList=getLink(); 
		   if(linkList==null && linkList.isEmpty()){
			   return;
		   }
		
//		   System.setProperty ( "webdriver.chrome.driver" , "E:/software/chromedriver_win32/chromedriver.exe" );
//		   WebDriver webDriver = new ChromeDriver();
		   System.setProperty ( "webdriver.gecko.driver" , "E:/software/geckodriver-v0.19.1-win64/geckodriver.exe" );
		   WebDriver webDriver = new FirefoxDriver();
		   
//		   WebDriver webDriver = new FirefoxDriver();
		   Dimension targetSize=new Dimension(10,10);
		   webDriver.manage().window().setSize(targetSize);
//		   webDriver.manage().window().maximize();
		   //与浏览器同步非常重要，必须等待浏览器加载完毕
		   webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		   webDriver.manage().deleteAllCookies();   //清除缓存
	   
		   //下面这句等价于webDriver.navigate().to("www.baidu.com");
		   webDriver.get("http://www.hdpfans.com/member.php?mod=logging&action=login&referer=");
		   
		   //在输入框中填写要搜索的内容
		   WebElement ls_username = webDriver.findElement(By.name("username"));
		   ls_username.sendKeys(account);
		   
		   WebElement ls_password = webDriver.findElement(By.name("password"));
		   ls_password.sendKeys("a123456789");
		   //点击搜索按钮
		   WebElement su = webDriver.findElement(By.name("loginsubmit"));
		   su.click();
		  
		   //隐式等待20秒后执行后面操作
//		   webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		   //显示等待，满足页面元素条件后执行后面操作
	        WebDriverWait wait = new WebDriverWait(webDriver, 20);
	        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".comiis_avt a")));  
//		   
//		   Thread.sleep(Long.parseLong("20000"));
		   
		   Set<Cookie> cookies=webDriver.manage().getCookies();
		   for (Cookie cookie : cookies) {
			System.out.println("name:"+cookie.getName()+" ,value:"+cookie.getValue());
		   }
		   
		   //列表页解析
//		   String t="http://www.hdpfans.com/forum.php?mod=forumdisplay&fid=46&filter=author&orderby=dateline";
//		   webDriver.get(t);
		   
		 //显示等待，满足页面元素条件后执行后面操作
//	       WebDriverWait wait1 = new WebDriverWait(webDriver, 20);
//	       wait1.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".comiis_kmlb_ftico")));
//		   
//		   webDriver.switchTo().defaultContent();
//	       List<WebElement> frames =webDriver.findElements(By.cssSelector(".new > .xi1"));
	       
		   String currentHandle = webDriver.getWindowHandle();  
		   System.out.println("currentHandle1:"+currentHandle);
		   
		   Set<String> oldhandles=null;
		   
		   for(int i=0;i<2;i++){ //每个账号一小时只能回5个贴
			   String href=linkList.get(i);
			   if(href!=null && !href.equals("") && href.startsWith("http")) {
				   System.out.println(href);
				   //打开新标签页,试了n遍终于成功了
//					   JavascriptExecutor oJavaScriptExecutor = (JavascriptExecutor)webDriver;
//					   oJavaScriptExecutor.executeScript("window.open('" + href + "')");
				   
				   String currentHandle0 = webDriver.getWindowHandle();  
				   System.out.println("currentHandle0:"+currentHandle0);
	   
			   //帖子发贴
//				   String href="http://www.hdpfans.com/forum.php?mod=viewthread&tid=794997&extra=page%3D1%26filter%3Dauthor%26orderby%3Ddateline&page=2";
				   JavascriptExecutor oJavaScriptExecutor = (JavascriptExecutor)webDriver;
				   oJavaScriptExecutor.executeScript("window.open('" + href + "')");

				   Set<String> handles = webDriver.getWindowHandles(); 
				   WebDriver subWebDriver=null; 
				   for (String s:handles) {
					   if(!oldhandles.contains(s)) {  //
						   System.out.println("窗口："+s);
						   if(!s.equals(currentHandle)) { //不等于当前窗口，就是新打开的窗口
							   subWebDriver=webDriver.switchTo().window(s); //跳到新窗口
						   }
					   }
				   }
				   String currentHandle2 = subWebDriver.getWindowHandle();  
				   System.out.println("currentHandle2:"+currentHandle2);
				   
				   
//			       WebDriverWait wait2 = new WebDriverWait(subWebDriver, 20);
//			       wait2.until(ExpectedConditions.presenceOfElementLocated(By.id("newspecial")));
//				   
//			       WebElement fastpostmessage = subWebDriver.findElement(By.id("fastpostmessage"));
//			       fastpostmessage.sendKeys("不错的帖子,谢谢分享。。。");
//			       
//			       WebElement fastpostsubmit = subWebDriver.findElement(By.id("fastpostsubmit"));
//			       fastpostsubmit.click();
//			       
			       if(subWebDriver!=null) { //关闭子窗口
			    	   webDriver.switchTo().window(currentHandle);  //切换回父窗口
//				    	   subWebDriver.close();
			       }
				   
				   Thread.sleep(10000);
			   }
				   
		   }
		   
		   
//		   String currentHandle3 = webDriver.getWindowHandle();  
//		   System.out.println("currentHandle3:"+currentHandle3);
		   
		   System.out.println("Hello World!");
		   
	}
	
}
