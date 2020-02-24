package com.example.kaypbackenddenemesi1;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FeedActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    BottomNavigationView bottomnavid;

    ListView listView;
    PostClass adapter;
    SearchView aramaCubugu;

    String sehirListesiForcubuk[];

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    ArrayList<String> kullanıcıEmailFromFB;
    ArrayList<String> kayipBilgisiFromFB;
    ArrayList<String> sehirBilgisiFromFB;
    ArrayList<String> semtBilgisiFromFB;
    ArrayList<String> kayipFotoFromFB;
    ArrayList<String> telefonNolarıFromFB;

    String yazılanSehirİsmi;

    String bulunduVSkayipAraması="Genel Kaybettim Paylasimlar";
    String turu="";
    Boolean yanTikladı=false;
    String KaybettimMıBuldummuPaylasimlar="";

    @Override
    public void onBackPressed() {
       //do nothing
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.setTitle("Ülke Geneli Kayıp İlanları");
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.bisi_paylas_menusu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.bisiPaylasTusuid)
        {
            Intent intent=new Intent(getApplicationContext(),uploadActivity.class);
            startActivity(intent);
        }
        if(item.getItemId()==R.id.logOutid)
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(getApplicationContext(),SignInActivity.class);
            startActivity(intent);
        }
        if(item.getItemId()==R.id.bişiBuldumTusuid)
        {
            Intent intent=new Intent(getApplicationContext(),uploadActivity.class);
            intent.putExtra("bisiBuldum",true);
            startActivity(intent);
        }
        if(toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void navViewBilgileriSetETME()
    {
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayouttt);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView=(NavigationView)findViewById(R.id.navViewid);
        bottomnavid=(BottomNavigationView)findViewById(R.id.bottomnavid);
        navigationView.setNavigationItemSelectedListener(this);
        bottomnavid.setOnNavigationItemSelectedListener(this);

        View headView=navigationView.getHeaderView(0);
        TextView hos=(TextView)headView.findViewById(R.id.hosid);
        hos.setText("HG "+FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        navViewBilgileriSetETME();

        listView=(ListView)findViewById(R.id.listView);

        kullanıcıEmailFromFB=new ArrayList<String>();
        kayipBilgisiFromFB=new ArrayList<String>();
        kayipFotoFromFB=new ArrayList<String>();
        sehirBilgisiFromFB=new ArrayList<String>();
        semtBilgisiFromFB=new ArrayList<String>();
        telefonNolarıFromFB=new ArrayList<String>();

        aramaCubugu=(SearchView)findViewById(R.id.aramaCubuk);
        setupArama();
        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();

        adapter=new PostClass(kullanıcıEmailFromFB,kayipBilgisiFromFB,kayipFotoFromFB, sehirBilgisiFromFB, semtBilgisiFromFB, this);
        // burayı değiştim
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(false);
        }

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  Toast.makeText(getApplicationContext(),sehirBilgisiFromFB.get(position),Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),telefonNolarıFromFB.get(position),Toast.LENGTH_LONG).show();
                //Intent
                Intent infoIntent=new Intent(getApplicationContext(),InfoActivity.class);
                Bundle b = new Bundle();
                b.putStringArray("info", new String[]{kayipBilgisiFromFB.get(position),sehirBilgisiFromFB.get(position),semtBilgisiFromFB.get(position),kullanıcıEmailFromFB.get(position),kayipFotoFromFB.get(position),telefonNolarıFromFB.get(position)});
                infoIntent.putExtras(b);
                startActivity(infoIntent);
            }
        });
        firebasedenDatalariAl("Genel Kaybettim Paylasimlar");
    }

    private void setupArama() {
        aramaCubugu.setIconifiedByDefault(false);
        aramaCubugu.setOnQueryTextListener(this);
        aramaCubugu.setSubmitButtonEnabled(true);
    }

    public void firebasedenDatalariAl(String hangiKayipPaylasimi)
    {
//        kullanıcıEmailFromFB.clear();
//        kayipBilgisiFromFB.clear();
//        sehirBilgisiFromFB.clear();
//        semtBilgisiFromFB.clear();
//        kayipFotoFromFB.clear();
//        DatabaseReference newReference=firebaseDatabase.getReference(hangiKayipPaylasimi);
//        newReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                kullanıcıEmailFromFB.clear();
//                kayipBilgisiFromFB.clear();
//                sehirBilgisiFromFB.clear();
//                semtBilgisiFromFB.clear();
//                kayipFotoFromFB.clear();
//
//                    for(DataSnapshot ds:dataSnapshot.getChildren())
//                    {
//                        HashMap<String,String> hashMap=(HashMap<String,String>)ds.getValue();
//
//                        kullanıcıEmailFromFB.add(hashMap.get("paylaşan e-mail"));
//                        kayipBilgisiFromFB.add(hashMap.get("Kayıp bilgisi"));
//                        sehirBilgisiFromFB.add(hashMap.get("Sehir bilgisi"));
//                        semtBilgisiFromFB.add(hashMap.get("Semt bilgisi"));
//                        kayipFotoFromFB.add(hashMap.get("Kayıp Foto URL"));
//                        adapter.notifyDataSetChanged();
//
//                        //Toast.makeText(getApplicationContext(),"YENİ EKLEMELER OLDU",Toast.LENGTH_LONG).show();
//                    }
//
//
//
//                System.out.println("FFF VALUE İSTEDİĞİM =="+dataSnapshot.getValue());
//
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


        adapter.notifyDataSetChanged();

        kullanıcıEmailFromFB.clear();
        kayipBilgisiFromFB.clear();
        sehirBilgisiFromFB.clear();
        semtBilgisiFromFB.clear();
        kayipFotoFromFB.clear();
        telefonNolarıFromFB.clear();
        DatabaseReference newReference=firebaseDatabase.getReference(hangiKayipPaylasimi);
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                kullanıcıEmailFromFB.clear();
                kayipBilgisiFromFB.clear();
                sehirBilgisiFromFB.clear();
                semtBilgisiFromFB.clear();
                kayipFotoFromFB.clear();
                telefonNolarıFromFB.clear();
                adapter.notifyDataSetChanged();

                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    HashMap<String,String> hashMap=(HashMap<String,String>)ds.getValue();

                    kullanıcıEmailFromFB.add(hashMap.get("paylaşan e-mail"));
                    kayipBilgisiFromFB.add(hashMap.get("Kayıp bilgisi"));
                    sehirBilgisiFromFB.add(hashMap.get("Sehir bilgisi"));
                    semtBilgisiFromFB.add(hashMap.get("Semt bilgisi"));
                    kayipFotoFromFB.add(hashMap.get("Kayıp Foto URL"));
                    telefonNolarıFromFB.add((hashMap.get("Tel No")));
                    adapter.notifyDataSetChanged();

                    //Toast.makeText(getApplicationContext(),"YENİ EKLEMELER OLDU",Toast.LENGTH_LONG).show();
                }



                System.out.println("FFF VALUE İSTEDİĞİM =="+dataSnapshot.getValue());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(newReference==null)
        {
            Toast.makeText(getApplicationContext(),"bos",Toast.LENGTH_SHORT).show();
        }

    }
    public void firebasedenDatalariAlTuruneGore(String hangiKayipPaylasimi,String turu)
    {
        adapter.notifyDataSetChanged();
        kullanıcıEmailFromFB.clear();
        kayipBilgisiFromFB.clear();
        sehirBilgisiFromFB.clear();
        semtBilgisiFromFB.clear();
        kayipFotoFromFB.clear();
        telefonNolarıFromFB.clear();
        DatabaseReference newReference=firebaseDatabase.getReference(hangiKayipPaylasimi).child(turu);
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                kullanıcıEmailFromFB.clear();
                kayipBilgisiFromFB.clear();
                sehirBilgisiFromFB.clear();
                semtBilgisiFromFB.clear();
                kayipFotoFromFB.clear();
                telefonNolarıFromFB.clear();
                adapter.notifyDataSetChanged();

                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    HashMap<String,String> hashMap=(HashMap<String,String>)ds.getValue();

                    kullanıcıEmailFromFB.add(hashMap.get("paylaşan e-mail"));
                    kayipBilgisiFromFB.add(hashMap.get("Kayıp bilgisi"));
                    sehirBilgisiFromFB.add(hashMap.get("Sehir bilgisi"));
                    semtBilgisiFromFB.add(hashMap.get("Semt bilgisi"));
                    kayipFotoFromFB.add(hashMap.get("Kayıp Foto URL"));
                    telefonNolarıFromFB.add(hashMap.get("Tel No"));
                    adapter.notifyDataSetChanged();

                    //Toast.makeText(getApplicationContext(),"YENİ EKLEMELER OLDU",Toast.LENGTH_LONG).show();
                }



                System.out.println("FFF VALUE İSTEDİĞİM =="+dataSnapshot.getValue());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String girilen) {
//        kullanıcıEmailFromFB.clear();
//        kayipBilgisiFromFB.clear();
//        sehirBilgisiFromFB.clear();
//        semtBilgisiFromFB.clear();
//        kayipFotoFromFB.clear();
//       // yazılanSehirİsmi=newText;
//        //adapter.notifyDataSetChanged();
//        Query querySehir=firebaseDatabase.getReference(bulunduVSkayipAraması)
//                .orderByChild("Sehir bilgisi").equalTo(girilen.toUpperCase());
//        Query querySemt=firebaseDatabase.getReference(bulunduVSkayipAraması)
//                .orderByChild("Semt bilgisi").equalTo(girilen.toUpperCase());
//        if(querySehir!=null)
//        {
//            querySehir.addValueEventListener(new ValueEventListener() {
//
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    for(DataSnapshot ds:dataSnapshot.getChildren())
//                    {
//                        HashMap<String,String> hashMap=(HashMap<String,String>)ds.getValue();
//
//                        kullanıcıEmailFromFB.add(hashMap.get("paylaşan e-mail"));
//                        kayipBilgisiFromFB.add(hashMap.get("Kayıp bilgisi"));
//                        sehirBilgisiFromFB.add(hashMap.get("Sehir bilgisi"));
//                        semtBilgisiFromFB.add(hashMap.get("Semt bilgisi"));
//                        kayipFotoFromFB.add(hashMap.get("Kayıp Foto URL"));
//                        adapter.notifyDataSetChanged();
//
//
//                    }
//                    System.out.println("FFF VALUE İSTEDİĞİM =="+dataSnapshot.getValue());
//
//
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }
//        if(querySemt!=null)
//        {
//            querySemt.addValueEventListener(new ValueEventListener() {
//
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    for(DataSnapshot ds:dataSnapshot.getChildren())
//                    {
//                        HashMap<String,String> hashMap=(HashMap<String,String>)ds.getValue();
//
//                        kullanıcıEmailFromFB.add(hashMap.get("paylaşan e-mail"));
//                        kayipBilgisiFromFB.add(hashMap.get("Kayıp bilgisi"));
//                        sehirBilgisiFromFB.add(hashMap.get("Sehir bilgisi"));
//                        semtBilgisiFromFB.add(hashMap.get("Semt bilgisi"));
//                        kayipFotoFromFB.add(hashMap.get("Kayıp Foto URL"));
//                        adapter.notifyDataSetChanged();
//
//
//                    }
//                    System.out.println("FFF VALUE İSTEDİĞİM =="+dataSnapshot.getValue());
//
//
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }

          //  turuneGoreFiltrele("Kaybettim Paylasimlar",girilen);

        //listView.setAdapter(adapter);





