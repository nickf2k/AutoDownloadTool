package sample;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CrawlData {
    private final static String URL = "http://www.manythings.org/voa/scripts/";
    public static void main(String[] args) throws IOException {
//        Document document = Jsoup.connect(URL).data("query", "Java").userAgent("Chrome").cookie("auth", "token").timeout(5000).post();
//        Elements elements = document.getElementsByClass("list").get(1).children();
//        for (Element e: elements){
//            Elements list_a = e.getElementsByTag("a");
//            for (Element element: list_a){
//                String href_a = element.attr("href");
//                System.out.println(href_a);
//            }
//        }

        connect("http://patft.uspto.gov/netacgi/nph-Parser?Sect1=PTO2&Sect2=HITOFF&p=1&u=%2Fnetahtml%2FPTO%2Fsearch-bool.html&r=0&f=S&l=50&TERM1=abc&FIELD1=&co1=AND&TERM2=uio&FIELD2=&d=PTXT");
    }
    private static void connect(String url) throws IOException {
        Document document= Jsoup.connect(url).get();
//        System.out.println(document.title());
//        System.out.println(document.html());
        Elements elements = document.getElementsByTag("table").get(1).children().get(0).children(); //list tr
        elements.remove(0);
        System.out.println(elements.size());
        for (Element e: elements){
            String key  = e.getElementsByTag("td").get(1).getElementsByTag("a").get(0).text();
            System.out.println(key);
        }

//        System.out.println(elements.html());
    }
}
