import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class HtmlUnitDemo1 {

    //    private static final String url = "http://www.bjsupervision.gov.cn/video/sp/xwbb/201701/t20170120_30664.html";
    private static final String url = "http://v.ccdi.gov.cn/ffsrt//zfffgffzhjjfschl/wzzb/index.shtml";


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

        List list = htmlPage.getByXPath("//div[@class='block4']");
        System.out.println();
        // 等待JS驱动dom完成获得还原后的网页
        webClient.waitForBackgroundJavaScript(10000);

        // 网页内容

        Document document = Jsoup.parse(htmlPage.asXml());

        Elements elements = document.select(".block4");

        System.out.println(elements);
//        System.out.println(document);
        webClient.closeAllWindows();
    }
}
