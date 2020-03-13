package com.example.fifaking;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {

    private String position, uid, firstname, lastname, email, password, admin, wins, loses, draw;

    private DatabaseReference mReference;

    public User() { } // Default constructor required for calls to DataSnapshot.getValue(User.class)

    public User(String position, String uid, String firstname, String lastname, String email, String password) {
        this.position = position;
        this.uid = uid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.admin = "0";
        this.wins = "0";
        this.loses = "0";
        this.draw = "0";

    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public String getLoses() {
        return loses;
    }

    public void setLoses(String loses) {
        this.loses = loses;
    }

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public String toString() {
        return getFirstname() + " " + getLastname() + ", Win: " + getWins() + ", Lose: " + getLoses() + ", Draw: " + getDraw();
    }

    public void updateUser(String position, String uid, String firstname, String lastname, String email, String password, String admin, String wins, String loses, String draw) {
        User newUser = new User();
        newUser.setPosition(position);
        newUser.setUid(uid);
        newUser.setFirstname(firstname);
        newUser.setLastname(lastname);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setAdmin(admin);
        newUser.setWins(wins);
        newUser.setLoses(loses);
        newUser.setDraw(draw);
        mReference = FirebaseDatabase.getInstance().getReference("Users").child("profile").child(uid);
        mReference.setValue(newUser);
    }
}
