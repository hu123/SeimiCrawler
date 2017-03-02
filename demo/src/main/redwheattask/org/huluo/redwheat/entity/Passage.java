package org.huluo.redwheat.entity;


import cn.wanghaomiao.seimi.annotation.Xpath;

public class Passage {

    private Integer id ;

    @Xpath("//div[@class='proDtit']/h1/text()")
    private String title;

    @Xpath("//div[@class='proDtit']/span/text()")
    private String dateAndTag ;

    private int currentPage;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateAndTag() {
        return dateAndTag;
    }

    public void setDateAndTag(String dateAndTag) {
        this.dateAndTag = dateAndTag;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
