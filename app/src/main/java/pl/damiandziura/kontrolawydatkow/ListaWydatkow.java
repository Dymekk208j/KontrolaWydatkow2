package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class ListaWydatkow extends AppCompatActivity {

    private int ID_KATEGORII = 0;
    private int ID_PODKATEGORI = 0;
    boolean WydatekDochod;

    private int LAST_LP = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_wydatkow);
        baza_danych BazaDanych = new baza_danych(this);
        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            ID_PODKATEGORI = extras.getInt("IdPodKategorii");
            ID_KATEGORII = extras.getInt("IdKategorii");
            WydatekDochod = extras.getBoolean("WydatekDochod");
        }

        String Nazwa_id_kategorii, Nazwa_id_podkategorii;
        if(ID_KATEGORII != 0) {
            Nazwa_id_kategorii = BazaDanych.getCategoryName(ID_KATEGORII);
        }else{ Nazwa_id_kategorii = getResources().getString(R.string.str_domyslna_kategoria);}

        if(ID_PODKATEGORI != 0) {
            Nazwa_id_podkategorii = BazaDanych.getSubcategoryName(ID_PODKATEGORI);
        }else{ Nazwa_id_podkategorii = getResources().getString(R.string.str_domyślna_podkategoria);}

        setTitle(Nazwa_id_kategorii + "/" + Nazwa_id_podkategorii + "/*");

        ArrayList<String> ListaNazwa;
        ArrayList<String> ListaDat;
        ArrayList<Double> ListaKwot;

        if(!WydatekDochod)
        {
            ListaNazwa = BazaDanych.getExpensesNames(ID_KATEGORII, ID_PODKATEGORI);
            ListaDat = BazaDanych.getExpensesDates(ID_KATEGORII, ID_PODKATEGORI);
            ListaKwot = BazaDanych.getExpensesAmount(ID_KATEGORII, ID_PODKATEGORI);
        }
        else
        {
            ListaNazwa = BazaDanych.getIncomeNames(ID_KATEGORII, ID_PODKATEGORI);
            ListaDat = BazaDanych.getIncomeDates(ID_KATEGORII, ID_PODKATEGORI);
            ListaKwot = BazaDanych.getIncomeAmounts(ID_KATEGORII, ID_PODKATEGORI);

        }
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
        b.setText(String.format(Locale.getDefault(), "%d.", LAST_LP));
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

        String buffor = "%.2f" + getResources().getString(R.string.str_skrot_waluty);
        b.setText(String.format(Locale.getDefault(), buffor, _kwota));

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
