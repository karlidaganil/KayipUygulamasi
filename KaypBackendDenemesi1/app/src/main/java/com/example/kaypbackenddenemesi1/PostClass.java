package com.example.kaypbackenddenemesi1;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostClass extends ArrayAdapter<String> {
    private final ArrayList<String> kullaniciEmailListesi;
    private final ArrayList<String> kayıpBilgileriListesi;
    private final ArrayList<String> kayipFotolariListesi;
    private  final ArrayList<String> sehirBilgisiListesi;
    private  final ArrayList<String> semtBilgisiListesi;
    private final Activity context;

    View customView;

    public PostClass(ArrayList<String> kullaniciEmailListesi, ArrayList<String> kayıpBilgileriListesi, ArrayList<String> kayipFotolariListesi, ArrayList<String> sehirBilgisiListesi, ArrayList<String> semtBilgisiListesi,Activity context) {
        super(context,R.layout.custom_view,kullaniciEmailListesi);
        this.kullaniciEmailListesi = kullaniciEmailListesi;
        this.kayıpBilgileriListesi = kayıpBilgileriListesi;
        this.kayipFotolariListesi = kayipFotolariListesi;
        this.sehirBilgisiListesi = sehirBilgisiListesi;
        this.semtBilgisiListesi = semtBilgisiListesi;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater=context.getLayoutInflater();
        View customView=layoutInflater.inflate(R.layout.custom_view,null,true);

        TextView KullanıcıEmailBilgisi=customView.findViewById(R.id.userEmailTextviewCustomView);
        TextView KayıpBilgisi=customView.findViewById(R.id.KayipBilgisitextViewCustomView);
        TextView SehirBilgisi=customView.findViewById(R.id.SehirBilgisitextViewCustomView);
        TextView SemtBilgisi=customView.findViewById(R.id.SemtBilgisitextViewCustomView);
        ImageView KayıpFotosu=customView.findViewById(R.id.kayipFotoimageViewCustomview);


        KullanıcıEmailBilgisi.setText(kullaniciEmailListesi.get(position));
        KayıpBilgisi.setText(kayıpBilgileriListesi.get(position));
        SehirBilgisi.setText(sehirBilgisiListesi.get(position));
        SemtBilgisi.setText(semtBilgisiListesi.get(position));
        Picasso.get().load(kayipFotolariListesi.get(position)).into(KayıpFotosu);


        return customView;

//        View row = null;
//
//        if(convertView==null)
//        {
//            LayoutInflater layoutInflater=context.getLayoutInflater();
//            customView=layoutInflater.inflate(R.layout.custom_view,null,true);
//        }
//        else
//        {
//            row=convertView;
//        }
//        TextView KullanıcıEmailBilgisi=customView.findViewById(R.id.userEmailTextviewCustomView);
//        TextView KayıpBilgisi=customView.findViewById(R.id.KayipBilgisitextViewCustomView);
//        TextView SehirBilgisi=customView.findViewById(R.id.SehirBilgisitextViewCustomView);
//        TextView SemtBilgisi=customView.findViewById(R.id.SemtBilgisitextViewCustomView);
//        ImageView KayıpFotosu=customView.findViewById(R.id.kayipFotoimageViewCustomview);
//
//
//        KullanıcıEmailBilgisi.setText(kullaniciEmailListesi.get(position));
//        KayıpBilgisi.setText(kayıpBilgileriListesi.get(position));
//        SehirBilgisi.setText(sehirBilgisiListesi.get(position));
//        SemtBilgisi.setText(semtBilgisiListesi.get(position));
//        Picasso.get().load(kayipFotolariListesi.get(position)).into(KayıpFotosu);
//
//
//
//
//        return row;




    }



}
