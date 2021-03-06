package dzHttpDemo.dzHttpDemo.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URLEncoder;
import java.nio.channels.SocketChannel;

public class TcpTestDemo {
	private int port;
	private String host;
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	public TcpTestDemo(String host, int port) {
		//代理设置 http://b-l-east.iteye.com/blog/1199237
		//以下地址是代理服务器的地址  
//		socket = new Socket("10.1.2.188", 80);  
		//写与的内容就是遵循HTTP请求协议格式的内容，请求百度  
//		socket.getOutputStream().write(new String("GET http://www.baidu.com/ HTTP/1.1\r\n\r\n").getBytes());  
		
		socket = new Socket();
		this.host = host;
		this.port = port;
	}
	
	public void sendGet() throws IOException
	{
		String path = "http://127.0.0.1/forum.php";
		SocketAddress dest = new InetSocketAddress(this.host, this.port);
		
		socket.setSoTimeout(2000); // 如果超过2000毫秒还没有数据，则抛出 SocketTimeoutException。解决socket阻塞问题
		socket.connect(dest);
		
		OutputStreamWriter streamWriter = new OutputStreamWriter(socket.getOutputStream());
		bufferedWriter = new BufferedWriter(streamWriter);
		
		bufferedWriter.write("GET " + path + " HTTP/1.1\r\n");
		bufferedWriter.write("Host: " + this.host + "\r\n");
		bufferedWriter.write("\r\n");
		bufferedWriter.flush();
		
		BufferedInputStream streamReader = new BufferedInputStream(socket.getInputStream());
		bufferedReader = new BufferedReader(new InputStreamReader(streamReader, "utf-8"));
		String line = null;
		
		try {
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
			} 
		} catch (Exception e) { //超时异常不处理
			
		}
		bufferedReader.close();
		bufferedWriter.close();
		socket.close();
	}
	
	public void sendPost() throws IOException
	{
		String path = "/zhigang/postDemo.php";
		String data = URLEncoder.encode("name", "utf-8") + "=" + URLEncoder.encode("gloomyfish", "utf-8") + "&" +
						URLEncoder.encode("age", "utf-8") + "=" + URLEncoder.encode("32", "utf-8");
		// String data = "name=zhigang_jia";
		SocketAddress dest = new InetSocketAddress(this.host, this.port);
		socket.connect(dest);
		OutputStreamWriter streamWriter = new OutputStreamWriter(socket.getOutputStream(), "utf-8");
		bufferedWriter = new BufferedWriter(streamWriter);
		
		bufferedWriter.write("POST " + path + " HTTP/1.1\r\n");
		bufferedWriter.write("Host: " + this.host + "\r\n");
		bufferedWriter.write("Content-Length: " + data.length() + "\r\n");
		bufferedWriter.write("Content-Type: application/x-www-form-urlencoded\r\n");
		bufferedWriter.write("\r\n");
		bufferedWriter.write(data);
		bufferedWriter.flush();
		bufferedWriter.write("\r\n");
		bufferedWriter.flush();
		
		BufferedInputStream streamReader = new BufferedInputStream(socket.getInputStream());
		bufferedReader = new BufferedReader(new InputStreamReader(streamReader, "utf-8"));
		String line = null;
		while((line = bufferedReader.readLine())!= null)
		{
			System.out.println(line);
		}
		bufferedReader.close();
		bufferedWriter.close();
		socket.close();
	}
	
	public static void main(String[] args)
	{
		TcpTestDemo td = new TcpTestDemo("127.0.0.1",18086);
		try {
			 td.sendGet(); //send HTTP GET Request
			
//			td.sendPost(); // send HTTP POST Request
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}
	
}