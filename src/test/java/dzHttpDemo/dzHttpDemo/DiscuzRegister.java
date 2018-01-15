package dzHttpDemo.dzHttpDemo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.exec.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.helper.StringUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import dzHttpDemo.dzHttpDemo.util.C3P0ConnentionProvider;
import dzHttpDemo.dzHttpDemo.util.FetchMail;

public class DiscuzRegister {
	
	private static Log logger = LogFactory.getLog(ProxyApi.class);
	private static Log logger1 = LogFactory.getLog("E");
    private static Connection connection = null;
	
	public static void sendMail() {
		
	}
    
    
	public static void register() throws IOException, InterruptedException {
		  //WebDriver是一个接口，每一种浏览器都有一个实现类（多态）
		   System.setProperty ( "webdriver.gecko.driver" , "E:/software/geckodriver-v0.19.1-win64/geckodriver.exe" );
		   
//		   String ip=ProxyApi.getProxyIp();
//		   String[] ips=ip.split(":");
//		   String proxyIp = ips[0];  
//		   int proxyPort = Integer.parseInt(ips[1]);  
//		   proxy.setHttpProxy(proxyIp+":"+proxyPort);
		   
//		   Proxy proxy=new Proxy(); 
//		   proxy.setHttpProxy(ip);
//		   FirefoxOptions options = new FirefoxOptions();  
//		   options.setProxy(proxy);
		   // 使用代理  
//		   WebDriver webDriver = new FirefoxDriver(options);
		   WebDriver webDriver = new FirefoxDriver();
		   
//		   System.setProperty ( "webdriver.chrome.driver" , "E:/software/chromedriver_win32/chromedriver.exe" );
//		   WebDriver webDriver = new ChromeDriver(options);
//		   WebDriver webDriver = new ChromeDriver();
		
//		   WebDriver webDriver = new FirefoxDriver();
//		   webDriver.manage().window().maximize();
		   Dimension targetSize=new Dimension(10,10);
		   webDriver.manage().window().setSize(targetSize);
		   //与浏览器同步非常重要，必须等待浏览器加载完毕
		   webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		   webDriver.manage().deleteAllCookies();   //清除缓存
		   
		   //下面这句等价于webDriver.navigate().to("www.baidu.com");
		   
	       String account="";
			
		   try {
			   
			webDriver.get("http://www.hdpfans.com/member.php?mod=registerhdp");
			WebDriverWait wait = new WebDriverWait(webDriver, 20);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("layer_reginfo_t")));
			//点击邮箱注册
			WebElement su = webDriver.findElement(By.id("comiis_mobile_tab_1"));
			su.click();
//			
			String mailAccount = getMailAccount();  //获取邮件账号
//			
			System.out.println("mailAccount:"+mailAccount);
			if(StringUtil.isBlank(mailAccount)) {
				new RuntimeException();
			}
//			
//			//在输入框中填写要搜索的内容
			WebElement ls_username = webDriver.findElement(By.id("hdpemail345"));
			ls_username.sendKeys(mailAccount);
			//点击搜索按钮
			WebElement registerformsubmit = webDriver.findElement(By.id("registerform"));
			//		   registerformsubmit.click();
			registerformsubmit.submit();
			WebDriverWait waitfwin = new WebDriverWait(webDriver, 90);
			waitfwin.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".fwin")));
			
			Thread.sleep(10000); //10秒后再去接收邮件
			String url =getMail(mailAccount);

			System.out.println("mail check url:" + url);
			if (url != null && !"".equals(url)) {
				webDriver.get(url);
			}
			WebDriverWait layer_reginfo_t = new WebDriverWait(webDriver, 20);
			layer_reginfo_t.until(ExpectedConditions.presenceOfElementLocated(By.id("layer_reginfo_t")));
			
			Thread.sleep(2000);
			
			//点击邮箱注册
			WebElement comiis_mobile_tab_1 = webDriver.findElement(By.id("comiis_mobile_tab_1"));
			comiis_mobile_tab_1.click();
			account = mailAccount.substring(0, mailAccount.indexOf("@"));
			WebElement hdpname564 = webDriver.findElement(By.id("hdpname564"));
			hdpname564.sendKeys(account);
			WebElement hdppwd877 = webDriver.findElement(By.id("hdppwd877"));
			hdppwd877.sendKeys("a123456789");
			WebElement hdppwd1145 = webDriver.findElement(By.id("hdppwd1145"));
			hdppwd1145.sendKeys("a123456789");
			WebElement realname = webDriver.findElement(By.id("realname"));
			realname.sendKeys(account);
			WebElement registerformsubmit1 = webDriver.findElement(By.id("registerform"));
			registerformsubmit1.submit();
			
			if(!StringUtil.isBlank(account)) {
				updateMailStatus(mailAccount);  //更新邮箱状态
			}
			
		} catch (Exception e) {
			webDriver.close();
			register();
			e.printStackTrace();
		}finally {
//			webDriver.close();
		}
		   
		logger1.error(account+",a123456789");
		   
		System.out.println("Hello World!");
	}
	
	
	public static String getMail(String account) {
		String url=null;
		try {
			url = FetchMail.getMail(account, "123456");
		} catch (IOException e) {
			getMail(account);
			e.printStackTrace();
		} catch (InterruptedException e) {
			getMail(account);
			e.printStackTrace();
		}
		if(url==null) {
			getMail(account);
		}
		return url;
	}
	
	
	public static String getMailAccount() {
		try {
			if (null == connection || connection.isClosed()) {
				connection = C3P0ConnentionProvider.getConnection();
			}
			//查询号码前一天每种类型的扣费记录
			String sql = "select * from mail_account m where m.`status`=0 order by id  LIMIT 1";
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
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("------register start-------");
		register();
		//邮件账号生成user.csv
//		String aString="";
//		for(int i=0;i<1000;i++) {
//			if(i%2==0) {
//				aString=getStringRandom(7);
//			}else if(i%3==0) {
//				aString=getStringRandom(8);
//			}else {
//				aString=getStringRandom(6);
//			}
//			
//			logger1.error(aString+",123456,mail.ccyunbo.com");
//		}
		
		
//		System.out.println(getStringRandom(5));
		
		System.out.println("------register end-------");
		   
	}
	
    //生成随机数字和字母,  
    public static String getStringRandom(int length) {  
          
        String val = "";  
        Random random = new Random();  
          
        //参数length，表示生成几位随机数  
        for(int i = 0; i < length; i++) {  
              
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
            //输出字母还是数字  
            if( "char".equalsIgnoreCase(charOrNum) ) {  
                //输出是大写字母还是小写字母  
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                val += (char)(random.nextInt(26) + temp);  
            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
                val += String.valueOf(random.nextInt(10));  
            }  
        }  
        return val;  
    }  
	
}
