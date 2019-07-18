package gov.niptex.process;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Download  {
    private static String DOWNLOAD_URL = "https://pimg-fpiw.uspto.gov/fdd/";
    protected ArrayList<Node> listNode;
    private String pathDownload;
    public Download(ArrayList<Node> listNode, String pathDownload) {
        this.listNode = listNode;
        this.pathDownload = pathDownload;
    }

    public Download(String pathDownload) {
        this.pathDownload = pathDownload;
    }

    protected void download() {
        listNode.forEach(node -> {
                downLoadFileCommonsApache(convertURLDown(node.getID()), convertFilePath(node.getID()));
        });
    }

    private void downLoadFileCommonsApache(String fileURL, String filePath){
        try {
            FileUtils.copyURLToFile(new URL(fileURL),
                        new File(filePath), 5000, 5000);
        } catch (IOException e) {
            System.out.println("connect time out");
        }

    }

    private String convertURLDown(String ID){
        String url = DOWNLOAD_URL;
        url+=ID.substring(ID.length()-2,ID.length()) +"/"+ID.substring(ID.length()-5,ID.length()-2)+"/";
        if (ID.contains("R")){
            url+="RE0/0.pdf";
        }else if (ID.contains("D")){
            url+="D0"+ID.substring(ID.length()-6,ID.length()-5)+"/0.pdf";
        }else if (ID.contains("PP")){
            url+="PP0/0.pdf";
        }else {
            String sub = ID.substring(0,ID.length()-5);
            if (sub.length()==2){
                url+="0"+sub+"/0.pdf";
            }
            else url+=sub+"/0.pdf" ;
        }
        System.out.println(url);
        return url;
    }

    private String convertFilePath(String ID){
        String filePath = pathDownload+"/US"+ID+".pdf";
        return filePath;
    }

    public void setListNode(ArrayList<Node> listNode) {
        this.listNode = listNode;
    }

}
