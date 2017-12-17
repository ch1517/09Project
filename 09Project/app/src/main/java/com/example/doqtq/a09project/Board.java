package com.example.doqtq.a09project;

import java.io.Serializable;

/**
 * Created by doqtq on 2017-11-30.
 */
//class putExtra하기 위해서
public class Board implements Serializable{
    private String idx; // 글 인덱스
    private String id; // 아이디
    private String title; // 글 제목
    private int price; // 가격
    private String content; // 글 내용
    private String photo; // 사진
    private int ordernum; //  총 공구 수
    private int ordernum2; // 주문한 사람 명수
    private int hit; // 조회수
    private String date; // 날짜

    public Board(String idx, String id, String title, int price, String content, String photo, int ordernum, int ordernum2, int hit, String date) {
        this.idx = idx;
        this.id = id;
        this.title = title;
        this.price = price;
        this.content = content;
        this.photo = photo;
        this.ordernum = ordernum;
        this.ordernum2 = ordernum2;
        this.hit = hit;
        this.date = date;
    }

    public int getOrdernum2() {
        return ordernum2;
    }

    public void setOrdernum2(int ordernum2) {
        this.ordernum2 = ordernum2;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(int ordernum) {
        this.ordernum = ordernum;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
