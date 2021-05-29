package com.patrollers.breezy.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "messageDb")
public class Message {

    @PrimaryKey(autoGenerate = true)
    private long primary_key;

    private String message;
    private boolean isReceived;

    public Message(){}

    @Ignore
    public Message(String message, boolean isReceived) {
        this.message = message;
        this.isReceived = isReceived;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(boolean isReceived) {
        this.isReceived = isReceived;
    }

    public long getPrimary_key() {
        return primary_key;
    }

    public void setPrimary_key(long primary_key) {
        this.primary_key = primary_key;
    }
}