package sample;

import ch.qos.logback.core.util.FileUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DownloadFile {
    private static String filePath = "F:\\HUST\\fresher\\AutoDownloadTool\\src\\sample\\fos.pdf";
    private static String sampleURL = "https://sachvui.com/sachvui-686868666888/ebooks/2018/pdf/Sachvui.Com-dan-than-sheryl-sandberg.pdf";
    public static void main(String[] args) throws IOException {
//        DownloadFileFromURL();
        DownLoadFileCommonsApache();
    }
    private static void DownloadFileFromURL(){
        URL urlObj = null;
        ReadableByteChannel rbcObj = null;
        FileOutputStream fosObj = null;

        Path filePathObj = Paths.get(filePath);
        boolean fileExist = Files.exists(filePathObj);
        if (fileExist){
            try {
                urlObj = new URL(sampleURL);
                rbcObj = Channels.newChannel(urlObj.openStream());
                fosObj = new FileOutputStream(filePath);
                fosObj.getChannel().transferFrom(rbcObj,0,Long.MAX_VALUE);
                System.out.println("Download Successfully");

            }catch (IOException e){
                System.out.println("Problem occured while Downloading the File = "+e.getMessage());
            }finally {
                try {
                    if (fosObj!=null){
                        fosObj.close();
                    }
                    if (rbcObj!=null){
                        rbcObj.close();
                    }
                }catch (IOException e){
                    System.out.println("Problem occured while Downloading the File = "+e.getMessage());
                }
            }
        }else {
            System.out.println("File not present! Check!");
        }
    }
    private static void DownLoadFileCommonsApache() throws IOException {
        FileUtils.copyURLToFile(new URL(sampleURL),
                new File(filePath), 5000,5000);
    }
}
