package com.queen.kartish.queen;

public class ProductModel {

    private String ad_details,discount,discounted_price,brand,ad_title,product_no,wish_no,userid;
    public String price,size,quantity,image1;

    public String getDetails() {
        return ad_details;
    }

    public void setDetails(String ad_details) {
        this.ad_details = ad_details;
    }

    public String getUser() {
        return userid;
    }

    public void setUser(String userid) {
        this.userid = userid;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getWish() {
        return wish_no;
    }

    public void setWish(String wish_no) {
        this.wish_no = wish_no;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductno() {
        return product_no;
    }

    public void setProductno(String product_no) {
        this.product_no = product_no;
    }

    public String getTitle() {
        return ad_title;
    }

    public void setTitle(String ad_title) {
        this.ad_title = ad_title;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscount_price() {
        return discounted_price;
    }

    public void setDiscount_price(String discounted_price) {
        this.discounted_price = discounted_price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl1() {
        return image1;
    }

    public void setUrl1(String image1) {
        this.image1 = image1;
    }

}

