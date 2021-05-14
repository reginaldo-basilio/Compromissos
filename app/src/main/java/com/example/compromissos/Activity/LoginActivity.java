package com.example.compromissos.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.view.BootstrapTextView;
import com.example.compromissos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private BootstrapButton btnLogin, btnCancel, btnRegister;
    private BootstrapEditText edtEmail, edtPassword;
    private TextView txtRecoveryPassword;

    //instanciando firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //recuperando instancia firebase auth
        mAuth = FirebaseAuth.getInstance();

        btnLogin = (BootstrapButton) findViewById(R.id.btnLogin);
        btnCancel = (BootstrapButton) findViewById(R.id.btnCancel);
        btnRegister = (BootstrapButton) findViewById(R.id.btnRegister);

        edtEmail = (BootstrapEditText) findViewById(R.id.edtEmail);
        edtPassword = (BootstrapEditText) findViewById(R.id.edtPassword);

        txtRecoveryPassword = (TextView) findViewById(R.id.txtRecoveryPassword);

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                login(edtEmail.getText().toString(), edtPassword.getText().toString());
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        txtRecoveryPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Login realizado com sucesso!", Toast.LENGTH_LONG).show();

                            //segundo o curso, nao usaremos esses dois
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "E-mail ou senha incorreto!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}