package com.dennistjahyadi.cashless.Models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Denn on 10/11/2017.
 */

public class HistoryTransaction implements Serializable {
    private String id;
    private String senderUserId;
    private String senderEmail;
    private String senderFullname;
    private String receiverUserId;
    private String receiverEmail;
    private String receiverFullname;
    private String datetime;
    private String description;
    private String amount;
    private String fee;
    private String totalAmount;
    private String method;
    private String status;
    private String bank;
    private String bankName;
    private String bankAccountName;
    private String bankAccountNumber;
    private SimpleDateFormat sdfDateParse;
    private SimpleDateFormat sdfDateFormat;
    private SimpleDateFormat sdfTimeFormat;

    public HistoryTransaction() {
        this.sdfDateParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.sdfDateFormat = new SimpleDateFormat("dd MMM yyyy");
        this.sdfTimeFormat = new SimpleDateFormat("HH:mm");

    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBank() {
        return bank;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(String receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public String getSenderFullname() {
        return senderFullname;
    }

    public void setSenderFullname(String senderFullname) {
        this.senderFullname = senderFullname;
    }

    public String getReceiverFullname() {
        return receiverFullname;
    }

    public void setReceiverFullname(String receiverFullname) {
        this.receiverFullname = receiverFullname;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getDate() {
        Date date = null;
        try {
            date = sdfDateParse.parse(getDatetime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdfDateFormat.format(date);
    }

    public String getTime() {
        Date date = null;
        try {
            date = sdfDateParse.parse(getDatetime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdfTimeFormat.format(date);
    }

}
