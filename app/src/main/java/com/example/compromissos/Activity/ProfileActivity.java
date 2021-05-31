package com.example.compromissos.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.compromissos.Entidades.User;
import com.example.compromissos.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class ProfileActivity extends AppCompatActivity {

    private BootstrapButton btnUpdate, btnCancel;
    private BootstrapEditText edtName, edtEmail, edtPassword, edtConfirmPassword;
    private RadioButton rbMasc, rbFem;

    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        edtName = (BootstrapEditText) findViewById(R.id.edtName);
        edtEmail = (BootstrapEditText) findViewById(R.id.edtEmail);
        edtPassword = (BootstrapEditText) findViewById(R.id.edtPassword);
        edtConfirmPassword = (BootstrapEditText) findViewById(R.id.edtConfirmPassword);
        btnUpdate = (BootstrapButton) findViewById(R.id.btnUpdate);
        btnCancel = (BootstrapButton) findViewById(R.id.btnCancel);
        rbMasc = (RadioButton) findViewById(R.id.rbMasc);
        rbFem = (RadioButton) findViewById(R.id.rbFem);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void popularDadosUsuario(){
        firebaseAuth = FirebaseAuth.getInstance();
        String emailCurrentUser = firebaseAuth.getCurrentUser().getEmail();
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("users").orderByChild("email").equalTo(emailCurrentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot userSnapShot : snapshot.getChildren()){
                    User user = userSnapShot.getValue(User.class);
                    edtName.setText(user.getName());
                    edtEmail.setText(user.getEmail());

                    if(user.getSex().equals("Masculino")){
                        rbMasc.setChecked(true);
                    }else if(user.getSex().equals("Feminino")){
                        rbFem.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}

