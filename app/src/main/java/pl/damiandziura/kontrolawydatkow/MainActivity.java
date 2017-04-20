package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Intent intent;

    PieChart pieChart;

    private baza_danych BazaDanych;
    TextView OstatniWydatek[] = new TextView[10];
    String sOstatniWydatek[] = new String[10];

    TextView OstatniDochod[] = new TextView[10];
    String sOstatniDochod[] = new String[10];
    TextView PosiadaneSrodki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Aplikacja do kontroli wydatków");
        setContentView(R.layout.activity_main);

        BazaDanych = new baza_danych(this);

        BazaDanych.PortfelUstawSaldo(BazaDanych.PortfelPrzeliczSaldo());
        PosiadaneSrodki = (TextView) findViewById(R.id.textView);
        String kwotaa = String.format("%.2f",BazaDanych.getPortfelSaldo());
        PosiadaneSrodki.setText("Saldo konta: " + kwotaa + "zł");

        OstatniWydatek[0] = (TextView) findViewById(R.id.TxtLastExp0);
        OstatniWydatek[1] = (TextView) findViewById(R.id.TxtLastExp1);
        OstatniWydatek[2] = (TextView) findViewById(R.id.TxtLastExp2);
        OstatniWydatek[3] = (TextView) findViewById(R.id.TxtLastExp3);
        OstatniWydatek[4] = (TextView) findViewById(R.id.TxtLastExp4);
        OstatniWydatek[5] = (TextView) findViewById(R.id.TxtLastExp5);
        OstatniWydatek[6] = (TextView) findViewById(R.id.TxtLastExp6);
        OstatniWydatek[7] = (TextView) findViewById(R.id.TxtLastExp7);
        OstatniWydatek[8] = (TextView) findViewById(R.id.TxtLastExp8);
        OstatniWydatek[9] = (TextView) findViewById(R.id.TxtLastExp9);

        OstatniDochod[0] = (TextView) findViewById(R.id.TxtOstatniDochod0);
        OstatniDochod[1] = (TextView) findViewById(R.id.TxtOstatniDochod1);
        OstatniDochod[2] = (TextView) findViewById(R.id.TxtOstatniDochod2);
        OstatniDochod[3] = (TextView) findViewById(R.id.TxtOstatniDochod3);
        OstatniDochod[4] = (TextView) findViewById(R.id.TxtOstatniDochod4);
        OstatniDochod[5] = (TextView) findViewById(R.id.TxtOstatniDochod5);
        OstatniDochod[6] = (TextView) findViewById(R.id.TxtOstatniDochod6);
        OstatniDochod[7] = (TextView) findViewById(R.id.TxtOstatniDochod7);
        OstatniDochod[8] = (TextView) findViewById(R.id.TxtOstatniDochod8);
        OstatniDochod[9] = (TextView) findViewById(R.id.TxtOstatniDochod9);

        sOstatniWydatek = BazaDanych.getOstatnieWydatki();
        sOstatniDochod = BazaDanych.getOstatnieDochody();

        for(int a = 0; a < 10; a++)
        {
            OstatniWydatek[a].setText(sOstatniWydatek[a]);
            OstatniDochod[a].setText(sOstatniDochod[a]);
        }

        pieChart = (PieChart) findViewById(R.id.idPieChart);
        pieChart.setRotationEnabled(true);
        pieChart.setDrawEntryLabels(false);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(false);


        addDataSet();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    public void dodajWydatek(View view) {
        intent = new Intent(this, AddNewExpenses.class);
        startActivity(intent);
    }

    public void dodajdochod(View view) {
        intent = new Intent(this, nowyPrzychod.class);
        startActivity(intent);
    }

    public void kategorie(View view) {
        intent = new Intent(this, Kategorie.class);
        startActivity(intent);
    }

    public void Informacje(View view) {
        intent = new Intent(this, Informacje.class);
        startActivity(intent);
    }


    private void addDataSet()
    {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> NazwyKategorii = new ArrayList<>();
        ArrayList<Float> WydanePieniadze = new ArrayList<>();
        ArrayList<Integer> intKategorie = BazaDanych.getINTKategorie();


        for(int a = 0; a < intKategorie.size(); a++)
        {
            float bufor = BazaDanych.getIleWydanoWkategorii(intKategorie.get(a));
            if(bufor > 0)
            {
                WydanePieniadze.add(bufor);

               if(a == 0)
               {
                   NazwyKategorii.add("0. Domyślna kategoria");
               }
               else
               {
                   NazwyKategorii.add(BazaDanych.getKategoriaName(intKategorie.get(a)));
               }
            }

        }

        for(int i = 0; i < NazwyKategorii.size(); i++)
        {
            yEntrys.add(new PieEntry(WydanePieniadze.get(i), NazwyKategorii.get(i)));
        }



        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(8);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.CYAN);
        colors.add(Color.DKGRAY);
        colors.add(Color.GREEN);
        colors.add(Color.MAGENTA);
        colors.add(Color.RED);
        colors.add(Color.YELLOW);

        pieDataSet.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());

        pieChart.setData(pieData);
        pieChart.invalidate();
    }

}
