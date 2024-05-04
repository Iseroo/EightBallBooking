package com.example.eightballbooking;

import org.jetbrains.annotations.Contract;
//@Contract(pure = true)
public class User {
    public static String UUID;
    public static String name;
    public static String email;
    public static String phone;

    public User(String UUID, String name, String email, String phone) {
        User.UUID = UUID;
        User.name = name;
        User.email = email;
        User.phone = phone;
    }
    public User() {
    }

}
