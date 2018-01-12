package dzHttpDemo.dzHttpDemo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DiscuzLogin {
	
	public static void main(String[] args) throws NumberFormatException, InterruptedException, ParseException {

		  //WebDriver是一个接口，每一种浏览器都有一个实现类（多态）
//		   System.setProperty ( "webdriver.gecko.driver" , "E:/software/geckodriver-v0.19.1-win64/geckodriver.exe" );
		   
//		   String proxyIp = "localhost";  
//		   int proxyPort = 28080;  
//		   Proxy proxy=new Proxy(); 
//		   proxy.setHttpProxy(proxyIp+":"+proxyPort);
//		   FirefoxOptions options = new FirefoxOptions();  
//		   options.setProxy(proxy);
		   // 使用代理  
//		   WebDriver webDriver = new FirefoxDriver(options);
		
		   System.setProperty ( "webdriver.chrome.driver" , "D:/software/chromedriver_win32/chromedriver.exe" );
		   WebDriver webDriver = new ChromeDriver();
		
//		   WebDriver webDriver = new FirefoxDriver();
		   webDriver.manage().window().maximize();
		   //与浏览器同步非常重要，必须等待浏览器加载完毕
		   webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		   webDriver.manage().deleteAllCookies();   //清除缓存
		   
		   //下面这句等价于webDriver.navigate().to("www.baidu.com");
		   webDriver.get("http://www.hdpfans.com/member.php?mod=logging&action=login&referer=");
		   
		   //在输入框中填写要搜索的内容
		   WebElement ls_username = webDriver.findElement(By.name("username"));
		   ls_username.sendKeys("我们不一样001");
		   
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
		   String t="http://www.hdpfans.com/forum.php?mod=forumdisplay&fid=46&filter=author&orderby=dateline";
		   webDriver.get(t);
		   
		 //显示等待，满足页面元素条件后执行后面操作
	       WebDriverWait wait1 = new WebDriverWait(webDriver, 20);
	       wait1.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".comiis_kmlb_ftico")));
//		   
//		   webDriver.switchTo().defaultContent();
//	       List<WebElement> frames =webDriver.findElements(By.cssSelector(".by>em>a"));
	       List<WebElement> frames =webDriver.findElements(By.cssSelector(".xi1"));
//	       List<WebElement> frames =webDriver.findElements(By.cssSelector(".new > .xi1"));
	       
		   String currentHandle = webDriver.getWindowHandle();  
		   System.out.println("currentHandle1:"+currentHandle);
		   
		   if(frames!=null && !frames.isEmpty()){
			   for(int i=0;i<frames.size();i++){
				   WebElement frame=frames.get(i);
				   String href=frame.getAttribute("href");
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
					   for (String s : handles) { 
						   System.out.println("窗口："+s);
						   if(!s.equals(currentHandle)) { //不等于当前窗口，就是新打开的窗口
							   subWebDriver=webDriver.switchTo().window(s); //跳到新窗口
						   }
					   }
					   String currentHandle2 = webDriver.getWindowHandle();  
					   System.out.println("currentHandle2:"+currentHandle2);
					   
					   
				       WebDriverWait wait2 = new WebDriverWait(webDriver, 20);
				       wait2.until(ExpectedConditions.presenceOfElementLocated(By.id("newspecial")));
					   
				       WebElement fastpostmessage = webDriver.findElement(By.id("fastpostmessage"));
				       fastpostmessage.sendKeys("谢谢分享,链接呢？");
				       
				       WebElement fastpostsubmit = webDriver.findElement(By.id("fastpostsubmit"));
				       fastpostsubmit.click();
				       
				       if(subWebDriver!=null) { //关闭子窗口
				    	   webDriver.switchTo().window(currentHandle);  //切换回父窗口
//				    	   subWebDriver.close();
				       }
					   
					   Thread.sleep(10000);
				   }

				   
			   }
		   }
		   
		   
//		   String currentHandle3 = webDriver.getWindowHandle();  
//		   System.out.println("currentHandle3:"+currentHandle3);
		   
		   System.out.println("Hello World!");
		   
	}
	
}
