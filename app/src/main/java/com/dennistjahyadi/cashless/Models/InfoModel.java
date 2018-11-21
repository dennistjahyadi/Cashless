package com.dennistjahyadi.cashless.Models;

/**
 * Created by Denn on 7/2/2017.
 */

public class InfoModel {
    private Integer id;
    private String infodesc;
    private String stat;
    private String category;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInfodesc() {
        return infodesc;
    }

    public void setInfodesc(String infodesc) {
        this.infodesc = infodesc;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
