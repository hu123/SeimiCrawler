package org.huluo.crawlers;

import cn.wanghaomiao.seimi.annotation.*;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Response;

@cn.wanghaomiao.seimi.annotation.Crawler(name = "crawler4")
public class Crawler4 extends BaseSeimiCrawler{
    private static final String startUrl = "http://www.ccdi.gov.cn/shipin/ffsrt/index_2.html";

    @Override
    public String[] startUrls() {
        return new String[]{startUrl};
    }

    @Override
    public void start(Response response) {

    }
}
