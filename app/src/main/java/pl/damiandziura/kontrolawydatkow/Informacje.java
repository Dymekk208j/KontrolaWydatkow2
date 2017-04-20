package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Informacje extends AppCompatActivity {

    private Intent intent;
    private TextView lblTest;
    private  baza_danych BazaDanych;



    ArrayList<String> NazwyKategorii;
    ArrayList<Double> WydanePieniadze;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacje);

        BazaDanych = new baza_danych(this);

        NazwyKategorii = BazaDanych.getKategorie();
        ArrayList<Integer> intKategorie = BazaDanych.getINTKategorie();
        WydanePieniadze = new ArrayList<>();

        for(int a = 0; a < intKategorie.size(); a++)
        {
            double bufor = BazaDanych.getIleWydanoWkategorii(intKategorie.get(a));
            WydanePieniadze.add(bufor);
        }


        lblTest = (TextView) findViewById(R.id.lblTest);
        lblTest.setText("W kategorii: " + NazwyKategorii.get(2) + " wydales: " + Double.toString(WydanePieniadze.get(2)) + " zł");

        //KategoriaIDlist = BazaDanych.getINTKategorie();
    }

    public void cofnij(View view) {
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
