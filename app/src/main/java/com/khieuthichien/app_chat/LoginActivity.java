package com.khieuthichien.app_chat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.khieuthichien.app_chat.model.User;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar loginProgress;
    private EditText edtLoginEmail;
    private EditText edtLoginPassword;
    private Button btnLogIn;
    private Button btnResetPassword;
    private Button btnCreateNewAccount;
    private SignInButton btnLoginGoogle;
    //private LoginButton btnLoginFacebook;

    //private CallbackManager callbackManager;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    GoogleApiClient mGoogleApiClient;

    private static String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();

        loginProgress = findViewById(R.id.login_progress);
        edtLoginEmail = findViewById(R.id.edt_login_email);
        edtLoginPassword = findViewById(R.id.edt_login_password);
        btnLogIn = findViewById(R.id.btn_log_in);
        btnResetPassword = findViewById(R.id.btn_reset_password);
        btnCreateNewAccount = findViewById(R.id.btn_create_new_account);
        btnLoginGoogle = findViewById(R.id.btn_login_google);

        btnResetPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnCreateNewAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), CreateNewAccountActivity.class));
                finish();
            }
        });

        btnLogIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = edtLoginEmail.getText().toString();
                final String password = edtLoginPassword.getText().toString();

                if (password.isEmpty() || email.isEmpty()){

                    if (email.equals("")) {
                        edtLoginEmail.setError("Không bỏ trống");
                        return;
                    }
                    if (password.equals("")) {
                        edtLoginPassword.setError("Không bỏ trống");
                        return;
                    }
                } else {

                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


    }


}

