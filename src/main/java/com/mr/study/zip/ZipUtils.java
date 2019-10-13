package com.mr.study.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author zhanxp
 * @version 1.0 2019/8/5
 */
public class ZipUtils {
    private final static String LINUX_SEPARATOR = "/";
    private final static String EMPTY = "";

    /**
     * 压缩文件
     *
     * @param sourceFile  原始文件（文件和目录都可以）
     * @param zipPath     生成压缩文件的目录
     * @param zipFileName 生成压缩文件文件名
     * @throws IOException
     */
    public static void compress(File sourceFile, String zipPath, String zipFileName) throws IOException {
        File zipFile = new File(zipPath + File.separator + zipFileName);
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
            if (sourceFile.isDirectory()) {
                zipDir(zipOutputStream, sourceFile, EMPTY);
            } else {
                zipFile(zipOutputStream, sourceFile, EMPTY);
            }
            zipOutputStream.flush();
        }
    }

    /**
     * 解压缩文件
     *
     * @param file     原始文件
     * @param basePath 解压缩的目录
     * @throws IOException
     */
    public static void decompress(File file, String basePath) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file))) {
            unZip(zipInputStream, basePath);
        }
    }

    /**
     * 压缩目录
     *
     * @param zos
     * @param file
     * @param path
     * @throws IOException
     */
    private static void zipDir(ZipOutputStream zos, File file, String path) throws IOException {
        path = path + file.getName() + LINUX_SEPARATOR;
        ZipEntry entry = new ZipEntry(path);
        zos.putNextEntry(entry);
        File[] files = file.listFiles();
        for (File x : files) {
            if (x.isDirectory()) {
                zipDir(zos, x, path);
            } else {
                zipFile(zos, x, path);
            }
        }
        zos.closeEntry();
    }

    /**
     * 压缩文件
     *
     * @param zos
     * @param file
     * @param path
     * @throws IOException
     */
    private static void zipFile(ZipOutputStream zos, File file, String path) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(path + file.getName());
            zos.putNextEntry(zipEntry);
            int len;
            byte[] buf = new byte[1024];
            while ((len = fileInputStream.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
        }
    }

    /**
     * 解压缩
     *
     * @param zis
     * @param basePath
     * @throws IOException
     */
    private static void unZip(ZipInputStream zis, String basePath) throws IOException {
        ZipEntry entry = zis.getNextEntry();
        while (entry != null) {
            File file = new File(basePath + LINUX_SEPARATOR + entry.getName());
            if (entry.getName().endsWith(LINUX_SEPARATOR)) {
                file.mkdir();
            } else {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    int len;
                    byte[] buf = new byte[1024];
                    while ((len = zis.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                }
                zis.closeEntry();
            }

            entry = zis.getNextEntry();
        }
    }

}
