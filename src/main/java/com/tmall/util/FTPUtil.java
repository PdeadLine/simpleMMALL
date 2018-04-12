package com.tmall.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * create by sintai
 */

public class FTPUtil {
    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);
    private static String ftpIp=PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser=PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass=PropertiesUtil.getProperty("ftp.password");

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    public static boolean uploadFile(List<File> fileList) {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPass);
        logger.info("开始链接ftp服务器");
        boolean result = ftpUtil.uploadFile("img", fileList);
        logger.info("开始连接FTP服务器，结束上传，上传结果:{}");
        return result;
    }

    /**
     * 上传文件
     * @param remotePath
     * @param fileList
     * @return
     */
    private boolean uploadFile(String remotePath, List<File> fileList) {
        boolean   uploaded=true;
        FileInputStream fis=null;
        //链接FTP服务器
        if (connectServer(this.ip, this.port, this.user, this.pwd)) {
            try {
                ftpClient.changeWorkingDirectory(remotePath); //修改ftp上传路径
                ftpClient.setSendBufferSize(1024);//设置缓冲大小
                ftpClient.setControlEncoding("UTF-8");//设置字符编码
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);//设置二进制类型，防止乱码
                ftpClient.enterLocalPassiveMode();//FTP上设了被动模式，这里也用被动
                for (File fileItem : fileList) {
                    fis = new FileInputStream(fileItem);//读取文件
                    ftpClient.storeFile(fileItem.getName(), fis);//上传
                }
                
            } catch (IOException e) {
                logger.error("上传文件异常",e);
                uploaded=false;//如异常，返回失败标志
            }
        }
        return uploaded;
    }

    /**
     * 连接FTP服务
     * @param ip
     * @param port
     * @param user
     * @param password
     * @return
     */
    private boolean connectServer(String ip, int port, String user, String password) {
        boolean isSuccess=false;
        FTPClient ftpClient= new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess=ftpClient.login(user, password);
        } catch (IOException e) {
            logger.error("链接FTP服务器异常",e);
        }
        return  isSuccess;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
