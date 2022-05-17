package com.example.simpletasks.domain.login;

import android.util.Log;
/**
 * User class is used to model the logged in or out states of a user.
 * Implemented as a thread safe singleton as only one user needs to be logged in
 * at a time.
 */
public class User {
    private static final String TAG = "User";
    // Track the loggedIn state of the user
    private boolean loggedIn;

    // Single instance
    private static volatile User USER;

    private User(){
        this.loggedIn = false;
    }

    /**
     * Get the instance reference of the singleton user
     * @return instance reference for the single user instance
     */
    public static User getUser(){
        // Optimization; don't always access the volatile user instance
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

    /**
     * Set the users loggedIn state to true (= logged in)
     */
    public void logIn(){
        Log.d(TAG, "user logged in");
        this.loggedIn = true;
    }

    /**
     * Set the users loggedIn state to false (= logged out)
     */
    public void logOut(){
        Log.d(TAG, "user logged out");
        this.loggedIn = false;
    }

    /**
     * Get the current loggedIn state of the user
     *
     * @return true if logged in, false otherwise
     */
    public boolean isLoggedIn(){return loggedIn;}
}
