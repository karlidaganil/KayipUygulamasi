package com.example.kaypbackenddenemesi1;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText eMailEditText;
    EditText sifreEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);
        eMailEditText=(EditText)findViewById(R.id.emailGirisEditText);
        sifreEditText=(EditText)findViewById(R.id.sifreGirisEditText);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser User=mAuth.getCurrentUser();
        if(User!=null)
        {
            Intent intent=new Intent(getApplicationContext(),FeedActivity.class);
            startActivity(intent);
        }
    }
    public void GirisYapMethodu(View view)
    {
        mAuth.signInWithEmailAndPassword(eMailEditText.getText().toString(),sifreEditText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Intent intent=new Intent(getApplicationContext(),FeedActivity.class);
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }
    public void KayıtOlMethodu(View view)
    {
        mAuth.createUserWithEmailAndPassword(eMailEditText.getText().toString(),sifreEditText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Intent intent=new Intent(getApplicationContext(),FeedActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),"kayıt oldunuz",Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
