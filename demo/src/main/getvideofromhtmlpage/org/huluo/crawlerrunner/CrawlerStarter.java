package org.huluo.crawlerrunner;

import cn.wanghaomiao.seimi.core.Seimi;
import org.huluo.crawlers.Crawler1;

public class CrawlerStarter {
    public static void main(String[] args) {
        Seimi seimi = new Seimi();
        seimi.goRun("crawler1");



    }
}
