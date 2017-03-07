package org.huluo.crawlers;

import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Response;
import cn.wanghaomiao.xpath.model.JXDocument;
import org.huluo.dbutil.InsertVideoUrlTodb;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;


/*
对该页面进行抓取http://www.bjsupervision.gov.cn/video/sp/
 */
@cn.wanghaomiao.seimi.annotation.Crawler(name = "crawler1")
public class Crawler1 extends BaseSeimiCrawler {

    private static final String startUrl = "http://www.bjsupervision.gov.cn/video/sp/";

    private static final String startUrl1 = "http://www.bjsupervision.gov.cn/video/sp/index_1.html";
    private static final String startUrl2 = "http://www.bjsupervision.gov.cn/video/sp/index_2.html";
    private static final String startUrl3 = "http://www.bjsupervision.gov.cn/video/sp/index_3.html";
    private static final String startUrl4 = "http://www.bjsupervision.gov.cn/video/sp/index_4.html";
    private static final String startUrl5 = "http://www.bjsupervision.gov.cn/video/sp/index_5.html";
    private static final String startUrl6 = "http://www.bjsupervision.gov.cn/video/sp/index_6.html";
    private static final String startUrl7 = "http://www.bjsupervision.gov.cn/video/sp/index_7.html";
    private static final String startUrl8 = "http://www.bjsupervision.gov.cn/video/sp/index_8.html";

    @Override
    public String[] startUrls() {
        return new String[]{startUrl};
    }

    @Override
    public void start(Response response) {
        JXDocument jxDocument = response.document();
        /* */
        try {
            Document document = Jsoup.connect(startUrl).get();
            Elements elements = document.select(".mh_list");
            Elements aElements = elements.select("a");
            Set<String> set = new HashSet<>();
            for (Element temp : aElements) {
                set.add(startUrl + temp.attr("href").substring(temp.attr("href").indexOf(".") + 2));
            }
            for(Iterator<String> iter = set.iterator(); iter.hasNext();) {
                System.out.println(iter.next());
            }


            method(set);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    //从指定的页面上拉取视频的源地址
    public void method(Set<String> set) {


        for(Iterator<String> iter =  set.iterator();iter.hasNext();) {
            try {
                Document document = Jsoup.connect(iter.next()).get();
                Elements elements = document.select("script");
                Element element = elements.get(2);
                String string = element.html();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(string.getBytes());
                InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String aa;
                while ((aa = bufferedReader.readLine()) != null) {
                    if (aa.contains(".mp4")) {
                        String goal = "http://" + aa.substring(aa.indexOf("www"), aa.lastIndexOf(".mp4")) + ".mp4";
                        System.out.println(goal);

                        InsertVideoUrlTodb.insertUrlTdb(UUID.randomUUID().toString().replace("-","").trim(),goal);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
