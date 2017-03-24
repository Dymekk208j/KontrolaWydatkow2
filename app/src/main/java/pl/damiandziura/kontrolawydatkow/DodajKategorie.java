package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DodajKategorie extends AppCompatActivity {
private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_kategorie);
        EditText editName = (EditText) findViewById(R.id.editText);
        editName.setSelected(false);
    }

    public void cofnij(View view) {
        intent = new Intent(this, Kategorie.class);
        startActivity(intent);


    }

    public void dodaj(View view)
    {

    }

    public void usun(View view)
    {

    }

    public void zmienNazwe(View view)
    {

    }

    public void dodajPodKat(View view)
    {
        TextView lblNazwaPodk = (TextView) findViewById(R.id.lblNazwaPodKat);
        EditText txtNazwaPodkat = (EditText) findViewById(R.id.txtNazwaPodkat);
        Button btZatwierdz = (Button) findViewById(R.id.btZatwierdz);

        lblNazwaPodk.setVisibility(View.VISIBLE);
        txtNazwaPodkat.setVisibility(View.VISIBLE);
        btZatwierdz.setVisibility(View.VISIBLE);
    }

    public void zatwierdz(View view)
    {

    }

}
