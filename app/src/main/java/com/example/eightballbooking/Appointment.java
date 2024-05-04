package com.example.eightballbooking;

import com.google.firebase.Timestamp;

public class Appointment {
    private String id;
    private Timestamp date;
    private String userId;
    private String userEmail;
    private String userName;
    private String userPhone;
    private int tableId;

    // Üres konstruktor a Firebase számára
    public Appointment() {
    }

    // Konstruktor az osztály példányosításához
    public Appointment(String id, Timestamp date, String userId, String userEmail, String userName, String userPhone, int tableId) {
        this.id = id;
        this.date = date;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPhone = userPhone;
        this.tableId = tableId;
    }

    // Getterek és setterek
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }
}

