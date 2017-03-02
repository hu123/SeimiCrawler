package org.huluo.redwheat.crawlers;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;
import cn.wanghaomiao.xpath.model.JXDocument;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Crawler(name = "testCrawler")
public class TestCrawler extends BaseSeimiCrawler {

    private static final Logger log = LoggerFactory.getLogger(TestCrawler.class);
    private static String startUrl = "http://www.soften.cn/focus.html";
    private static String baseUrl = "";

    @Override
    public String[] startUrls() {
        baseUrl = startUrl.substring(0, startUrl.lastIndexOf("/"));
        System.out.println(baseUrl);
        return new String[]{startUrl, startUrl};
    }

    @Override
    public void start(Response response) {
        JXDocument startDocument = response.document();
        String nextPageXpathString = "//a[text()='>']";
        String concretePassageXpathString = "//a[@href*='focus-']";


        /*
        //抓取具体的文章的东西
        try {
            List<Object> concretePassageRelativeUrllist = startDocument.sel(concretePassageXpathString);
            for (Object obj : concretePassageRelativeUrllist) {
                if (obj instanceof Element) {
                    String concretePassageAbsoluteUrl = baseUrl + ((Element) obj).attr("href");
                    push(new Request(concretePassageAbsoluteUrl, "concreteCatch"));
                }
            }
        } catch (XpathSyntaxErrorException e) {
            e.printStackTrace();
        }

*/
        //抓到下一页的页面
        try {
            List<Object> nextPageLinkList = startDocument.sel(nextPageXpathString);

            Element nextPageLinkElement = (Element) nextPageLinkList.get(0);

            String nextPathRelativePathHrefValue = nextPageLinkElement.attr("href");
            log.warn(nextPathRelativePathHrefValue);
            System.out.println(baseUrl + nextPathRelativePathHrefValue);
            super.push(new Request(baseUrl + nextPathRelativePathHrefValue, "getNextPageLinkRecursively"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //用于爬取大纲性质的东西,
    //如下一页,各条新闻的url
    public void outlineCatch(Response response) {
        JXDocument anotherStartDocument = response.document();


    }


    //递归性的拿到下一页的超链接元素  ,第一页-> 第二页 -> 第三页
    public void getNextPageLinkRecursively(Response response) {
        JXDocument jxDocument = response.document();

        System.out.println("回调得到了执行");
        try {
            List<Object> list = jxDocument.sel("//a[text()='>']");
            if (list.size() > 0) {
                Element element = (Element) list.get(0);
                String nextPageRelativeUrl = element.attr("href");
                String nextPageAbsoluteUrl = baseUrl + nextPageRelativeUrl;
                System.out.println(nextPageAbsoluteUrl);
                push(new Request(nextPageAbsoluteUrl, "tryConcretePassageAgain"));
                push(new Request(nextPageAbsoluteUrl, "getNextPageLinkRecursively"));
            }
        } catch (XpathSyntaxErrorException e) {
            e.printStackTrace();
        }

    }


    //从下一页得到的start的html页面进行解析
    public void tryConcretePassageAgain(Response response) {
        JXDocument jxDocument = response.document();
        try {
            List<Object> list = jxDocument.sel("//a[@href*='focus-']");

            for (Object object : list) {
                if (object instanceof Element) {
                    String absoluteUrl = baseUrl + ((Element) object).attr("href");
//                    System.out.println("绝对地址" + absoluteUrl);
                    String url = response.getUrl();
                    Integer integer = Integer.valueOf(url.substring(url.lastIndexOf("/") + 1));
                    Integer pageNumer = integer / 10 + 1;
                    //将当前的页数放进map中
                    Map<String, String> currentPage = new HashMap<>();
                    currentPage.put("currentPage", String.valueOf(pageNumer));
                    Request request = new Request(absoluteUrl, "concreteCatch");
                    //将当前的页码信息放入request对象中
                    request.setMeta(currentPage);
                    push(request);
                }
            }
        } catch (XpathSyntaxErrorException e) {
            e.printStackTrace();
        }
    }




    //用于爬取具体的内容
    public void concreteCatch(Response response) {
        System.out.println("再一次请求" + response.getUrl());
        JXDocument concretePassageDocument = response.document();
        /*
        String dateAndTagXpathExpression = "//div[@class='proDtit']/span/text()";
        try {
            List<Object> dateAndTagList = concretePassageDocument.sel(dateAndTagXpathExpression);
            Element dateAndTagElement = (Element) dateAndTagList.get(0);
            System.out.println(dateAndTagElement);

        } catch (XpathSyntaxErrorException e) {
            e.printStackTrace();
        }
        */
        try {
            System.out.println(concretePassageDocument.sel("//div[@class='proDtit']/span/text()").get(0));
            System.out.println(concretePassageDocument.sel("//div[@class='proDtit']/h1/text()").get(0));
            System.out.println(response.getUrl() + "当前的连接de页码在" + response.getRequest().getMeta().get("currentPage"));
        } catch (XpathSyntaxErrorException e) {
            e.printStackTrace();
        }

    }
}
