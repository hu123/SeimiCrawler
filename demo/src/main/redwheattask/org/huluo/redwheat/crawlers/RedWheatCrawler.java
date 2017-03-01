package org.huluo.redwheat.crawlers;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Response;
import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;
import cn.wanghaomiao.xpath.model.JXDocument;
import cn.wanghaomiao.xpath.model.JXNode;

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
//        String xpathString = "//meta[@name='keywords']";
        String xpathString = "//*[@id=\"nav\"]/li[6]/div/div[3]";
//        String xpathString = "//meta";
        try {
            List<Object> list = jxDocument.sel(xpathString);
            for (Object obj : list) {
                String temp = obj.toString();
                if (temp.contains("/focus-") && !temp.contains("_blank")) {
                    System.out.println(obj);
                }
            }
        } catch (XpathSyntaxErrorException e) {
            e.printStackTrace();
        }

    }
}
