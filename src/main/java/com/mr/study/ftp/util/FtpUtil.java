package com.mr.study.ftp.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author zhanxp
 * @version 1.0 2019/5/23
 */
public class FtpUtil {
    /**
     * 维护FTPClient实例
     */
    private final static LinkedBlockingQueue<FTPClient> FTP_CLIENT_QUEUE = new LinkedBlockingQueue<>();

    public static boolean download(FtpConfig ftpConfig, String remotePath, String fileName, String localPath, String localName) {
        try {
            FTPClient ftpClient = connectClient(ftpConfig);
            boolean changeResult = ftpClient.changeWorkingDirectory(remotePath);
            if (!changeResult) {
                throw new RuntimeException("切换目录失败");
            }
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            File file = new File(localPath, localName);
            if (!file.getParentFile().exists()) {
                boolean mkdirsResult = file.getParentFile().mkdirs();
                if (!mkdirsResult) {
                    throw new RuntimeException("创建目录失败");
                }
            }
            if (!file.exists()) {
                boolean createFileResult = file.createNewFile();
                if (!createFileResult) {
                    throw new RuntimeException("创建文件失败");
                }
            }
            OutputStream outputStream = new FileOutputStream(file);
            boolean result = ftpClient.retrieveFile(fileName, outputStream);
            outputStream.flush();
            outputStream.close();
            // 退出FTP
            ftpClient.logout();
            //归还对象
            offer(ftpClient);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean upload(FtpConfig ftpConfig, String remotePath, InputStream inputStream, String fileName) {
        try {
            FTPClient ftpClient = connectClient(ftpConfig);
            boolean changeResult = ftpClient.changeWorkingDirectory(remotePath);
            if (!changeResult) {
                throw new RuntimeException("切换目录失败");
            }
            // 设置被动模式
            ftpClient.enterLocalPassiveMode();
            // 设置流上传方式
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            // 设置二进制上传
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            //中文存在问题
            // 上传 fileName为上传后的文件名
            ftpClient.storeFile(fileName, inputStream);
            // 关闭本地文件流
            inputStream.close();
            // 退出FTP
            ftpClient.logout();
            //归还对象
            offer(ftpClient);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private static FTPClient connectClient(FtpConfig ftpConfig) throws IOException {
        FTPClient ftpClient = getClient();
        // 连接FTP服务器
        ftpClient.connect(ftpConfig.ip, ftpConfig.port);
        // 登录FTP
        ftpClient.login(ftpConfig.userName, ftpConfig.password);
        // 正常返回230登陆成功
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new RuntimeException("连接ftp失败");
        }
        ftpClient.setControlEncoding("GBK");
        return ftpClient;
    }

    private static FTPClient getClient() {
        FTPClient ftpClient = FTP_CLIENT_QUEUE.poll();
        if (ftpClient != null) {
            return ftpClient;
        }
        return new FTPClient();
    }

    private static void offer(FTPClient ftpClient) {
        FTP_CLIENT_QUEUE.offer(ftpClient);
    }

    public static class FtpConfig {
        private String ip;
        private int port;
        private String userName;
        private String password;

        public FtpConfig setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public FtpConfig setPort(int port) {
            this.port = port;
            return this;
        }

        public FtpConfig setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public FtpConfig setPassword(String password) {
            this.password = password;
            return this;
        }
    }
}
