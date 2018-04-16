package com.matt.bookapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Comments {

    FirebaseUser user;
    String comment;
    int stars;

    public Comments(){
    }

    public Comments(String comment, int stars){
        user = FirebaseAuth.getInstance().getCurrentUser();
        this.comment = comment;
        this.stars = stars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }
}
