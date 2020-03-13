package com.example.fifaking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

//    private DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;

    private EditText firstnameEditText, lastnameEditText, emailEditText, passwordEditText;

    private int userPosition;

    private Singleton s = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userPosition = s.getPos();

//        databaseReference = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        firstnameEditText = findViewById(R.id.FirstnameEditText);
        lastnameEditText = findViewById(R.id.LastnameEditText);
        emailEditText = findViewById(R.id.EmailEditText);
        passwordEditText = findViewById(R.id.PasswordEditText);
    }

    public void checkUser(View view) { // Takes the details the user has entered
        final String firstname = firstnameEditText.getText().toString();
        final String lastname = lastnameEditText.getText().toString();
        final String email = emailEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        if(isValid(email, password)){
            signUpNewUser(email, password, firstname, lastname);
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            builder.setCancelable(true);
            builder.setTitle("Email & Password");
            builder.setMessage("Email must be valid.\n" +
                    "Password must contain at least 8 characters, at least 2 numbers, characters and numbers together.");
            builder.show();
        }
    }

    private boolean isValid(String email, String password){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null){
            return false;
        }
        else {
            if (password.length() < 8) {
                return false;
            } else {
                char c;
                int count = 1;
                for (int i = 0; i < password.length() - 1; i++) {
                    c = password.charAt(i);
                    if (!Character.isLetterOrDigit(c)) {
                        return false;
                    } else if (Character.isDigit(c)) {
                        count++;
                        if (count < 2)   {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }

    private void signUpNewUser(final String email, final String password, final String firstname, final String lastname) { // Register a new user for the system
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                        builder.setCancelable(true);
                        if (task.isSuccessful()) { // Sign in success
                            String userUid = firebaseAuth.getUid();
                            s.signUpNewUserToDatabase(userPosition, userUid, email, password, firstname, lastname);
                            loginPage();
                        } else { // If sign in fails, display a message to the user.
                            builder.setTitle("Registration failed");
                            builder.setMessage("One of the details you entered is incorrect");
                            builder.show();
                        }
                    }
                });
    }

//    private void signUpNewUserToDatabase(String email, String password, String firstname, String lastname) { // Register a new user into a database
//        User newUser = new User(String.valueOf(++userId), firstname, lastname, email, password);
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("profile").child(String.valueOf(userId)); // path in the database to the profile
//        databaseReference.setValue(newUser); // push the new user to database
//        loginPage();
//    }

    private void loginPage() { // Moves the user to the game page
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


//    private void getPos () {
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
//        databaseReference.child("profile").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                userId = (int) dataSnapshot.getChildrenCount();
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//    }

}
