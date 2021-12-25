package com.kc7.communicate.userlist;

import com.google.firebase.firestore.PropertyName;

public class ModelUserList {

    String name, uid, latestchat;

    public ModelUserList(String name, String uid, String latestchat){
        this.name = name;
        this.uid = uid;
this.latestchat = latestchat;

    }

    public ModelUserList() {
        //Empty constructor needed
    }
    @PropertyName("name")
    public String getname() {
        return name;
    }
    @PropertyName("name")
    public void setname(String name) {
        this.name = name;
    }
    @PropertyName("uid")
    public String getuid() {
        return uid;
    }
    @PropertyName("uid")
    public void setuid(String uid) {
        this.uid = uid;
    }
    @PropertyName("latestchat")
    public String getlatestchat() {
        return latestchat;
    }
    @PropertyName("latestchat")
    public void setlatestchat(String latestchat) {
        this.latestchat = latestchat;
    }

}
