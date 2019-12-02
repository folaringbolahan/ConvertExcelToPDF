/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itext.pdf.model;

/**
 *
 * @author emmanuel.idoko
 */
public class EmailPlaceholder {
    private String first_name;
    private String last_name;
    private String username;
    private String password;
    private String coop_name;
    private String email_address;
    private String loan_status;
    private String coop_admin_comment;
    private String tnx;
    private double amount;
    private String desc;
    private double balance;
    private String transaction_date;

    public EmailPlaceholder() {
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCoop_name() {
        return coop_name;
    }

    public void setCoop_name(String coop_name) {
        this.coop_name = coop_name;
    }

    public String getLoan_status() {
        return loan_status;
    }

    public void setLoan_status(String loan_status) {
        this.loan_status = loan_status;
    }

    public String getCoop_admin_comment() {
        return coop_admin_comment;
    }

    public void setCoop_admin_comment(String coop_admin_comment) {
        this.coop_admin_comment = coop_admin_comment;
    }

    public String getTnx() {
        return tnx;
    }

    public void setTnx(String tnx) {
        this.tnx = tnx;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }
    
    
}
