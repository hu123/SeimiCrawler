package org.huluo.crawlers;

import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Response;
import org.huluo.dbutil.InsertVideoUrlTodb;
import org.huluo.entity.AntiCorruptionEntity;
import org.huluo.entity.DataStoreWrapper;
import org.huluo.util.HtmlPageFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/*
   从http://www.ccdi.gov.cn/shipin/ffsrt/index_1.html网站爬取
   表结构
    imgUrl onlineVedioUrl title  chatcontent
 */
@cn.wanghaomiao.seimi.annotation.Crawler(name = "crawler3")
public class Crawler3 extends BaseSeimiCrawler {

    private static final String startUrl = "http://www.ccdi.gov.cn/shipin/ffsrt/index_1.html";

    @Override
    public String[] startUrls() {
        return new String[]{startUrl};
    }

    @Override
    public void start(Response response) {
        String url = response.getUrl();
        Document document = Jsoup.parse(HtmlPageFactory.getInstance(startUrl).asXml());
        Elements elements = document.select(".img");
        Elements aElements = elements.select("a");

        for (Element aElement : aElements) {
            AntiCorruptionEntity antiCorruptionEntity = new AntiCorruptionEntity();
            String imgRelativeUrl = aElement.select("img").attr("src");
            //拼接img的绝对地址
            String imgAbsoluteUrl = startUrl.substring(0, startUrl.indexOf("index_1.html") - 1) + imgRelativeUrl.substring(imgRelativeUrl.indexOf(".") + 1);
            antiCorruptionEntity.setImageUrl(imgAbsoluteUrl);
            //拿到页面的url,并增加到对象里面
            antiCorruptionEntity.setOnlineVedioUrl(aElement.attr("href"));
            DataStoreWrapper.antiCorruptionEntitySet.add(antiCorruptionEntity);
        }
        for (AntiCorruptionEntity antiCorruptionEntity : DataStoreWrapper.antiCorruptionEntitySet) {
            antiCorruptionEntity.setId(UUID.randomUUID().toString());
        }

        InsertVideoUrlTodb.insertDataToDbWithoutTitleAndChatContent(DataStoreWrapper.antiCorruptionEntitySet);



    }

}