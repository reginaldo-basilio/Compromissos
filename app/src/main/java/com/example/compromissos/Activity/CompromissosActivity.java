package com.example.compromissos.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.compromissos.Entidades.Compromisso;
import com.example.compromissos.Entidades.User;
import com.example.compromissos.Helper.MaskEditText;
import com.example.compromissos.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CompromissosActivity extends AppCompatActivity {

    private BootstrapEditText edtTitulo;
    private BootstrapEditText edtDescription;
    private BootstrapEditText edtDate;
    private BootstrapButton btnInserir;
    private BootstrapButton btnCancel;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private Compromisso compromisso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compromissos);

        database = FirebaseDatabase.getInstance();

        edtTitulo = (BootstrapEditText) findViewById(R.id.edtTitulo);
        edtDescription = (BootstrapEditText) findViewById(R.id.edtDescription);
        edtDate = (BootstrapEditText) findViewById(R.id.edtDate);
        edtDate.addTextChangedListener(MaskEditText.mask(edtDate, MaskEditText.FORMAT_DATE));

        btnInserir = (BootstrapButton) findViewById(R.id.btnInsert);
        btnCancel = (BootstrapButton) findViewById(R.id.btnCancel);

        btnInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compromisso = new Compromisso();
                compromisso.setTitulo(edtTitulo.getText().toString());
                compromisso.setDescricao(edtDescription.getText().toString());
                compromisso.setDate(edtDate.getText().toString());

                inserirCompromissoDatabase(compromisso);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void inserirCompromissoDatabase(Compromisso compromisso){
        myRef = database.getReference("compromissos");
        String key = myRef.child("compromissos").push().getKey();
        compromisso.setKeyCompromisso(key);
        myRef.child(key).setValue(compromisso);
        Toast.makeText(this, "Compromisso agendado!", Toast.LENGTH_SHORT).show();
    }

}