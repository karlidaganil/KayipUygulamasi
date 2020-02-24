package com.example.kaypbackenddenemesi1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PaylasimlarimActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;

    ListView listView;
    PostClass adapter;

    ArrayList<String> kullanıcıEmailFromFB;
    ArrayList<String> kayipBilgisiFromFB;
    ArrayList<String> sehirBilgisiFromFB;
    ArrayList<String> semtBilgisiFromFB;
    ArrayList<String> kayipFotoFromFB;
    String bilgisi="";
    @Override
    public void onBackPressed() {
        //do nothing
        Intent intent=new Intent(getApplicationContext(),FeedActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTitle("Paylastıklarım");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paylasimlarim);
        listView=(ListView)findViewById(R.id.PaylasimlarimListView);

        kullanıcıEmailFromFB=new ArrayList<String>();
        kayipBilgisiFromFB=new ArrayList<String>();
        kayipFotoFromFB=new ArrayList<String>();
        sehirBilgisiFromFB=new ArrayList<String>();
        semtBilgisiFromFB=new ArrayList<String>();

        adapter=new PostClass(kullanıcıEmailFromFB,kayipBilgisiFromFB,kayipFotoFromFB, sehirBilgisiFromFB, semtBilgisiFromFB, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               bilgisi=kayipBilgisiFromFB.get(position);
               // b.putStringArray("info", new String[]{kayipBilgisiFromFB.get(position),sehirBilgisiFromFB.get(position),semtBilgisiFromFB.get(position),kullanıcıEmailFromFB.get(position),kayipFotoFromFB.get(position)});

            }
        });

        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        doldur();
    }
    public void doldur()
    {

                Query GenelPaylasimlar=firebaseDatabase.getReference("Genel Paylasimlar")
                .orderByChild("paylaşan e-mail").equalTo(mAuth.getCurrentUser().getEmail());
        GenelPaylasimlar.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                kullanıcıEmailFromFB.clear();
                kayipBilgisiFromFB.clear();
                sehirBilgisiFromFB.clear();
                semtBilgisiFromFB.clear();
                kayipFotoFromFB.clear();
                adapter.notifyDataSetChanged();

                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    HashMap<String,String> hashMap=(HashMap<String,String>)ds.getValue();

                    kullanıcıEmailFromFB.add(hashMap.get("paylaşan e-mail"));
                    kayipBilgisiFromFB.add(hashMap.get("Kayıp bilgisi"));
                    sehirBilgisiFromFB.add(hashMap.get("Sehir bilgisi"));
                    semtBilgisiFromFB.add(hashMap.get("Semt bilgisi"));
                    kayipFotoFromFB.add(hashMap.get("Kayıp Foto URL"));
                    adapter.notifyDataSetChanged();


                }
                System.out.println("FFF VALUE İSTEDİĞİM =="+dataSnapshot.getValue());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void silMethod(View v)
    {

//     DatabaseReference x= (DatabaseReference) firebaseDatabase.getReference("Genel Paylasimlar").child("08771daa-8e98-4eb7-8e0e-bb565f8f4430");
//     x.removeValue();
             //  orderByChild("paylaşan e-mail").equalTo(mAuth.getCurrentUser().getEmail());

//        Query querySehir=firebaseDatabase.getReference("Genel Paylasimlar")
//                .orderByChild("paylaşan e-mail").equalTo("anil@hotmail.com");
//        String key=querySehir.getRef().push().getKey();

   //Genel paylasimlardan silinme
        if(bilgisi.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Once silmek istediginiz paylasımı secin",Toast.LENGTH_LONG).show();
        }
        else
        {
            final  DatabaseReference GenelPaylasimlar= (DatabaseReference) firebaseDatabase.getReference("Genel Paylasimlar");
            // x.removeValue();
            GenelPaylasimlar.orderByChild("Kayıp bilgisi").equalTo(bilgisi).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    GenelPaylasimlar.child(dataSnapshot.getKey()).setValue(null);
                    //also work
                    //dataSnapshot.getRef().setValue(null);
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            //Genel Kaybettimlerden Silme
            final  DatabaseReference GenelKaybettim= (DatabaseReference) firebaseDatabase.getReference("Genel Kaybettim Paylasimlar");
            // x.removeValue();
            GenelKaybettim.orderByChild("Kayıp bilgisi").equalTo(bilgisi).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    GenelKaybettim.child(dataSnapshot.getKey()).setValue(null);
                    //also work
                    //dataSnapshot.getRef().setValue(null);
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            //Genel Buldumlardan Silme
            final  DatabaseReference GenelBudumlar= (DatabaseReference) firebaseDatabase.getReference("Genel Buldum Paylasimlar");
            // x.removeValue();
            GenelBudumlar.orderByChild("Kayıp bilgisi").equalTo(bilgisi).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    GenelBudumlar.child(dataSnapshot.getKey()).setValue(null);
                    //also work
                    //dataSnapshot.getRef().setValue(null);
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            //Kaybettim Paylasimlar
            final  DatabaseReference KaybettimPaylasimlarInsan= (DatabaseReference) firebaseDatabase.getReference("Kaybettim Paylasimlar/Insan");
            // x.removeValue();
            KaybettimPaylasimlarInsan.orderByChild("Kayıp bilgisi").equalTo(bilgisi).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    KaybettimPaylasimlarInsan.child(dataSnapshot.getKey()).setValue(null);
                    //also work
                    //dataSnapshot.getRef().setValue(null);
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            ////
            final  DatabaseReference KaybettimPaylasimlarHayvan= (DatabaseReference) firebaseDatabase.getReference("Kaybettim Paylasimlar/Hayvan");
            // x.removeValue();
            KaybettimPaylasimlarHayvan.orderByChild("Kayıp bilgisi").equalTo(bilgisi).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    KaybettimPaylasimlarHayvan.child(dataSnapshot.getKey()).setValue(null);
                    //also work
                    //dataSnapshot.getRef().setValue(null);
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            /////
            final  DatabaseReference KaybettimPaylasimlarEsya= (DatabaseReference) firebaseDatabase.getReference("Kaybettim Paylasimlar/Esya");
            // x.removeValue();
            KaybettimPaylasimlarEsya.orderByChild("Kayıp bilgisi").equalTo(bilgisi).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    KaybettimPaylasimlarEsya.child(dataSnapshot.getKey()).setValue(null);
                    //also work
                    //dataSnapshot.getRef().setValue(null);
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            //Buldum Paylasimlar silme
            final  DatabaseReference BuldumPaylasimlarInsan= (DatabaseReference) firebaseDatabase.getReference("Buldum Paylasimlar/Insan");
            // x.removeValue();
            BuldumPaylasimlarInsan.orderByChild("Kayıp bilgisi").equalTo(bilgisi).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    BuldumPaylasimlarInsan.child(dataSnapshot.getKey()).setValue(null);
                    //also work
                    //dataSnapshot.getRef().setValue(null);
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            ////
            final  DatabaseReference BuldumPaylasimlarHayvan= (DatabaseReference) firebaseDatabase.getReference("Buldum Paylasimlar/Hayvan");
            // x.removeValue();
            BuldumPaylasimlarHayvan.orderByChild("Kayıp bilgisi").equalTo(bilgisi).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    BuldumPaylasimlarHayvan.child(dataSnapshot.getKey()).setValue(null);
                    //also work
                    //dataSnapshot.getRef().setValue(null);
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            /////
            final  DatabaseReference BuldumPaylasimlarEsya= (DatabaseReference) firebaseDatabase.getReference("Buldum Paylasimlar/Esya");
            // x.removeValue();
            BuldumPaylasimlarEsya.orderByChild("Kayıp bilgisi").equalTo(bilgisi).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    BuldumPaylasimlarEsya.child(dataSnapshot.getKey()).setValue(null);
                    //also work
                    //dataSnapshot.getRef().setValue(null);
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            Toast.makeText(getApplicationContext(),"silindi",Toast.LENGTH_LONG).show();
            adapter.notifyDataSetChanged();
            bilgisi="";
        }

    }
}
