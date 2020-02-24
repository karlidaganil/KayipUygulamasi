package com.example.kaypbackenddenemesi1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

public class uploadActivity extends AppCompatActivity {
    ImageView kayıpImageView;
    // ImageView kameradanSecimageView;
    ProgressBar progressBar;
    EditText kayıpBilgisiEditText;
    EditText kayipSehirEditText;
    EditText kayipSemtEditText;
    EditText telnoEditText;

    FirebaseDatabase firebasedatabase;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;

    Uri secilenKayipFotosuUri;
    FeedActivity obj;
    String ne = "";
    RadioButton insan, hayvan, esya;
    String turu = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getBooleanExtra("bisiBuldum", false) == true) {
            this.setTitle("Bulduğunuzu  Paylaşın");
            ne = "bisiBuldum";
        } else {
            this.setTitle("Kayıp Paylaş");
            ne = "bisiKaybettim";
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        insan = (RadioButton) findViewById(R.id.insanRadioBoxid);
        hayvan = (RadioButton) findViewById(R.id.hayvanRadioBoxid);
        esya = (RadioButton) findViewById(R.id.esyaRadioBoxid);

        kayıpImageView = (ImageView) findViewById(R.id.kayipImageView);

        kayıpBilgisiEditText = (EditText) findViewById(R.id.kayipBilgisiEditTextid);
        kayipSehirEditText = (EditText) findViewById(R.id.kayipSehirEditTextid);
        kayipSemtEditText = (EditText) findViewById(R.id.kayipSemtEditTextid);
        telnoEditText=(EditText)findViewById(R.id.telnoEditTextid);



        progressBar=(ProgressBar)findViewById(R.id.progressid);

        firebasedatabase=FirebaseDatabase.getInstance();
        myRef=firebasedatabase.getReference();

        mAuth=FirebaseAuth.getInstance();

        mStorageRef= FirebaseStorage.getInstance().getReference();
    }

    public void kayibiPaylasMethod(View view)
    {
//        PaylasilanTuruSetEt();
//        if(ne=="bisiKaybettim")
//        {
//            Toast.makeText(getApplicationContext(),"bişi kaybettime girdim",Toast.LENGTH_LONG).show();
//            UUID uuid=UUID.randomUUID();
//            final String fotoismi="KaybettimFotolari/"+uuid+".jpg";
//            StorageReference storageReference=mStorageRef.child(fotoismi);
//            storageReference.putFile(secilenKayipFotosuUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                    //download URL
//                    StorageReference newReference=FirebaseStorage.getInstance().getReference(fotoismi);
//                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            String dowloandURI=uri.toString();
//                            FirebaseUser user=mAuth.getCurrentUser();
//                            String kullanıcıEmail=user.getEmail();
//                            String kayıpBilgisi=kayıpBilgisiEditText.getText().toString();
//                            String SehirBilgisi=kayipSehirEditText.getText().toString().toUpperCase();
//                            String SemtBilgisi=kayipSemtEditText.getText().toString().toUpperCase();
//
//
//                            UUID uuid1=UUID.randomUUID();
//                            String kayipİsimleriRandom=uuid1.toString();
//                            myRef.child("Kaybettim Paylasimlar").child(kayipİsimleriRandom).child("paylaşan e-mail").setValue(kullanıcıEmail);
//                            myRef.child("Kaybettim Paylasimlar").child(kayipİsimleriRandom).child("Kayıp bilgisi").setValue(kayıpBilgisi);
//                            myRef.child("Kaybettim Paylasimlar").child(kayipİsimleriRandom).child("Sehir bilgisi").setValue(SehirBilgisi);
//                            myRef.child("Kaybettim Paylasimlar").child(kayipİsimleriRandom).child("Semt bilgisi").setValue(SemtBilgisi);
//                            myRef.child("Kaybettim Paylasimlar").child(kayipİsimleriRandom).child("Kayıp Foto URL").setValue(dowloandURI);
//                            myRef.child("Kaybettim Paylasimlar").child(kayipİsimleriRandom).child("Turu").setValue(turu);
//
//                            Toast.makeText(getApplicationContext(),"Kayıp paylaşıldı",Toast.LENGTH_LONG).show();
//                            Intent intent=new Intent(getApplicationContext(),FeedActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//                    //KullanıcıAdı, Kayıp Bilgisi
//
//                }
//            }).addOnFailureListener(this, new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//        if(ne=="bisiBuldum")
//        {
//            Toast.makeText(getApplicationContext(),"bişi buldum a girdim",Toast.LENGTH_LONG).show();
//            UUID uuid=UUID.randomUUID();
//            final String fotoismi="BuldumFotolari/"+uuid+".jpg";
//            StorageReference storageReference=mStorageRef.child(fotoismi);
//            storageReference.putFile(secilenKayipFotosuUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                    //download URL
//                    StorageReference newReference=FirebaseStorage.getInstance().getReference(fotoismi);
//                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            String dowloandURI=uri.toString();
//                            FirebaseUser user=mAuth.getCurrentUser();
//                            String kullanıcıEmail=user.getEmail();
//                            String kayıpBilgisi=kayıpBilgisiEditText.getText().toString();
//                            String SehirBilgisi=kayipSehirEditText.getText().toString().toUpperCase();
//                            String SemtBilgisi=kayipSemtEditText.getText().toString().toUpperCase();
//
//
//                            UUID uuid1=UUID.randomUUID();
//                            String kayipİsimleriRandom=uuid1.toString();
//                            myRef.child("Buldum Paylasimlar").child(kayipİsimleriRandom).child("paylaşan e-mail").setValue(kullanıcıEmail);
//                            myRef.child("Buldum Paylasimlar").child(kayipİsimleriRandom).child("Kayıp bilgisi").setValue(kayıpBilgisi);
//                            myRef.child("Buldum Paylasimlar").child(kayipİsimleriRandom).child("Sehir bilgisi").setValue(SehirBilgisi);
//                            myRef.child("Buldum Paylasimlar").child(kayipİsimleriRandom).child("Semt bilgisi").setValue(SemtBilgisi);
//                            myRef.child("Buldum Paylasimlar").child(kayipİsimleriRandom).child("Kayıp Foto URL").setValue(dowloandURI);
//                            myRef.child("Buldum Paylasimlar").child(kayipİsimleriRandom).child("Turu").setValue(turu);
//
//                            Toast.makeText(getApplicationContext(),"Kayıp paylaşıldı",Toast.LENGTH_LONG).show();
//                            Intent intent=new Intent(getApplicationContext(),FeedActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//                    //KullanıcıAdı, Kayıp Bilgisi
//
//                }
//            }).addOnFailureListener(this, new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
//                }
//            });
//        }
        progressBar.setVisibility(View.VISIBLE);

            Toast.makeText(getApplicationContext(),"giriorm",Toast.LENGTH_SHORT).show();
            PaylasilanTuruSetEt();
            if(ne=="bisiKaybettim")
            {
                Toast.makeText(getApplicationContext(),"bişi kaybettime girdim",Toast.LENGTH_LONG).show();
                UUID uuid=UUID.randomUUID();
                final String fotoismi="KaybettimFotolari/"+turu+"/"+uuid+".jpg";
                StorageReference storageReference=mStorageRef.child(fotoismi);
                storageReference.putFile(secilenKayipFotosuUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //download URL
                        StorageReference newReference=FirebaseStorage.getInstance().getReference(fotoismi);
                        newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String dowloandURI=uri.toString();
                                FirebaseUser user=mAuth.getCurrentUser();
                                String kullanıcıEmail=user.getEmail();
                                String kayıpBilgisi=kayıpBilgisiEditText.getText().toString();
                                String SehirBilgisi=kayipSehirEditText.getText().toString().toUpperCase();
                                String SemtBilgisi=kayipSemtEditText.getText().toString().toUpperCase();
                                String telNo=telnoEditText.getText().toString();

                                UUID uuid1=UUID.randomUUID();
                                String kayipİsimleriRandom=uuid1.toString();


                                myRef.child("Kaybettim Paylasimlar").child(turu).child(kayipİsimleriRandom).child("paylaşan e-mail").setValue(kullanıcıEmail);
                                myRef.child("Kaybettim Paylasimlar").child(turu).child(kayipİsimleriRandom).child("Kayıp bilgisi").setValue(kayıpBilgisi);
                                myRef.child("Kaybettim Paylasimlar").child(turu).child(kayipİsimleriRandom).child("Sehir bilgisi").setValue(SehirBilgisi);
                                myRef.child("Kaybettim Paylasimlar").child(turu).child(kayipİsimleriRandom).child("Semt bilgisi").setValue(SemtBilgisi);
                                myRef.child("Kaybettim Paylasimlar").child(turu).child(kayipİsimleriRandom).child("Kayıp Foto URL").setValue(dowloandURI);
                                myRef.child("Kaybettim Paylasimlar").child(turu).child(kayipİsimleriRandom).child("Turu").setValue(turu);
                                myRef.child("Kaybettim Paylasimlar").child(turu).child(kayipİsimleriRandom).child("Tel No").setValue(telNo);

                                //----------------------------------------
                                myRef.child("Genel Kaybettim Paylasimlar").child(kayipİsimleriRandom).child("paylaşan e-mail").setValue(kullanıcıEmail);
                                myRef.child("Genel Kaybettim Paylasimlar").child(kayipİsimleriRandom).child("Kayıp bilgisi").setValue(kayıpBilgisi);
                                myRef.child("Genel Kaybettim Paylasimlar").child(kayipİsimleriRandom).child("Sehir bilgisi").setValue(SehirBilgisi);
                                myRef.child("Genel Kaybettim Paylasimlar").child(kayipİsimleriRandom).child("Semt bilgisi").setValue(SemtBilgisi);
                                myRef.child("Genel Kaybettim Paylasimlar").child(kayipİsimleriRandom).child("Kayıp Foto URL").setValue(dowloandURI);
                                myRef.child("Genel Kaybettim Paylasimlar").child(kayipİsimleriRandom).child("Turu").setValue(turu);
                                myRef.child("Genel Kaybettim Paylasimlar").child(kayipİsimleriRandom).child("Tel No").setValue(telNo);



                                Toast.makeText(getApplicationContext(),"Kayıp paylaşıldı",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(getApplicationContext(),FeedActivity.class);
                                startActivity(intent);
                            }
                        });
                        //KullanıcıAdı, Kayıp Bilgisi

                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
            if(ne=="bisiBuldum")
            {
                Toast.makeText(getApplicationContext(),"bişi buldum a girdim",Toast.LENGTH_LONG).show();
                UUID uuid=UUID.randomUUID();
                final String fotoismi="BuldumFotolari/"+turu+"/"+uuid+".jpg";
                StorageReference storageReference=mStorageRef.child(fotoismi);
                storageReference.putFile(secilenKayipFotosuUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //download URL
                        StorageReference newReference=FirebaseStorage.getInstance().getReference(fotoismi);
                        newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String dowloandURI=uri.toString();
                                FirebaseUser user=mAuth.getCurrentUser();
                                String kullanıcıEmail=user.getEmail();
                                String kayıpBilgisi=kayıpBilgisiEditText.getText().toString();
                                String SehirBilgisi=kayipSehirEditText.getText().toString().toUpperCase();
                                String SemtBilgisi=kayipSemtEditText.getText().toString().toUpperCase();
                                String telNo=telnoEditText.getText().toString();

                                UUID uuid1=UUID.randomUUID();
                                String kayipİsimleriRandom=uuid1.toString();
                                myRef.child("Buldum Paylasimlar").child(turu).child(kayipİsimleriRandom).child("paylaşan e-mail").setValue(kullanıcıEmail);
                                myRef.child("Buldum Paylasimlar").child(turu).child(kayipİsimleriRandom).child("Kayıp bilgisi").setValue(kayıpBilgisi);
                                myRef.child("Buldum Paylasimlar").child(turu).child(kayipİsimleriRandom).child("Sehir bilgisi").setValue(SehirBilgisi);
                                myRef.child("Buldum Paylasimlar").child(turu).child(kayipİsimleriRandom).child("Semt bilgisi").setValue(SemtBilgisi);
                                myRef.child("Buldum Paylasimlar").child(turu).child(kayipİsimleriRandom).child("Kayıp Foto URL").setValue(dowloandURI);
                                myRef.child("Buldum Paylasimlar").child(turu).child(kayipİsimleriRandom).child("Turu").setValue(turu);
                                myRef.child("Buldum Paylasimlar").child(turu).child(kayipİsimleriRandom).child("Tel No").setValue(telNo);

                                //----------------------------------------
                                myRef.child("Genel Buldum Paylasimlar").child(kayipİsimleriRandom).child("paylaşan e-mail").setValue(kullanıcıEmail);
                                myRef.child("Genel Buldum Paylasimlar").child(kayipİsimleriRandom).child("Kayıp bilgisi").setValue(kayıpBilgisi);
                                myRef.child("Genel Buldum Paylasimlar").child(kayipİsimleriRandom).child("Sehir bilgisi").setValue(SehirBilgisi);
                                myRef.child("Genel Buldum Paylasimlar").child(kayipİsimleriRandom).child("Semt bilgisi").setValue(SemtBilgisi);
                                myRef.child("Genel Buldum Paylasimlar").child(kayipİsimleriRandom).child("Kayıp Foto URL").setValue(dowloandURI);
                                myRef.child("Genel Buldum Paylasimlar").child(kayipİsimleriRandom).child("Turu").setValue(turu);
                                myRef.child("Genel Buldum Paylasimlar").child(kayipİsimleriRandom).child("Tel No").setValue(telNo);

                                Toast.makeText(getApplicationContext(),"Kayıp paylaşıldı",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(getApplicationContext(),FeedActivity.class);
                                startActivity(intent);
                            }
                        });
                        //KullanıcıAdı, Kayıp Bilgisi

                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        }
    /////////////////////////////


        UUID uuid=UUID.randomUUID();
        final String fotoismi="GenelPaylasimlarFotolari/"+uuid+".jpg";
        StorageReference storageReference=mStorageRef.child(fotoismi);
        storageReference.putFile(secilenKayipFotosuUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //download URL
                StorageReference newReference=FirebaseStorage.getInstance().getReference(fotoismi);
                newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String dowloandURI=uri.toString();
                        FirebaseUser user=mAuth.getCurrentUser();
                        String kullanıcıEmail=user.getEmail();
                        String kayıpBilgisi=kayıpBilgisiEditText.getText().toString();
                        String SehirBilgisi=kayipSehirEditText.getText().toString().toUpperCase();
                        String SemtBilgisi=kayipSemtEditText.getText().toString().toUpperCase();
                        String telNo=telnoEditText.getText().toString();

                        UUID uuid1=UUID.randomUUID();
                        String kayipİsimleriRandom=uuid1.toString();


                        myRef.child("Genel Paylasimlar").child(kayipİsimleriRandom).child("paylaşan e-mail").setValue(kullanıcıEmail);
                        myRef.child("Genel Paylasimlar").child(kayipİsimleriRandom).child("Kayıp bilgisi").setValue(kayıpBilgisi);
                        myRef.child("Genel Paylasimlar").child(kayipİsimleriRandom).child("Sehir bilgisi").setValue(SehirBilgisi);
                        myRef.child("Genel Paylasimlar").child(kayipİsimleriRandom).child("Semt bilgisi").setValue(SemtBilgisi);
                        myRef.child("Genel Paylasimlar").child(kayipİsimleriRandom).child("Kayıp Foto URL").setValue(dowloandURI);
                        myRef.child("Genel Paylasimlar").child(kayipİsimleriRandom).child("Turu").setValue(turu);
                        myRef.child("Genel Paylasimlar").child(kayipİsimleriRandom).child("Tel No").setValue(telNo);

                    }
                });
                //KullanıcıAdı, Kayıp Bilgisi

            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });







    }
    public void FotoSeçMethodu(View view)
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        else
        {
            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==2 && resultCode==RESULT_OK && data!=null)
        {
            secilenKayipFotosuUri=data.getData();
            try {
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),secilenKayipFotosuUri);
                kayıpImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void PaylasilanTuruSetEt()
    {
        if(insan.isChecked())
        {
            turu="Insan";
        }
        if(hayvan.isChecked())
        {
            turu="Hayvan";
        }
        if(esya.isChecked())
        {
            turu="Esya";
        }
    }

}
