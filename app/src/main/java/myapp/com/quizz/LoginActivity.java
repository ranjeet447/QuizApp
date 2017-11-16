package myapp.com.quizz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static myapp.com.quizz.R.string.email;

public class LoginActivity extends AppCompatActivity {
    AppCompatButton mAppCompatButton;
    AppCompatButton mLoginButton;

    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;

    TextInputLayout mEmailTextInputLayout;
    TextInputLayout mPasswordTextInputLayout;

    EditText mEmailEditText;
    EditText mPasswordEditText;
    ProgressDialog progress;
    TextView mForgetPaasTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailTextInputLayout = (TextInputLayout) findViewById(R.id.emailTextInputLayout);
        mPasswordTextInputLayout = (TextInputLayout) findViewById(R.id.passwordTextInputLayout);

        mEmailEditText = (EditText) findViewById(R.id.emaiEditTextView);
        mPasswordEditText = (EditText) findViewById(R.id.passwordEditTextView);
        mForgetPaasTextView = (TextView) findViewById(R.id.forgetPasswordTextView) ;


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mAppCompatButton = (AppCompatButton) findViewById(R.id.not_a_member_sign_up_button);
        mAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        mLoginButton = (AppCompatButton) findViewById(R.id.login_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(LoginActivity.this, null, "Signing In...", true);

                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();

                email = email.trim();        //trim the extra spaces.
                password = password.trim();   // //trim the extra spaces.

                mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(LoginActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progress.dismiss();
                            finish();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Sign In Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        mForgetPaasTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgetPasswordActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

