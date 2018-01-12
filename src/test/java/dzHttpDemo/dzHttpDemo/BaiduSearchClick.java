package dzHttpDemo.dzHttpDemo;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * 模拟浏览器百度搜索点击
 * @author huangdb
 */
public class BaiduSearchClick {

	public static void main(String[] args) {

		  //WebDriver是一个接口，每一种浏览器都有一个实现类（多态）
		   System.setProperty ( "webdriver.gecko.driver" , "D:/software/geckodriver-v0.19.1-win64/geckodriver.exe" );
////		   System.setProperty ( "webdriver.firefox.bin" , "C:/Program Files/Mozilla Firefox/firefox.exe" );
		   WebDriver webDriver = new FirefoxDriver();
		   webDriver.manage().window().maximize();
		   //与浏览器同步非常重要，必须等待浏览器加载完毕
		   webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		   //下面这句等价于webDriver.navigate().to("www.baidu.com");
		   webDriver.get("http://www.baidu.com");
		   //在输入框中填写要搜索的内容
		   WebElement kw = webDriver.findElement(By.id("kw"));
		   kw.sendKeys("java用selenium库控制chrome");
		   //点击搜索按钮
		   WebElement su = webDriver.findElement(By.id("su"));
		   su.click();
//		   webDriver.findElements(By.className("result")).forEach(
//				   x -> { System.out.println(x.getText());});
		   System.out.println("!@@@@@@@@@@"+su.getText());
		   
		   //寻找包含weiyinfu的搜索条目
//		   webDriver.findElement(By.partialLinkText("selenium+java+chrome环境搭建 - sincoolvip - 博客园")).click();  //按链接搜索
		   webDriver.findElement(By.className("pc").linkText("3")).click();	//按百度页码搜索
		   //webDriver.close();
		   System.out.println("Hello World!");
		   
	}
	
	
}
