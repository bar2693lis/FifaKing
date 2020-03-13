package com.example.fifaking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.primitives.Ints;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button showScoreButton, enterResultButton, showKingsNameButton;

    private TextView kingNameTextView;

    private ArrayList<String> usersArrayList;

    private String[] usersArray;

    private String kingPosition;

    private DatabaseReference databaseReference;

    private Singleton s = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersArrayList = s.getListOfUsers(1);

        kingNameTextView = findViewById(R.id.KingNameTextView);

        showKingsNameButton = findViewById(R.id.ShowKingsNameButton);
        showScoreButton = findViewById(R.id.ShowScoreButton);
        enterResultButton = findViewById(R.id.EnterResultButton);

        showScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterScoreboard();
            }
        });

        enterResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterResult();
            }
        });

        showKingsNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookForTheKing();
            }
        });
    }

    private void enterScoreboard() {
        Intent intent = new Intent(this, ScoreboardActivity.class);
        startActivity(intent);
    }

    private void enterResult() {
        Intent intent = new Intent(this, EnterResultActivity.class);
        startActivity(intent);
    }

//    private void getListOfUsers() {
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("profile");
//        databaseReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                String id = dataSnapshot.getValue(User.class).getId();
//                String wins = dataSnapshot.getValue(User.class).getWins();
//                String loses = dataSnapshot.getValue(User.class).getLoses();
//                usersArrayList.add(id + ":" + wins + "-" + loses);
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

    private void lookForTheKing() {
        usersArray = usersArrayList.toArray(new String[0]);
        int maxValue = 0, minValue = 0;
        for(int i = 0 ; i < usersArray.length ; i++) {
            int iend = usersArray[i].indexOf(":"), jend = usersArray[i].indexOf("-");
            int valueMax = Integer.parseInt(usersArray[i].substring(iend+1 , jend)), valueMin = Integer.parseInt(usersArray[i].substring(jend+1 , usersArray[i].length()));
            if (valueMax >= maxValue) {
                if(valueMax == maxValue) {
                    if(valueMin < minValue) {
                        maxValue = valueMax;
                        minValue = valueMin;
                        kingPosition = usersArray[i].substring(0 , iend);
                    }
                } else {
                    maxValue = valueMax;
                    minValue = valueMin;
                    kingPosition = usersArray[i].substring(0 , iend);
                }
            }
        }
        showKing();
    }

    private void showKing()
    {
        String kingUid = s.getUserUidAndPosition(kingPosition);
        Log.i("kingUid", kingUid);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("profile").child(kingUid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String firstname = dataSnapshot.getValue(User.class).getFirstname();
                String lastname = dataSnapshot.getValue(User.class).getLastname();
                kingNameTextView.setText(firstname + " " + lastname);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
