package com.mukhayy.pizzeria.Models;

public class Orders {

    String amount;
    String address;
    String phone;
    String name;
    String currentDateTime;
    String pizzaName;
    String price;
    String photo;
    //for unique order
    String currentDateTimeAddress;
    String id;


    public Orders() {
    }

    public Orders(String pizzaName,String amount, String address, String phone, String name, String currentDateTime, String currentDateTimeAddress, String price, String photo, String id) {
        this.amount = amount;
        this.address = address;
        this.phone = phone;
        this.name = name;
        this.currentDateTime = currentDateTime;
        this.pizzaName = pizzaName;
        this.currentDateTimeAddress = currentDateTimeAddress;
        this.price = price;
        this.photo = photo;
        this.id = id;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrentDateTimeAddress() {
        return currentDateTimeAddress;
    }

    public void setCurrentDateTimeAddress(String currentDateTimeAddress) {
        this.currentDateTimeAddress = currentDateTimeAddress;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public String getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(String currentDateTime) {
        this.currentDateTime = currentDateTime;
    }
}
