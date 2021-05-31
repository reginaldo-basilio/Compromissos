package com.example.compromissos.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.compromissos.Entidades.User;
import com.example.compromissos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private BootstrapButton btnUpdate, btnCancel;
    private BootstrapEditText edtName, edtEmail, edtPassword, edtConfirmPassword;
    private RadioButton rbMasc, rbFem;

    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

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

        popularDadosUsuario();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                String emailCurrentUser = firebaseAuth.getCurrentUser().getEmail();
                reference = FirebaseDatabase.getInstance().getReference();
                reference.child("users").orderByChild("email").equalTo(emailCurrentUser).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for(DataSnapshot userSnapShot : snapshot.getChildren()){
                            User user = userSnapShot.getValue(User.class);
                            if(rbFem.isChecked()){
                                updateUser(edtName.getText().toString(), user.getEmail(), "Feminino", user.getKeyUser(), user.getUid(), edtPassword.getText().toString());
                            }else if(rbMasc.isChecked()){
                                updateUser(edtName.getText().toString(), user.getEmail(), "Masculino", user.getKeyUser(), user.getUid(), edtPassword.getText().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

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
                    if(edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())){
                        if(user.getSex().equals("Masculino")){
                            rbMasc.setChecked(true);
                        }else if(user.getSex().equals("Feminino")){
                            rbFem.setChecked(true);
                        }
                    }else{
                        Toast.makeText(ProfileActivity.this, "As senhas não correspondem!", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void updateUser(String name, String email, String sex, String keyUser, String uid, String newPassword){
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("users");

        //Linha comentada gera nova chave
        //String key = mDatabase.child("posts").push().getKey();
        //Post post = new Post(userId, username, title, body);

        //Recebe chave como parametro
        User user = new User(name, email, sex, keyUser, uid);

        Map<String, Object> userValues = user.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + keyUser, userValues);
        //childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        reference.updateChildren(childUpdates);

        if(!newPassword.equals("")){
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            // - nao utilizado agora --- String newPassword = "SOME-SECURE-PASSWORD";

            //passamos newPassword como parametro da funcao
            firebaseUser.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "User password updated.");
                            }
                        }
                    });
        }
        Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        finish();



    }


}

