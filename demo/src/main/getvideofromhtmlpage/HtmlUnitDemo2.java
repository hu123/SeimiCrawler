import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

public class HtmlUnitDemo2 {
    private static final String url = "http://www.ccdi.gov.cn/shipin/zxft/index_1.html";

    public static void main(String[] args) throws IOException {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);

        // 1 启动JS
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

        HtmlPage htmlPage = webClient.getPage(url);

        System.out.println(htmlPage.asXml());
    }
}
