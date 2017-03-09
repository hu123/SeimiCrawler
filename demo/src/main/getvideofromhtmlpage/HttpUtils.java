import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created with IntelliJ IDEA.
 * User: lsz
 * Date: 14-4-22
 * Time: 下午1:17
 * utils for http
 */

//参考网址http://www.oicqzone.com/pc/2014091319763.html
public class HttpUtils {
    public static String getAjaxCotnent(String url) throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec("F:/phantomjs/phantomjs-2.1.1-windows/phantomjs-2.1.1-windows/bin/phantomjs.exe F:/phantomjs/codes.js " + url);
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        InputStream is = p.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuffer sbf = new StringBuffer();
        String tmp = "";
        while ((tmp = br.readLine()) != null) {
            sbf.append(tmp);
        }
        System.out.println(sbf.toString());
        return sbf.toString();
    }

    public static void main(String[] args) throws IOException {
        getAjaxCotnent("http://v.ccdi.gov.cn/ffsrt/hnrzsxyg/index.shtml");
    }
}   