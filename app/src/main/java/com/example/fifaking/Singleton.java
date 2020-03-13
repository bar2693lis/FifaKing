package com.example.fifaking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Singleton {

    private static Singleton firstInstance = null;

    private ArrayList<String> userUidAndPosition;
    private ArrayList<String> usersArrayList;

    private DatabaseReference databaseReference;

    private int userPosition;

    private Singleton() {

    }

    public static Singleton getInstance() {

        synchronized(Singleton.class){
            if(firstInstance == null) {
                firstInstance = new Singleton();
            }
        }
        return firstInstance;
    }

    public String getUserUidAndPosition(String position)
    {
        userUidAndPosition = new ArrayList<String>();
        listOfUsers(4);
        int iend;
        String uid = null;
        String[] userUidAndPositionArray = userUidAndPosition.toArray(new String[0]);
        for(int i = 0 ; i < userUidAndPositionArray.length ; i++)
        {
            iend = userUidAndPositionArray[i].indexOf(":");
            if(userUidAndPositionArray[i].substring(0 , iend).equals(position))
            {
                uid = userUidAndPositionArray[i].substring(iend+1 , userUidAndPositionArray[i].length());
            }
        }
        return uid;
    }

    private void listOfUsers(final int x)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("profile");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(x == 1)
                {
                    String position = dataSnapshot.getValue(User.class).getPosition();
                    String wins = dataSnapshot.getValue(User.class).getWins();
                    String loses = dataSnapshot.getValue(User.class).getLoses();
                    usersArrayList.add(position + ":" + wins + "-" + loses);
                }
                else if(x == 2)
                {
                    String position = dataSnapshot.getValue(User.class).getPosition();
                    String firstname = dataSnapshot.getValue(User.class).getFirstname();
                    String lastname = dataSnapshot.getValue(User.class).getLastname();
                    usersArrayList.add(position + ": " + firstname + " " + lastname);
                }
                else if(x == 3)
                {
                    userPosition = (int) dataSnapshot.getChildrenCount();
                }
                else if(x == 4)
                {
                    String uid = dataSnapshot.getValue(User.class).getUid();
                    String position = dataSnapshot.getValue(User.class).getPosition();

                    userUidAndPosition.add(position + ": " + uid);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public int getPos ()
    {
        listOfUsers(3);
        return userPosition;
    }

    public ArrayList<String> getListOfUsers(int x)
    {
        usersArrayList = new ArrayList<>();
        listOfUsers(x);
        return usersArrayList;
    }

    public void addingScore(String userPosition, final int win, final int lose, final int draw) {
        String uid = getUserUidAndPosition(userPosition);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("profile").child(uid);
        final int[] i = {0};
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String position = dataSnapshot.getValue(User.class).getPosition();
                String uid = dataSnapshot.getValue(User.class).getUid();
                String firstname = dataSnapshot.getValue(User.class).getFirstname();
                String lastname = dataSnapshot.getValue(User.class).getLastname();
                String email = dataSnapshot.getValue(User.class).getEmail();
                String passwoard = dataSnapshot.getValue(User.class).getPassword();
                String admin = dataSnapshot.getValue(User.class).getAdmin();

                String wins = dataSnapshot.getValue(User.class).getWins();
                int w = Integer.parseInt(wins);
                w += win;

                String loses = dataSnapshot.getValue(User.class).getLoses();
                int l = Integer.parseInt(loses);
                l += lose;

                String draws = dataSnapshot.getValue(User.class).getDraw();
                int d = Integer.parseInt(draws);
                d += draw;

                if(i[0] == 0) {
                    User newUser = new User();
                    newUser.updateUser(position, uid, firstname, lastname, email, passwoard, admin, String.valueOf(w), String.valueOf(l), String.valueOf(d));
                    i[0] = 1;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void signUpNewUserToDatabase(int userPosition, String uid, String email, String password, String firstname, String lastname) { // Register a new user into a database
        User newUser = new User(String.valueOf(++userPosition), uid, firstname, lastname, email, password);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("profile").child(uid); // path in the database to the profile
        databaseReference.setValue(newUser); // push the new user to database
    }

    public boolean checkIfAdmin(String userUid) { // Checks whether the user is admin
        final Boolean[] checkAdmin = {null};
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("profile").child(userUid).child("admin");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String admin = dataSnapshot.getValue().toString();
                if(admin.equals("1"))
                {
                    checkAdmin[0] = true;
                }
                else
                {
                    checkAdmin[0] = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return checkAdmin[0];
    }
}
