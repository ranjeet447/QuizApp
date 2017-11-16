package myapp.com.quizz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.id;
import static android.R.attr.name;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";


    TextInputLayout mNmaeTextInputLayout;
    TextInputLayout mEmailTextInputLayout;
    TextInputLayout mPasswordTextInputLayout;

    EditText mNameEditText;
    EditText mEmailEditText;
    EditText mPasswordEditText;

    AppCompatButton mAppCompatButton;

    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        mNmaeTextInputLayout = (TextInputLayout) findViewById(R.id.nameTextInputLayout);
        mEmailTextInputLayout = (TextInputLayout) findViewById(R.id.emailTextInputLayout);
        mPasswordTextInputLayout = (TextInputLayout) findViewById(R.id.passwordTextInputLayout);

        mNameEditText = (EditText) findViewById(R.id.nameEditTextView);
        mEmailEditText = (EditText) findViewById(R.id.emailEditTextView);
        mPasswordEditText = (EditText) findViewById(R.id.passwordEditTextView);

        mAppCompatButton = (AppCompatButton) findViewById(R.id.registerAppCompatButton);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        mAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();
                final String name = mNameEditText.getText().toString().trim();

                mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            saveUserInformation();
                        } else {
                            Toast.makeText(getApplicationContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private void saveUserInformation(){
        String name = mNameEditText.getText().toString().trim();
        String email = mEmailEditText.getText().toString().trim();

        UserInformation userInformation = new UserInformation( name,email);
        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        userInformation.setName(name);
        userInformation.setEmail(email);

        databaseReference.child(user.getUid()).setValue(userInformation);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
