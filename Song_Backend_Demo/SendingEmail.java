package com.email; 
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import javax.activation.*;
import javax.mail.*;
import javax.mail.Message.*;
import javax.mail.internet.*; 
/**
 * <p>Copyright: Copyright (c) 2004</p>
 * @author flyxxxxx@163.com
 * @version 1.0
 */ 
public class MailSender
    implements Runnable { 
private int time = 5 * 60 * 1000; //扫描数据库时间间隔
  private boolean flag = true; //停止线程标记
  private String server = "127.0.0.1"; //SMTP服务器地址
  private String port = "25"; //SMTP服务器端口
  private String address; //用于发送EMAIL的发送者地址
  private String username; //发送者的用户名
  private String password; //发送者的密码
  private boolean validate = true; //邮件服务器是否要求验证
  private File root = null; //WEB根目录
  MailConfigManager manager = MailConfigManager.getInstance(); 
  public MailSender() {
  } 
  public void setAddress(String address) {
    this.address = address;
  } 
  public void setPassword(String password) {
    this.password = password;
  } 
  public void setPort(String port) {
    if (port != null && port.length() > 0) {
      this.port = port;
    }
  } 
  public void setServer(String server) {
    this.server = server;
  } 
  public void setUsername(String username) {
    this.username = username;
  } 
  public void setValidate(boolean validate) {
    this.validate = validate;
  } 
  public void setTime(int minute) {
    this.time = minute * 60 * 1000;
  } 
  public void run() {
    long lastTime = new Date().getTime(); //保存前一次发送邮件的时间
    while (flag) { //服务器停止时退出线程
      long k = new Date().getTime() - lastTime;
      if (k < -1000) { //防止系统修改时间
        lastTime = new Date().getTime();
        continue;
      }
      if (k > time) { //超过设定时间间隔开始发送邮件
        sendData();
        lastTime = new Date().getTime();
      }
      try {
        Thread.sleep(100);
      }
      catch (Exception e) {}
    }
  } 
  public void stop() {
    flag = false; 
} 
  /**
   * 检索数据库，并发送邮件
   */
  private void sendData() {
    ResultSet rs=null;//读取数据库数据
    
    try {
      Session session = Session.getInstance(getProperties(), new Authentic());
      while(flag) {//服务器停止时退出线程
        String toAddress = null;//发送地址（从rs中得到）
        String subject = null;//邮件主题
        String content = null ;//邮件内容
        String file[] = null;//所有附件（绝对路径）
          
        sendMail(session, toAddress, subject, content, file);//发送邮件
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      if(rs!=null){
        try{
          rs.close();
        }
        catch(SQLException e){}
      }
    }
  } 
  /**
   * 发送邮件
   * @param session 会话
   * @param toAddress 目的地址
   * @param subject 邮件主题
   * @param content 邮件内容（HTML）
   * @param files 邮件附件
   * @return 是否发送成功
   */
  private boolean sendMail(Session session, String toAddress, String subject,
                           String content, String[] files) { 
    toAddress = "lijin@regaltec.com.cn"; 
    try {
      Message rs = new MimeMessage(session); 
      Address from = new InternetAddress(address);
      rs.setFrom(from); //发送地址
      rs.setRecipient(RecipientType.TO, new InternetAddress(toAddress)); //接收地址
      rs.setSubject(subject); //邮件主题
      Multipart mp = new MimeMultipart();
      BodyPart html = new MimeBodyPart(); 
      html.setContent(content, "text/html; charset=GBK"); //邮件HTML内容
      mp.addBodyPart(html);
      if (files != null && files.length > 0) { //邮件附件
        for (int i = 0; i < files.length; i++) {
          MimeBodyPart mbp = new MimeBodyPart();
          FileDataSource fds = new FileDataSource(files[i]);
          mbp.setDataHandler(new DataHandler(fds));
          mbp.setFileName(MimeUtility.encodeWord(files[i]), "GBK", null));
          mp.addBodyPart(mbp);
        }
      }
      rs.setContent(mp); //邮件全部内容
      rs.setSentDate(new Date()); //发送时间
      Transport.send(rs); //发送
      return true;
    }
    catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  } 
  /**
   * 邮件会话属性
   * @return 会话属性
   */
  private Properties getProperties() {
    Properties rs = new Properties();
    rs.put("mail.smtp.host", server);
    rs.put("mail.smtp.port", port);
    rs.put("mail.smtp.auth", validate ? "true" : "false");
    return rs;
  } 
  public void setRoot(File root) {
    this.root = root;
  } 
  class Authentic
      extends Authenticator { //验证密码 
    public Authentic() {
    } 
    public PasswordAuthentication getPasswordAuthentication() {
      return new PasswordAuthentication(username, password);
    } 
  } 
} 