package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
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

public class DodajKategorie extends AppCompatActivity
{
    private Intent intent;
    private baza_danych BazaDanych;
    private EditText editName;
    private ListView listView ;
    private ArrayList<String> listaPodKat;
    private int IdKategorii = 0;
    private int IdPodKategorii = 0;
    ArrayList<Integer> PodkategoriaIdList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dodaj_kategorie);
        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            IdKategorii = extras.getInt("IdKategorii");
        }

        editName = (EditText) findViewById(R.id.editText);
        BazaDanych = new baza_danych(this);


        listView = (ListView) findViewById(R.id.LVlistaPodkategorii);

        if(IdKategorii > 0)
        {
            listaPodKat = BazaDanych.getpodKategorie(IdKategorii);
            listaPodKat.remove(0);
            PodkategoriaIdList = BazaDanych.getINTpodKategorie(IdKategorii);
        }else
        {
            listaPodKat = new ArrayList<String>();
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listaPodKat);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                IdPodKategorii = PodkategoriaIdList.get(position+1);
                edytujPodKat(IdPodKategorii);
               // Toast.makeText(getApplicationContext(), "IdPodKategorii " + IdPodKategorii, Toast.LENGTH_SHORT).show();

            }

        });

    }

    public void cofnij(View view) {
        intent = new Intent(this, Kategorie.class);
        startActivity(intent);


    }

    public void dodaj(View view)
    {
        BazaDanych.AddKategoria(editName.getText().toString());
        Toast.makeText(this, "Nowa kategoria " + editName.getText().toString() + " została dodana.", Toast.LENGTH_SHORT).show();
        intent = new Intent(this, Kategorie.class);
        startActivity(intent);
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
        intent.putExtra("NumerKategorii", IdKategorii);
        intent.putExtra("NumerPodKategorii", 0);
        intent.putExtra("Nazwa_okna", getResources().getString(R.string.str_dodaj_podk));
        startActivity(intent);
    }

    public void edytujPodKat(int IdPodkategorii)
    {
        Intent intent = new Intent(this, Dodajpodkategorie.class);
        intent.putExtra("NumerKategorii", 0);
        intent.putExtra("NumerPodKategorii", IdPodkategorii);
        intent.putExtra("Nazwa_okna", getResources().getString(R.string.strEdycjaPodKategorii));
        startActivity(intent);
    }

    public void zatwierdz(View view)
    {

    }

}
