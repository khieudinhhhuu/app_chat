package com.khieuthichien.app_chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateNewAccountActivity extends AppCompatActivity {

    private EditText edtSignupFirstName;
    private EditText edtSignupLastName;
    private EditText edtSignupPassword;
    private EditText edtSignupUsername;
    private EditText edtSignupEmail;
    private Button btnRegister;
    private Button btnCancel;

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private static String TAG = "CreateNewAccountActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtSignupFirstName = findViewById(R.id.edt_signup_firstName);
        edtSignupLastName = findViewById(R.id.edt_signup_lastName);
        edtSignupPassword = findViewById(R.id.edt_signup_password);
        edtSignupUsername = findViewById(R.id.edt_signup_username);
        edtSignupEmail = findViewById(R.id.edt_signup_email);
        btnRegister = findViewById(R.id.btn_register);
        btnCancel = findViewById(R.id.btn_cancel);

        auth = FirebaseAuth.getInstance();


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = edtSignupFirstName.getText().toString();
                String lastName = edtSignupLastName.getText().toString();
                String password = edtSignupPassword.getText().toString();
                String username = edtSignupUsername.getText().toString();
                String email = edtSignupEmail.getText().toString();

                if (firstName.isEmpty() || lastName.isEmpty() ||
                        password.isEmpty() || username.isEmpty() || email.isEmpty() ) {

                    if (firstName.equals("")) {
                        edtSignupFirstName.setError("Không bỏ trống");
                        return;
                    }
                    if (lastName.equals("")) {
                        edtSignupLastName.setError("Không bỏ trống");
                        return;
                    }
                    if (email.equals("")) {
                        edtSignupEmail.setError("Không bỏ trống");
                        return;
                    }
                    if (username.equals("")) {
                        edtSignupUsername.setError("Không bỏ trống");
                        return;
                    }
                    if (password.equals("")) {
                        edtSignupPassword.setError("Không bỏ trống");
                        return;
                    }
                } else {
                    if (password.length() < 6) {
                        if (password.length() < 6) {
                            edtSignupPassword.setError("Mật khẩu phải trên 6 kí tự");
                            return;
                        }
                    } else {
                        Register(firstName, lastName, password, username, email);
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), LoginActivity.class));
                finish();
            }
        });

    }


    private void Register(final String firstName, final String lastName, final String password, final String username, final String email) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("avatarLink", "default");
                            hashMap.put("firstName", firstName);
                            hashMap.put("lastName", lastName);
                            hashMap.put("password", password);
                            hashMap.put("username", username);
                            hashMap.put("email", email);
                            hashMap.put("status", "offline");
                            hashMap.put("search", username.toLowerCase());

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(CreateNewAccountActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(CreateNewAccountActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
