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

@cn.wanghaomiao.seimi.annotation.Crawler(name = "crawler7")
public class Crawler7 extends BaseSeimiCrawler {
    private static final String startUrl = "http://www.ccdi.gov.cn/shipin/zxft/index_5.html";

    @Override
    public String[] startUrls() {
        return new String[]{startUrl};
    }

    @Override
    public void start(Response response) {
        String url = response.getUrl();
        HtmlPage htmlPage = HtmlPageFactory.getInstance(startUrl);
        Document document = Jsoup.parse(htmlPage.asXml());

        //拿到包含img的连接和<a>标签
        Elements elements = document.select(".img");
//        System.out.println(elements);

        for (Element element : elements) {
            AntiCorruptionEntity antiCorruptionEntity = new AntiCorruptionEntity();
            Elements aElements = element.select("a");
//            System.out.println(aElements.attr("href"));

            /**/
            //拿到在线播放的地址
            antiCorruptionEntity.setOnlineVedioUrl(aElements.attr("href"));
            String imageRelativeUrl = element.select("img").attr("src");

            //拿到img的绝对地址
            String imageAbsoluteUrl = startUrl.substring(0, startUrl.indexOf("index")) + imageRelativeUrl.substring(imageRelativeUrl.indexOf("./") + 2);
            antiCorruptionEntity.setImageUrl(imageAbsoluteUrl);
            DataStoreWrapper.antiCorruptionEntitySet.add(antiCorruptionEntity);

        }

//        System.out.println(DataStoreWrapper.antiCorruptionEntitySet.size());


        for (AntiCorruptionEntity temp : DataStoreWrapper.antiCorruptionEntitySet) {
            String onlineVedioUrl = temp.getOnlineVedioUrl();
            //拿到在线播放的HtmlPage对象
            HtmlPage onlineVedioUrlHtmlPage = HtmlPageFactory.getInstance(onlineVedioUrl);

            Document onlineVedioUrlDocument = Jsoup.parse(onlineVedioUrlHtmlPage.asXml());
            Elements chatContent = onlineVedioUrlDocument.select(".text");
            //将聊天内容设置进来
            temp.setChatcontent(chatContent.toString());
            Elements titleElements = onlineVedioUrlDocument.select("h3");
            temp.setTitle(titleElements.toString());
        }
        System.out.println(DataStoreWrapper.antiCorruptionEntitySet.size());
        for (AntiCorruptionEntity antiCorruptionEntity : DataStoreWrapper.antiCorruptionEntitySet) {
            System.out.println(antiCorruptionEntity.getChatcontent());
            System.out.println(antiCorruptionEntity.getImageUrl());
            System.out.println(antiCorruptionEntity.getOnlineVedioUrl());
            System.out.println(antiCorruptionEntity.getTitle());
        }
/*
        System.out.println(DataStoreWrapper.antiCorruptionEntitySet);*/
        InsertVideoUrlTodb.insertDataToDb(DataStoreWrapper.antiCorruptionEntitySet);
    }


}
