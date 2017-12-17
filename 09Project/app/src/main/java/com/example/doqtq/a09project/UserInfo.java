package com.example.doqtq.a09project;

/**
 * Created by doqtq on 2017-11-29.
 */

public class UserInfo {
    private String id;
    private String name;
    private String phone;
    private String address;
    private String token;

    UserInfo(String id, String name, String phone, String address, String token){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.token = token;
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
    void setToken(String token){ this.token = token; }
    String getToken(){ return token; }
}
