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
import java.util.Arrays;

public class Dodajpodkategorie extends AppCompatActivity {
    private baza_danych BazaDanych;
    private int IdKategorii =0;
    private int IdPodKategorii =0;
    private EditText NazwaPodkat;
    private String WindowName = "";
    private Button btUsun;
    boolean edycja = false;
    TextView lblWindowName;
    ArrayList<String> ListaPodkategorii;
    String BufforList[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodajpodkategorie);
        Bundle extras = getIntent().getExtras();
        BazaDanych = new baza_danych(this);
        NazwaPodkat = (EditText) findViewById(R.id.txtPodKatName);
        btUsun = (Button) findViewById(R.id.btUsun);


        if(extras != null)
        {

            IdKategorii = extras.getInt("NumerKategorii");
            IdPodKategorii = extras.getInt("NumerPodKategorii");
            WindowName = extras.getString("Nazwa_okna");
            edycja = extras.getBoolean("edycja");
            BufforList = extras.getStringArray("listaPodkategorii");
            ListaPodkategorii = new ArrayList<String>(Arrays.asList(BufforList));


        }
        if(edycja == false)
        {
            btUsun.setVisibility(View.INVISIBLE);

        }
        else
        {
            NazwaPodkat.setText(BazaDanych.getPodKategoriaName(IdPodKategorii));
            btUsun.setVisibility(View.VISIBLE);
        }
        //Toast.makeText(this, "NumerPodKategorii: " + IdPodKategorii, Toast.LENGTH_SHORT).show();
        if(!WindowName.equals(""))
        {
            lblWindowName = (TextView) findViewById(R.id.lblWindowNamePodKat);
            lblWindowName.setText(WindowName);
        }


    }

    public void cofnij(View view) {
        Intent intent = new Intent(this, DodajKategorie.class);
        intent.putExtra("IdKategorii", IdKategorii);
        intent.putExtra("nazwa_okna", "Edycja kategorii:");
        startActivity(intent);
    }
    public void dodaj(View view)
    {
        if(edycja == false) //Dodawanie nowej podkategorii do istniejace kategorii
        {
            if (!NazwaPodkat.getText().toString().equals("")) {
                BazaDanych.AddPodKategoria(NazwaPodkat.getText().toString(), IdKategorii);
                Toast.makeText(this, "Dodano nową podkategorie " + NazwaPodkat.getText().toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, DodajKategorie.class);
                intent.putExtra("IdKategorii", IdKategorii);
                intent.putExtra("nazwa_okna", "Edycja kategorii:");
                startActivity(intent);
            } else {
                Toast.makeText(this, "Pole nazwy nie może być puste!", Toast.LENGTH_SHORT).show();
            }
        }
        else //Dodawanie nowej podkategorii do jeszcze nie istniejace kategorii
        {
            ListaPodkategorii.add(NazwaPodkat.getText().toString());
            BufforList = ListaPodkategorii.toArray(new String[0]);
            Intent intent = new Intent(this, DodajKategorie.class);
            intent.putExtra("IdKategorii", IdKategorii);
            intent.putExtra("nazwa_okna", "Dodaj kategorie:");
            intent.putExtra("edycja", false);
            intent.putExtra("listapodkategorii", BufforList);
            startActivity(intent);

        }
    }

    public void usun(View view)
    {
        BazaDanych.RemovePodKategoria(IdPodKategorii);
        Toast.makeText(this, "Pomyślnie usunięto podkategorie " + BazaDanych.getPodKategoriaName(IdPodKategorii), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DodajKategorie.class);
        intent.putExtra("IdKategorii", IdKategorii);
        intent.putExtra("nazwa_okna", "Edycja kategorii:");
        startActivity(intent);
    }


}
