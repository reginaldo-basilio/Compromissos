package com.example.compromissos.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.compromissos.Entidades.User;
import com.example.compromissos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private BootstrapButton btnUpdate, btnCancel;
    private BootstrapEditText edtName, edtEmail, edtPassword, edtConfirmPassword;
    private RadioButton rbMasc, rbFem;
    private ImageView imgPictureProfile;

    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference profileRef;

    private Dialog dialog;
    private TextView txtTakePicture;
    private TextView txtUploadPicture;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

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
        imgPictureProfile = (ImageView) findViewById(R.id.imgPictureProfile);

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

        imgPictureProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialog();
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
                    preencherImagemPerfil();
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

        //>>>>>>>>>>>>>>>>>...
        storageRef = storage.getReference();
        profileRef = storageRef.child("profile/"+ uid +".jpg");
        // Get the data from an ImageView as bytes
        imgPictureProfile.setDrawingCacheEnabled(true);
        imgPictureProfile.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgPictureProfile.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = profileRef.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(ProfileActivity.this, "Falha ao enviar imagem!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                // Sign in success, update UI with the signed-in user's information
                Log.d("TAG", "createUserWithEmail:success");

            }
        });

        //>>>>>>>>>>>>>>>..

        Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        finish();

    }

    private void preencherImagemPerfil(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = firebaseUser.getUid();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        storageRef.child("profile/" + uid + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                imgPictureProfile.setBackgroundColor(Color.TRANSPARENT);
                Picasso.get().load(uri.toString()).into(imgPictureProfile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    private void tirarFoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void fazerUploadFoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), 123);

    }

    private void abrirDialog(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_picture);
        txtTakePicture = (TextView) dialog.findViewById(R.id.txtTakePicture);
        txtUploadPicture = (TextView) dialog.findViewById(R.id.txtUploadPicture);

        txtTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tirarFoto();
                dialog.dismiss();
            }
        });

        txtUploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fazerUploadFoto();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == 123){
                imgPictureProfile.setBackgroundColor(Color.TRANSPARENT);
                Uri image = data.getData();
                Picasso.get().load(image.toString()).into(imgPictureProfile);
            }else if(requestCode == REQUEST_IMAGE_CAPTURE){
                imgPictureProfile.setBackgroundColor(Color.TRANSPARENT);
                Bundle extra = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extra.get("data");
                imgPictureProfile.setImageBitmap(imageBitmap);
            }
        }
    }

}

