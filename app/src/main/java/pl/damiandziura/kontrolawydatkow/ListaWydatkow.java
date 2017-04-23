package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class ListaWydatkow extends AppCompatActivity {

    private int ID_KATEGORII = 0;
    private int ID_PODKATEGORI = 0;
    boolean WydatekDochod;

    private int LAST_LP = 0;

    private baza_danych BazaDanych;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_wydatkow);
        BazaDanych = new baza_danych(this);
        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            ID_PODKATEGORI = extras.getInt("IdPodKategorii");
            ID_KATEGORII = extras.getInt("IdKategorii");
            WydatekDochod = extras.getBoolean("WydatekDochod");
        }

        String Nazwa_id_kategorii, Nazwa_id_podkategorii;
        if(ID_KATEGORII != 0) {
            Nazwa_id_kategorii = BazaDanych.getKategoriaName(ID_KATEGORII);
        }else{ Nazwa_id_kategorii = "Domyślna kategoria";}

        if(ID_PODKATEGORI != 0) {
            Nazwa_id_podkategorii = BazaDanych.getPodKategoriaName(ID_PODKATEGORI);
        }else{ Nazwa_id_podkategorii = "Domyślna podkategoria";}

        setTitle(Nazwa_id_kategorii + "/" + Nazwa_id_podkategorii + "/Wydatki");

        ArrayList<String> ListaNazwa = BazaDanych.getWydatkiNames(ID_KATEGORII, ID_PODKATEGORI);
        ArrayList<String> ListaDat = BazaDanych.getWydatkiDaty(ID_KATEGORII, ID_PODKATEGORI);
        ArrayList<Double> ListaKwot = BazaDanych.getWydatkiKwoty(ID_KATEGORII, ID_PODKATEGORI);

        for(int a = 0; a < ListaNazwa.size(); a++)
        {
            DodajDane(ListaNazwa.get(a), ListaKwot.get(a), ListaDat.get(a));
        }



    }

    public void DodajDane(String _Nazwa, double _kwota, String _data)
    {
        /* Find Tablelayout defined in main.xml */
        TableLayout tl = (TableLayout) findViewById(R.id.TableNazwa);
        /* Create a new row to be added. */
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        /* Create a Button to be the row-content. */
        TextView b = new TextView(this);
        b.setText(_Nazwa);
        b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        /* Add Button to row. */
        tr.addView(b);
        /* Add row to TableLayout. */
        //tr.setBackgroundResource(R.drawable.sf_gradient_03);
        tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        /* Find Tablelayout defined in main.xml */
        tl = (TableLayout) findViewById(R.id.TableLp);
        /* Create a new row to be added. */
        tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        /* Create a Button to be the row-content. */
        b = new TextView(this);
        b.setText(Integer.toString(LAST_LP) + "." );
        LAST_LP++;
        b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        /* Add Button to row. */
        tr.addView(b);
        /* Add row to TableLayout. */
        //tr.setBackgroundResource(R.drawable.sf_gradient_03);
        tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        /* Find Tablelayout defined in main.xml */
        tl = (TableLayout) findViewById(R.id.TableKwota);
        /* Create a new row to be added. */
        tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        /* Create a Button to be the row-content. */
        b = new TextView(this);
        b.setText(Double.toString(_kwota) + " zł");
        b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        /* Add Button to row. */
        tr.addView(b);
        /* Add row to TableLayout. */
        //tr.setBackgroundResource(R.drawable.sf_gradient_03);
        tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        /* Find Tablelayout defined in main.xml */
        tl = (TableLayout) findViewById(R.id.TableData);
        /* Create a new row to be added. */
        tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        /* Create a Button to be the row-content. */
        b = new TextView(this);
        b.setText(_data);
        b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        /* Add Button to row. */
        tr.addView(b);
        /* Add row to TableLayout. */
        //tr.setBackgroundResource(R.drawable.sf_gradient_03);
        tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

    }

    public void Cofnij(View view)
    {
        Intent intent = new Intent(this, SzczegoloweInformacje.class);
        intent.putExtra("WydatekDochod", WydatekDochod); // wydatek
        intent.putExtra("IdKategorii", ID_KATEGORII);
        startActivity(intent);
    }


}
