package org.huluo.crawlers;

import cn.wanghaomiao.seimi.annotation.*;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Response;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.huluo.dbutil.InsertVideoUrlTodb;
import org.huluo.entity.AntiCorruptionEntity;
import org.huluo.entity.DataStoreWrapper;
import org.huluo.util.HtmlPageFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.IOException;

@cn.wanghaomiao.seimi.annotation.Crawler(name = "crawler6")
public class Crawler6 extends BaseSeimiCrawler {
    private static final String startUrl = "http://www.ccdi.gov.cn/shipin/zxft/index_6.html";

    @Override
    public String[] startUrls() {
        return new String[]{startUrl};
    }

    @Override
    public void start(Response response) {
        String url = response.getUrl();
        try {
            HtmlPage htmlPage = HtmlPageFactory.getInstance(startUrl);

            Document document = Jsoup.parse(htmlPage.asXml());
            Elements elements = document.select(".img");


            for (Element element : elements) {
                AntiCorruptionEntity antiCorruptionEntity = new AntiCorruptionEntity();

                antiCorruptionEntity.setOnlineVedioUrl(element.select("a").attr("href"));
                String imageRelativeUrl = element.select("img").attr("src");
                //拼接出img的绝对url
                String imageAbsoluteUrl = startUrl.substring(0, startUrl.indexOf("index") - 1) + imageRelativeUrl.substring(imageRelativeUrl.indexOf("./") + 1);
                antiCorruptionEntity.setImageUrl(imageAbsoluteUrl);
                DataStoreWrapper.antiCorruptionEntitySet.add(antiCorruptionEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (AntiCorruptionEntity antiCorruptionEntity : DataStoreWrapper.antiCorruptionEntitySet) {
            String onlineVedioUrl = antiCorruptionEntity.getOnlineVedioUrl();
            //拿到运行js脚本的页面
            HtmlPage htmlPage = HtmlPageFactory.getInstance(onlineVedioUrl);
            Document document = Jsoup.parse(htmlPage.asXml());
            //抓取标题
            Elements titleElements = document.select("h2.f12_0044");
            antiCorruptionEntity.setTitle(titleElements.toString());
            //拼接ajax接口地址
            String ajaxInterfaceUrl = onlineVedioUrl.replace("index.shtml", "02/index.shtml");

            HtmlPage ajaxInterfaceHtmlPage = HtmlPageFactory.getInstance(ajaxInterfaceUrl);
            Document ajaxInterfaceDocument = Jsoup.parse(ajaxInterfaceHtmlPage.asXml());
            Elements chatContentElements = ajaxInterfaceDocument.select(".block4");

            antiCorruptionEntity.setChatcontent(chatContentElements.toString());
        }


        for (AntiCorruptionEntity antiCorruptionEntity : DataStoreWrapper.antiCorruptionEntitySet) {
            System.out.println(antiCorruptionEntity.getTitle());
            System.out.println(antiCorruptionEntity.getOnlineVedioUrl());
            System.out.println(antiCorruptionEntity.getImageUrl());
            System.out.println(antiCorruptionEntity.getChatcontent());
            System.out.println("有运行么");
        }
        InsertVideoUrlTodb.insertDataToDb(DataStoreWrapper.antiCorruptionEntitySet);



    }
}
