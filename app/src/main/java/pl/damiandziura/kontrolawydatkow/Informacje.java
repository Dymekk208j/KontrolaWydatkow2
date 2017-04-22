package pl.damiandziura.kontrolawydatkow;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class Informacje extends AppCompatActivity {

    private Intent intent;
    PieChart pieChart, pieChart2;
    private baza_danych BazaDanych;
    private ArrayList<Integer> colors;
    Random r = new Random();
    int Low = 0;
    int High = 255;
    ArrayList<Integer> IdWydatku, IdDochodu;
    ArrayList<Integer> intKategorie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Szczegółowe informacje");
        setContentView(R.layout.activity_informacje);

        BazaDanych = new baza_danych(this);
        intKategorie = BazaDanych.getINTKategorie();

        Description desc = new Description();
        desc.setText(" ");

        pieChart = (PieChart) findViewById(R.id.idPieChart);
        pieChart.setRotationEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setDescription(desc);
        pieChart.animateXY(1500, 1500);



        colors = new ArrayList<>();
        for(int i = 0; i < BazaDanych.getKategoriaMaxId(); i++)
        {
            colors.add(Color.rgb(r.nextInt(High-Low) + Low,r.nextInt(High-Low) + Low,r.nextInt(High-Low) + Low));
        }



        dodajDaneWydatkow();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                SzczegolyKategoriiWydatku(h);
            }

            @Override
            public void onNothingSelected() {

            }
        });


        pieChart2 = (PieChart) findViewById(R.id.idPieChart2);
        pieChart2.setRotationEnabled(false);
        pieChart2.setDrawEntryLabels(false);
        pieChart2.setUsePercentValues(true);
        pieChart2.setDrawHoleEnabled(false);
        pieChart2.setDescription(desc);
        pieChart2.animateXY(1500, 1500);

        dodajDaneDochodow();

        pieChart2.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                SzczegolyKategoriiDochodu(h);
            }

            @Override
            public void onNothingSelected() {

            }
        });




    }

    public void cofnij(View view) {
       intent = new Intent(this, MainActivity.class);
       startActivity(intent);
    }


    private void dodajDaneWydatkow()
    {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> NazwyKategorii = new ArrayList<>();
        ArrayList<Float> WydanePieniadze = new ArrayList<>();
        IdWydatku = new ArrayList<>();




        for(int a = 0; a < intKategorie.size(); a++)
        {
            float bufor = BazaDanych.getIleWydanoWkategorii(intKategorie.get(a));
            if(bufor > 0)
            {
                WydanePieniadze.add(bufor);

                if(a == 0)
                {
                    NazwyKategorii.add("Domyślna kategoria");
                    IdWydatku.add(0);
                }
                else
                {
                    NazwyKategorii.add(BazaDanych.getKategoriaName(intKategorie.get(a)));
                    IdWydatku.add(intKategorie.get(a));
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



        pieDataSet.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART_CENTER);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());

        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void dodajDaneDochodow()
    {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> NazwyKategorii = new ArrayList<>();
        ArrayList<Float> ZarobionePieniadze = new ArrayList<>();
        IdDochodu = new ArrayList<>();



        for(int a = 0; a < intKategorie.size(); a++)
        {
            float bufor = BazaDanych.getIleDochoduWkategorii(intKategorie.get(a));
            if(bufor > 0)
            {
                ZarobionePieniadze.add(bufor);

                if(a == 0)
                {
                    NazwyKategorii.add("Domyślna kategoria");
                    IdDochodu.add(0);
                }
                else
                {
                    NazwyKategorii.add(BazaDanych.getKategoriaName(intKategorie.get(a)));
                    IdDochodu.add(intKategorie.get(a));
                }
            }

        }

        for(int i = 0; i < NazwyKategorii.size(); i++)
        {
            yEntrys.add(new PieEntry(ZarobionePieniadze.get(i), NazwyKategorii.get(i)));
        }



        PieDataSet pieDataSet2 = new PieDataSet(yEntrys, "");
        pieDataSet2.setSliceSpace(2);
        pieDataSet2.setValueTextSize(8);


        pieDataSet2.setColors(colors);

        Legend legend2 = pieChart2.getLegend();
        legend2.setForm(Legend.LegendForm.SQUARE);
        legend2.setPosition(Legend.LegendPosition.LEFT_OF_CHART_CENTER);

        PieData pieData2 = new PieData(pieDataSet2);
        pieData2.setValueFormatter(new PercentFormatter());

        pieChart2.setData(pieData2);
        pieChart2.invalidate();
    }

    void SzczegolyKategoriiWydatku(Highlight he)
    {
        Toast.makeText(this, BazaDanych.getKategoriaName(IdWydatku.get((int)he.getX())), Toast.LENGTH_SHORT).show();
        intent = new Intent(this, SzczegoloweInformacje.class);
        intent.putExtra("NazwaKategorii", "Szczegóły kategorii " + BazaDanych.getKategoriaName(IdWydatku.get((int)he.getX())));
        intent.putExtra("WydatekDochod", false); // wydatek
        intent.putExtra("IdKategorii", IdWydatku.get((int)he.getX()));
        startActivity(intent);
    }

    void SzczegolyKategoriiDochodu(Highlight he)
    {
        Toast.makeText(this, BazaDanych.getKategoriaName(IdWydatku.get((int)he.getX())), Toast.LENGTH_SHORT).show();
        intent = new Intent(this, SzczegoloweInformacje.class);
        intent.putExtra("NazwaKategorii", "Szczegóły kategorii " + BazaDanych.getKategoriaName(IdDochodu.get((int)he.getX())));
        intent.putExtra("WydatekDochod", true); // wydatek
        intent.putExtra("IdKategorii", IdDochodu.get((int)he.getX()));
        startActivity(intent);
    }

}
