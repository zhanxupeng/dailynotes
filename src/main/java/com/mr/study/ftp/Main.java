package com.mr.study.ftp;

import com.mr.study.ftp.util.FtpUtil;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author zhanxp
 * @version 1.0 2019/5/23
 */
public class Main {
    public static void main(String[] args) throws Exception {
        FtpUtil.FtpConfig ftpConfig = new FtpUtil.FtpConfig().setIp("10.60.45.127").setPort(21).setUserName("ftpChina").setPassword("zhan123456");
//        FtpUtil.upload(ftpConfig,
//                "", new FileInputStream(new File("H:\\work\\log\\TestWeb.log.2019-05-16.log")), "zhanxp1.txt");
//        FtpUtil.upload(ftpConfig,
//                "", new FileInputStream(new File("H:\\work\\log\\TestWeb.log.2019-05-16.log")), "zhanxp2.txt");

        boolean result = FtpUtil.download(ftpConfig, "", "zhanxp1.txt", "H:\\work\\log\\local", "test1.txt");
        System.out.println(result);
//        String ip = "10.60.45.127";
//        int port = 21;
//        String username = "ftpChina";
//        String passwd = "zhan123456";
//        // 初始化FTP客户端
//        FTPClient ftp = new FTPClient();
//        try {
//
//            int reply;
//            ftp.connect(ip, port); // 连接FTP服务器
//            ftp.login(username, passwd); // 登录FTP
//            reply = ftp.getReplyCode(); // 正常返回230登陆成功
//            System.out.println(reply);
//            if (!FTPReply.isPositiveCompletion(reply)) {
//                ftp.disconnect();
//            }
//            ftp.changeWorkingDirectory(""); // ftpPath为要上传的FTP路径
//            File file = new File("H:\\work\\log\\TestWeb.log.2019-05-16.log");
//            System.out.println(file.exists());
//            FileInputStream input = new FileInputStream(file); // localFilePath为要上传的本地文件路径
//            ftp.enterLocalPassiveMode(); // 设置被动模式
//            ftp.setFileTransferMode(FTP.STREAM_TRANSFER_MODE); // 设置流上传方式
//            ftp.setFileType(FTP.BINARY_FILE_TYPE); // 设置二进制上传
//            //中文存在问题
//            ftp.storeFile("zhanxp1.txt", input); // 上传 fileName为上传后的文件名
//            input.close(); // 关闭本地文件流
//            ftp.logout(); // 退出FTP
//        } catch (IOException e) {
//        } catch (Exception e) {
//        }
    }
}
