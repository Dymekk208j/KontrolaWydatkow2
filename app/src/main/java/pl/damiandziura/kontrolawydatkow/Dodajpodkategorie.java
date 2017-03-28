package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Dodajpodkategorie extends AppCompatActivity {
    private baza_danych BazaDanych;
    private int IdKategorii =0;
    private int IdPodKategorii =0;
    private EditText NazwaPodkat;
    private String WindowName = "";
    private Button btUsun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodajpodkategorie);
        Bundle extras = getIntent().getExtras();
        TextView lblWindowName;
        BazaDanych = new baza_danych(this);
        NazwaPodkat = (EditText) findViewById(R.id.txtPodKatName);
        btUsun = (Button) findViewById(R.id.btUsun);

        if(extras != null)
        {
            IdKategorii = extras.getInt("NumerKategorii");
            IdPodKategorii = extras.getInt("NumerPodKategorii");
            WindowName = extras.getString("Nazwa_okna");
        }
        Toast.makeText(this, "IdKategorii: " + IdKategorii, Toast.LENGTH_SHORT).show();
        if(!WindowName.equals(""))
        {
            lblWindowName = (TextView) findViewById(R.id.lblWindowNamePodKat);
            lblWindowName.setText(WindowName);
        }
        if(IdKategorii == 0 && IdPodKategorii != 0)//czeli edycja
        {
            //NazwaPodkat.
        }
    }

    public void cofnij(View view) {
        Intent intent = new Intent(this, DodajKategorie.class);
        intent.putExtra("IdKategorii", IdKategorii);
        startActivity(intent);
    }
    public void dodaj(View view)
    {
        if(IdKategorii > 0) //dodawanie nowej kategorii
        {
            if (!NazwaPodkat.getText().toString().equals("")) {
                BazaDanych.AddPodKategoria(NazwaPodkat.getText().toString(), IdKategorii);
                Toast.makeText(this, "Dodano nową podkategorie " + NazwaPodkat.getText().toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, DodajKategorie.class);
                intent.putExtra("IdKategorii", IdKategorii);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Pole nazwy nie może być puste!", Toast.LENGTH_SHORT).show();
            }
        }
        else // edycja kategorii
        {

        }
    }

}
