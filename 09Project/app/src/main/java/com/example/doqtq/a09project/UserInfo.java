package com.example.doqtq.a09project;

/**
 * Created by doqtq on 2017-11-29.
 */

public class UserInfo {
    private String id;
    private String name;
    private String phone;
    private String address;
    UserInfo(String id, String name, String phone, String address){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }
    void setId(String id){
        this.id = id;
    }
    String getId(){
        return id;
    }
    void setName(String name){
        this.name = name;
    }
    String getName(){
        return name;
    }
    void setPhone(String phone){
        this.phone = phone;
    }
    String getPhone(){
        return phone;
    }
    void setAddress(String address){
        this.address = address;
    }
    String getAddress(){
        return address;
    }
}
