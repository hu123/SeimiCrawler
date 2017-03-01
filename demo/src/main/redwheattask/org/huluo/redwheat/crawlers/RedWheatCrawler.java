package org.huluo.redwheat.crawlers;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
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


*/
@Crawler(name = "redWheatCrawler")
public class RedWheatCrawler extends BaseSeimiCrawler {

    @Override
    public String[] startUrls() {

        return new String[]{"http://www.soften.cn/focus.html"};
    }

    @Override
    public void start(Response response) {
        JXDocument jxDocument = response.document();
        try {
            //jsoup不支持一些原生的字符串的函数,所以这里的写法变了
            List<Object> list = jxDocument.sel("//a[@href*='focus-']");
            for (Object obj : list) {
                if (obj instanceof Element) {
                    System.out.println(((Element) obj).html());
                }
            }
        } catch (XpathSyntaxErrorException e) {
            e.printStackTrace();
        }

    }
}
