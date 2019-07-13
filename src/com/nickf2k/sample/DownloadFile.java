package com.nickf2k.sample;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DownloadFile {
    private static String filePath = "F:\\HUST\\fresher\\AutoDownloadTool\\fos.pdf";
    private static String sampleURL = "https://pimg-fpiw.uspto.gov/fdd/33/288/102/0.pdf";

    public static void main(String[] args) throws IOException {
//        DownloadFileFromURL();
//        DownLoadFileCommonsApache();
        DownLoadFileCommonsApache();
    }

    private static void DownloadFileFromURL() {
        URL urlObj = null;
        ReadableByteChannel rbcObj = null;
        FileOutputStream fosObj = null;

        Path filePathObj = Paths.get(filePath);
        boolean fileExist = Files.exists(filePathObj);
        if (fileExist) {
            try {
                urlObj = new URL(sampleURL);
                rbcObj = Channels.newChannel(urlObj.openStream());
                fosObj = new FileOutputStream(filePath);
                fosObj.getChannel().transferFrom(rbcObj, 0, Long.MAX_VALUE);
                System.out.println("Download Successfully");

            } catch (IOException e) {
                System.out.println("Problem occured while Downloading the File = " + e.getMessage());
            } finally {
                try {
                    if (fosObj != null) {
                        fosObj.close();
                    }
                    if (rbcObj != null) {
                        rbcObj.close();
                    }
                } catch (IOException e) {
                    System.out.println("Problem occured while Downloading the File = " + e.getMessage());
                }
            }
        } else {
            System.out.println("File not present! Check!");
        }
    }

    private static void DownLoadFileCommonsApache() throws IOException {
        FileUtils.copyURLToFile(new URL(sampleURL),
                new File(filePath), 5000, 5000);
    }

    private static void FileCopy() throws IOException {
        URL url = new URL(sampleURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestProperty("User-Agent","Mozilla/4.76");

        try (InputStream in = url.openStream()) {
            Files.copy(in, Paths.get("fos.pdf"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("hello");

    }

    private static void request() {

    }
}
