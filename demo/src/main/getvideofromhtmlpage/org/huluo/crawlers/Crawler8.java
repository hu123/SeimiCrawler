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

@cn.wanghaomiao.seimi.annotation.Crawler(name = "crawler8")
public class Crawler8 extends BaseSeimiCrawler {

    private static final String startUrl = "http://www.ccdi.gov.cn/shipin/zxft/index_15.html";

    @Override
    public String[] startUrls() {
        return new String[]{startUrl};
    }

    @Override
    public void start(Response response) {
        String url = response.getUrl();
        HtmlPage htmlPage = HtmlPageFactory.getInstance(startUrl);
        Document document = Jsoup.parse(htmlPage.asXml());
        Elements elements = document.select(".img");

//        System.out.println(elements);
        for (Element element : elements) {
            AntiCorruptionEntity antiCorruptionEntity = new AntiCorruptionEntity();
            antiCorruptionEntity.setOnlineVedioUrl(element.select("a").attr("href"));

            String imageRelativeUrl = element.select("img").attr("src");
            String imageAbsoluteUrl = startUrl.substring(0, startUrl.indexOf("index")) + imageRelativeUrl.substring(imageRelativeUrl.indexOf(".") + 2);
            antiCorruptionEntity.setImageUrl(imageAbsoluteUrl);
            DataStoreWrapper.antiCorruptionEntitySet.add(antiCorruptionEntity);
        }

        for (AntiCorruptionEntity antiCorruptionEntity : DataStoreWrapper.antiCorruptionEntitySet) {
            String onlineVedioUrl = antiCorruptionEntity.getOnlineVedioUrl();
            HtmlPage onlineVedioUrlHtmlPage = HtmlPageFactory.getInstance(onlineVedioUrl);
            Document onlineVedioUrlDocument = Jsoup.parse(onlineVedioUrlHtmlPage.asXml());
            Elements titleElements = onlineVedioUrlDocument.select("h2.f12_0044");
            antiCorruptionEntity.setTitle(titleElements.toString());

            String ajaxInterfaceUrl = onlineVedioUrl.replace("ccdi", "mos").replace("/index.shtml", "/01/index.shtml");
            HtmlPage ajaxInterfaceUrlHtmlPage = HtmlPageFactory.getInstance(ajaxInterfaceUrl);
            Document ajaxInterfaceUrlDocument = Jsoup.parse(ajaxInterfaceUrlHtmlPage.asXml());
            Elements chatContentElements = ajaxInterfaceUrlDocument.select(".block4");
            antiCorruptionEntity.setChatcontent(chatContentElements.toString());
        }

        for (AntiCorruptionEntity temp : DataStoreWrapper.antiCorruptionEntitySet) {
            System.out.println(temp.getOnlineVedioUrl());
            System.out.println(temp.getTitle());
            System.out.println(temp.getImageUrl());
            System.out.println(temp.getChatcontent());
        }

        InsertVideoUrlTodb.insertDataToDb(DataStoreWrapper.antiCorruptionEntitySet);

    }
}
