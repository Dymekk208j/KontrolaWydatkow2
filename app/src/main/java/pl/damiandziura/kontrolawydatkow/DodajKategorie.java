package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class DodajKategorie extends AppCompatActivity
{
    private Intent intent;
    private baza_danych BazaDanych;
    private EditText editName;
    private ListView listView ;
    private ArrayList<String> listaPodKat;
    private int IdKategorii = 0;
    private int IdPodKategorii = 0;
    private ArrayList<Integer> PodkategoriaIdList;
    String Nazwa_okna = "";
    String Nazwa_kategorii;
    TextView lblNazwaOkna;
    Boolean EdycjaKategorii = false;
    ArrayList<String> ListaPodkategori;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dodaj_kategorie);
        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            IdKategorii = extras.getInt("IdKategorii");
            Nazwa_okna = extras.getString("nazwa_okna");
            EdycjaKategorii = extras.getBoolean("edycja");
            Nazwa_kategorii = extras.getString("Nazwa_kategorii");

            String Buffor[] = extras.getStringArray("ListaPodkategori");

            if(Buffor == null)
            {
                ListaPodkategori = new ArrayList<String>();
                ListaPodkategori.add(0, "(Domyślna)");
            }else
            {
                ListaPodkategori = new ArrayList<String>(Arrays.asList(Buffor));
                ListaPodkategori.add(0, "(Domyślna)");
            }

        }


        editName = (EditText) findViewById(R.id.editText);
        BazaDanych = new baza_danych(this);
        lblNazwaOkna = (TextView) findViewById(R.id.lblName);

        if(!Nazwa_okna.equals("")) lblNazwaOkna.setText(Nazwa_okna);

        listView = (ListView) findViewById(R.id.LVlistaPodkategorii);

        if(EdycjaKategorii == true)//edycja kategorii
        {
            listaPodKat = BazaDanych.getpodKategorie(IdKategorii);
            listaPodKat.remove(0);
            PodkategoriaIdList = BazaDanych.getINTpodKategorie(IdKategorii);
            editName.setText(BazaDanych.getKategoriaName(IdKategorii));

        }else
        {
            listaPodKat = ListaPodkategori;
           listaPodKat.remove(0);
        }

        if(Nazwa_kategorii != null) editName.setText(Nazwa_kategorii);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listaPodKat);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {

                int bufor = position;

                    edytujPodKat(bufor);


            }

        });

    }

    public void cofnij(View view) {
        intent = new Intent(this, Kategorie.class);
        startActivity(intent);


    }

    public void dodaj(View view)
    {
        if(EdycjaKategorii == true)
        {
            BazaDanych.AddKategoria(editName.getText().toString());
            Toast.makeText(this, "Nowa kategoria " + editName.getText().toString() + " została dodana.", Toast.LENGTH_SHORT).show();
            intent = new Intent(this, Kategorie.class);
            startActivity(intent);
        }
        else
        {
            BazaDanych.AddKategoria(editName.getText().toString());
            //SELECT id FROM Kategoria ORDER BY id DESC LIMIT 1
            for(int a = 0; a < listaPodKat.size(); a++)
            {
                BazaDanych.AddPodKategoria(listaPodKat.get(a), BazaDanych.getKategoriaMaxId());
            }
            Toast.makeText(this, "Nowa kategoria " + editName.getText().toString() + " została dodana.", Toast.LENGTH_SHORT).show();
            intent = new Intent(this, Kategorie.class);
            startActivity(intent);
        }

    }

    public void usun(View view)
    {

    }

    public void zmienNazwe(View view)
    {

    }

    public void dodajPodKat(View view)
    {
        Intent intent = new Intent(this, Dodajpodkategorie.class);

        if(EdycjaKategorii == true)
        {
            intent.putExtra("NumerKategorii", IdKategorii);
            intent.putExtra("NumerPodKategorii", 0);
            intent.putExtra("Nazwa_okna", getResources().getString(R.string.str_dodaj_podk));
            intent.putExtra("edycja", true);
            intent.putExtra("Nazwa_kategorii", editName.getText().toString());

        }
        else
        {
            intent.putExtra("NumerKategorii", 0);
            intent.putExtra("NumerPodKategorii", 0);
            String listapodkat[] = ListaPodkategori.toArray(new String[0]);
            intent.putExtra("Nazwa_okna", "Edytuj podkategorie");
            intent.putExtra("Nazwa_kategorii", editName.getText().toString());
            intent.putExtra("listaPodkategorii", listapodkat);
            intent.putExtra("edycja", false);
        }
        startActivity(intent);


    }

    public void edytujPodKat(int IdPodkategorii)
    {
        if(EdycjaKategorii == true)
        {
            Intent intent = new Intent(this, Dodajpodkategorie.class);
            intent.putExtra("NumerKategorii", IdKategorii);
            intent.putExtra("NumerPodKategorii", PodkategoriaIdList.get(IdPodkategorii+1));
            intent.putExtra("Nazwa_okna", getResources().getString(R.string.strEdycjaPodKategorii));
            intent.putExtra("Nazwa_kategorii", editName.getText().toString());
            intent.putExtra("edycja", true);
            startActivity(intent);
        }else
        {
            int bufor = IdPodkategorii+1;
            Intent intent = new Intent(this, Dodajpodkategorie.class);
            intent.putExtra("NumerKategorii", 0);
            intent.putExtra("NumerPodKategorii", bufor);
            intent.putExtra("Nazwa_okna", getResources().getString(R.string.strEdycjaPodKategorii));
            intent.putExtra("Nazwa_kategorii", editName.getText().toString());
            intent.putExtra("edycja", false);
            String listapodkat[] = ListaPodkategori.toArray(new String[0]);
            intent.putExtra("listaPodkategorii", listapodkat);

            /*
             intent.putExtra("NumerKategorii", 0);
            intent.putExtra("NumerPodKategorii", 0);

            intent.putExtra("Nazwa_okna", "Edytuj podkategorie");
            intent.putExtra("Nazwa_kategorii", editName.getText().toString());

            intent.putExtra("edycja", false);
             */
            startActivity(intent);
        }

    }

    public void zatwierdz(View view)
    {

    }

}
