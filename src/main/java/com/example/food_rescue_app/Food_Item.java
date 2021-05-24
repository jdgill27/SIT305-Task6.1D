package com.example.food_rescue_app;

public class Food_Item {
    public String image, title, desc, date, pick_up_time, quantity, location;

    public Food_Item(){}

    public Food_Item( String title, String desc, String date, String pick_up_time, String quantity, String location) {
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.pick_up_time = pick_up_time;
        this.quantity = quantity;
        this.location = location;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getDate() {
        return date;
    }

    public String getPick_up_time() {
        return pick_up_time;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getLocation() {
        return location;
    }
}
