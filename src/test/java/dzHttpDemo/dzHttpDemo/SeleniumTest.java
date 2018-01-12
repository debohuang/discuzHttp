package dzHttpDemo.dzHttpDemo;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumTest {

	public static void main(String[] args) {

		  //WebDriver是一个接口，每一种浏览器都有一个实现类（多态）
		   System.setProperty ( "webdriver.gecko.driver" , "D:/software/geckodriver-v0.19.1-win64/geckodriver.exe" );
		   WebDriver webDriver = new FirefoxDriver();
		   webDriver.manage().window().maximize();
		   //与浏览器同步非常重要，必须等待浏览器加载完毕
		   webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		   //下面这句等价于webDriver.navigate().to("www.baidu.com");
		   webDriver.get("http://localhost:8092/");
		   
		   //在输入框中填写要搜索的内容
		   WebElement ls_username = webDriver.findElement(By.id("ls_username"));
		   ls_username.sendKeys("test123");
		   
		   WebElement ls_password = webDriver.findElement(By.id("ls_password"));
		   ls_password.sendKeys("123456");
//		   //点击搜索按钮
		   WebElement su = webDriver.findElement(By.xpath("/html/body/div[5]/div/div[1]/form/div/div/table/tbody/tr[2]/td[3]/button"));
		   su.click();
		  
		   //隐式等待20秒后执行后面操作
//		   webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		   //显示等待，满足页面元素条件后执行后面操作
	        WebDriverWait wait = new WebDriverWait(webDriver, 20);
	        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("vwmy")));
		   
		   Set<Cookie> cookies=webDriver.manage().getCookies();
		   for (Cookie cookie : cookies) {
			System.out.println("name:"+cookie.getName()+" ,value:"+cookie.getValue());
		   }
		   
//		   webDriver.findElements(By.className("result")).forEach(
//				   x -> { System.out.println(x.getText());});
//		   System.out.println("!@@@@@@@@@@"+su.getText());
		   
		   //寻找包含weiyinfu的搜索条目
//		   webDriver.findElement(By.partialLinkText("selenium+java+chrome环境搭建 - sincoolvip - 博客园")).click();  //按链接搜索
//		   webDriver.findElement(By.className("pc").linkText("3")).click();	//按百度页码搜索
		   //webDriver.close();
		   System.out.println("Hello World!");
		   
	}
	
	
}