//        if(yanTiklanan.isEmpty())
//        {
//            aramaYardımcısı(girilen,"Hayvan");
//            aramaYardımcısı(girilen,"Insan");
//            aramaYardımcısı(girilen,"Esya");
//        }
//
//        else if(yanTiklanan.equalsIgnoreCase("Insan"))
//        {
//            aramaYardımcısı(girilen,"Insan");
//        }
//        else if(yanTiklanan.equalsIgnoreCase("Hayvan"))
//        {
//            aramaYardımcısı(girilen,"Hayvan");
//        }
//        else if(yanTiklanan.equalsIgnoreCase("Esya"))
//        {
//            aramaYardımcısı(girilen,"Esya");
//        }

        if(yanTikladı==false)
        {
            aramaCubuguAyarlama(girilen);
        }
        else if(yanTikladı==true)
        {
            aramaCubuguTuruneGoreAyarlama(girilen,turu);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {


//          if(newText.isEmpty())
//          {
//              yanTiklanan="";
//              if(bulunduVSkayipAraması=="Kaybettim Paylasimlar")
//              {
//                  firebasedenDatalariAl("Kaybettim Paylasimlar");
//              }
//              if(bulunduVSkayipAraması=="Buldum Paylasimlar")
//              {
//                  firebasedenDatalariAl("Buldum Paylasimlar");
//              }
//
//          }


        if(yanTikladı==false)
        {
            if(newText.isEmpty())
            {
                firebasedenDatalariAl(bulunduVSkayipAraması);
            }
        }
        else if(yanTikladı==true)
        {
            if(newText.isEmpty())
            {
                firebasedenDatalariAlTuruneGore(KaybettimMıBuldummuPaylasimlar,turu);
            }
        }





        //listView.setAdapter(null);
        return false; //true da olablir
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId()==R.id.ilkYanMenuİtemid)   //Genel Kayıp İlanları
        {
            this.setTitle("Ülke Geneli Kayıp İlanları");
            Toast.makeText(getApplicationContext(),"ilk yan menu tuşu",Toast.LENGTH_LONG).show();
//            firebasedenDatalariAl("Kaybettim Paylasimlar");

//            yanTiklanan="";
            drawerLayout.closeDrawers();
            bulunduVSkayipAraması="Genel Kaybettim Paylasimlar";
            firebasedenDatalariAl("Genel Kaybettim Paylasimlar");
            yanTikladı=false;
        }
        if(menuItem.getItemId()==R.id.ikinciYanMenuİtemid)    //Genel Bulunan Ilanları
        {
            this.setTitle("Ülke Geneli Bulunanların İlanları");
         //   Toast.makeText(getApplicationContext(),"ikinci yan menu tuşu",Toast.LENGTH_LONG).show();
//            firebasedenDatalariAl("Buldum Paylasimlar");
//            yanTiklanan="";
            drawerLayout.closeDrawers();
            bulunduVSkayipAraması="Genel Buldum Paylasimlar";
            firebasedenDatalariAl("Genel Buldum Paylasimlar");
            yanTikladı=false;
        }
        if(menuItem.getItemId()==R.id.insanKayiplariMenuitemid)
        {
           // turuneGoreFiltrele("Kaybettim Paylasimlar","Insan");
            Toast.makeText(getApplicationContext(),"insan Kayıpları",Toast.LENGTH_LONG).show();
          //  bulunduVSkayipAraması="Kaybettim Paylasimlar";
           // yanTiklanan="Insan";
            drawerLayout.closeDrawers();
            firebasedenDatalariAlTuruneGore("Kaybettim Paylasimlar","Insan");
            yanTikladı=true;
            turu="Insan";
            KaybettimMıBuldummuPaylasimlar="Kaybettim Paylasimlar";
            return false;
        }
        if(menuItem.getItemId()==R.id.hayvanKayiplariMenuitemid)
        {
           // turuneGoreFiltrele("Kaybettim Paylasimlar","Hayvan");
            Toast.makeText(getApplicationContext(),"Hayvan Kayıpları",Toast.LENGTH_LONG).show();
//            bulunduVSkayipAraması="Kaybettim Paylasimlar";
//            turu="Hayvan";
//            yanTiklanan="Hayvan";
            drawerLayout.closeDrawers();
            firebasedenDatalariAlTuruneGore("Kaybettim Paylasimlar","Hayvan");
            yanTikladı=true;
            turu="Hayvan";
            KaybettimMıBuldummuPaylasimlar="Kaybettim Paylasimlar";
        }
        if(menuItem.getItemId()==R.id.esyaKayiplariMenuitemid)
        {
           // turuneGoreFiltrele("Kaybettim Paylasimlar","Esya");
            Toast.makeText(getApplicationContext(),"Esya Kayıpları",Toast.LENGTH_LONG).show();
//            bulunduVSkayipAraması="Kaybettim Paylasimlar";
//            yanTiklanan="Esya";
            drawerLayout.closeDrawers();
            firebasedenDatalariAlTuruneGore("Kaybettim Paylasimlar","Esya");
            yanTikladı=true;
            turu="Esya";
            KaybettimMıBuldummuPaylasimlar="Kaybettim Paylasimlar";
        }
        if(menuItem.getItemId()==R.id.insanBulunanlariMenuitemid)
        {
          //  turuneGoreFiltrele("Buldum Paylasimlar","Insan");
            Toast.makeText(getApplicationContext(),"Insan Bulunanlar",Toast.LENGTH_LONG).show();
//            bulunduVSkayipAraması="Buldum Paylasimlar";
//            yanTiklanan="Insan";
            drawerLayout.closeDrawers();
            firebasedenDatalariAlTuruneGore("Buldum Paylasimlar","Insan");
            yanTikladı=true;
            turu="Insan";
            KaybettimMıBuldummuPaylasimlar="Buldum Paylasimlar";
        }
        if(menuItem.getItemId()==R.id.hayvanBulunanlariMenuitemid)
        {
          //  turuneGoreFiltrele("Buldum Paylasimlar","Hayvan");
            Toast.makeText(getApplicationContext(),"Hayvan Bulunanlar",Toast.LENGTH_LONG).show();
//            bulunduVSkayipAraması="Buldum Paylasimlar";
//            yanTiklanan="Hayvan";
            drawerLayout.closeDrawers();
            firebasedenDatalariAlTuruneGore("Buldum Paylasimlar","Hayvan");
            yanTikladı=true;
            turu="Hayvan";
            KaybettimMıBuldummuPaylasimlar="Buldum Paylasimlar";
        }
        if(menuItem.getItemId()==R.id.esyaKBulunanlariMenuitemid)
        {
          //  turuneGoreFiltrele("Buldum Paylasimlar","Esya");
            Toast.makeText(getApplicationContext(),"Esya Bulunanlar",Toast.LENGTH_LONG).show();
//            bulunduVSkayipAraması="Buldum Paylasimlar";
//            yanTiklanan="Esya";
            drawerLayout.closeDrawers();
            firebasedenDatalariAlTuruneGore("Buldum Paylasimlar","Esya");
            yanTikladı=true;
            turu="Esya";
            KaybettimMıBuldummuPaylasimlar="Buldum Paylasimlar";
        }
        if(menuItem.getItemId()==R.id.homeid)
        {

        }
        if(menuItem.getItemId()==R.id.myprofilid)
        {
            Intent intent=new Intent(getApplicationContext(),PaylasimlarimActivity.class);
            startActivity(intent);
        }
        return false;
    }

    public void turuneGoreFiltrele(String kayıpvsBulundu,String kayıpTuru)
    {
        kullanıcıEmailFromFB.clear();
        kayipBilgisiFromFB.clear();
        sehirBilgisiFromFB.clear();
        semtBilgisiFromFB.clear();
        kayipFotoFromFB.clear();
        telefonNolarıFromFB.clear();
        Query turuQuery=firebaseDatabase.getReference(kayıpvsBulundu).child(kayıpTuru)
                .orderByChild("Turu").equalTo(kayıpTuru);
        turuQuery.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    HashMap<String,String> hashMap=(HashMap<String,String>)ds.getValue();

                    kullanıcıEmailFromFB.add(hashMap.get("paylaşan e-mail"));
                    kayipBilgisiFromFB.add(hashMap.get("Kayıp bilgisi"));
                    sehirBilgisiFromFB.add(hashMap.get("Sehir bilgisi"));
                    semtBilgisiFromFB.add(hashMap.get("Semt bilgisi"));
                    kayipFotoFromFB.add(hashMap.get("Kayıp Foto URL"));
                    telefonNolarıFromFB.add(hashMap.get("Tel No"));
                    adapter.notifyDataSetChanged();


                }
                System.out.println("FFF VALUE İSTEDİĞİM =="+dataSnapshot.getValue());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

