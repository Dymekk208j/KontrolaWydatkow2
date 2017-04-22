package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Random;

public class SzczegoloweInformacje extends AppCompatActivity {

    private Intent intent;
    PieChart pieChart;
    private baza_danych BazaDanych;
    private ArrayList<Integer> colors;
    Random r = new Random();
    int Low = 0;
    int High = 255;
    ArrayList<Integer> IdWydatku;

    String NazwaKategorii = "";
    int IdKategorii = 0;
    boolean WydatekDochod = false; //0 wydatek, 1 dochod

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_szczegolowe_informacje);
        BazaDanych = new baza_danych(this);
        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            NazwaKategorii = extras.getString("NazwaKategorii");
            WydatekDochod = extras.getBoolean("WydatekDochod");
            IdKategorii = extras.getInt("IdKategorii");
        }

        setTitle(NazwaKategorii);



        Description desc = new Description();
        desc.setText("");

        pieChart = (PieChart) findViewById(R.id.idPieChart);
        pieChart.setRotationEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setDescription(desc);




        colors = new ArrayList<>();
        for(int i = 0; i < 20; i++)
        {
            colors.add(Color.rgb(r.nextInt(High-Low) + Low,r.nextInt(High-Low) + Low,r.nextInt(High-Low) + Low));
        }

        dodajDanePodkategorii();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    private void dodajDanePodkategorii()
    {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> NazwyPodKategorii = new ArrayList<>();
        ArrayList<Float> WydanePieniadze = new ArrayList<>();
        IdWydatku = new ArrayList<>();

        ArrayList<Integer> intPodKategorie = BazaDanych.getINTpodKategorie(IdKategorii);


        for(int a = 0; a < intPodKategorie.size(); a++)
        {
            float bufor = BazaDanych.getIleWydanoWPodkategorii(intPodKategorie.get(a));
            if(bufor > 0)
            {
                WydanePieniadze.add(bufor);

                if(a == 0)
                {
                    NazwyPodKategorii.add("Domyślna podkategoria");
                    IdWydatku.add(0);
                }
                else
                {
                    NazwyPodKategorii.add(BazaDanych.getPodKategoriaName(intPodKategorie.get(a)));
                    IdWydatku.add(intPodKategorie.get(a));
                }
            }

        }

        for(int i = 0; i < NazwyPodKategorii.size(); i++)
        {
            yEntrys.add(new PieEntry(WydanePieniadze.get(i), NazwyPodKategorii.get(i)));
        }



        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(8);



        pieDataSet.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART_CENTER);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());

        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    public void cofnij(View view) {
        intent = new Intent(this, Informacje.class);
        startActivity(intent);
    }

    public void test(View view)
    {
        
    }

}
