package org.huluo.crawlers;

import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Response;

public class Crawler5 extends BaseSeimiCrawler {
    private static final String startUrl = "http://www.ccdi.gov.cn/shipin/ffsrt/index_3.html";

    @Override
    public String[] startUrls() {
        return new String[0];
    }

    @Override
    public void start(Response response) {

    }
}