//    public void aramaYardımcısı(String girilen,String Turu)
//    {
//        kullanıcıEmailFromFB.clear();
//        kayipBilgisiFromFB.clear();
//        sehirBilgisiFromFB.clear();
//        semtBilgisiFromFB.clear();
//        kayipFotoFromFB.clear();
//        // yazılanSehirİsmi=newText;
//        //adapter.notifyDataSetChanged();
//        Query querySehir=firebaseDatabase.getReference(bulunduVSkayipAraması).child(Turu)
//                .orderByChild("Sehir bilgisi").equalTo(girilen.toUpperCase());
//        Query querySemt=firebaseDatabase.getReference(bulunduVSkayipAraması).child(Turu)
//                .orderByChild("Semt bilgisi").equalTo(girilen.toUpperCase());
//        if(querySehir!=null)
//        {
//            querySehir.addValueEventListener(new ValueEventListener() {
//
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    for(DataSnapshot ds:dataSnapshot.getChildren())
//                    {
//                        HashMap<String,String> hashMap=(HashMap<String,String>)ds.getValue();
//
//                        kullanıcıEmailFromFB.add(hashMap.get("paylaşan e-mail"));
//                        kayipBilgisiFromFB.add(hashMap.get("Kayıp bilgisi"));
//                        sehirBilgisiFromFB.add(hashMap.get("Sehir bilgisi"));
//                        semtBilgisiFromFB.add(hashMap.get("Semt bilgisi"));
//                        kayipFotoFromFB.add(hashMap.get("Kayıp Foto URL"));
//                        adapter.notifyDataSetChanged();
//
//
//                    }
//                    System.out.println("FFF VALUE İSTEDİĞİM =="+dataSnapshot.getValue());
//
//
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }
//        if(querySemt!=null)
//        {
//            querySemt.addValueEventListener(new ValueEventListener() {
//
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    for(DataSnapshot ds:dataSnapshot.getChildren())
//                    {
//                        HashMap<String,String> hashMap=(HashMap<String,String>)ds.getValue();
//
//                        kullanıcıEmailFromFB.add(hashMap.get("paylaşan e-mail"));
//                        kayipBilgisiFromFB.add(hashMap.get("Kayıp bilgisi"));
//                        sehirBilgisiFromFB.add(hashMap.get("Sehir bilgisi"));
//                        semtBilgisiFromFB.add(hashMap.get("Semt bilgisi"));
//                        kayipFotoFromFB.add(hashMap.get("Kayıp Foto URL"));
//                        adapter.notifyDataSetChanged();
//
//
//                    }
//                    System.out.println("FFF VALUE İSTEDİĞİM =="+dataSnapshot.getValue());
//
//
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }
//    }
    public void aramaCubuguAyarlama(String girilen)
    {
        kullanıcıEmailFromFB.clear();
        kayipBilgisiFromFB.clear();
        sehirBilgisiFromFB.clear();
        semtBilgisiFromFB.clear();
        kayipFotoFromFB.clear();
        telefonNolarıFromFB.clear();
        // yazılanSehirİsmi=newText;
        //adapter.notifyDataSetChanged();
        Query querySehir=firebaseDatabase.getReference(bulunduVSkayipAraması)
                .orderByChild("Sehir bilgisi").equalTo(girilen.toUpperCase());
        Query querySemt=firebaseDatabase.getReference(bulunduVSkayipAraması)
                .orderByChild("Semt bilgisi").equalTo(girilen.toUpperCase());
        if(querySehir!=null)
        {
            querySehir.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds:dataSnapshot.getChildren())
                    {
                        HashMap<String,String> hashMap=(HashMap<String,String>)ds.getValue();

                        kullanıcıEmailFromFB.add(hashMap.get("paylaşan e-mail"));
                        kayipBilgisiFromFB.add(hashMap.get("Kayıp bilgisi"));
                        sehirBilgisiFromFB.add(hashMap.get("Sehir bilgisi"));
                        semtBilgisiFromFB.add(hashMap.get("Semt bilgisi"));
                        kayipFotoFromFB.add(hashMap.get("Kayıp Foto URL"));
                        telefonNolarıFromFB.add(hashMap.get("Tel No"));
                        adapter.notifyDataSetChanged();


                    }
                    System.out.println("FFF VALUE İSTEDİĞİM =="+dataSnapshot.getValue());



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        if(querySemt!=null)
        {
            querySemt.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds:dataSnapshot.getChildren())
                    {
                        HashMap<String,String> hashMap=(HashMap<String,String>)ds.getValue();

                        kullanıcıEmailFromFB.add(hashMap.get("paylaşan e-mail"));
                        kayipBilgisiFromFB.add(hashMap.get("Kayıp bilgisi"));
                        sehirBilgisiFromFB.add(hashMap.get("Sehir bilgisi"));
                        semtBilgisiFromFB.add(hashMap.get("Semt bilgisi"));
                        kayipFotoFromFB.add(hashMap.get("Kayıp Foto URL"));
                        telefonNolarıFromFB.add(hashMap.get("Tel No"));
                        adapter.notifyDataSetChanged();


                    }
                    System.out.println("FFF VALUE İSTEDİĞİM =="+dataSnapshot.getValue());



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    public void aramaCubuguTuruneGoreAyarlama(String girilen,String turu)
    {
        kullanıcıEmailFromFB.clear();
        kayipBilgisiFromFB.clear();
        sehirBilgisiFromFB.clear();
        semtBilgisiFromFB.clear();
        kayipFotoFromFB.clear();
        telefonNolarıFromFB.clear();
        // yazılanSehirİsmi=newText;
        //adapter.notifyDataSetChanged();
        Query querySehir=firebaseDatabase.getReference(KaybettimMıBuldummuPaylasimlar).child(turu)
                .orderByChild("Sehir bilgisi").equalTo(girilen.toUpperCase());
        Query querySemt=firebaseDatabase.getReference(KaybettimMıBuldummuPaylasimlar).child(turu)
                .orderByChild("Semt bilgisi").equalTo(girilen.toUpperCase());
        if(querySehir!=null)
        {
            querySehir.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds:dataSnapshot.getChildren())
                    {
                        HashMap<String,String> hashMap=(HashMap<String,String>)ds.getValue();

                        kullanıcıEmailFromFB.add(hashMap.get("paylaşan e-mail"));
                        kayipBilgisiFromFB.add(hashMap.get("Kayıp bilgisi"));
                        sehirBilgisiFromFB.add(hashMap.get("Sehir bilgisi"));
                        semtBilgisiFromFB.add(hashMap.get("Semt bilgisi"));
                        kayipFotoFromFB.add(hashMap.get("Kayıp Foto URL"));
                        telefonNolarıFromFB.add(hashMap.get("Tel No"));
                        adapter.notifyDataSetChanged();


                    }
                    System.out.println("FFF VALUE İSTEDİĞİM =="+dataSnapshot.getValue());



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        if(querySemt!=null)
        {
            querySemt.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds:dataSnapshot.getChildren())
                    {
                        HashMap<String,String> hashMap=(HashMap<String,String>)ds.getValue();

                        kullanıcıEmailFromFB.add(hashMap.get("paylaşan e-mail"));
                        kayipBilgisiFromFB.add(hashMap.get("Kayıp bilgisi"));
                        sehirBilgisiFromFB.add(hashMap.get("Sehir bilgisi"));
                        semtBilgisiFromFB.add(hashMap.get("Semt bilgisi"));
                        kayipFotoFromFB.add(hashMap.get("Kayıp Foto URL"));
                        telefonNolarıFromFB.add(hashMap.get("Tel No"));
                        adapter.notifyDataSetChanged();


                    }
                    System.out.println("FFF VALUE İSTEDİĞİM =="+dataSnapshot.getValue());



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

public void fabMethod(View view)
{
    Toast.makeText(getApplicationContext(),"fab tıklandı",Toast.LENGTH_LONG).show();
}

}
