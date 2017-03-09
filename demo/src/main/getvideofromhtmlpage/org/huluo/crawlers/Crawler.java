package org.huluo.crawlers;

import org.huluo.dbutil.InsertVideoUrlTodb;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.UUID;

/*
 抓取在这个http://www.shjjjc.gov.cn/2015jjw/n2230/n2238/index.html网址的在线视频播放的源地址
 */
public class Crawler {
    public static void main(String[] args) {
        try {
            Document document =  Jsoup.connect("http://www.shjjjc.gov.cn/2015jjw/n2230/n2238/index.html").get();
            Elements videoElements = document.select(".video");
            Elements aElements = videoElements.select("a");
            InsertVideoUrlTodb.insertUrlTdb(UUID.randomUUID().toString().replace("-", "").trim(), aElements.attr("href"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
