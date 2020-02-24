package com.example.chatappjava;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class registeractivity extends AppCompatActivity {

    EditText username,email,password;
    Button signup1;
FirebaseAuth auth;
DatabaseReference reference;
Toolbar toolbar=findViewById(R.id.toolbar);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeractivity);
    username=findViewById(R.id.username);
    email=findViewById(R.id.email);
    password=findViewById(R.id.password);
    signup1=findViewById(R.id.signup);

auth=FirebaseAuth.getInstance();
signup1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String username_text=username.getText().toString();
        String email_text=email.getText().toString();
        String password_text=password.getText().toString();
        if(TextUtils.isEmpty(username_text)||TextUtils.isEmpty(email_text)||TextUtils.isEmpty(password_text))
        {

            Toast.makeText(registeractivity.this, "All fields are manadatory", Toast.LENGTH_SHORT).show();
        }else if (password_text.length()>6)
        {

            Toast.makeText(registeractivity.this, "Password length must be at least 6 characters", Toast.LENGTH_SHORT).show();
        }
        else{
            register(username_text,email_text,password_text);
        }
    }
});
    }
    private void register(final String username, String email, String password)
    {


        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            FirebaseUser firebaseUser=auth.getCurrentUser();
                            String userid=firebaseUser.getUid();
                            reference= FirebaseDatabase.getInstance().getReference("users").child(userid);

                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username",username);
                            hashMap.put("imageURL","default");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {

                                        startActivity(new Intent(registeractivity.this,MainActivity.class));

                                    }
                                    else
                                    {

                                        Toast.makeText(registeractivity.this, "Enter valid EmailID or Password ", Toast.LENGTH_SHORT).show();
                                        
                                    }
                                }
                            });

                        }
                    }
                });
    }
}
