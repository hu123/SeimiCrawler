package org.huluo.redwheat.crawlers;

import cn.wanghaomiao.dao.mybatis.PassageDao;
import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;
import cn.wanghaomiao.xpath.model.JXDocument;
import org.huluo.redwheat.entity.Passage;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Crawler(name = "testCrawler1")
public class TestCrawler1 extends BaseSeimiCrawler {

    @Autowired
    private PassageDao passageDao;
    private static final Logger log = LoggerFactory.getLogger(TestCrawler1.class);
    private static String startUrl = "http://www.soften.cn/focus-1.html";
    private static String baseUrl = "http://www.soften.cn";
    private static  String nextPassageRelativeUrlXpath = "//b[text()^='下一篇']/parent::*";

    @Override
    public String[] startUrls() {
        return new String[]{startUrl};
    }

    @Override
    public void start(Response response) {
        JXDocument jxDocument = response.document();
        //抓取第一个页面的title 日期、标签以及正文
        try {
            Passage passage = response.render(Passage.class);
            passageDao.save(passage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //选取下一篇的超链接元素
        try {
            List<Object> list = jxDocument.sel(nextPassageRelativeUrlXpath);
            Element element = (Element) list.get(0);
            String absoluteNextPageUrl = baseUrl + element.attr("href");
            push(new Request(absoluteNextPageUrl, "catchrecursively"));
        } catch (XpathSyntaxErrorException e) {
            e.printStackTrace();
        }
    }

    //每一次寻找下一篇的超链接按钮.
    //递归的爬取数据
    public void catchrecursively(Response response) {
        JXDocument jxDocument = response.document();
        try {
            Passage passage = response.render(Passage.class);
            passageDao.save(passage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            List<Object> list = jxDocument.sel(nextPassageRelativeUrlXpath);
            if (list.size() > 0) {
                Element element = (Element) list.get(0);
                String nextPageRelativeUrl = element.attr("href");
                String nextPageAbsoluteUrl = baseUrl + nextPageRelativeUrl;
                push(new Request(nextPageAbsoluteUrl,"catchrecursively"));
            }
        } catch (XpathSyntaxErrorException e) {
            e.printStackTrace();
        }
    }
}
