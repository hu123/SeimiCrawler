package org.huluo.redwheat.crawlers;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;
import cn.wanghaomiao.xpath.model.JXDocument;
import cn.wanghaomiao.xpath.model.JXNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

/*

    解析title的xpath表达式://div[@class='proDtit']/h1/text()

    日期和标签的值合在一起的xpath表达式://div[@class='proDtit']/span/text()

    页面下一页的超链接元素 xpath表达式 String xpathString = "//a[text()='>']";
*/
@Crawler(name = "redWheatCrawler")
public class RedWheatCrawler extends BaseSeimiCrawler {

    private static String startUrl = "http://www.soften.cn/focus.html";
    private static String baseUrl = "";

    @Override
    public String[] startUrls() {
        baseUrl = startUrl.substring(0, startUrl.lastIndexOf("/"));
        System.out.println(baseUrl);
        return new String[]{startUrl};
    }

    @Override
    public void start(Response response) {
        /*
        //获取超链接地址包含 focus-的元素
        JXDocument jxDocument = response.document();
        try {
            //jsoup不支持一些原生的字符串的函数,所以这里的写法变了
            //超链接的标签href属性包含focus-的标签元素
            List<Object> list = jxDocument.sel("//a[@href*='focus-']");

            for (Object obj : list) {
                System.out.println(baseUrl + ((Element) obj).attr("href"));
                push(new Request(baseUrl + ((Element) obj).attr("href"),"myCallBack"));

            }
        } catch (XpathSyntaxErrorException e) {
            e.printStackTrace();
        }
        */


        /*
        拿到下一页的页面元素  >
     */

        JXDocument jxDocument = response.document();
        String xpathString = "//a[text()='>']";
        try {
            List<Object> list = jxDocument.sel(xpathString);
            for (Object obj : list) {
                System.out.println(obj);
            }
        } catch (XpathSyntaxErrorException e) {
            e.printStackTrace();
        }


    }





    public void myCallBack(Response response) {
        JXDocument doc = response.document();

        String xpathString = "//span[text()^='发布日期']";

        try {
            List<Object> list = doc.sel(xpathString);
            for (Object obj : list) {
                System.out.println(obj);
            }
        } catch (XpathSyntaxErrorException e) {
            e.printStackTrace();
        }
    }
}
