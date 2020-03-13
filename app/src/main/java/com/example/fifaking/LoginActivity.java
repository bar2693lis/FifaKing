package com.example.fifaking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

//    private DatabaseReference databaseReference;

    private EditText emailEditText, passwordEditText;

    private Button registerButton;

    private Singleton s = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.EmailEditText);
        passwordEditText = findViewById(R.id.PasswordEditText);

        registerButton = findViewById(R.id.RegisterButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerPage();
            }
        });
    }

    public void checkIfUserIsValid(View view) { // Takes the details the user has entered
        final String email = emailEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        if(isValid(email, password)){
            signInUser(email, password);
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

    private void signInUser(final String email, final String password) { // Checks whether the user exists on the system or not
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { // Sign in success = User exist
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String userUid = firebaseUser.getUid();
//                            if(s.checkIfAdmin(userUid))
//                            {
//                                adminGamePage();
//                            }
//                            else
//                            {
//                                gamePage();
//                            }
                            gamePage();
                        } else { // No user exists
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setCancelable(true);
                            builder.setTitle("Sign-in failed");
                            builder.setMessage("The user does not exist or the user is incorrectly written");
                            builder.show();
                        }
                    }
                });
    }

//    private void checkIfAdmin(String userId) { // Checks whether the user is admin
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("profile").child(userId).child("admin");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String admin = dataSnapshot.getValue().toString();
//                if(admin.equals("1")) { // If yes, send the user to the admin's game page
//                    adminGamePage();
//                } else { // If not, send the user to the standard game page
//                    gamePage();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void registerPage() { // Moves the user to the game page
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    private void gamePage() { // Moves the user to the game page
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void adminGamePage() { // Moves the admin to the admin game page
        //Intent intent = new Intent(this, AdminActivity.class);
        //startActivity(intent);
    }
}
