package com.example.simpletasks.domain.user;

public class User {
    private boolean loggedIn;
    private static volatile User USER;

    private User(){
        this.loggedIn = false;
    }

    public static User getUser(){
        User result = USER;

        if (result == null) {
            synchronized (User.class) {
                result = USER;
                if (result == null) {
                    USER = result = new User();
                }
            }
        }

        return result;
    }


    public void logIn(){
        this.loggedIn = true;
    }

    public void logOut(){
        this.loggedIn = false;
    }

    public boolean isLoggedIn(){return loggedIn;}
}
