package com.example.fifaking;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class EnterResultActivity extends AppCompatActivity {

    private Button playerOneButton, playerTwoButton, returnButton, saveButton;

    private RadioGroup radioGroup1, radioGroup2;

    private RadioButton radioButton1, radioButton2;

    private TextView player1TextView, player2TextView;

    private ArrayList<String> usersArrayList = new ArrayList<>();

    private String[] usersArray;

    private String user1position, user2position;

//    private DatabaseReference databaseReference;

    private Singleton s = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_result);

        usersArrayList = s.getListOfUsers(2);

        playerOneButton = findViewById(R.id.PlayerOneButton);
        playerTwoButton = findViewById(R.id.PlayerTwoButton);
        saveButton = findViewById(R.id.SaveButton);
        returnButton = findViewById(R.id.ReturnButtonER);

        player1TextView = findViewById(R.id.PlayerTextView1);
        player2TextView = findViewById(R.id.PlayerTextView2);

        radioGroup1 = findViewById(R.id.RadioGroup1);
        radioGroup2 = findViewById(R.id.RadioGroup2);

        playerOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlayer(player1TextView, 1);
            }
        });
        playerTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlayer(player2TextView, 2);
            }
        });
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToGame();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosingWinner();
            }
        });
    }

//    private void getListOfUsers() {
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("profile");
//        databaseReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                String id = dataSnapshot.getValue(User.class).getId();
//                String firstname = dataSnapshot.getValue(User.class).getFirstname();
//                String lastname = dataSnapshot.getValue(User.class).getLastname();
//                usersArrayList.add(id + ": " + firstname + " " + lastname);
//            }
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) { }
//        });
//    }

    private void selectPlayer(final TextView playerTextView, final int player) {
        usersArray = usersArrayList.toArray(new String[0]);
        final AlertDialog.Builder builder = new AlertDialog.Builder(EnterResultActivity.this);
        builder.setTitle("Select A Player:");
        builder.setIcon(R.drawable.icon);
        builder.setSingleChoiceItems(usersArray, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                playerTextView.setText(usersArray[which]);
                dialog.dismiss();
                int iend = usersArray[which].indexOf(":");
                if (iend != -1) {
                    if(player == 1) {
                        user1position = usersArray[which].substring(0 , iend); //this will give player id
                    } else {
                        user2position = usersArray[which].substring(0 , iend); //this will give player id
                    }
                }
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    private void returnToGame() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void choosingWinner() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EnterResultActivity.this);
        builder.setCancelable(true);
        if(user1position.equals(user2position)) {
            builder.setTitle("Wrong choice!");
            builder.setMessage("You have to choose two different players");
            builder.show();
        }
        else {
            int radioId1 = radioGroup1.getCheckedRadioButtonId();
            radioButton1 = findViewById(radioId1);

            int radioId2 = radioGroup2.getCheckedRadioButtonId();
            radioButton2 = findViewById(radioId2);

            if(radioButton1.getText().equals("Won") && radioButton2.getText().equals("Lost")) {
                s.addingScore(user1position,1,0,0);
                s.addingScore(user2position,0,1,0);
                returnToGame();
            } else if(radioButton1.getText().equals("Lost") && radioButton2.getText().equals("Won")) {
                s.addingScore(user1position,0,1,0);
                s.addingScore(user2position,1,0,0);
                returnToGame();
            } else if(radioButton1.getText().equals("Draw") && radioButton2.getText().equals("Draw")) {
                s.addingScore(user1position,0,0,1);
                s.addingScore(user2position,0,0,1);
                returnToGame();
            } else {
                builder.setTitle("Wrong choice!");
                builder.setMessage("You have to pick one player who wins and one player who loses or draws between two players");
                builder.show();
            }
        }
    }

//    private void addingScore(String userId, final int win, final int lose, final int draw) {
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("profile").child(userId);
//        final int[] i = {0};
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String id = dataSnapshot.getValue(User.class).getId();
//                String firstname = dataSnapshot.getValue(User.class).getFirstname();
//                String lastname = dataSnapshot.getValue(User.class).getLastname();
//                String email = dataSnapshot.getValue(User.class).getEmail();
//                String passwoard = dataSnapshot.getValue(User.class).getPassword();
//                String admin = dataSnapshot.getValue(User.class).getAdmin();
//
//                String wins = dataSnapshot.getValue(User.class).getWins();
//                int w = Integer.parseInt(wins);
//                w += win;
//
//                String loses = dataSnapshot.getValue(User.class).getLoses();
//                int l = Integer.parseInt(loses);
//                l += lose;
//
//                String draws = dataSnapshot.getValue(User.class).getDraw();
//                int d = Integer.parseInt(draws);
//                d += draw;
//
//                if(i[0] == 0) {
//                    User newUser = new User();
//                    newUser.updateUser(id, firstname, lastname, email, passwoard, admin, String.valueOf(w), String.valueOf(l), String.valueOf(d));
//                    i[0] = 1;
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) { }
//        });
//        returnToGame();
//    }
}
