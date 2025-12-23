package com.example.savoryrestaurant.model;

public class MenuItem {

    public String name;
    public String price;
    public String category;
    public int imageResId; // image support

    public MenuItem(String name, String price, String category, int imageResId) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.imageResId = imageResId;
    }
}
