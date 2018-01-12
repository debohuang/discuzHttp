package dzHttpDemo.dzHttpDemo.util;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.spi.CalendarDataProvider;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FetchMail {
	
	public static String getMail(String account,String pwd) throws IOException, InterruptedException{
        String protocol = "imap";
        boolean isSSL = false;
        String host = "mail.ccyunbo.com";
        int port = 993;
        String username = account;
        String password = pwd;

        Properties props = new Properties();
//        props.put("mail.imap.ssl.enable", isSSL);
        props.put("mail.store.protocol", "imap");
        props.put("mail.imap.host", host);
//        props.put("mail.imap.port", port);

        Session session = Session.getDefaultInstance(props);

        Store store = null;
        Folder folder = null;
        String url=null;
        try {
            store = session.getStore(protocol);
            store.connect(username, password);

            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            int size = folder.getMessageCount();
            System.out.println("邮件数量："+size);
            if(size==0) {
            	Thread.sleep(10000);
            	getMail(account,pwd);
            	return null;
            }
//            for(int i=1;i<=size;i++) {
//                Message message = folder.getMessage(i);
//                
//                String from = message.getFrom()[0].toString();
//                String subject = message.getSubject();
//                Date date = message.getSentDate();
//                String content=message.getContent().toString();
//                
//                System.out.println(i+"邮件到达时间: " + date.toString());
//            }
            
            Message message = folder.getMessage(size);   //取最新一封邮件
//           Message message = folder.getMessage(1);	 //取第一封邮件
            
            String from = message.getFrom()[0].toString();
            String subject = message.getSubject();
            Date date = message.getSentDate();
            String content=message.getContent().toString();
            
            System.out.println("邮件到达时间: " + date.toString());
            System.out.println("get mail success,From: " + from);
            
            if(!from.contains("hdpfans")){
            	Thread.sleep(10000);
            	getMail(account,pwd);
            }
//            if(!from.contains("ccyunbo")){
//            	Thread.sleep(10000);
//            	getMail(account,pwd);
//            }
            
//            System.out.println("content: " + content);
            
            Thread.sleep(2000);
            
            try {
            	
            	Document document = Jsoup.parse(content);   
				url = document.getElementsByTag("a").get(0).text();
			} catch (Exception e) {
				getMail(account,pwd);
			}
            
            System.out.println(url);
            
//            System.out.println("Date: " + date);
            
        } catch (Exception e) {
        	Thread.sleep(10000);
        	getMail(account,pwd);
            e.printStackTrace();
        } finally {
            try {
                if (folder != null) {
                    folder.close(false);
                }
                if (store != null) {
                    store.close();
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        
//        System.out.println("接收完毕！");
        return url;
	}
	
    public static void main(String[] args) throws IOException, InterruptedException {
    	String url=getMail("2n04DZ@mail.ccyunbo.com","123456");
    	System.out.println(url);
    	
    }
    
    
}