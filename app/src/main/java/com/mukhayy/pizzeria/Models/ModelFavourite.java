package com.mukhayy.pizzeria.Models;

public class ModelFavourite {
    String userid;
    String foodid;

    public ModelFavourite(){

    }

    public ModelFavourite(String userid, String foodid) {
        this.userid = userid;
        this.foodid = foodid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }
}
