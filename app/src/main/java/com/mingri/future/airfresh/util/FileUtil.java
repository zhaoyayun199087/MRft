package com.mingri.future.airfresh.util;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import mingrifuture.gizlib.code.util.FileUtils;

/**
 * Created by Spring on 2015/11/7.
 */
public class FileUtil {
    private static final int FILE_SIZE = 6*1024* 1024;
    private static String SDPATH;


    public FileUtil() {
        SDPATH = Environment.getExternalStorageDirectory() + "/" ;
    }

    public String getSDPATH() {
        return SDPATH;
    }

    public FileUtil(String SDPATH){
        //得到外部存储设备的目录（/SDCARD）
        SDPATH = Environment.getExternalStorageDirectory() + "/" ;
    }

    /**
     * 在SD卡上创建文件
     * @param fileName
     * @return
     * @throws java.io.IOException
     */
    public File createSDFile(String fileName) throws IOException {
        File file = new File(SDPATH + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上创建目录
     * @param dirName 目录名字
     * @return 文件目录
     */
    public File createDir(String dirName){
        File dir = new File(SDPATH + dirName);
        dir.mkdir();
        return dir;
    }

    /**
     * 判断文件是否存在
     * @param fileName
     * @return
     */
    public boolean isFileExist(String fileName){
        File file = new File(SDPATH + fileName);
        return file.exists();
    }

    /**
     * 判断文件是否存在
     * @param fileName
     * @return
     */
    public static boolean deleteFile(String fileName){
        File file = new File(SDPATH + fileName);
        return file.delete();
    }

    public File write2SDFromInput(String path,String fileName,InputStream input, HttpDownloader.downloadCallback cb){
        File file = null;
        OutputStream output = null;

        try {
            createDir(path);
            file =createSDFile(path + fileName);
            output = new FileOutputStream(file);
            byte [] buffer = new byte[4 * 1024];
            int count;
            count = ( input.read(buffer));
            while( count != -1){
                cb.percent(count );
                output.write(buffer);
                output.flush();
                count = ( input.read(buffer));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if( output != null ) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}