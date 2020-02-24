package com.example.kaypbackenddenemesi1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class InfoActivity extends AppCompatActivity {
    ImageView kayipOrBulunanİnfoFotosu;
    TextView Bilgisi;
    TextView Ili;
    TextView Semt;
    TextView PaylasanKisi;
    TextView telNoBilgisi;
    String gerekliTumBilgiler[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTitle("info activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);
        kayipOrBulunanİnfoFotosu=(ImageView)findViewById(R.id.infoImageViewid);
        Bilgisi=(TextView)findViewById(R.id.paylasimBilgisiid);
        Ili=(TextView)findViewById(R.id.ilBilgisiid);
        Semt=(TextView)findViewById(R.id.SemtBilgisiid);
        PaylasanKisi=(TextView)findViewById(R.id.paylasanKisiid);
        telNoBilgisi=(TextView)findViewById(R.id.telNoTextViewİnfo);

        Bundle b = this.getIntent().getExtras();
        gerekliTumBilgiler=b.getStringArray("info");

        Bilgisi.setText(gerekliTumBilgiler[0]);
        Ili.setText(gerekliTumBilgiler[1]);
        Semt.setText(gerekliTumBilgiler[2]);
        PaylasanKisi.setText(gerekliTumBilgiler[3]);
        telNoBilgisi.setText(gerekliTumBilgiler[5]);
        Picasso.get().load(gerekliTumBilgiler[4]).into(kayipOrBulunanİnfoFotosu);

    }
    public void ara(View view)
    {
        String tel="tel:";
        tel=tel+gerekliTumBilgiler[5];
        Intent intent=new Intent(Intent.ACTION_DIAL);
//      intent.setData(Uri.parse(("tel:123456789")));
        intent.setData(Uri.parse((tel)));
        startActivity(intent);
    }
}
