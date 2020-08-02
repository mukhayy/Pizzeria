package com.mukhayy.pizzeria;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mukhayy.pizzeria.Models.User;
import com.mukhayy.pizzeria.OperatorSideActivities.OperatorActivity;
import com.mukhayy.pizzeria.UserSideActivities.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.emailEd)
    EditText emailEd;
    @BindView(R.id.passwordEd)
    EditText passwordEd;
    @BindView(R.id.fullnameEd)
    EditText fullnameEd;
    @BindView(R.id.phoneEd)
    EditText phoneEd;
    @BindView(R.id.regBtn)
    Button regBtn;
    @BindView(R.id.operatorBtn)
    Button operatorButton;
    @BindView(R.id.signInBtn)
    Button signInBtn;

    FirebaseAuth firebaseAuth;

    DatabaseReference databaseReference;

    SharedPreferences sPref;
    final String SAVED_EMAIL = "saved_email";
    final String SAVED_NAME = "saved_name";
    final String SAVED_PHONENUMBER = "saved_phoneNumber";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register");
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailEd.getText().toString();
                String password = passwordEd.getText().toString();
                final String fullname = fullnameEd.getText().toString();
                final String phone = phoneEd.getText().toString();

                saveText(email, fullname, phone);

                if(email.isEmpty()){
                    emailEd.setError("Required");
                    return;
                }
                if(password.isEmpty()){
                    passwordEd.setError("Required");
                    return;
                }
                if(fullname.isEmpty()){
                    fullnameEd.setError("Required");
                    return;
                }
                if(phone.isEmpty()){
                   phoneEd.setError("Required");
                   return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.putExtra("email", email);
                                    intent.putExtra("fullname", fullname);
                                    intent.putExtra("phone", phone);

                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"E-mail or password is wrong",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                databaseReference = FirebaseDatabase.getInstance().getReference("User");
                User user = new User(fullname, phone, email);
                databaseReference.push().setValue(user);

            }
        });


        operatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, OperatorActivity.class);
                startActivity(intent);
            }
        });


        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(RegisterActivity.this, SignInActivity.class);
               startActivity(intent);
            }
        });



    }


    void saveText(String email, String name, String phone) {
        sPref = getSharedPreferences("preferences", MODE_PRIVATE );
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_EMAIL, email);
        ed.putString(SAVED_NAME, name);
        ed.putString(SAVED_PHONENUMBER, phone);
        ed.apply();
     //   Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
    }

}
