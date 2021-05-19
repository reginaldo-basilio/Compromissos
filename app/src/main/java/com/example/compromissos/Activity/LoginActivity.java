package com.example.compromissos.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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

    private static final int PERMISSION_CAMERA = 10;
    private static final int PERMISSION_WRITE = 11;
    private static final int PERMISSION_READ = 12;

    private BootstrapButton btnLogin, btnCancel, btnRegister, btnCancelAlert, btnSendEmail;
    private BootstrapEditText edtEmail, edtPassword, edtSendEmail;
    private TextView txtRecoveryPassword;

    //instanciando firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Dialog dialog;

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
                efetuarLogin(edtEmail.getText().toString(), edtPassword.getText().toString());
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                edtEmail.setText("");
                edtPassword.setText("");
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                abrirCadastro();
            }
        });

        txtRecoveryPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                abrirDialog();
            }
        });
    }

    private void efetuarLogin(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Login realizado com sucesso!", Toast.LENGTH_LONG).show();
                            abrirMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "E-mail ou senha incorreto!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void abrirCadastro(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        verificarPermissoes();

        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //reload();
            abrirMainActivity();
        }
    }

    private void abrirMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void abrirDialog(){
        dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.alert_recovery_password);
        btnCancelAlert = (BootstrapButton) dialog.findViewById(R.id.btnCancelAlert);
        btnSendEmail = (BootstrapButton) dialog.findViewById(R.id.btnSendEmail);
        edtSendEmail = (BootstrapEditText) dialog.findViewById(R.id.edtSendEmail);

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.sendPasswordResetEmail(edtSendEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("TAG", "Email sent.");
                                    Toast.makeText(LoginActivity.this, "Verifique sua caixa de entrada", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(LoginActivity.this, "Falha ao enviar o e-mail", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                dialog.dismiss();
            }
        });

        btnCancelAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void verificarPermissoes(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){

                /////

            }else{
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, PERMISSION_CAMERA);
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE);
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ);
            }

        }
    }


}