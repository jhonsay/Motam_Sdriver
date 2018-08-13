package com.cryptoull.motam_sdriver;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "LoginActivity ->";
    private FirebaseAuth mAuth;
    private EditText mEmailField,mPasswordField;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button mLoginBtn;

    private boolean firsTime = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mEmailField = (EditText) findViewById(R.id.email);
        mPasswordField = (EditText) findViewById(R.id.pass);

        //mEmailField.setText("test@test.com");
        //mPasswordField.setText("123456");

        mEmailField.setText("test001@cryptoull.com");
        mPasswordField.setText("test001");

        //FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        mLoginBtn = (Button) findViewById(R.id.login);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser()!=null){

                    Comunicador.setObjeto(mAuth);
                    Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                    startActivity(intent);

                    //Toast.makeText(LoginActivity.this, "Yujuuuuu!", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(LoginActivity.this,Main2Activity.class));

                }
                else {
                    if (!firsTime) Toast.makeText(LoginActivity.this, "Datos Incorrectos", Toast.LENGTH_SHORT).show();

                }
            }
        };






        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateForm()) {
                    return;
                }

                LoginUser();
                firsTime = false;


/*
                FirebaseUser usr = mAuth.getCurrentUser();

                if (usr != null){

                    Toast.makeText(LoginActivity.this,usr.toString(), Toast.LENGTH_LONG);

                    startActivity(new Intent(LoginActivity.this,Main2Activity.class));
                }else{
                    Toast.makeText(LoginActivity.this,"Error!", Toast.LENGTH_LONG);
                }*/


            }
        });

    }

    private void LoginUser(){

        String email=mEmailField.getText().toString();
        String password=mPasswordField.getText().toString();


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(LoginActivity.this, "Authentication Success.",
                                    Toast.LENGTH_LONG).show();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });


    }


    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.signOut();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthListener);
        //updateUI(currentUser);
    }
}
