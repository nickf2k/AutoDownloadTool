package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Example1 {
    public static void main(String[] args) throws IOException {
        URL url = new URL("http://stackoverflow.com/questions/6159118/using-java-to-pull-data-from-a-webpage");
        URLConnection connection = url.openConnection();
        InputStream is = connection.getInputStream();

        BufferedReader bf = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = bf.readLine()) != null) {
            System.out.println(line);
        }
    }
}
