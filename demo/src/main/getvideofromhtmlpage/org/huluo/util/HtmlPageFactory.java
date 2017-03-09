package org.huluo.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

public abstract class HtmlPageFactory {

    //有的页面是经过js渲染生成的。
    //所以,直接将html文档请求到,html页面上有的元素会不存在,
    // 这里通过用htmlunit的形式来包装
    public static HtmlPage getInstance(String url) {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);

        webClient.setAjaxController(new NicelyResynchronizingAjaxController());

        // 2 禁用Css，可避免自动二次请求CSS进行渲染
        webClient.getOptions().setCssEnabled(false);
        // 3 启动客户端重定向
        webClient.getOptions().setRedirectEnabled(true);

        // 4 js运行错误时，是否抛出异常
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        // 5 设置超时
        webClient.getOptions().setTimeout(50000);
        try {
            HtmlPage htmlPage = webClient.getPage(url);
            return htmlPage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
