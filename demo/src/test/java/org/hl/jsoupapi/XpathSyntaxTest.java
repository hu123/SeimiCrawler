package org.hl.jsoupapi;

import cn.wanghaomiao.xpath.model.JXDocument;
import org.junit.Test;

import java.util.List;

public class XpathSyntaxTest {
    @Test
    public void testName() throws Exception {
        //xml文档以字符串的形式给出
        JXDocument jxDocument = new JXDocument("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><bookstore><book><title lang=\"英语\">Harry Potter</title><price>29.99</price></book><book><title lang=\"汉语\">Learning XML</title><price>39.95</price></book></bookstore>");
        //找到所有有lang属性的节点元素
        List<Object> list = jxDocument.sel("//@lang");
        //找到属性值,并打印出来
        for (Object temp : list) {
            if (temp instanceof String) {
                System.out.println(temp);
            }
        }
    }
}
