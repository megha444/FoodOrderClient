package com.meghaagarwal.foodorderclient;

public class Food{

    String name, price, desc, image;

    public Food(){}

    public Food(String name, String desc, String price)
    {
        this.name = name;
        this.desc = desc;
        this.price = price;
    }

    public Food(String name, String desc, String price, String image)
    {
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

 }
