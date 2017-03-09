package org.huluo.crawlers;

import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Request;
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
import java.util.Iterator;
import java.util.Set;


/*
    从该网址http://www.ccdi.gov.cn/shipin/ffsrt/（反腐三人谈）爬取,
    表结构
    imgUrl onlineVedioUrl title  chatcontent
 */
@cn.wanghaomiao.seimi.annotation.Crawler(name = "crawler2")
public class Crawler2 extends BaseSeimiCrawler {

    private static final String startUrl = "http://www.ccdi.gov.cn/shipin/ffsrt/";


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
            String imgAbsoluteUrl = startUrl + imgRelativeUrl.substring(imgRelativeUrl.indexOf(".") + 1);
            antiCorruptionEntity.setImageUrl(imgAbsoluteUrl);
            antiCorruptionEntity.setOnlineVedioUrl(aElement.attr("href"));
            DataStoreWrapper.antiCorruptionEntitySet.add(antiCorruptionEntity);
        }

/*
        for (Iterator<AntiCorruptionEntity> iter = DataStoreWrapper.antiCorruptionEntitySet.iterator(); iter.hasNext(); ) {

            System.out.println(iter.next().getImageUrl());
        }
        */
        this.completeAllDataCatch(DataStoreWrapper.antiCorruptionEntitySet);

        InsertVideoUrlTodb.insertDataToDb(DataStoreWrapper.antiCorruptionEntitySet);
    }

    //经过此方法的调用后,DataStoreWrapper里的set集合里的对象里的成员变量只有imageUrl和onlineVedioUrl被赋值
    public void addDataToDataStoreWrapper(String url) {
        Document document = Jsoup.parse(HtmlPageFactory.getInstance(url).asXml());
        Elements elements = document.select(".img");
        Elements aElements = elements.select("a");
        for (Element aElement : aElements) {

            AntiCorruptionEntity antiCorruptionEntity = new AntiCorruptionEntity();
            String imgRelativeUrl = aElement.select("img").attr("src");
            String imgAbsoluteUrl = startUrl + imgRelativeUrl.substring(imgRelativeUrl.indexOf(".") + 2);
            antiCorruptionEntity.setImageUrl(imgAbsoluteUrl);
            antiCorruptionEntity.setOnlineVedioUrl(aElement.attr("href"));
            DataStoreWrapper.antiCorruptionEntitySet.add(antiCorruptionEntity);
        }
    }


    public void completeAllDataCatch(Set<? extends AntiCorruptionEntity> set) {
        for (AntiCorruptionEntity antiCorruptionEntity : set) {
            String upperLevelUrl = antiCorruptionEntity.getOnlineVedioUrl();

            System.out.println(upperLevelUrl);
//            http://v.ccdi.gov.cn/ffsrt/zfffgffzhjjfschl/index.shtml
//            http://v.ccdi.gov.cn/ffsrt//zfffgffzhjjfschl/wzzb/index.shtml  (ajax接口)
            StringBuffer stringBuffer = new StringBuffer(upperLevelUrl);
            stringBuffer.insert(upperLevelUrl.indexOf("ffsrt") + 5, "/");
            stringBuffer.insert(upperLevelUrl.indexOf("index"), "/wzzb");
            String ajaxInterface = stringBuffer.toString();
            Document document = Jsoup.parse(HtmlPageFactory.getInstance(ajaxInterface).asXml());

            antiCorruptionEntity.setChatcontent(document.select(".block4").toString());
            try {

                Document doc = Jsoup.connect(upperLevelUrl).get();

                Elements title = doc.select(".f12_0044");
                antiCorruptionEntity.setTitle(title.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
