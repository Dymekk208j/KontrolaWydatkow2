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
    EditText editName;
    ListView listView ;
    ArrayList<String> listaPodKat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_kategorie);
        editName = (EditText) findViewById(R.id.editText);
        BazaDanych = new baza_danych(this);


        listView = (ListView) findViewById(R.id.LVlistaPodkategorii);

        listaPodKat = new ArrayList<String>();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listaPodKat);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                int itemPosition    = position;
                String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();

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

    }

    public void zatwierdz(View view)
    {

    }

}
