package com.batch.model;

public class User {
    private String ver;
    private String product_family;
    private String country;
    private String os;


    public User() {
    }

    public User(String ver, String product_family, String country, String os) {
        this.ver = ver;
        this.product_family = product_family;
        this.country = country;
        this.os = os;

    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getProduct_family() {
        return product_family;
    }

    public void setProduct_family(String product_family) {
        this.product_family = product_family;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
}
