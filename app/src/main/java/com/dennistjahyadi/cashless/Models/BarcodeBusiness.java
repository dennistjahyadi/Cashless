package com.dennistjahyadi.cashless.Models;

/**
 * Created by Denn on 10/20/2017.
 */

public class BarcodeBusiness {

    private String id;
    private String fk_users;
    private String business_name;
    private String business_desc;
    private String receiverFullname;
    private String receiverEmail;
    private Integer amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiverFullname() {
        return receiverFullname;
    }

    public void setReceiverFullname(String receiverFullname) {
        this.receiverFullname = receiverFullname;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getFk_users() {
        return fk_users;
    }

    public void setFk_users(String fk_users) {
        this.fk_users = fk_users;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getBusiness_desc() {
        return business_desc;
    }

    public void setBusiness_desc(String business_desc) {
        this.business_desc = business_desc;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
