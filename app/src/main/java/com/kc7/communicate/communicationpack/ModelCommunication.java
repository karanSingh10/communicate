package com.kc7.communicate.communicationpack;

import com.google.firebase.firestore.PropertyName;

public class ModelCommunication {

    String message, time;
    Boolean sendorrecieve;

    public ModelCommunication(String message, String time, Boolean sendorrecieve){
        this.message = message;
        this.time = time;
this.sendorrecieve = sendorrecieve;

    }

    public ModelCommunication() {
        //Empty constructor needed
    }
    @PropertyName("message")
    public String getmessage() {
        return message;
    }
    @PropertyName("message")
    public void setmessage(String message) {
        this.message = message;
    }
    @PropertyName("time")
    public String gettime() {
        return time;
    }
    @PropertyName("time")
    public void settime(String time) {
        this.time = time;
    }
    @PropertyName("send")
    public Boolean getsendorrecieve() {
        return sendorrecieve;
    }
    @PropertyName("send")
    public void setSendorrecieve(Boolean sendorrecieve) {
        this.sendorrecieve = sendorrecieve;
    }

}
