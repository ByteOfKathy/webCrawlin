package com.email; 
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*; 
public class MailSenderServlet
    extends HttpServlet {
  private MailSender sender;
  private Thread t; 
  public void init() throws ServletException {
    sender = new MailSender();
    sender.setRoot(root);
    String server = getInitParameter("server"); 
String port = getInitParameter("port");
    String address = getInitParameter("address");
    String username = getInitParameter("username");
    String password = getInitParameter("password");
    if (server == null || port == null || address == null || username == null ||
        password == null) {
      System.out.println("系统文件web.xml错误：邮件发送程序初始化失败！");
      return;
    }
    sender.setServer(server);
    sender.setPort(port);
    sender.setAddress(address);
    sender.setUsername(username);
    sender.setPassword(password); 
    String time = getInitParameter("time");
    if (time != null) {
      sender.setTime(Integer.parseInt(time));
    }
    String validate = getInitParameter("validate");
    if (validate != null) {
      sender.setValidate(Boolean.valueOf(validate).booleanValue());
    }
    t = new Thread(sender);
    t.start(); //启动邮件发送线程
  } 
  public void destroy() {
    sender.stop(); //停止邮件发送线程
    try {
      t.join(1000);
      if (t.isAlive()) {
        System.out.println("邮件发送线程未停止。");
      }
    }
    catch (Exception e) {}
  } 
} 