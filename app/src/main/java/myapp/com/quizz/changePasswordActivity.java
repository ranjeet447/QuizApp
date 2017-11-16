package myapp.com.quizz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class changePasswordActivity extends AppCompatActivity {

    EditText mEmail,mOdlPassword,mNewPassword;
    Button mResetPasswordButton;

    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mEmail = (EditText) findViewById(R.id.email_resetPassword);
        mOdlPassword = (EditText) findViewById(R.id.oldPass_resetPassword);
        mNewPassword = (EditText) findViewById(R.id.newPass_resetPassword);
        mResetPasswordButton = (Button) findViewById(R.id.resetPassword_button);

        mResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });


    }

    public void resetPassword(){

        progressDialog = ProgressDialog.show(this,null,"Changing Password...",true);

        String email =mEmail.getText().toString().trim();
        String oldPassword = mOdlPassword.getText().toString().trim();
        final String newPassword = mNewPassword.getText().toString().trim();

        AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);

        mFirebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mFirebaseUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Password Updated",Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                finish();
                                startActivity(new Intent(changePasswordActivity.this, LoginActivity.class));

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),"Password could not be changed",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}
